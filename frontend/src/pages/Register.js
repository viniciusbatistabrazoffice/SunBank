import { Link } from 'react-router-dom';

function Register() {
  return (
    <div style={{ padding: '2rem' }}>
      <h1>Cadastro</h1>
      <p>Crie sua conta no SunBank.</p>
      <Link to="/">Voltar para o início</Link>
    </div>
  );
}

export default Register;
