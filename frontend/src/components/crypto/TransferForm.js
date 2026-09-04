import { useState } from 'react';
import { useTransfer } from '../../hooks/useTransfer';

export function TransferForm({ balances }) {
  const [currency, setCurrency] = useState('');
  const [amount, setAmount] = useState('');
  const [recipient, setRecipient] = useState('');
  const { recipients, loading, error, sendCrypto } = useTransfer();

  async function handleSubmit(e) {
    e.preventDefault();
    await sendCrypto({ currency, amount: Number(amount), recipient });
    setAmount('');
  }

  return (
    <form onSubmit={handleSubmit}>
      <h2>Transferir cripto</h2>
      <label>
        Criptomoeda:
        <select value={currency} onChange={(e) => setCurrency(e.target.value)} required>
          <option value="">Selecione</option>
          {balances.map((b) => (
            <option key={b.currency} value={b.currency}>
              {b.currency}
            </option>
          ))}
        </select>
      </label>
      <label>
        Quantidade:
        <input
          type="number"
          step="any"
          value={amount}
          onChange={(e) => setAmount(e.target.value)}
          required
        />
      </label>
      <label>
        Favorecido:
        <select value={recipient} onChange={(e) => setRecipient(e.target.value)} required>
          <option value="">Selecione</option>
          {recipients.map((r) => (
            <option key={r.id} value={r.id}>
              {r.name}
            </option>
          ))}
        </select>
      </label>
      <button type="submit" disabled={loading}>
        Transferir
      </button>
      {error && <p style={{ color: 'red' }}>{error}</p>}
    </form>
  );
}
