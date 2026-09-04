const prices = {
  BTC: 350000,
  ETH: 18000,
  BRL: 1,
  USDC: 5.5,
};

const balances = [
  { currency: 'BTC', amount: 0.42 },
  { currency: 'ETH', amount: 3.1 },
  { currency: 'BRL', amount: 12450 },
  { currency: 'USDC', amount: 1500 },
];

const currencyNames = {
  BTC: 'Bitcoin',
  ETH: 'Ethereum',
  BRL: 'Real',
  USDC: 'USD Coin',
};

const recipients = [
  { id: '1', name: 'João Silva' },
  { id: '2', name: 'Maria Souza' },
  { id: '3', name: 'Crypto Safe Inc.' },
];

function delay(ms = 300) {
  return new Promise((resolve) => setTimeout(resolve, ms));
}

function parseBody(options) {
  if (!options.body) return {};
  try {
    return JSON.parse(options.body);
  } catch {
    return {};
  }
}

export async function mockRequest(path, options = {}) {
  await delay(300);

  switch (path) {
    case '/wallet/balances':
      return balances.map((b) => ({ ...b }));

    case '/crypto/currencies':
      return Object.keys(prices).map((code) => ({
        code,
        name: currencyNames[code] || code,
      }));

    case '/crypto/prices':
      return { ...prices };

    case '/transfers/recipients':
      return recipients.map((r) => ({ ...r }));

    case '/transfers/crypto': {
      const data = parseBody(options);
      return {
        id: `tx-${Date.now()}`,
        status: 'completed',
        amount: data.amount,
        currency: data.currency,
        recipient: data.recipient,
        bank: data.bank,
        agency: data.agency,
        accountNumber: data.accountNumber,
      };
    }

    case '/transfers/withdraw': {
      const data = parseBody(options);
      return {
        id: `wd-${Date.now()}`,
        status: 'completed',
        amount: data.amount,
      };
    }

    case '/conversions/quote': {
      const data = parseBody(options);
      const rate = prices[data.from] || 0;
      const value = (data.amount || 0) * rate;
      return {
        from: data.from,
        to: data.to,
        amount: data.amount,
        rate,
        value,
      };
    }

    case '/conversions': {
      const data = parseBody(options);
      const rate = prices[data.from] || 0;
      const value = (data.amount || 0) * rate;
      return {
        id: `conv-${Date.now()}`,
        from: data.from,
        to: data.to,
        amount: data.amount,
        rate,
        value,
        accountNumber: data.accountNumber,
        agency: data.agency,
      };
    }

    default:
      throw new Error(`Endpoint não mockado: ${path}`);
  }
}
