import styled from 'styled-components';
import { FaCcVisa, FaCcMastercard, FaCreditCard } from 'react-icons/fa';

const cardBrandIcons = {
  Visa: FaCcVisa,
  Mastercard: FaCcMastercard,
};

function getCardIcon(brand) {
  return cardBrandIcons[brand] || FaCreditCard;
}

function formatCurrency(value) {
  return value.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
}

const Container = styled.div`
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(320px, 1fr));
  gap: ${({ theme }) => theme.spacing.lg};
`;

const Card = styled.div`
  position: relative;
  background: linear-gradient(135deg, ${({ $type }) =>
      $type === 'credit' ? '#1A1F2A' : '#141824'} 0%,
    ${({ $type }) => ($type === 'credit' ? '#0B0E14' : '#12151C')} 100%);
  border: 1px solid ${({ theme }) => theme.colors.border};
  border-radius: ${({ theme }) => theme.radii.lg};
  padding: ${({ theme }) => theme.spacing.xl};
  min-height: 220px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  overflow: hidden;
  box-shadow: ${({ theme }) => theme.shadows.lg};

  &::before {
    content: '';
    position: absolute;
    top: -40px;
    right: -40px;
    width: 180px;
    height: 180px;
    border-radius: 50%;
    background: rgba(245, 184, 0, 0.08);
  }
`;

const CardTop = styled.div`
  display: flex;
  align-items: center;
  justify-content: space-between;
  z-index: 1;
`;

const Chip = styled.div`
  width: 48px;
  height: 36px;
  border-radius: 8px;
  background: linear-gradient(135deg, #D4AF37 0%, #F0D878 50%, #D4AF37 100%);
  position: relative;
  overflow: hidden;

  &::before,
  &::after {
    content: '';
    position: absolute;
    background: rgba(0, 0, 0, 0.1);
  }

  &::before {
    width: 100%;
    height: 1px;
    top: 50%;
    left: 0;
    transform: translateY(-50%);
  }

  &::after {
    width: 1px;
    height: 100%;
    top: 0;
    left: 50%;
    transform: translateX(-50%);
  }
`;

const BrandIcon = styled.div`
  color: #F5B800;
  z-index: 1;
`;

const CardNumber = styled.p`
  font-size: ${({ theme }) => theme.fontSizes.xl};
  color: ${({ theme }) => theme.colors.text};
  letter-spacing: 3px;
  font-family: 'Courier New', monospace;
  margin: 0;
  z-index: 1;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
`;

const CardBottom = styled.div`
  display: flex;
  align-items: flex-end;
  justify-content: space-between;
  z-index: 1;
`;

const CardInfo = styled.div`
  display: flex;
  flex-direction: column;
  gap: ${({ theme }) => theme.spacing.xs};

  strong {
    font-size: ${({ theme }) => theme.fontSizes.sm};
    color: ${({ theme }) => theme.colors.text};
    font-weight: 600;
    text-transform: uppercase;
    letter-spacing: 1px;
  }

  span {
    font-size: ${({ theme }) => theme.fontSizes.sm};
    color: ${({ theme }) => theme.colors.textMuted};
  }
`;

const CardMeta = styled.div`
  display: flex;
  gap: ${({ theme }) => theme.spacing.lg};

  div {
    display: flex;
    flex-direction: column;
    gap: ${({ theme }) => theme.spacing.xs};
  }

  label {
    font-size: ${({ theme }) => theme.fontSizes.xs};
    color: ${({ theme }) => theme.colors.textMuted};
    text-transform: uppercase;
  }

  span {
    font-size: ${({ theme }) => theme.fontSizes.md};
    color: ${({ theme }) => theme.colors.text};
    font-family: 'Courier New', monospace;
  }
`;

const CardValue = styled.div`
  text-align: right;
  z-index: 1;

  strong {
    font-size: ${({ theme }) => theme.fontSizes.xxl};
    font-weight: 700;
    color: #F5B800;
  }

  span {
    display: block;
    font-size: ${({ theme }) => theme.fontSizes.sm};
    color: ${({ theme }) => theme.colors.textMuted};
  }
`;

export function CardWallet({ cards }) {
  return (
    <Container>
      {cards.map((card) => {
        const CardIcon = getCardIcon(card.brand);
        const available =
          card.type === 'credit' ? (card.limit || 0) - (card.used || 0) : card.balance || 0;

        return (
          <Card key={card.id} $type={card.type}>
            <CardTop>
              <Chip />
              <BrandIcon>
                <CardIcon size={48} />
              </BrandIcon>
            </CardTop>

            <CardNumber>{card.number}</CardNumber>

            <CardBottom>
              <CardInfo>
                <strong>{card.holder}</strong>
                <CardMeta>
                  <div>
                    <label>Validade</label>
                    <span>{card.expiry}</span>
                  </div>
                  <div>
                    <label>CVV</label>
                    <span>{card.cvv}</span>
                  </div>
                </CardMeta>
              </CardInfo>

              <CardValue>
                <strong>{formatCurrency(available)}</strong>
                <span>
                  {card.type === 'credit' ? 'Limite disponível' : 'Saldo disponível'}
                </span>
              </CardValue>
            </CardBottom>
          </Card>
        );
      })}
    </Container>
  );
}
