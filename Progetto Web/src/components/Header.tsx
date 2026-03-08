import { Search, Mail, Bell } from 'lucide-react';

const Header: React.FC = () => {
    return (
        <div className="h-20 flex items-center justify-between px-8 bg-brand-bg/80 backdrop-blur-md sticky top-0 z-10 border-b border-gray-100/50">
            <div className="flex-1 max-w-lg">
                <div className="relative group">
                    <div className="absolute inset-y-0 left-0 pl-4 flex items-center pointer-events-none text-gray-400 group-focus-within:text-brand-dark transition-colors">
                        <Search size={18} />
                    </div>
                    <input
                        type="text"
                        className="w-full pl-11 pr-14 py-2.5 bg-white border border-gray-200 rounded-2xl text-sm focus:outline-none focus:ring-2 focus:ring-brand-light focus:border-brand-medium transition-all shadow-sm placeholder:text-gray-400 font-medium"
                        placeholder="Search datasets, history..."
                    />
                    <div className="absolute inset-y-0 right-0 pr-3 flex items-center pointer-events-none">
                        <span className="text-xs font-semibold text-gray-400 border border-gray-200 px-2 py-1 rounded-md bg-gray-50 uppercase shadow-sm">⌘ F</span>
                    </div>
                </div>
            </div>

            <div className="flex items-center gap-5 ml-4">
                <button className="w-10 h-10 rounded-full flex items-center justify-center bg-white border border-gray-200 text-gray-500 hover:text-brand-dark hover:border-brand-medium hover:bg-brand-light/20 transition-all shadow-sm relative">
                    <Mail size={18} />
                    <span className="absolute top-2 right-2.5 w-2 h-2 bg-red-500 rounded-full border-2 border-white"></span>
                </button>
                <button className="w-10 h-10 rounded-full flex items-center justify-center bg-white border border-gray-200 text-gray-500 hover:text-brand-dark hover:border-brand-medium hover:bg-brand-light/20 transition-all shadow-sm">
                    <Bell size={18} />
                </button>

                <div className="flex items-center gap-3 pl-4 border-l border-gray-200">
                    <div className="w-10 h-10 rounded-full bg-gradient-to-tr from-brand-medium to-brand-dark p-[2px] shadow-sm">
                        <img
                            src="https://api.dicebear.com/7.x/avataaars/svg?seed=Felix&backgroundColor=f3f4f6"
                            alt="User"
                            className="w-full h-full rounded-full border-2 border-white bg-white object-cover"
                        />
                    </div>
                    <div className="hidden md:block">
                        <p className="text-sm font-semibold text-gray-900 leading-tight">Admin User</p>
                        <p className="text-xs text-gray-500 font-medium">admin@knn.local</p>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Header;
