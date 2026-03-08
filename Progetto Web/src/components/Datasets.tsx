import React, { useEffect, useState } from 'react';
import { Database, FileText, Activity, Layers, RefreshCw, Eye, X } from 'lucide-react';

interface DatasetInfo {
    name: string;
}

interface DatasetContent {
    schema: { name: string; type: string }[];
    data: string[][];
    rows?: string;
}

const Datasets: React.FC = () => {
    const [datasets, setDatasets] = useState<DatasetInfo[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    const [viewingDataset, setViewingDataset] = useState<string | null>(null);
    const [datasetContent, setDatasetContent] = useState<DatasetContent | null>(null);
    const [loadingContent, setLoadingContent] = useState(false);

    const fetchDatasets = async () => {
        setLoading(true);
        setError(null);
        try {
            const response = await fetch('http://localhost:8080/api/datasets');
            if (!response.ok) throw new Error('Failed to fetch datasets');
            const data = await response.json();
            setDatasets(data);
        } catch (err: any) {
            console.error(err);
            setError(err.message || 'Unable to connect to server');
        } finally {
            setLoading(false);
        }
    };

    const handleViewDataset = async (name: string) => {
        setViewingDataset(name);
        setLoadingContent(true);
        setDatasetContent(null);
        try {
            const response = await fetch(`http://localhost:8080/api/dataset?name=${name}`);
            if (!response.ok) throw new Error('Failed to fetch dataset content');
            const data = await response.json();
            setDatasetContent(data);
        } catch (err: any) {
            console.error(err);
            alert("Error loading dataset content: " + err.message);
            setViewingDataset(null);
        } finally {
            setLoadingContent(false);
        }
    };

    useEffect(() => {
        fetchDatasets();
    }, []);

    return (
        <div className="p-8 max-w-7xl mx-auto pb-20 relative">
            <div className="flex justify-between items-end mb-8">
                <div>
                    <h1 className="text-3xl font-bold text-gray-900 mb-2 tracking-tight">Available Datasets</h1>
                    <p className="text-gray-500 font-medium">Explore the training sets ready to use for KNN predictions.</p>
                </div>
                <button
                    onClick={fetchDatasets}
                    className="px-5 py-2.5 bg-white border border-gray-200 text-gray-700 hover:text-brand-dark hover:border-brand-medium font-semibold rounded-full shadow-sm transition-all flex items-center gap-2"
                >
                    <RefreshCw size={18} className={loading ? 'animate-spin' : ''} /> Refresh List
                </button>
            </div>

            {error && (
                <div className="bg-red-50 text-red-600 p-4 rounded-xl border border-red-200 mb-8 font-medium">
                    Error loading datasets: {error}
                </div>
            )}

            {loading ? (
                <div className="flex justify-center py-20">
                    <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-brand-dark"></div>
                </div>
            ) : datasets.length === 0 && !error ? (
                <div className="text-center py-20 bg-gray-50 rounded-3xl border border-gray-100">
                    <Database size={48} className="mx-auto text-gray-300 mb-4" />
                    <p className="text-lg font-medium text-gray-500">No datasets found in map2122/File/</p>
                </div>
            ) : (
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                    {datasets.map((dataset, idx) => (
                        <div key={idx} className="bg-white p-6 flex flex-col rounded-3xl shadow-sm border border-gray-100 hover:border-brand-medium hover:shadow-md transition-all group relative overflow-hidden">
                            <div className="absolute top-0 right-0 w-32 h-32 bg-brand-light rounded-bl-[100px] -z-0 opacity-50 group-hover:bg-brand-medium/10 transition-colors"></div>

                            <div className="relative z-10 flex-1">
                                <div className="flex justify-between items-start mb-4">
                                    <div className="w-12 h-12 bg-gray-50 text-brand-dark rounded-xl flex items-center justify-center shadow-sm border border-gray-100 group-hover:scale-110 transition-transform">
                                        <FileText size={24} />
                                    </div>
                                    <span className="text-xs font-bold bg-green-50 text-green-700 px-3 py-1 rounded-full border border-green-100">Ready</span>
                                </div>

                                <h3 className="text-xl font-bold text-gray-900 mb-1">{dataset.name}</h3>
                                <p className="text-sm text-gray-500 font-medium mb-6">Local .dat file</p>

                                <div className="space-y-3 pt-4 border-t border-gray-100 mb-6">
                                    <div className="flex items-center gap-3 text-sm text-gray-600">
                                        <Layers size={16} className="text-gray-400" />
                                        <span>Use syntax: <span className="font-mono text-brand-dark font-semibold bg-brand-light px-1 rounded">{dataset.name}</span></span>
                                    </div>
                                    <div className="flex items-center gap-3 text-sm text-gray-600">
                                        <Activity size={16} className="text-gray-400" />
                                        <span>Can run predictions</span>
                                    </div>
                                </div>
                            </div>

                            <div className="relative z-10 mt-auto">
                                <button
                                    onClick={() => handleViewDataset(dataset.name)}
                                    className="w-full py-2.5 bg-gray-50 hover:bg-brand-dark hover:text-white text-gray-700 font-semibold rounded-xl transition-colors border border-gray-200 hover:border-brand-dark flex flex-row items-center justify-center gap-2"
                                >
                                    <Eye size={16} /> View Data
                                </button>
                            </div>
                        </div>
                    ))}
                </div>
            )}

            {/* Modal for viewing data */}
            {viewingDataset && (
                <div className="fixed inset-0 bg-gray-900/50 backdrop-blur-sm z-50 flex items-center justify-center p-4 sm:p-8">
                    <div className="bg-white w-full max-w-5xl max-h-[90vh] rounded-3xl shadow-2xl flex flex-col overflow-hidden animate-in fade-in zoom-in-95 duration-200">

                        {/* Modal Header */}
                        <div className="px-8 py-5 border-b border-gray-100 flex items-center justify-between bg-gray-50/50">
                            <div className="flex items-center gap-3">
                                <div className="w-10 h-10 bg-brand-light text-brand-dark rounded-xl flex items-center justify-center shadow-sm border border-brand-light/50">
                                    <Database size={20} />
                                </div>
                                <div>
                                    <h2 className="text-xl font-bold text-gray-900 leading-tight">{viewingDataset}.dat</h2>
                                    <p className="text-xs text-gray-500 font-medium">Data Preview (Max 100 rows)</p>
                                </div>
                            </div>
                            <button
                                onClick={() => setViewingDataset(null)}
                                className="w-10 h-10 rounded-full hover:bg-gray-100 flex items-center justify-center text-gray-500 transition-colors"
                            >
                                <X size={20} />
                            </button>
                        </div>

                        {/* Modal Content */}
                        <div className="flex-1 overflow-auto p-8 bg-gray-50/30">
                            {loadingContent ? (
                                <div className="flex flex-col items-center justify-center h-64 gap-4">
                                    <div className="animate-spin rounded-full h-10 w-10 border-b-2 border-brand-dark"></div>
                                    <p className="text-gray-500 font-medium text-sm animate-pulse">Parsing dataset...</p>
                                </div>
                            ) : datasetContent ? (
                                <div className="bg-white rounded-2xl border border-gray-200 shadow-sm overflow-hidden">
                                    <div className="overflow-x-auto">
                                        <table className="w-full text-sm text-left">
                                            <thead className="text-xs text-gray-700 uppercase bg-gray-50 border-b border-gray-200">
                                                <tr>
                                                    <th className="px-6 py-4 font-bold text-gray-400 w-16">#</th>
                                                    {datasetContent.schema.map((col, i) => (
                                                        <th key={i} className="px-6 py-4 font-bold">
                                                            <div className="flex flex-col">
                                                                <span className={col.type === 'target' ? 'text-brand-dark' : 'text-gray-900'}>{col.name}</span>
                                                                <span className="text-[10px] font-medium text-gray-500 tracking-wider mt-0.5">{col.type}</span>
                                                            </div>
                                                        </th>
                                                    ))}
                                                </tr>
                                            </thead>
                                            <tbody className="divide-y divide-gray-100">
                                                {datasetContent.data.map((row, rowIdx) => (
                                                    <tr key={rowIdx} className="hover:bg-brand-light/10 transition-colors">
                                                        <td className="px-6 py-3 font-medium text-gray-400">{rowIdx + 1}</td>
                                                        {row.map((cell, cellIdx) => {
                                                            const isTarget = datasetContent.schema[cellIdx]?.type === 'target';
                                                            return (
                                                                <td key={cellIdx} className={`px-6 py-3 font-medium ${isTarget ? 'text-brand-dark font-bold' : 'text-gray-600'}`}>
                                                                    {cell}
                                                                </td>
                                                            );
                                                        })}
                                                    </tr>
                                                ))}
                                                {datasetContent.data.length === 0 && (
                                                    <tr>
                                                        <td colSpan={datasetContent.schema.length + 1} className="px-6 py-8 text-center text-gray-500">
                                                            No data rows found in this file.
                                                        </td>
                                                    </tr>
                                                )}
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            ) : (
                                <div className="flex flex-col items-center justify-center h-64 text-red-500">
                                    <p>Could not load content.</p>
                                </div>
                            )}
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
};

export default Datasets;
