import { useState } from 'react';
import { useConversion } from '../../hooks/useConversion';

export function ConversionForm({ balances }) {
  const [from, setFrom] = useState('');
  const [amount, setAmount] = useState('');
  const { quote, loading, error, getQuote, convert } = useConversion();

  async function handleQuote(e) {
    e.preventDefault();
    await getQuote({ from, to: 'BRL', amount: Number(amount) });
  }

  async function handleConvert() {
    const result = await convert({ from, to: 'BRL', amount: Number(amount) });
    if (result) {
      setFrom('');
      setAmount('');
    }
  }

  return (
    <form onSubmit={handleQuote}>
      <h2>Converter para BRL</h2>
      <label>
        Criptomoeda:
        <select value={from} onChange={(e) => setFrom(e.target.value)} required>
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
      <button type="submit" disabled={loading}>
        Cotar
      </button>
      {quote && (
        <div>
          <p>Cotação: {quote.value.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })}</p>
          <button type="button" onClick={handleConvert} disabled={loading}>
            Confirmar conversão
          </button>
        </div>
      )}
      {error && <p style={{ color: 'red' }}>{error}</p>}
    </form>
  );
}
