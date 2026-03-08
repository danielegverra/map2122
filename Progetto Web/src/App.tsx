import { useState } from 'react';
import Sidebar from './components/Sidebar';
import Header from './components/Header';
import Dashboard from './components/Dashboard';
import Help from './components/Help';
import Datasets from './components/Datasets';

function App() {
  const [activeTab, setActiveTab] = useState('dashboard');

  return (
    <div className="flex h-screen overflow-hidden bg-brand-bg relative">
      <Sidebar activeTab={activeTab} setActiveTab={setActiveTab} />

      <div className="flex-1 ml-64 flex flex-col h-screen overflow-y-auto">
        <Header />

        <main className="flex-1">
          {activeTab === 'dashboard' && <Dashboard />}
          {activeTab === 'help' && <Help />}
          {activeTab === 'datasets' && <Datasets />}
          {activeTab !== 'dashboard' && activeTab !== 'help' && activeTab !== 'datasets' && (
            <div className="flex items-center justify-center h-full text-gray-500 font-medium">
              <div className="text-center">
                <h2 className="text-2xl font-bold text-gray-800 mb-2 uppercase tracking-wide">{activeTab}</h2>
                <p>This module is under construction.</p>
              </div>
            </div>
          )}
        </main>
      </div>
    </div>
  );
}

export default App;
