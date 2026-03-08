package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import data.Data;
import example.Example;

public class HttpServerBridge {

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/api/predict", new PredictHandler());
        server.createContext("/api/datasets", new DatasetsHandler());
        server.createContext("/api/dataset", new DatasetViewHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("HTTP Server started on port 8080");
    }

    static class PredictHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

            if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            if ("POST".equals(exchange.getRequestMethod())) {
                InputStream is = exchange.getRequestBody();
                String requestBody = new String(is.readAllBytes(), StandardCharsets.UTF_8);

                try {
                    String tableName = extractValue(requestBody, "\"tableName\"\\s*:\\s*\"([^\"]+)\"");
                    String kStr = extractValue(requestBody, "\"k\"\\s*:\\s*(\\d+)");
                    String attributesStr = extractValue(requestBody, "\"attributes\"\\s*:\\s*\"([^\"]+)\"");

                    if (tableName == null || kStr == null || attributesStr == null) {
                        sendResponse(exchange, 400,
                                "{\"error\":\"Missing required fields: tableName, k, attributes\"}");
                        return;
                    }

                    int k = Integer.parseInt(kStr);
                    String[] attrs = attributesStr.split(",");

                    // Resolving path relative to execution directory.
                    // The jar usually runs in map2122/Progetto GUI/Jar-Bat/
                    // Or if executed from Server root: map2122/Progetto GUI/Codice Sorgente/Server/
                    // We will just use the same path hardcoded in the original server
                    String path = "../../../File/" + tableName + ".dat";
                    java.io.File file = new java.io.File(path);
                    if (!file.exists()) {
                        path = "../../../../File/" + tableName + ".dat"; // Fallback if executed deep in src
                    }

                    Data data = new Data(path);

                    Example e = new Example(attrs.length);
                    for (int i = 0; i < attrs.length; i++) {
                        String val = attrs[i].trim();
                        try {
                            e.set(Double.parseDouble(val), i);
                        } catch (NumberFormatException ex) {
                            e.set(val, i);
                        }
                    }

                    double result = data.avgClosest(e, k);
                    String response = "{\"prediction\":" + result + "}";
                    sendResponse(exchange, 200, response);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    sendResponse(exchange, 500, "{\"error\":\"" + ex.getMessage().replace("\"", "\\\"") + "\"}");
                }
            } else {
                sendResponse(exchange, 405, "{\"error\":\"Method Not Allowed\"}");
            }
        }

        private String extractValue(String json, String regex) {
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
            java.util.regex.Matcher matcher = pattern.matcher(json);
            if (matcher.find()) {
                return matcher.group(1);
            }
            return null;
        }

        private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
            byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(statusCode, bytes.length);
            OutputStream os = exchange.getResponseBody();
            os.write(bytes);
            os.close();
        }
    }

    static class DatasetsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

            if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            if ("GET".equals(exchange.getRequestMethod())) {
                try {
                    String path = "../../../File/";
                    java.io.File dir = new java.io.File(path);
                    if (!dir.exists() || !dir.isDirectory()) {
                        path = "../../../../File/";
                        dir = new java.io.File(path);
                    }

                    if (dir.exists() && dir.isDirectory()) {
                        java.io.File[] files = dir.listFiles((dir1, name) -> name.endsWith(".dat"));
                        StringBuilder json = new StringBuilder("[");
                        if (files != null) {
                            for (int i = 0; i < files.length; i++) {
                                String name = files[i].getName().replace(".dat", "");
                                json.append("{\"name\":\"").append(name).append("\"}");
                                if (i < files.length - 1)
                                    json.append(",");
                            }
                        }
                        json.append("]");
                        sendResponse(exchange, 200, json.toString());
                    } else {
                        sendResponse(exchange, 404, "{\"error\":\"Directory not found\"}");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    sendResponse(exchange, 500, "{\"error\":\"" + ex.getMessage().replace("\"", "\\\"") + "\"}");
                }
            } else {
                sendResponse(exchange, 405, "{\"error\":\"Method Not Allowed\"}");
            }
        }

        private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
            byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(statusCode, bytes.length);
            OutputStream os = exchange.getResponseBody();
            os.write(bytes);
            os.close();
        }
    }

    static class DatasetViewHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

            if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            if ("GET".equals(exchange.getRequestMethod())) {
                try {
                    String query = exchange.getRequestURI().getQuery();
                    String name = null;
                    if (query != null && query.startsWith("name=")) {
                        name = query.split("=")[1];
                    }

                    if (name == null || name.isEmpty()) {
                        sendResponse(exchange, 400, "{\"error\":\"Missing dataset name\"}");
                        return;
                    }

                    String path = "../../../File/" + name + ".dat";
                    java.io.File file = new java.io.File(path);
                    if (!file.exists()) {
                        path = "../../../../File/" + name + ".dat";
                        file = new java.io.File(path);
                    }

                    if (file.exists() && file.isFile()) {
                        java.util.Scanner sc = new java.util.Scanner(file);
                        StringBuilder json = new StringBuilder("{\"schema\":[], \"data\":[],");

                        java.util.List<String> schemaLines = new java.util.ArrayList<>();
                        java.util.List<String> dataLines = new java.util.ArrayList<>();
                        boolean isData = false;

                        while (sc.hasNextLine()) {
                            String line = sc.nextLine().trim();
                            if (line.isEmpty())
                                continue;

                            if (line.startsWith("@data")) {
                                isData = true;
                                json.append("\"rows\":").append(line.split(" ")[1]).append("}");
                                continue;
                            }

                            if (!isData) {
                                if (line.startsWith("@desc") || line.startsWith("@target")) {
                                    schemaLines.add(line);
                                }
                            } else {
                                dataLines.add(line);
                            }
                        }
                        sc.close();

                        // Rewrite json to properly insert schema and data arrays
                        StringBuilder finalJson = new StringBuilder();
                        finalJson.append("{");

                        // Serialize schema
                        finalJson.append("\"schema\":[");
                        for (int i = 0; i < schemaLines.size(); i++) {
                            String[] parts = schemaLines.get(i).split(" ");
                            finalJson.append("{\"name\":\"").append(parts[1]).append("\", \"type\":\"")
                                    .append(parts.length > 2 ? parts[2] : "target").append("\"}");
                            if (i < schemaLines.size() - 1)
                                finalJson.append(",");
                        }
                        finalJson.append("],");

                        // Serialize data
                        finalJson.append("\"data\":[");
                        for (int i = 0; i < Math.min(dataLines.size(), 100); i++) { // limit to 100 rows for view
                            String[] row = dataLines.get(i).split(",");
                            finalJson.append("[");
                            for (int j = 0; j < row.length; j++) {
                                finalJson.append("\"").append(row[j].trim()).append("\"");
                                if (j < row.length - 1)
                                    finalJson.append(",");
                            }
                            finalJson.append("]");
                            if (i < Math.min(dataLines.size(), 100) - 1)
                                finalJson.append(",");
                        }
                        finalJson.append("]");
                        finalJson.append("}");

                        sendResponse(exchange, 200, finalJson.toString());
                    } else {
                        sendResponse(exchange, 404, "{\"error\":\"File not found\"}");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    sendResponse(exchange, 500, "{\"error\":\"" + ex.getMessage().replace("\"", "\\\"") + "\"}");
                }
            } else {
                sendResponse(exchange, 405, "{\"error\":\"Method Not Allowed\"}");
            }
        }

        private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
            byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(statusCode, bytes.length);
            OutputStream os = exchange.getResponseBody();
            os.write(bytes);
            os.close();
        }
    }
}
