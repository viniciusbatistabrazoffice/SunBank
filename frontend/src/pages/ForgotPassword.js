import { Link } from 'react-router-dom';

function ForgotPassword() {
  return (
    <div style={{ padding: '2rem' }}>
      <h1>Recuperar senha</h1>
      <p>Solicite a redefinição da sua senha.</p>
      <Link to="/">Voltar para o início</Link>
    </div>
  );
}

export default ForgotPassword;
