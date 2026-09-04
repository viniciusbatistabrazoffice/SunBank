import { request } from './api';

export const conversionService = {
  async quote(data) {
    return request('/conversions/quote', {
      method: 'POST',
      body: JSON.stringify(data),
    });
  },

  async convert(data) {
    return request('/conversions', {
      method: 'POST',
      body: JSON.stringify(data),
    });
  },
};
