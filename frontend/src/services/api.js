const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:3001/api';

export async function request(path, options = {}) {
  const url = `${API_BASE_URL}${path}`;

  const config = {
    headers: {
      'Content-Type': 'application/json',
      ...options.headers,
    },
    ...options,
  };

  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }

  const response = await fetch(url, config);

  if (!response.ok) {
    const error = await response.json().catch(() => ({}));
    throw new Error(error.message || `Erro ${response.status}: ${response.statusText}`);
  }

  return response.status === 204 ? null : response.json();
}
