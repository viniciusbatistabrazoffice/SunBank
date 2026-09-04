import { createContext, useContext, useState, useCallback, useMemo } from 'react';
import { cryptoService } from '../services/cryptoService';

const WalletContext = createContext(null);

export function WalletProvider({ children }) {
  const [balances, setBalances] = useState([]);
  const [prices, setPrices] = useState({});
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const fetchWallet = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const [balancesData, pricesData] = await Promise.all([
        cryptoService.getBalances(),
        cryptoService.getMarketPrices(),
      ]);
      setBalances(balancesData);
      setPrices(pricesData);
    } catch (err) {
      setError(err.message || 'Erro ao carregar carteira');
    } finally {
      setLoading(false);
    }
  }, []);

  const totalBrl = useMemo(() => {
    return balances.reduce((sum, balance) => {
      const price = prices[balance.currency] || 0;
      return sum + balance.amount * price;
    }, 0);
  }, [balances, prices]);

  const value = useMemo(
    () => ({
      balances,
      prices,
      loading,
      error,
      fetchWallet,
      totalBrl,
    }),
    [balances, prices, loading, error, fetchWallet, totalBrl]
  );

  return <WalletContext.Provider value={value}>{children}</WalletContext.Provider>;
}

export function useWallet() {
  const context = useContext(WalletContext);
  if (!context) {
    throw new Error('useWallet deve ser usado dentro de WalletProvider');
  }
  return context;
}
