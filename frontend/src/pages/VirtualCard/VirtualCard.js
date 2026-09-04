import { useEffect, useMemo } from 'react';
import styled from 'styled-components';
import { FaSyncAlt } from 'react-icons/fa';
import { useWallet } from '../../contexts/WalletContext';
import { CardWallet } from '../../components/wallet/CardWallet';
import { Header, PageTitle } from '../../components/Layout';
import { Button } from '../../components/Button';

const Page = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: ${({ theme }) => theme.spacing.xl};
`;

const Content = styled.div`
  display: flex;
  flex-direction: column;
  gap: ${({ theme }) => theme.spacing.xl};
`;

const CardSection = styled.div`
  max-width: 520px;
`;

const DetailsGrid = styled.div`
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
  gap: ${({ theme }) => theme.spacing.lg};
`;

const InfoCard = styled.div`
  background: ${({ theme }) => theme.colors.surface};
  border: 1px solid ${({ theme }) => theme.colors.border};
  border-radius: ${({ theme }) => theme.radii.lg};
  padding: ${({ theme }) => theme.spacing.lg};
  display: flex;
  flex-direction: column;
  gap: ${({ theme }) => theme.spacing.md};
`;

const InfoTitle = styled.h3`
  font-size: ${({ theme }) => theme.fontSizes.md};
  color: ${({ theme }) => theme.colors.textMuted};
  font-weight: 600;
  margin: 0;
`;

const InfoValue = styled.div`
  font-size: ${({ theme }) => theme.fontSizes.xxl};
  font-weight: 700;
  color: ${({ theme }) => theme.colors.primary};
`;

const InfoSub = styled.p`
  font-size: ${({ theme }) => theme.fontSizes.sm};
  color: ${({ theme }) => theme.colors.textMuted};
  margin: 0;
`;

const Status = styled.p`
  font-size: ${({ theme }) => theme.fontSizes.md};
  color: ${({ theme, $error }) => ($error ? theme.colors.danger : theme.colors.primary)};
  display: flex;
  align-items: center;
  gap: ${({ theme }) => theme.spacing.sm};
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

const ProgressTrack = styled.div`
  width: 100%;
  height: 10px;
  background: ${({ theme }) => theme.colors.surfaceLight};
  border-radius: ${({ theme }) => theme.radii.full};
  overflow: hidden;
  margin-top: ${({ theme }) => theme.spacing.sm};
`;

const ProgressFill = styled.div`
  height: 100%;
  width: ${({ $width }) => $width}%;
  background: ${({ theme }) => theme.colors.primary};
  border-radius: ${({ theme }) => theme.radii.full};
  transition: width 0.4s ease;
`;

function formatCurrency(value) {
  return value.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
}

export function VirtualCard() {
  const { cards, loading, error, fetchWallet } = useWallet();

  useEffect(() => {
    fetchWallet();
  }, [fetchWallet]);

  const card = useMemo(
    () => cards.find((c) => c.type === 'credit') || cards[0],
    [cards]
  );

  if (!card) {
    return (
      <Page>
        <Header>
          <PageTitle>Cartão Virtual</PageTitle>
        </Header>
        <Status>Nenhum cartão encontrado.</Status>
      </Page>
    );
  }

  const available = (card.limit || 0) - (card.used || 0);
  const usedPercent = card.limit > 0 ? ((card.used || 0) / card.limit) * 100 : 0;

  return (
    <Page>
      <Header>
        <PageTitle>Cartão Virtual</PageTitle>
        <Button $variant="secondary" onClick={fetchWallet} disabled={loading}>
          <FaSyncAlt size={16} />
          Atualizar
        </Button>
      </Header>

      <Content>
        {loading && (
          <Status>
            <Spinner size={18} />
            Carregando cartão virtual...
          </Status>
        )}

        {error && <Status $error>{error}</Status>}

        <CardSection>
          <CardWallet cards={[card]} />
        </CardSection>

        <DetailsGrid>
          <InfoCard>
            <InfoTitle>Limite total</InfoTitle>
            <InfoValue>{formatCurrency(card.limit || 0)}</InfoValue>
          </InfoCard>

          <InfoCard>
            <InfoTitle>Limite usado</InfoTitle>
            <InfoValue>{formatCurrency(card.used || 0)}</InfoValue>
            <ProgressTrack>
              <ProgressFill $width={usedPercent.toFixed(2)} />
            </ProgressTrack>
            <InfoSub>{usedPercent.toFixed(1)}% do limite utilizado</InfoSub>
          </InfoCard>

          <InfoCard>
            <InfoTitle>Limite disponível</InfoTitle>
            <InfoValue>{formatCurrency(available)}</InfoValue>
          </InfoCard>

          <InfoCard>
            <InfoTitle>Fatura atual</InfoTitle>
            <InfoValue>{formatCurrency(card.statement || 0)}</InfoValue>
            <InfoSub>Vencimento: {card.dueDate}</InfoSub>
          </InfoCard>

          <InfoCard>
            <InfoTitle>Status</InfoTitle>
            <InfoValue>{card.status || '—'}</InfoValue>
            <InfoSub>Titular: {card.holder}</InfoSub>
          </InfoCard>

          <InfoCard>
            <InfoTitle>Final do cartão</InfoTitle>
            <InfoValue>**** {card.lastDigits || card.number?.slice(-4)}</InfoValue>
            <InfoSub>Validade: {card.expiry}</InfoSub>
          </InfoCard>
        </DetailsGrid>
      </Content>
    </Page>
  );
}
