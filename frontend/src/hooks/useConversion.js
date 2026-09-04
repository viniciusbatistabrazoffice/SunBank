import { useState, useCallback } from 'react';
import { conversionService } from '../services/conversionService';

export function useConversion() {
  const [quote, setQuote] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const getQuote = useCallback(async (data) => {
    setLoading(true);
    setError(null);
    try {
      const result = await conversionService.quote(data);
      setQuote(result);
      return result;
    } catch (err) {
      setError(err.message || 'Erro ao cotar conversão');
      return null;
    } finally {
      setLoading(false);
    }
  }, []);

  const convert = useCallback(async (data) => {
    setLoading(true);
    setError(null);
    try {
      const result = await conversionService.convert(data);
      setQuote(null);
      return result;
    } catch (err) {
      setError(err.message || 'Erro na conversão');
      return null;
    } finally {
      setLoading(false);
    }
  }, []);

  return { quote, loading, error, getQuote, convert };
}
