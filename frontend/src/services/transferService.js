import { request } from './api';

export const transferService = {
  async getRecipients() {
    return request('/transfers/recipients');
  },

  async sendCrypto(data) {
    return request('/transfers/crypto', {
      method: 'POST',
      body: JSON.stringify(data),
    });
  },

  async withdrawBrl(data) {
    return request('/transfers/withdraw', {
      method: 'POST',
      body: JSON.stringify(data),
    });
  },
};
