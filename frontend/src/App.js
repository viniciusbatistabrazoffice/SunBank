import { BrowserRouter, Routes, Route, Link } from 'react-router-dom';
import Login from './pages/Login';
import Register from './pages/Register';
import ForgotPassword from './pages/ForgotPassword';
import Dashboard from './pages/Dashboard';
import Operacoes from './pages/Operacoes';
import Carteira from './pages/Carteira';
import Cartoes from './pages/Cartoes';
import NotFound from './pages/NotFound';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login />} />
        <Route path="/cadastro" element={<Register />} />
        <Route path="/recuperar-senha" element={<ForgotPassword />} />
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/operacoes" element={<Operacoes />} />
        <Route path="/carteira" element={<Carteira />} />
        <Route path="/cartoes" element={<Cartoes />} />
        <Route path="*" element={<NotFound />} />
      </Routes>
    </BrowserRouter>
  );
}

function Home() {
  return (
    <div style={{ padding: '2rem' }}>
      <h1>SunBank</h1>
      <nav>
        <ul>
          <li><Link to="/login">Login</Link></li>
          <li><Link to="/cadastro">Cadastro</Link></li>
          <li><Link to="/recuperar-senha">Recuperar senha</Link></li>
          <li><Link to="/dashboard">Dashboard</Link></li>
        </ul>
      </nav>
    </div>
  );
}

export default App;
