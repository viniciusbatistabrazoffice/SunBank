import styled from 'styled-components';
import { CryptoBalance } from '../crypto/CryptoBalance';
import { CardWallet } from './CardWallet';

const Section = styled.section`
  display: flex;
  flex-direction: column;
  gap: ${({ theme }) => theme.spacing.lg};
`;

const SectionTitle = styled.h2`
  font-size: ${({ theme }) => theme.fontSizes.lg};
  font-weight: 600;
  color: ${({ theme }) => theme.colors.text};
  margin: 0;
`;

export function WalletList({ balances, prices, totalBrl, cards }) {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '2rem' }}>
      <Section>
        <SectionTitle>Criptomoedas</SectionTitle>
        <CryptoBalance balances={balances} prices={prices} totalBrl={totalBrl} />
      </Section>

      <Section>
        <SectionTitle>Cartões</SectionTitle>
        <CardWallet cards={cards} />
      </Section>
    </div>
  );
}
