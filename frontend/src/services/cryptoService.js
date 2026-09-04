import { request } from './api';
import { marketService } from './marketService';

export const cryptoService = {
  async getBalances() {
    return request('/wallet/balances');
  },

  async getSupportedCurrencies() {
    return request('/crypto/currencies');
  },

  async getMarketPrices() {
    try {
      return await marketService.getPrices('brl');
    } catch {
      return request('/crypto/prices');
    }
  },
};
