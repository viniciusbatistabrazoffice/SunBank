import { Link } from 'react-router-dom';

function Dashboard() {
  return (
    <div style={{ padding: '2rem' }}>
      <h1>Dashboard</h1>
      <p>Visão geral da conta SunBank.</p>
      <nav>
        <ul>
          <li><Link to="/operacoes">Operações</Link></li>
          <li><Link to="/carteira">Carteira</Link></li>
          <li><Link to="/cartoes">Cartões</Link></li>
          <li><Link to="/">Sair</Link></li>
        </ul>
      </nav>
    </div>
  );
}

export default Dashboard;
