import { Link } from 'react-router-dom';

function Operacoes() {
  return (
    <div style={{ padding: '2rem' }}>
      <h1>Operações</h1>
      <p>Gerencie suas operações bancárias.</p>
      <Link to="/dashboard">Voltar</Link>
    </div>
  );
}

export default Operacoes;
