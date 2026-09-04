import { Link } from 'react-router-dom';

export function Header() {
  return (
    <header style={{ padding: '1rem', borderBottom: '1px solid #ddd' }}>
      <nav style={{ display: 'flex', gap: '1rem' }}>
        <Link to="/">SunBank</Link>
        <Link to="/">Dashboard</Link>
        <Link to="/convert">Converter</Link>
        <Link to="/transfer">Transferir</Link>
      </nav>
    </header>
  );
}
