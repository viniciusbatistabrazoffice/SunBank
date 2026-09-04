import { useEffect } from 'react';
import styled from 'styled-components';
import { TransferForm } from '../../components/crypto/TransferForm';
import { useWallet } from '../../contexts/WalletContext';
import { useTransfer } from '../../hooks/useTransfer';

const PageWrapper = styled.main`
  min-height: 100vh;
  padding: ${({ theme }) => theme.spacing.xl};
  padding-top: calc(64px + ${({ theme }) => theme.spacing.xl});
  padding-left: calc(260px + ${({ theme }) => theme.spacing.xl});
  background: ${({ theme }) => theme.colors.background};
  color: ${({ theme }) => theme.colors.text};
`;

const PageHeader = styled.div`
  margin-bottom: ${({ theme }) => theme.spacing.xl};
`;

const PageTitle = styled.h1`
  font-size: ${({ theme }) => theme.fontSizes.xxl};
  font-weight: 700;
  color: ${({ theme }) => theme.colors.text};
`;

const PageSubtitle = styled.p`
  margin-top: ${({ theme }) => theme.spacing.sm};
  color: ${({ theme }) => theme.colors.textMuted};
  font-size: ${({ theme }) => theme.fontSizes.md};
`;

export function Transfer() {
  const { balances, prices, fetchWallet } = useWallet();
  const { fetchRecipients } = useTransfer();

  useEffect(() => {
    fetchWallet();
    fetchRecipients();
  }, [fetchWallet, fetchRecipients]);

  return (
    <PageWrapper>
      <PageHeader>
        <PageTitle>Transferir cripto</PageTitle>
        <PageSubtitle>Envie cripto de forma rápida e segura para seus favorecidos.</PageSubtitle>
      </PageHeader>
      <TransferForm balances={balances} prices={prices} />
    </PageWrapper>
  );
}
