import React from 'react'
import {BrowserRouter as Router, Navigate, Route, Routes} from 'react-router-dom'
import {PrimeReactProvider} from 'primereact/api'
import 'primereact/resources/themes/lara-light-indigo/theme.css'
import 'primereact/resources/primereact.min.css'
import 'primeicons/primeicons.css'
import 'primeflex/primeflex.css'

import Header from './components/Header'
import MovieListPage from './pages/movie/MovieListPage.jsx'
import SpecialOperationsPage from './pages/SpecialOperationsPage'
import PersonListPage from "./pages/person/PersonListPage.jsx";
import ImportOperationPage from "./pages/import/ImportOperationPage.jsx";

function App() {
    return (
        <PrimeReactProvider>
            <Router>
                <div className="App">
                    <Header/>
                    <div className="container">
                        <Routes>
                            <Route path="/" element={<MovieListPage/>}/>
                            <Route path="/movies" element={<MovieListPage/>}/>
                            <Route path="/people" element={<PersonListPage />} />
                            <Route path="/import-operations" element={<ImportOperationPage />} />
                            <Route path="/special-operations" element={<SpecialOperationsPage/>}/>
                            <Route path="*" element={<Navigate to="/" replace/>}/>
                        </Routes>
                    </div>
                </div>
            </Router>
        </PrimeReactProvider>
    )
}

export default App