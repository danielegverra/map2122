import React from 'react';
import { Database, PlayCircle, Info, ChevronRight, Calculator, FileText } from 'lucide-react';

const Help: React.FC = () => {
    return (
        <div className="p-8 max-w-5xl mx-auto pb-20">
            <div className="mb-10">
                <h1 className="text-3xl font-bold text-gray-900 mb-2 tracking-tight">Support & Documentation</h1>
                <p className="text-gray-500 font-medium text-lg">Tutto ciò che c'è da sapere sull'algoritmo KNN e l'utilizzo della piattaforma.</p>
            </div>

            <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">

                {/* Left Column: Main Explanations */}
                <div className="lg:col-span-2 space-y-8">

                    {/* Section 1: Intro */}
                    <div className="bg-white p-8 rounded-3xl shadow-sm border border-gray-100 relative overflow-hidden">
                        <div className="absolute top-0 left-0 w-2 h-full bg-brand-medium"></div>
                        <div className="flex items-center gap-4 mb-6">
                            <div className="w-12 h-12 bg-brand-light text-brand-dark rounded-2xl flex items-center justify-center">
                                <Info size={24} />
                            </div>
                            <h2 className="text-2xl font-bold text-gray-900">Scopo del Progetto</h2>
                        </div>
                        <p className="text-gray-600 leading-relaxed mb-4 text-[15px]">
                            L'obiettivo di questa applicazione è implementare il noto algoritmo di Machine Learning <strong>K-Nearest Neighbors (KNN)</strong> per task di <strong>Regressione</strong>.
                            Dato un insieme di dati pregressi (il <em>Training Set</em>), il software è in grado di stimare/prevedere il valore ignoto (il <em>Target Continuo</em>) di un nuovo elemento inserito ("query"), calcolandone la distanza rispetto ai dati già noti.
                        </p>
                        <p className="text-gray-600 leading-relaxed text-[15px]">
                            A differenza dei classici algoritmi di classificazione, questo applicativo restituisce una predizione puramente numerica derivata da una simulazione di prossimità statistica.
                        </p>
                    </div>

                    {/* Section 2: Algorithm */}
                    <div className="bg-white p-8 rounded-3xl shadow-sm border border-gray-100">
                        <div className="flex items-center gap-4 mb-6">
                            <div className="w-12 h-12 bg-gray-50 text-gray-700 rounded-2xl flex items-center justify-center border border-gray-200">
                                <Calculator size={24} />
                            </div>
                            <h2 className="text-2xl font-bold text-gray-900">Come funziona l'algoritmo KNN?</h2>
                        </div>
                        <div className="space-y-6">
                            <div className="flex gap-4">
                                <div className="flex-shrink-0 w-8 h-8 bg-brand-dark text-white rounded-full flex items-center justify-center font-bold text-sm">1</div>
                                <div>
                                    <h4 className="font-bold text-gray-900 mb-1">Distanza dei dati (Similarity)</h4>
                                    <p className="text-gray-600 text-sm leading-relaxed">
                                        Per prima cosa, l'algoritmo calcola "quanto è distante" la tua query da tutti gli altri esempi nel dataset. Se l'attributo testato è <em>discreto</em> (es. una lettera), viene calcolata la distanza di Hamming (0 se sono uguali, 1 se sono diversi). Se l'attributo è <em>continuo</em> (numero), si utilizza la procedura logica dello Scaling (Min-Max) per non falsare le cifre.
                                    </p>
                                </div>
                            </div>
                            <div className="flex gap-4">
                                <div className="flex-shrink-0 w-8 h-8 bg-brand-dark text-white rounded-full flex items-center justify-center font-bold text-sm">2</div>
                                <div>
                                    <h4 className="font-bold text-gray-900 mb-1">La ricerca dei K-vicini</h4>
                                    <p className="text-gray-600 text-sm leading-relaxed">
                                        Dopo aver calcolato tutte le distanze, il codice le ordina in senso crescente e isola esattamente <code>K</code> esempi (ossia quelli "più vicini" per caratteristiche alla query dell'utente). Il fattore <em>K</em> è deciso dall'utente.
                                    </p>
                                </div>
                            </div>
                            <div className="flex gap-4">
                                <div className="flex-shrink-0 w-8 h-8 bg-brand-dark text-white rounded-full flex items-center justify-center font-bold text-sm">3</div>
                                <div>
                                    <h4 className="font-bold text-gray-900 mb-1">Il calcolo della Media (Regressione)</h4>
                                    <p className="text-gray-600 text-sm leading-relaxed">
                                        In fase output, prende il valore <code>Target</code> di tali <code>K</code> elementi vicini trovati precedentemente e ne estrae la media matematica. Questo numero reale estratto sarà la predizione finale.
                                    </p>
                                </div>
                            </div>
                        </div>
                    </div>

                    {/* Section 3: Usage guide */}
                    <div className="bg-gradient-to-br from-brand-dark to-[#165B33] p-8 rounded-3xl shadow-xl text-white relative overflow-hidden">
                        <div className="absolute top-0 right-0 -mr-10 -mt-10 w-40 h-40 bg-white/10 rounded-full blur-2xl"></div>
                        <div className="flex items-center gap-4 mb-8 relative z-10">
                            <div className="w-12 h-12 bg-white/20 text-white rounded-2xl flex items-center justify-center border border-white/20 backdrop-blur-md">
                                <PlayCircle size={24} />
                            </div>
                            <h2 className="text-2xl font-bold text-white">Guida rapida all'utilizzo</h2>
                        </div>

                        <div className="grid grid-cols-1 md:grid-cols-2 gap-6 relative z-10">
                            <div className="bg-black/20 p-5 rounded-2xl border border-white/10">
                                <h3 className="font-bold text-brand-light mb-2 flex items-center gap-2"><ChevronRight size={16} /> 1. Dataset Name</h3>
                                <p className="text-white/80 text-sm leading-relaxed">Inserisci nel primo campo il nome esatto del file privo di estensione (es. <code>servo</code>). Il server lo andrà a prendere direttamente dalla cartella nativa <em>map2122/File/</em>.</p>
                            </div>

                            <div className="bg-black/20 p-5 rounded-2xl border border-white/10">
                                <h3 className="font-bold text-brand-light mb-2 flex items-center gap-2"><ChevronRight size={16} /> 2. Valore K</h3>
                                <p className="text-white/80 text-sm leading-relaxed">Assegna un numero intero al valore K (es. <code>3</code>). Un valore troppo alto rende la media troppo piatta, uno troppo basso espone ai rumori.</p>
                            </div>

                            <div className="bg-black/20 p-5 rounded-2xl border border-white/10 md:col-span-2">
                                <h3 className="font-bold text-brand-light mb-2 flex items-center gap-2"><ChevronRight size={16} /> 3. Query Attributes</h3>
                                <p className="text-white/80 text-sm leading-relaxed">
                                    Questa è la riga che vuoi valutare! Inserisci qui le feature (attributi) separate da <strong>virgola (,)</strong>.<br />
                                    Devi inserirle nell'ordine esatto dello schema. Ad esempio se lo schema ha una colonna lettera e due numeri, devi inserire <code>A, 5.0, 7.3</code>.
                                </p>
                            </div>
                        </div>
                    </div>

                </div>

                {/* Right Column: Datasets Available */}
                <div className="space-y-6">
                    <div className="bg-white p-6 rounded-3xl shadow-sm border border-gray-100">
                        <div className="flex items-center gap-3 mb-6 pb-4 border-b border-gray-100">
                            <Database size={20} className="text-brand-medium" />
                            <h3 className="font-bold text-gray-900">Dataset Pronti</h3>
                        </div>

                        <p className="text-sm text-gray-500 mb-5">Ecco i training set testuali precaricati nella repository, utilizzabili fin da subito e da cui ricavare esempi per il caricamento in dashboard:</p>

                        <div className="space-y-4">
                            {/* provaC */}
                            <div className="group border border-gray-200 rounded-2xl p-4 hover:border-brand-medium hover:shadow-md transition-all">
                                <div className="flex justify-between items-start mb-2">
                                    <h4 className="font-bold text-brand-dark flex items-center gap-2"><FileText size={14} /> provaC.dat</h4>
                                    <span className="text-[10px] bg-gray-100 text-gray-600 font-bold px-2 py-1 rounded-md">Base</span>
                                </div>
                                <div className="text-xs text-gray-600 space-y-1 bg-gray-50 p-3 rounded-xl mt-3">
                                    <p><strong>Schema (2 Attr):</strong> X (Discreto), Y (Continuo)</p>
                                    <p><strong>Target:</strong> Continuo</p>
                                    <p className="text-gray-400 mt-2 block pt-2 border-t border-gray-200">
                                        <em>Esempio input da provare:<br /><code className="text-brand-medium font-medium">A, 5.5</code></em>
                                    </p>
                                </div>
                            </div>

                            {/* servo.dat */}
                            <div className="group border border-gray-200 rounded-2xl p-4 hover:border-brand-medium hover:shadow-md transition-all">
                                <div className="flex justify-between items-start mb-2">
                                    <h4 className="font-bold text-brand-dark flex items-center gap-2"><FileText size={14} /> servo.dat</h4>
                                    <span className="text-[10px] bg-brand-light text-brand-dark border border-brand-medium/20 font-bold px-2 py-1 rounded-md">Complesso (167 Righe)</span>
                                </div>
                                <div className="text-xs text-gray-600 space-y-1 bg-gray-50 p-3 rounded-xl mt-3">
                                    <p><strong>Schema (4 Attr):</strong> motor (Disc), screw (Disc), pgain (Cont), vgain (Cont)</p>
                                    <p><strong>Target:</strong> Continuo</p>
                                    <p className="text-gray-400 mt-2 block pt-2 border-t border-gray-200">
                                        <em>Esempio input da provare:<br /><code className="text-brand-medium font-medium">C, E, 4.0, 1.0</code></em>
                                    </p>
                                </div>
                            </div>

                            {/* simple.dat */}
                            <div className="group border border-gray-200 rounded-2xl p-4 hover:border-brand-medium hover:shadow-md transition-all">
                                <div className="flex justify-between items-start mb-2">
                                    <h4 className="font-bold text-brand-dark flex items-center gap-2"><FileText size={14} /> simple.dat</h4>
                                    <span className="text-[10px] bg-gray-100 text-gray-600 font-bold px-2 py-1 rounded-md">Base</span>
                                </div>
                                <div className="text-xs text-gray-600 space-y-1 bg-gray-50 p-3 rounded-xl mt-3">
                                    <p><strong>Schema (2 Attr):</strong> X (Discreto), Y (Continuo)</p>
                                    <p><strong>Target:</strong> Continuo</p>
                                    <p className="text-gray-400 mt-2 block pt-2 border-t border-gray-200">
                                        <em>Esempio input da provare:<br /><code className="text-brand-medium font-medium">B, 12</code></em>
                                    </p>
                                </div>
                            </div>
                        </div>

                    </div>
                </div>

            </div>
        </div>
    );
};

export default Help;
