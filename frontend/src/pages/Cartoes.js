import { Link } from 'react-router-dom';

function Cartoes() {
  return (
    <div style={{ padding: '2rem' }}>
      <h1>Cartões</h1>
      <p>Visualize e gerencie seus cartões virtuais.</p>
      <Link to="/dashboard">Voltar</Link>
    </div>
  );
}

export default Cartoes;
