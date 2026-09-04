import { request } from './api';

export const cryptoService = {
  async getBalances() {
    return request('/wallet/balances');
  },

  async getSupportedCurrencies() {
    return request('/crypto/currencies');
  },

  async getMarketPrices() {
    return request('/crypto/prices');
  },
};
