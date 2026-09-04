import { useState, useCallback } from 'react';
import { cryptoService } from '../services/cryptoService';

export function useCrypto() {
  const [currencies, setCurrencies] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const fetchCurrencies = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await cryptoService.getSupportedCurrencies();
      setCurrencies(data);
    } catch (err) {
      setError(err.message || 'Erro ao carregar criptomoedas');
    } finally {
      setLoading(false);
    }
  }, []);

  return { currencies, loading, error, fetchCurrencies };
}
