import {
    Home,
    CheckSquare,
    Calendar,
    BarChart2,
    Users,
    Settings,
    HelpCircle,
    LogOut,
    Target
} from 'lucide-react';

interface SidebarProps {
    activeTab: string;
    setActiveTab: (tab: string) => void;
}

const Sidebar = ({ activeTab, setActiveTab }: SidebarProps) => {
    const menuItems = [
        { id: 'dashboard', label: 'Dashboard', icon: Home },
        { id: 'datasets', label: 'Datasets', icon: CheckSquare, badge: '12+' },
        { id: 'history', label: 'History', icon: Calendar },
        { id: 'analytics', label: 'Analytics', icon: BarChart2 },
        { id: 'team', label: 'Team', icon: Users },
    ];

    const generalItems = [
        { id: 'settings', label: 'Settings', icon: Settings },
        { id: 'help', label: 'Help', icon: HelpCircle },
        { id: 'logout', label: 'Logout', icon: LogOut },
    ];

    return (
        <div className="w-64 bg-white h-screen fixed left-0 top-0 border-r border-gray-100 flex flex-col pt-6 pb-6 shadow-sm z-10 transition-all">
            <div className="flex items-center gap-3 px-8 mb-10">
                <div className="w-8 h-8 bg-brand-dark rounded-xl flex items-center justify-center text-white">
                    <Target size={20} />
                </div>
                <span className="font-bold text-xl text-gray-900 tracking-tight">KNN Predictor</span>
            </div>

            <div className="px-6 mb-2">
                <p className="text-xs font-semibold text-gray-400 mb-4 px-2 uppercase tracking-wider">MENU</p>
                <ul className="space-y-1">
                    {menuItems.map((item) => (
                        <li key={item.id}>
                            <button
                                onClick={() => setActiveTab(item.id)}
                                className={`w-full flex items-center justify-between px-3 py-2.5 rounded-xl transition-all duration-200 ${activeTab === item.id
                                    ? 'bg-brand-light text-brand-dark font-medium shadow-sm border border-brand-light/50 relative'
                                    : 'text-gray-500 hover:bg-gray-50 hover:text-gray-900 border border-transparent'
                                    }`}
                            >
                                {activeTab === item.id && (
                                    <div className="absolute -left-6 top-1/2 -translate-y-1/2 w-1.5 h-8 bg-brand-dark rounded-r-full" />
                                )}
                                <div className="flex items-center gap-3">
                                    <item.icon size={18} strokeWidth={activeTab === item.id ? 2.5 : 2} />
                                    <span>{item.label}</span>
                                </div>
                                {item.badge && (
                                    <span className="bg-brand-dark text-white text-[10px] font-bold px-2 py-0.5 rounded-full">
                                        {item.badge}
                                    </span>
                                )}
                            </button>
                        </li>
                    ))}
                </ul>
            </div>

            <div className="px-6 mt-6 flex-1">
                <p className="text-xs font-semibold text-gray-400 mb-4 px-2 uppercase tracking-wider">GENERAL</p>
                <ul className="space-y-1">
                    {generalItems.map((item) => (
                        <li key={item.id}>
                            <button
                                onClick={() => setActiveTab(item.id)}
                                className={`w-full flex items-center gap-3 px-3 py-2.5 transition-colors rounded-xl border border-transparent ${activeTab === item.id
                                        ? 'bg-brand-light text-brand-dark font-medium shadow-sm border border-brand-light/50'
                                        : 'text-gray-500 hover:bg-gray-50 hover:text-gray-900'
                                    }`}
                            >
                                <item.icon size={18} />
                                <span>{item.label}</span>
                            </button>
                        </li>
                    ))}
                </ul>
            </div>

            <div className="px-6 mt-auto">
                <div className="bg-gradient-to-br from-brand-dark to-brand-medium rounded-2xl p-4 text-white relative overflow-hidden shadow-lg border border-brand-dark/20">
                    <div className="absolute top-0 right-0 -mr-4 -mt-4 w-24 h-24 bg-white/10 rounded-full blur-xl mix-blend-overlay"></div>
                    <div className="absolute bottom-0 left-0 -ml-4 -mb-4 w-16 h-16 bg-white/20 rounded-full blur-lg mix-blend-overlay"></div>
                    <Target className="mb-3 text-white/90" size={24} />
                    <p className="font-semibold text-sm mb-1">Server Status</p>
                    <p className="text-xs text-white/80 mb-3">Connected to localhost:8080</p>
                    <button className="w-full py-2 bg-white/20 hover:bg-white/30 backdrop-blur-md transition-all rounded-lg text-xs font-medium border border-white/20">
                        Disconnect
                    </button>
                </div>
            </div>
        </div>
    );
};

export default Sidebar;
