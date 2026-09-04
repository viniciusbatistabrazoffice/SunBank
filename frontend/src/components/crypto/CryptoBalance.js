export function CryptoBalance({ balances, prices, totalBrl }) {
  return (
    <section>
      <h2>Saldo em cripto</h2>
      <p>Total em BRL: {totalBrl.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })}</p>
      <ul>
        {balances.map((balance) => {
          const price = prices[balance.currency] || 0;
          const brlValue = balance.amount * price;
          return (
            <li key={balance.currency}>
              {balance.currency}: {balance.amount} (~{' '}
              {brlValue.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })})
            </li>
          );
        })}
      </ul>
    </section>
  );
}
