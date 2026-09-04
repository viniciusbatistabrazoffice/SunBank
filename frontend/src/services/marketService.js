const COINGECKO_BASE_URL =
  import.meta.env.VITE_COINGECKO_API_URL || 'https://api.coingecko.com/api/v3';

const COIN_IDS = {
  BTC: 'bitcoin',
  ETH: 'ethereum',
  USDC: 'usd-coin',
};

const CACHE_KEY = 'sunbank_market_prices';
const CACHE_TTL = 60_000;

function getCached() {
  const raw = sessionStorage.getItem(CACHE_KEY);
  if (!raw) return null;
  try {
    const { timestamp, prices } = JSON.parse(raw);
    if (Date.now() - timestamp < CACHE_TTL) return prices;
  } catch {
    // ignore parse errors
  }
  return null;
}

function setCached(prices) {
  sessionStorage.setItem(CACHE_KEY, JSON.stringify({ timestamp: Date.now(), prices }));
}

export const marketService = {
  async getPrices(vsCurrency = 'brl') {
    const cached = getCached();
    if (cached) return cached;

    const ids = Object.values(COIN_IDS).join(',');
    const response = await fetch(
      `${COINGECKO_BASE_URL}/simple/price?ids=${ids}&vs_currencies=${vsCurrency}`
    );

    if (!response.ok) {
      throw new Error('Erro ao buscar cotações de mercado');
    }

    const data = await response.json();

    const prices = {
      BTC: data[COIN_IDS.BTC]?.[vsCurrency] || 0,
      ETH: data[COIN_IDS.ETH]?.[vsCurrency] || 0,
      USDC: data[COIN_IDS.USDC]?.[vsCurrency] || 0,
      BRL: 1,
    };

    setCached(prices);
    return prices;
  },

  async getQuote(from, to, amount) {
    const prices = await this.getPrices(to.toLowerCase());
    const rate = prices[from] || 0;
    const value = (amount || 0) * rate;
    return {
      from,
      to,
      amount,
      rate,
      value,
    };
  },
};
