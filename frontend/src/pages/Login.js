import { Link } from 'react-router-dom';

function Login() {
  return (
    <div style={{ padding: '2rem' }}>
      <h1>Login</h1>
      <p>Página de autenticação do SunBank.</p>
      <Link to="/">Voltar para o início</Link>
    </div>
  );
}

export default Login;
