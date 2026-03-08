import { useState } from 'react';
import { ArrowUpRight, FolderOpen, Activity, Play, Database, FileText, CheckCircle2 } from 'lucide-react';

const Dashboard: React.FC = () => {
    const [sourceType, setSourceType] = useState<'file' | 'db'>('file');
    const [tableName, setTableName] = useState('');
    const [kValue, setKValue] = useState<number>(3);
    const [attributes, setAttributes] = useState('');

    const [prediction, setPrediction] = useState<string | null>(null);
    const [status, setStatus] = useState<{ message: string; type: 'idle' | 'loading' | 'success' | 'error' }>({ message: 'Ready', type: 'idle' });

    const handlePredict = async (e: React.FormEvent) => {
        e.preventDefault();
        setStatus({ message: 'Calculating...', type: 'loading' });
        try {
            const response = await fetch('http://localhost:8080/api/predict', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    tableName,
                    k: kValue,
                    attributes
                })
            });

            const data = await response.json();
            if (!response.ok) throw new Error(data.error || 'Server error');

            setPrediction(Number(data.prediction).toFixed(2));
            setStatus({ message: 'Predicted successfully', type: 'success' });
        } catch (err: any) {
            console.error(err);
            setStatus({ message: err.message || 'Prediction failed', type: 'error' });
        }
    };

    return (
        <div className="p-8 max-w-7xl mx-auto">
            <div className="flex justify-between items-end mb-8">
                <div>
                    <h1 className="text-3xl font-bold text-gray-900 mb-2 tracking-tight">K-NN Dashboard</h1>
                    <p className="text-gray-500 font-medium">Configure and run K-Nearest Neighbors regression on your datasets.</p>
                </div>
                <div className="flex gap-4">
                    <button className="px-5 py-2.5 bg-brand-dark hover:bg-brand-medium text-white font-semibold rounded-full shadow-lg shadow-brand-dark/20 transition-all flex items-center gap-2">
                        <FolderOpen size={18} /> Load New Dataset
                    </button>
                    <button className="px-5 py-2.5 bg-white border border-gray-200 text-gray-700 hover:text-brand-dark hover:border-brand-medium font-semibold rounded-full shadow-sm transition-all flex items-center gap-2">
                        Import DB
                    </button>
                </div>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-4 gap-6 mb-8">
                {/* Stat Cards mimicking the design */}
                <div className="bg-gradient-to-br from-brand-dark via-[#1a6e3e] to-brand-dark p-6 rounded-3xl text-white shadow-xl shadow-brand-dark/10 relative overflow-hidden group">
                    <div className="absolute top-0 right-0 p-6">
                        <div className="w-10 h-10 bg-white/10 rounded-full flex items-center justify-center backdrop-blur-md border border-white/20 group-hover:scale-110 transition-transform">
                            <ArrowUpRight size={20} className="text-white" />
                        </div>
                    </div>
                    <p className="text-white/80 font-medium mb-1">Current K Value</p>
                    <h2 className="text-4xl font-bold mb-4">{kValue}</h2>
                    <div className="inline-flex items-center gap-2 bg-white/20 px-3 py-1.5 rounded-full text-xs font-semibold backdrop-blur-md">
                        <Activity size={14} /> Optimized for Map7
                    </div>
                </div>

                {[
                    { title: 'Dataset Rows', val: '1,240', inc: '12%', color: 'from-blue-50 to-blue-100/50 text-blue-900 border-blue-100' },
                    { title: 'Features (Atter)', val: '5', inc: '0%', color: 'from-amber-50 to-amber-100/50 text-amber-900 border-amber-100' },
                    { title: 'Predicts Run', val: '84', inc: '24%', color: 'from-purple-50 to-purple-100/50 text-purple-900 border-purple-100' }
                ].map((stat, i) => (
                    <div key={i} className={`bg-gradient-to-br ${stat.color} p-6 rounded-3xl shadow-sm border relative group`}>
                        <div className="absolute top-0 right-0 p-6">
                            <div className="w-10 h-10 bg-white shadow-sm rounded-full flex items-center justify-center group-hover:scale-110 transition-transform">
                                <ArrowUpRight size={20} className="opacity-60" />
                            </div>
                        </div>
                        <p className="font-medium opacity-70 mb-1">{stat.title}</p>
                        <h2 className="text-4xl font-bold mb-4">{stat.val}</h2>
                        <div className="inline-flex items-center gap-1.5 px-3 py-1.5 rounded-full text-xs font-semibold bg-white shadow-sm">
                            <span className="text-brand-medium">↗ {stat.inc}</span> vs last dataset
                        </div>
                    </div>
                ))}
            </div>

            <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
                {/* Main Controls - Left 2 Columns */}
                <div className="lg:col-span-2 space-y-6">
                    <div className="bg-white p-8 rounded-3xl shadow-sm border border-gray-100">
                        <div className="flex justify-between items-center mb-6">
                            <h3 className="text-xl font-bold text-gray-900">Run Prediction</h3>
                            <div className="flex bg-gray-50 p-1 rounded-xl">
                                <button
                                    onClick={() => setSourceType('file')}
                                    className={`px-4 py-1.5 text-sm font-semibold rounded-lg flex items-center gap-2 transition-all ${sourceType === 'file' ? 'bg-white text-brand-dark shadow-sm' : 'text-gray-500 hover:text-gray-900'}`}
                                >
                                    <FileText size={16} /> File (.dat)
                                </button>
                                <button
                                    onClick={() => setSourceType('db')}
                                    className={`px-4 py-1.5 text-sm font-semibold rounded-lg flex items-center gap-2 transition-all ${sourceType === 'db' ? 'bg-white text-brand-dark shadow-sm' : 'text-gray-500 hover:text-gray-900'}`}
                                >
                                    <Database size={16} /> Database
                                </button>
                            </div>
                        </div>

                        <form onSubmit={handlePredict} className="space-y-6">
                            <div className="grid grid-cols-2 gap-6">
                                <div>
                                    <label className="block text-sm font-medium text-gray-700 mb-2">Dataset Name</label>
                                    <input
                                        type="text"
                                        value={tableName}
                                        onChange={(e) => setTableName(e.target.value)}
                                        placeholder="e.g. map7"
                                        className="w-full px-4 py-3 bg-gray-50 border border-gray-200 rounded-xl focus:ring-2 focus:ring-brand-light focus:border-brand-medium focus:bg-white outline-none transition-all"
                                    />
                                </div>
                                <div>
                                    <label className="block text-sm font-medium text-gray-700 mb-2">K Value</label>
                                    <input
                                        type="number"
                                        min="1"
                                        value={kValue}
                                        onChange={(e) => setKValue(parseInt(e.target.value))}
                                        className="w-full px-4 py-3 bg-gray-50 border border-gray-200 rounded-xl focus:ring-2 focus:ring-brand-light focus:border-brand-medium focus:bg-white outline-none transition-all"
                                    />
                                </div>
                            </div>

                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-2">Query Attributes (comma separated)</label>
                                <input
                                    type="text"
                                    value={attributes}
                                    onChange={(e) => setAttributes(e.target.value)}
                                    placeholder="e.g. M, 35.5, High, 1.2"
                                    className="w-full px-4 py-3 bg-gray-50 border border-gray-200 rounded-xl focus:ring-2 focus:ring-brand-light focus:border-brand-medium focus:bg-white outline-none transition-all"
                                />
                            </div>

                            <div className="flex items-center justify-between pt-4 border-t border-gray-100">
                                <div className="flex items-center gap-3">
                                    <div className={`w-3 h-3 rounded-full ${status.type === 'success' ? 'bg-green-500' : status.type === 'error' ? 'bg-red-500' : status.type === 'loading' ? 'bg-yellow-500 animate-pulse' : 'bg-gray-300'}`}></div>
                                    <span className="text-sm font-medium text-gray-600">{status.message}</span>
                                </div>

                                <button
                                    type="submit"
                                    disabled={!tableName || !attributes || status.type === 'loading'}
                                    className="bg-brand-dark hover:bg-brand-medium text-white px-8 py-3 rounded-xl font-bold flex items-center gap-2 shadow-lg shadow-brand-dark/20 transition-all disabled:opacity-50 disabled:cursor-not-allowed"
                                >
                                    <Play size={18} fill="currentColor" /> Compute Target
                                </button>
                            </div>
                        </form>
                    </div>

                    <div className="grid grid-cols-2 gap-6">
                        <div className="bg-brand-bg rounded-3xl p-6 border-dashed border-2 border-brand-medium/30 flex flex-col items-center justify-center text-center">
                            <div className="w-12 h-12 bg-white rounded-full flex items-center justify-center text-brand-dark shadow-sm mb-4">
                                <FileText size={24} />
                            </div>
                            <h4 className="font-bold text-gray-900 mb-1">Batch Predict</h4>
                            <p className="text-xs font-medium text-gray-500">Upload a CSV for multiple predictions</p>
                        </div>

                        <div className="bg-brand-bg rounded-3xl p-6 border-dashed border-2 border-brand-medium/30 flex flex-col items-center justify-center text-center">
                            <div className="w-12 h-12 bg-white rounded-full flex items-center justify-center text-brand-dark shadow-sm mb-4">
                                <Activity size={24} />
                            </div>
                            <h4 className="font-bold text-gray-900 mb-1">Model Tuning</h4>
                            <p className="text-xs font-medium text-gray-500">Auto-discover best K parameter</p>
                        </div>
                    </div>
                </div>

                {/* Right Column - Results Dashboard */}
                <div className="space-y-6">
                    <div className="bg-gradient-to-br from-[#0a311b] via-[#0d4023] to-[#165B33] p-8 rounded-3xl shadow-xl border border-white/10 relative overflow-hidden">
                        <div className="absolute inset-0 bg-[url('https://www.transparenttextures.com/patterns/cubes.png')] opacity-20 mix-blend-overlay"></div>
                        <div className="relative z-10 flex flex-col h-full items-center justify-center text-center py-6">
                            <p className="text-brand-light font-medium tracking-widest uppercase text-sm mb-4">Prediction Result</p>

                            {prediction ? (
                                <div className="animate-in zoom-in duration-300">
                                    <h2 className="text-6xl font-black text-white mb-2 drop-shadow-md">{prediction}</h2>
                                    <p className="text-white/80 font-medium">Estimated Target Value</p>
                                </div>
                            ) : (
                                <div className="opacity-50">
                                    <h2 className="text-6xl font-black text-white mb-2">--</h2>
                                    <p className="text-white/80 font-medium">Awaiting Input</p>
                                </div>
                            )}
                        </div>

                        {prediction && (
                            <div className="relative z-10 mt-8 pt-6 border-t border-white/20 flex justify-between items-center text-white/90 text-sm font-medium">
                                <span className="flex items-center gap-2"><CheckCircle2 size={16} /> Inference OK</span>
                                <button className="bg-white/20 hover:bg-white/30 px-3 py-1.5 rounded-lg transition-colors border border-white/20">
                                    Save Result
                                </button>
                            </div>
                        )}
                    </div>

                    <div className="bg-white p-6 rounded-3xl shadow-sm border border-gray-100">
                        <div className="flex justify-between items-center mb-4">
                            <h3 className="font-bold text-gray-900">Recent Runs</h3>
                            <button className="text-brand-medium font-semibold text-sm hover:underline">View All</button>
                        </div>
                        <div className="space-y-3">
                            {[
                                { db: 'map2', k: 5, res: '1.20', status: 'Completed' },
                                { db: 'iris', k: 3, res: 'Setosa', status: 'Completed' },
                                { db: 'map7', k: 7, res: '23.4', status: 'Pending' }
                            ].map((run, i) => (
                                <div key={i} className="flex items-center justify-between p-3 rounded-2xl hover:bg-gray-50 border border-transparent hover:border-gray-100 transition-all cursor-pointer">
                                    <div className="flex items-center gap-4">
                                        <div className="w-10 h-10 rounded-full bg-brand-light text-brand-dark flex items-center justify-center">
                                            <Database size={16} />
                                        </div>
                                        <div>
                                            <p className="font-bold text-sm text-gray-900">{run.db} dataset</p>
                                            <p className="text-xs font-medium text-gray-500">k={run.k}</p>
                                        </div>
                                    </div>
                                    <div className="text-right">
                                        <span className={`text-[10px] font-bold px-2 py-1 rounded-md uppercase ${run.status === 'Completed' ? 'bg-green-100 text-green-700' : 'bg-yellow-100 text-yellow-700'}`}>
                                            {run.status}
                                        </span>
                                    </div>
                                </div>
                            ))}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Dashboard;
