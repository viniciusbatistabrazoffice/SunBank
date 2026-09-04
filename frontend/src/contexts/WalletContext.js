import { createContext, useContext, useState, useCallback, useMemo } from 'react';
import { cryptoService } from '../services/cryptoService';

const WalletContext = createContext(null);

const defaultCards = [
  {
    id: 'debit-1',
    type: 'debit',
    label: 'Cartão de Débito',
    brand: 'Visa',
    number: '4532 1234 5678 9012',
    holder: 'Usuário SunBank',
    expiry: '12/28',
    cvv: '***',
    balance: 5420.5,
  },
  {
    id: 'credit-1',
    type: 'credit',
    label: 'Cartão de Crédito',
    brand: 'Mastercard',
    number: '5500 9876 5432 1098',
    holder: 'Usuário SunBank',
    expiry: '08/27',
    cvv: '***',
    limit: 10000,
    used: 3240.75,
    status: 'Ativo',
    dueDate: '10/10/2026',
    statement: 1840.3,
    lastDigits: '1098',
  },
];

export function WalletProvider({ children }) {
  const [balances, setBalances] = useState([]);
  const [prices, setPrices] = useState({});
  const [cards, setCards] = useState(defaultCards);
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

  const totalCardsBalance = useMemo(() => {
    return cards.reduce((sum, card) => {
      if (card.type === 'debit') return sum + (card.balance || 0);
      if (card.type === 'credit') return sum + ((card.limit || 0) - (card.used || 0));
      return sum;
    }, 0);
  }, [cards]);

  const value = useMemo(
    () => ({
      balances,
      prices,
      cards,
      loading,
      error,
      fetchWallet,
      totalBrl,
      totalCardsBalance,
    }),
    [balances, prices, cards, loading, error, fetchWallet, totalBrl, totalCardsBalance]
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
