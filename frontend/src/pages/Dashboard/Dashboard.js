import { useEffect } from 'react';
import { CryptoBalance } from '../../components/crypto/CryptoBalance';
import { useWallet } from '../../contexts/WalletContext';

export function Dashboard() {
  const { balances, prices, loading, error, fetchWallet, totalBrl } = useWallet();

  useEffect(() => {
    fetchWallet();
  }, [fetchWallet]);

  return (
    <main style={{ padding: '1rem' }}>
      <h1>Dashboard</h1>
      {loading && <p>Carregando...</p>}
      {error && <p style={{ color: 'red' }}>{error}</p>}
      <CryptoBalance balances={balances} prices={prices} totalBrl={totalBrl} />
    </main>
  );
}
