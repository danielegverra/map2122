import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import controller.StartController;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/newStartPage.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            StartController newController = (StartController)fxmlLoader.getController();

            //definiamo le operazioni da compiere quando una dimensione della schermata viene modificata
			primaryStage.heightProperty().addListener(new ChangeListener<Number>() {
				public void changed(ObservableValue<? extends Number> value, Number number, Number t1) {
					newController.resize(primaryStage.getHeight(), primaryStage.getWidth());
				}
			});
	
			primaryStage.widthProperty().addListener(new ChangeListener<Number>() {
				public void changed(ObservableValue<? extends Number> value, Number number, Number t1) {
					newController.resize(primaryStage.getHeight(), primaryStage.getWidth());
				}
			});

            //ridimensione delle componenti della scena prima del set
            newController.resize(primaryStage.getHeight(), primaryStage.getWidth());

            //set della scena e show
            primaryStage.setTitle("KNN");
            primaryStage.setScene(scene);            
            primaryStage.getIcons().add(new Image(App.class.getResourceAsStream("/fxml/1Icon.jpg")));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void stop() throws Exception {
        System.out.println("--> Chiusura app in corso...");
        Platform.exit();
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);    
    }

}