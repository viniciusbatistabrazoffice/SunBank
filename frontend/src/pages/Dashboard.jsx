import { useEffect, useMemo } from 'react';
import { useNavigate } from 'react-router-dom';
import styled from 'styled-components';
import {
  FaWallet,
  FaCoins,
  FaTrophy,
  FaBitcoin,
  FaEthereum,
  FaSyncAlt,
  FaExchangeAlt,
  FaPaperPlane,
} from 'react-icons/fa';
import {
  PieChart,
  Pie,
  Cell,
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer,
  Legend,
} from 'recharts';
import { Button } from '../components/Button';
import { Card, CardTitle, CardValue } from '../components/Card';
import { Header, PageTitle, Grid } from '../components/Layout';
import { useWallet } from '../contexts/WalletContext';

const Section = styled.section`
  margin-top: ${({ theme }) => theme.spacing.xl};
`;

const SectionTitle = styled.h2`
  font-size: ${({ theme }) => theme.fontSizes.lg};
  font-weight: 600;
  color: ${({ theme }) => theme.colors.text};
  margin-bottom: ${({ theme }) => theme.spacing.lg};
`;

const Status = styled.p`
  font-size: ${({ theme }) => theme.fontSizes.md};
  color: ${({ theme }) => theme.colors.primary};
  display: flex;
  align-items: center;
  gap: ${({ theme }) => theme.spacing.sm};
`;

const Error = styled.p`
  font-size: ${({ theme }) => theme.fontSizes.md};
  color: ${({ theme }) => theme.colors.danger};
`;

const Spinner = styled(FaSyncAlt)`
  animation: spin 1s linear infinite;

  @keyframes spin {
    from {
      transform: rotate(0deg);
    }
    to {
      transform: rotate(360deg);
    }
  }
`;

const AssetName = styled.span`
  font-size: ${({ theme }) => theme.fontSizes.sm};
  color: ${({ theme }) => theme.colors.textMuted};
`;

const QuickActions = styled.div`
  display: flex;
  flex-wrap: wrap;
  gap: ${({ theme }) => theme.spacing.md};
  margin-top: ${({ theme }) => theme.spacing.md};
`;

const ChartContainer = styled.div`
  height: 280px;
  width: 100%;
  margin-top: ${({ theme }) => theme.spacing.md};
`;

const ChartsGrid = styled.div`
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(320px, 1fr));
  gap: ${({ theme }) => theme.spacing.lg};
`;

const CHART_COLORS = ['#F5B800', '#3B82F6', '#22C55E', '#627EEA', '#EF4444', '#8A929F'];

const tooltipStyle = {
  backgroundColor: '#121720',
  border: '1px solid #222834',
  borderRadius: '8px',
};

function formatCurrency(value) {
  if (value === undefined || value === null) return '—';
  return value.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
}

function formatAmount(amount, currency) {
  const digits = currency === 'BRL' || currency === 'USDC' ? 2 : 8;
  return amount.toLocaleString('pt-BR', {
    minimumFractionDigits: 0,
    maximumFractionDigits: digits,
  });
}

export function Dashboard() {
  const navigate = useNavigate();
  const { balances, prices, totalBrl, loading, error, fetchWallet } = useWallet();

  useEffect(() => {
    fetchWallet();
  }, [fetchWallet]);

  const topAsset = useMemo(() => {
    if (!balances.length || !totalBrl) return null;
    return [...balances]
      .map((b) => ({ ...b, brlValue: b.amount * (prices[b.currency] || 0) }))
      .sort((a, b) => b.brlValue - a.brlValue)[0];
  }, [balances, prices, totalBrl]);

  const portfolioData = useMemo(() => {
    if (!totalBrl) return [];
    return balances
      .map((b) => ({
        currency: b.currency,
        value: b.amount * (prices[b.currency] || 0),
      }))
      .filter((d) => d.value > 0);
  }, [balances, prices, totalBrl]);

  const stats = [
    {
      title: 'Saldo Total',
      value: formatCurrency(totalBrl),
      icon: FaWallet,
      color: '#22C55E',
    },
    {
      title: 'Ativos na Carteira',
      value: balances.length.toString(),
      icon: FaCoins,
      color: '#3B82F6',
    },
    topAsset && {
      title: 'Maior Posição',
      value: `${topAsset.currency} ${formatAmount(topAsset.amount, topAsset.currency)}`,
      icon: FaTrophy,
      color: '#F5B800',
    },
    {
      title: 'Cotação BTC',
      value: formatCurrency(prices.BTC),
      icon: FaBitcoin,
      color: '#F7931A',
    },
    {
      title: 'Cotação ETH',
      value: formatCurrency(prices.ETH),
      icon: FaEthereum,
      color: '#627EEA',
    },
  ].filter(Boolean);

  return (
    <>
      <Header>
        <PageTitle>Dashboard</PageTitle>
        <Button $variant="primary" onClick={() => navigate('/transfer')}>
          Nova Transferência
        </Button>
      </Header>

      {loading && (
        <Status>
          <Spinner size={18} />
          Atualizando informações...
        </Status>
      )}

      {error && <Error>{error}</Error>}

      <Grid>
        {stats.map(({ title, value, icon: Icon, color }) => (
          <Card key={title}>
            <CardTitle>
              <Icon size={14} style={{ marginRight: '8px', color }} />
              {title}
            </CardTitle>
            <CardValue>{value}</CardValue>
          </Card>
        ))}
      </Grid>

      <Section>
        <SectionTitle>Seus ativos</SectionTitle>
        <Grid>
          {balances.map((balance) => {
            const price = prices[balance.currency] || 0;
            const brlValue = balance.amount * price;
            const share = totalBrl > 0 ? (brlValue / totalBrl) * 100 : 0;

            return (
              <Card key={balance.currency}>
                <CardTitle>{balance.currency}</CardTitle>
                <CardValue>{formatCurrency(brlValue)}</CardValue>
                <AssetName>
                  {formatAmount(balance.amount, balance.currency)} {balance.currency} ·{' '}
                  {share.toFixed(1)}% da carteira
                </AssetName>
              </Card>
            );
          })}
        </Grid>
      </Section>

      <Section>
        <SectionTitle>Gráficos</SectionTitle>
        <ChartsGrid>
          <Card>
            <CardTitle>Alocação da carteira</CardTitle>
            <ChartContainer>
              <ResponsiveContainer width="100%" height="100%">
                <PieChart>
                  <Pie
                    data={portfolioData}
                    dataKey="value"
                    nameKey="currency"
                    cx="50%"
                    cy="50%"
                    outerRadius={80}
                    innerRadius={40}
                    paddingAngle={3}
                  >
                    {portfolioData.map((_, index) => (
                      <Cell
                        key={`cell-${index}`}
                        fill={CHART_COLORS[index % CHART_COLORS.length]}
                      />
                    ))}
                  </Pie>
                  <Tooltip
                    formatter={(value) => formatCurrency(value)}
                    contentStyle={tooltipStyle}
                    itemStyle={{ color: '#F0F2F5' }}
                    labelStyle={{ color: '#F0F2F5' }}
                  />
                  <Legend
                    wrapperStyle={{ color: '#F0F2F5' }}
                    formatter={(value) => (
                      <span style={{ color: '#F0F2F5' }}>{value}</span>
                    )}
                  />
                </PieChart>
              </ResponsiveContainer>
            </ChartContainer>
          </Card>

          <Card>
            <CardTitle>Valor por ativo</CardTitle>
            <ChartContainer>
              <ResponsiveContainer width="100%" height="100%">
                <BarChart data={portfolioData}>
                  <CartesianGrid strokeDasharray="3 3" stroke="#222834" />
                  <XAxis dataKey="currency" tick={{ fill: '#8A929F' }} axisLine={{ stroke: '#222834' }} />
                  <YAxis
                    tick={{ fill: '#8A929F' }}
                    axisLine={{ stroke: '#222834' }}
                    tickFormatter={(value) => `R$ ${(value / 1000).toFixed(0)}k`}
                  />
                  <Tooltip
                    formatter={(value) => formatCurrency(value)}
                    contentStyle={tooltipStyle}
                    itemStyle={{ color: '#F0F2F5' }}
                    labelStyle={{ color: '#F0F2F5' }}
                  />
                  <Bar dataKey="value" radius={[6, 6, 0, 0]}>
                    {portfolioData.map((_, index) => (
                      <Cell
                        key={`bar-${index}`}
                        fill={CHART_COLORS[index % CHART_COLORS.length]}
                      />
                    ))}
                  </Bar>
                </BarChart>
              </ResponsiveContainer>
            </ChartContainer>
          </Card>
        </ChartsGrid>
      </Section>

      <Section>
        <SectionTitle>Cotações do mercado</SectionTitle>
        <Grid>
          {Object.entries(prices).map(([currency, price]) => (
            <Card key={currency}>
              <CardTitle>{currency}</CardTitle>
              <CardValue>{formatCurrency(price)}</CardValue>
              <AssetName>preço unitário em BRL</AssetName>
            </Card>
          ))}
        </Grid>
      </Section>

      <Section>
        <SectionTitle>Ações rápidas</SectionTitle>
        <Card>
          <QuickActions>
            <Button $variant="primary" onClick={() => navigate('/convert')}>
              <FaExchangeAlt size={16} />
              Converter
            </Button>
            <Button $variant="secondary" onClick={() => navigate('/transfer')}>
              <FaPaperPlane size={16} />
              Transferir
            </Button>
          </QuickActions>
        </Card>
      </Section>
    </>
  );
}
