import { useEffect } from 'react';
import { ConversionForm } from '../../components/crypto/ConversionForm';
import { useWallet } from '../../contexts/WalletContext';

export function Convert() {
  const { balances, fetchWallet } = useWallet();

  useEffect(() => {
    fetchWallet();
  }, [fetchWallet]);

  return (
    <main style={{ padding: '1rem' }}>
      <h1>Converter cripto para BRL</h1>
      <ConversionForm balances={balances} />
    </main>
  );
}
