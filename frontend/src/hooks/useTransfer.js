import { useState, useCallback } from 'react';
import { transferService } from '../services/transferService';

export function useTransfer() {
  const [recipients, setRecipients] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const fetchRecipients = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await transferService.getRecipients();
      setRecipients(data);
    } catch (err) {
      setError(err.message || 'Erro ao carregar favorecidos');
    } finally {
      setLoading(false);
    }
  }, []);

  const sendCrypto = useCallback(async (data) => {
    setLoading(true);
    setError(null);
    try {
      return await transferService.sendCrypto(data);
    } catch (err) {
      setError(err.message || 'Erro ao transferir cripto');
      return null;
    } finally {
      setLoading(false);
    }
  }, []);

  const withdrawBrl = useCallback(async (data) => {
    setLoading(true);
    setError(null);
    try {
      return await transferService.withdrawBrl(data);
    } catch (err) {
      setError(err.message || 'Erro ao sacar BRL');
      return null;
    } finally {
      setLoading(false);
    }
  }, []);

  return { recipients, loading, error, fetchRecipients, sendCrypto, withdrawBrl };
}
