import styled from 'styled-components';
import { FaBitcoin, FaEthereum, FaDollarSign, FaCoins } from 'react-icons/fa';

const currencyInfo = {
  BTC: { name: 'Bitcoin', icon: FaBitcoin, color: '#F7931A' },
  ETH: { name: 'Ethereum', icon: FaEthereum, color: '#627EEA' },
  BRL: { name: 'Real', icon: FaDollarSign, color: '#22C55E' },
  USDC: { name: 'USD Coin', icon: FaCoins, color: '#2775CA' },
};

function getCurrencyInfo(code) {
  return currencyInfo[code] || { name: code, icon: FaCoins, color: '#F5B800' };
}

const Container = styled.div`
  display: flex;
  flex-direction: column;
  gap: ${({ theme }) => theme.spacing.lg};
`;

const TotalCard = styled.section`
  background: linear-gradient(135deg, ${({ theme }) => theme.colors.surface} 0%, ${({ theme }) => theme.colors.surfaceLight} 100%);
  border: 1px solid ${({ theme }) => theme.colors.border};
  border-radius: ${({ theme }) => theme.radii.lg};
  padding: ${({ theme }) => theme.spacing.xl};
  box-shadow: ${({ theme }) => theme.shadows.md};
`;

const TotalLabel = styled.p`
  font-size: ${({ theme }) => theme.fontSizes.sm};
  color: ${({ theme }) => theme.colors.textMuted};
  margin-bottom: ${({ theme }) => theme.spacing.sm};
`;

const TotalValue = styled.div`
  font-size: ${({ theme }) => theme.fontSizes.xxl};
  font-weight: 700;
  color: ${({ theme }) => theme.colors.primary};
`;

const Grid = styled.div`
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: ${({ theme }) => theme.spacing.lg};
`;

const AssetCard = styled.div`
  background: ${({ theme }) => theme.colors.surface};
  border: 1px solid ${({ theme }) => theme.colors.border};
  border-radius: ${({ theme }) => theme.radii.lg};
  padding: ${({ theme }) => theme.spacing.lg};
  box-shadow: ${({ theme }) => theme.shadows.md};
  transition: transform 0.2s ease, box-shadow 0.2s ease;

  &:hover {
    transform: translateY(-2px);
    box-shadow: ${({ theme }) => theme.shadows.lg};
  }
`;

const AssetHeader = styled.div`
  display: flex;
  align-items: center;
  gap: ${({ theme }) => theme.spacing.md};
  margin-bottom: ${({ theme }) => theme.spacing.lg};
`;

const IconBox = styled.div`
  width: 48px;
  height: 48px;
  border-radius: ${({ theme }) => theme.radii.full};
  display: flex;
  align-items: center;
  justify-content: center;
  background: ${({ $color }) => `${$color}20`};
  color: ${({ $color }) => $color};
  font-size: 1.5rem;
`;

const AssetTitle = styled.div`
  display: flex;
  flex-direction: column;

  strong {
    font-size: ${({ theme }) => theme.fontSizes.lg};
    color: ${({ theme }) => theme.colors.text};
    font-weight: 700;
  }

  span {
    font-size: ${({ theme }) => theme.fontSizes.sm};
    color: ${({ theme }) => theme.colors.textMuted};
  }
`;

const AssetValue = styled.div`
  font-size: ${({ theme }) => theme.fontSizes.xl};
  font-weight: 700;
  color: ${({ theme }) => theme.colors.text};
  margin-bottom: ${({ theme }) => theme.spacing.xs};
`;

const AssetAmount = styled.div`
  font-size: ${({ theme }) => theme.fontSizes.sm};
  color: ${({ theme }) => theme.colors.textMuted};
  margin-bottom: ${({ theme }) => theme.spacing.md};
`;

const BarTrack = styled.div`
  height: 8px;
  width: 100%;
  background: ${({ theme }) => theme.colors.surfaceLight};
  border-radius: ${({ theme }) => theme.radii.full};
  overflow: hidden;
`;

const BarFill = styled.div`
  height: 100%;
  width: ${({ $width }) => $width}%;
  background: ${({ $color }) => $color};
  border-radius: ${({ theme }) => theme.radii.full};
  transition: width 0.4s ease;
`;

const Percent = styled.div`
  margin-top: ${({ theme }) => theme.spacing.sm};
  font-size: ${({ theme }) => theme.fontSizes.sm};
  color: ${({ theme }) => theme.colors.textMuted};
  text-align: right;
`;

function formatAmount(amount, currency) {
  const digits = currency === 'BRL' || currency === 'USDC' ? 2 : 8;
  return amount.toLocaleString('pt-BR', {
    minimumFractionDigits: 0,
    maximumFractionDigits: digits,
  });
}

function formatCurrency(value) {
  return value.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
}

export function CryptoBalance({ balances, prices, totalBrl }) {
  return (
    <Container>
      <TotalCard>
        <TotalLabel>Valor total da carteira</TotalLabel>
        <TotalValue>{formatCurrency(totalBrl)}</TotalValue>
      </TotalCard>

      <Grid>
        {balances.map((balance) => {
          const { icon: Icon, name, color } = getCurrencyInfo(balance.currency);
          const price = prices[balance.currency] || 0;
          const brlValue = balance.amount * price;
          const percent = totalBrl > 0 ? (brlValue / totalBrl) * 100 : 0;

          return (
            <AssetCard key={balance.currency}>
              <AssetHeader>
                <IconBox $color={color}>
                  <Icon size={24} />
                </IconBox>
                <AssetTitle>
                  <strong>{balance.currency}</strong>
                  <span>{name}</span>
                </AssetTitle>
              </AssetHeader>

              <AssetValue>{formatCurrency(brlValue)}</AssetValue>
              <AssetAmount>
                {formatAmount(balance.amount, balance.currency)} {balance.currency} · {formatCurrency(price)} / unidade
              </AssetAmount>

              <BarTrack>
                <BarFill $width={percent.toFixed(2)} $color={color} />
              </BarTrack>
              <Percent>{percent.toFixed(1)}% da carteira</Percent>
            </AssetCard>
          );
        })}
      </Grid>
    </Container>
  );
}
