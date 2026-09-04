import { Link } from 'react-router-dom';

function Carteira() {
  return (
    <div style={{ padding: '2rem' }}>
      <h1>Carteira</h1>
      <p>Consulte seus saldos e ativos.</p>
      <Link to="/dashboard">Voltar</Link>
    </div>
  );
}

export default Carteira;
