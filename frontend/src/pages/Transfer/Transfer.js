import { useEffect } from 'react';
import { TransferForm } from '../../components/crypto/TransferForm';
import { useWallet } from '../../contexts/WalletContext';
import { useTransfer } from '../../hooks/useTransfer';

export function Transfer() {
  const { balances, fetchWallet } = useWallet();
  const { fetchRecipients } = useTransfer();

  useEffect(() => {
    fetchWallet();
    fetchRecipients();
  }, [fetchWallet, fetchRecipients]);

  return (
    <main style={{ padding: '1rem' }}>
      <h1>Transferir cripto</h1>
      <TransferForm balances={balances} />
    </main>
  );
}
