import { useEffect } from 'react';
import styled from 'styled-components';
import { FaSyncAlt } from 'react-icons/fa';
import { CryptoBalance } from '../../components/crypto/CryptoBalance';
import { useWallet } from '../../contexts/WalletContext';
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

export function Wallet() {
  const { balances, prices, loading, error, fetchWallet, totalBrl } = useWallet();

  useEffect(() => {
    fetchWallet();
  }, [fetchWallet]);

  return (
    <Page>
      <Header>
        <PageTitle>Carteira</PageTitle>
        <Button $variant="secondary" onClick={fetchWallet} disabled={loading}>
          <FaSyncAlt size={16} />
          Atualizar
        </Button>
      </Header>

      <Content>
        {loading && (
          <Status>
            <Spinner size={18} />
            Carregando carteira...
          </Status>
        )}

        {error && <Status $error>{error}</Status>}

        <CryptoBalance balances={balances} prices={prices} totalBrl={totalBrl} />
      </Content>
    </Page>
  );
}
