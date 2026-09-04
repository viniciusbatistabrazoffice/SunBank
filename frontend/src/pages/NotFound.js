import { Link } from 'react-router-dom';

function NotFound() {
  return (
    <div style={{ padding: '2rem' }}>
      <h1>404 - Página não encontrada</h1>
      <Link to="/">Ir para o início</Link>
    </div>
  );
}

export default NotFound;
