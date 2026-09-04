import { useState, useMemo } from 'react';
import styled from 'styled-components';
import {
  FaPaperPlane,
  FaCoins,
  FaWallet,
  FaUser,
  FaUniversity,
  FaCheckCircle,
  FaBuilding,
} from 'react-icons/fa';
import { useTransfer } from '../../hooks/useTransfer';

const Container = styled.div`
  display: flex;
  flex-wrap: wrap;
  gap: ${({ theme }) => theme.spacing.xl};
  align-items: flex-start;
`;

const Card = styled.div`
  flex: 1 1 420px;
  max-width: 520px;
  padding: ${({ theme }) => theme.spacing.xl};
  border-radius: ${({ theme }) => theme.radii.lg};
  background: ${({ theme }) => theme.colors.surface};
  border: 1px solid ${({ theme }) => theme.colors.border};
  box-shadow: ${({ theme }) => theme.shadows.md};
`;

const DetailsCard = styled(Card)`
  display: flex;
  flex-direction: column;
  gap: ${({ theme }) => theme.spacing.lg};
`;

const CardHeader = styled.div`
  display: flex;
  align-items: center;
  gap: ${({ theme }) => theme.spacing.md};
  margin-bottom: ${({ theme }) => theme.spacing.lg};
`;

const IconBox = styled.div`
  display: flex;
  align-items: center;
  justify-content: center;
  width: 48px;
  height: 48px;
  border-radius: ${({ theme }) => theme.radii.md};
  background: ${({ theme }) => theme.colors.surfaceLight};
  color: ${({ theme }) => theme.colors.primary};
`;

const CardTitle = styled.h2`
  font-size: ${({ theme }) => theme.fontSizes.xl};
  font-weight: 700;
  color: ${({ theme }) => theme.colors.text};
`;

const CardSubtitle = styled.p`
  font-size: ${({ theme }) => theme.fontSizes.sm};
  color: ${({ theme }) => theme.colors.textMuted};
`;

const Form = styled.form`
  display: flex;
  flex-direction: column;
  gap: ${({ theme }) => theme.spacing.lg};
`;

const FieldGroup = styled.div`
  display: flex;
  flex-direction: column;
  gap: ${({ theme }) => theme.spacing.sm};
`;

const Label = styled.label`
  display: flex;
  align-items: center;
  gap: ${({ theme }) => theme.spacing.sm};
  font-size: ${({ theme }) => theme.fontSizes.sm};
  font-weight: 600;
  color: ${({ theme }) => theme.colors.text};
`;

const Select = styled.select`
  width: 100%;
  padding: ${({ theme }) => `${theme.spacing.md} ${theme.spacing.md}`};
  border: 1px solid ${({ theme }) => theme.colors.border};
  border-radius: ${({ theme }) => theme.radii.md};
  background: ${({ theme }) => theme.colors.surfaceLight};
  color: ${({ theme }) => theme.colors.text};
  font-size: ${({ theme }) => theme.fontSizes.md};
  outline: none;
  transition: all 0.2s ease;

  &:focus {
    border-color: ${({ theme }) => theme.colors.primary};
    background: ${({ theme }) => theme.colors.background};
  }
`;

const Input = styled.input`
  width: 100%;
  padding: ${({ theme }) => theme.spacing.md};
  border: 1px solid ${({ theme }) => theme.colors.border};
  border-radius: ${({ theme }) => theme.radii.md};
  background: ${({ theme }) => theme.colors.surfaceLight};
  color: ${({ theme }) => theme.colors.text};
  font-size: ${({ theme }) => theme.fontSizes.md};
  outline: none;
  transition: all 0.2s ease;

  &::placeholder {
    color: ${({ theme }) => theme.colors.textMuted};
  }

  &:focus {
    border-color: ${({ theme }) => theme.colors.primary};
    background: ${({ theme }) => theme.colors.background};
  }
`;

const BalancePill = styled.div`
  display: inline-flex;
  align-items: center;
  gap: ${({ theme }) => theme.spacing.sm};
  padding: ${({ theme }) => `${theme.spacing.sm} ${theme.spacing.md}`};
  border-radius: ${({ theme }) => theme.radii.full};
  background: ${({ theme }) => theme.colors.surfaceLight};
  color: ${({ theme }) => theme.colors.textMuted};
  font-size: ${({ theme }) => theme.fontSizes.sm};
  width: fit-content;
`;

const Summary = styled.div`
  padding: ${({ theme }) => theme.spacing.md};
  border-radius: ${({ theme }) => theme.radii.md};
  background: ${({ theme }) => theme.colors.surfaceLight};
  border: 1px solid ${({ theme }) => theme.colors.border};
`;

const SummaryRow = styled.div`
  display: flex;
  justify-content: space-between;
  font-size: ${({ theme }) => theme.fontSizes.sm};
  color: ${({ theme }) => theme.colors.textMuted};
  margin-bottom: ${({ theme }) => theme.spacing.sm};

  &:last-child {
    margin-bottom: 0;
  }
`;

const SummaryValue = styled.span`
  font-weight: 600;
  color: ${({ theme }) => theme.colors.text};
`;

const SubmitButton = styled.button`
  display: flex;
  align-items: center;
  justify-content: center;
  gap: ${({ theme }) => theme.spacing.md};
  width: 100%;
  padding: ${({ theme }) => theme.spacing.md};
  border: none;
  border-radius: ${({ theme }) => theme.radii.md};
  background: ${({ theme }) => theme.colors.primary};
  color: ${({ theme }) => theme.colors.background};
  font-size: ${({ theme }) => theme.fontSizes.md};
  font-weight: 700;
  cursor: pointer;
  transition: all 0.2s ease;

  &:hover:not(:disabled) {
    background: ${({ theme }) => theme.colors.primaryHover};
    box-shadow: ${({ theme }) => theme.shadows.glow};
  }

  &:disabled {
    opacity: 0.6;
    cursor: not-allowed;
  }
`;

const ErrorMessage = styled.p`
  display: flex;
  align-items: center;
  gap: ${({ theme }) => theme.spacing.sm};
  padding: ${({ theme }) => theme.spacing.md};
  border-radius: ${({ theme }) => theme.radii.md};
  background: rgba(239, 68, 68, 0.1);
  color: ${({ theme }) => theme.colors.danger};
  font-size: ${({ theme }) => theme.fontSizes.sm};
`;

const SuccessMessage = styled.div`
  display: flex;
  align-items: center;
  gap: ${({ theme }) => theme.spacing.md};
  padding: ${({ theme }) => theme.spacing.md};
  border-radius: ${({ theme }) => theme.radii.md};
  background: rgba(34, 197, 94, 0.1);
  color: ${({ theme }) => theme.colors.success};
  font-size: ${({ theme }) => theme.fontSizes.md};
  font-weight: 600;
`;

const DetailsSection = styled.div`
  display: flex;
  flex-direction: column;
  gap: ${({ theme }) => theme.spacing.md};
`;

const DetailsSectionTitle = styled.h3`
  display: flex;
  align-items: center;
  gap: ${({ theme }) => theme.spacing.sm};
  font-size: ${({ theme }) => theme.fontSizes.md};
  font-weight: 600;
  color: ${({ theme }) => theme.colors.primary};
`;

const DetailsItem = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: ${({ theme }) => `${theme.spacing.md} 0`};
  border-bottom: 1px solid ${({ theme }) => theme.colors.border};

  &:last-child {
    border-bottom: none;
  }
`;

const DetailsLabel = styled.span`
  color: ${({ theme }) => theme.colors.textMuted};
  font-size: ${({ theme }) => theme.fontSizes.sm};
`;

const DetailsValue = styled.span`
  font-weight: 600;
  color: ${({ theme }) => theme.colors.text};
  font-size: ${({ theme }) => theme.fontSizes.sm};
`;

const EmptyState = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: ${({ theme }) => theme.spacing.xl};
  text-align: center;
  color: ${({ theme }) => theme.colors.textMuted};
  font-size: ${({ theme }) => theme.fontSizes.md};
  gap: ${({ theme }) => theme.spacing.md};
`;

const PreviewBox = styled.div`
  padding: ${({ theme }) => theme.spacing.md};
  border-radius: ${({ theme }) => theme.radii.md};
  background: ${({ theme }) => theme.colors.background};
  border: 1px solid ${({ theme }) => theme.colors.border};
  display: flex;
  flex-direction: column;
  gap: ${({ theme }) => theme.spacing.md};
`;

const PreviewRow = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
`;

const PreviewLabel = styled.span`
  color: ${({ theme }) => theme.colors.textMuted};
  font-size: ${({ theme }) => theme.fontSizes.sm};
`;

const PreviewValue = styled.span`
  color: ${({ theme }) => theme.colors.text};
  font-size: ${({ theme }) => theme.fontSizes.md};
  font-weight: 600;
`;

const NetValue = styled(PreviewValue)`
  color: ${({ theme }) => theme.colors.primary};
  font-size: ${({ theme }) => theme.fontSizes.lg};
`;

const HelperText = styled.p`
  color: ${({ theme }) => theme.colors.textMuted};
  font-size: ${({ theme }) => theme.fontSizes.xs};
  line-height: 1.5;
`;

const banks = [
  { id: '001', name: 'Banco do Brasil', code: '001' },
  { id: '077', name: 'Banco Inter', code: '077' },
  { id: '237', name: 'Bradesco', code: '237' },
  { id: '341', name: 'Itaú', code: '341' },
  { id: '260', name: 'NuBank', code: '260' },
  { id: '033', name: 'Santander', code: '033' },
  { id: '104', name: 'Caixa Econômica Federal', code: '104' },
];

export function TransferForm({ balances, prices = {} }) {
  const [currency, setCurrency] = useState('');
  const [amount, setAmount] = useState('');
  const [recipient, setRecipient] = useState('');
  const [bank, setBank] = useState('');
  const [agency, setAgency] = useState('');
  const [accountNumber, setAccountNumber] = useState('');
  const [success, setSuccess] = useState(false);
  const { recipients, loading, error, sendCrypto } = useTransfer();

  const selectedBalance = useMemo(
    () => balances.find((b) => b.currency === currency),
    [balances, currency]
  );

  const selectedRecipient = useMemo(
    () => recipients.find((r) => String(r.id) === recipient),
    [recipients, recipient]
  );

  const selectedBank = useMemo(
    () => banks.find((b) => b.id === bank),
    [bank]
  );

  const numericAmount = useMemo(() => Number(amount) || 0, [amount]);
  const unitPrice = useMemo(() => prices[currency] || 0, [prices, currency]);
  const grossBrl = useMemo(() => numericAmount * unitPrice, [numericAmount, unitPrice]);
  const feeBrl = useMemo(() => grossBrl * 0.01, [grossBrl]);
  const netBrl = useMemo(() => grossBrl - feeBrl, [grossBrl, feeBrl]);
  const balanceExceeded = useMemo(
    () => selectedBalance && numericAmount > selectedBalance.amount,
    [selectedBalance, numericAmount]
  );

  const formatCurrency = (value) =>
    value.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });

  async function handleSubmit(e) {
    e.preventDefault();
    const result = await sendCrypto({
      currency,
      amount: Number(amount),
      recipient,
      bank,
      agency,
      accountNumber,
    });
    if (result) {
      setAmount('');
      setCurrency('');
      setRecipient('');
      setBank('');
      setAgency('');
      setAccountNumber('');
      setSuccess(true);
      setTimeout(() => setSuccess(false), 5000);
    }
  }

  const hasSelection = currency || bank;

  return (
    <Container>
      <Card>
        <CardHeader>
          <IconBox>
            <FaPaperPlane size={22} />
          </IconBox>
          <div>
            <CardTitle>Nova transferência</CardTitle>
            <CardSubtitle>Preencha os dados abaixo para enviar.</CardSubtitle>
          </div>
        </CardHeader>

        <Form onSubmit={handleSubmit}>
          <FieldGroup>
            <Label htmlFor="currency">
              <FaCoins size={14} />
              Criptomoeda
            </Label>
            <Select
              id="currency"
              value={currency}
              onChange={(e) => setCurrency(e.target.value)}
              required
            >
              <option value="">Selecione</option>
              {balances.map((b) => (
                <option key={b.currency} value={b.currency}>
                  {b.currency}
                </option>
              ))}
            </Select>
            {selectedBalance && (
              <BalancePill>
                <FaWallet size={12} />
                Saldo disponível: {selectedBalance.amount} {selectedBalance.currency}
              </BalancePill>
            )}
          </FieldGroup>

          <FieldGroup>
            <Label htmlFor="amount">
              <FaCoins size={14} />
              Quantidade
            </Label>
            <Input
              id="amount"
              type="number"
              step="any"
              min="0"
              placeholder="0,00"
              value={amount}
              onChange={(e) => setAmount(e.target.value)}
              required
            />
          </FieldGroup>

          <FieldGroup>
            <Label htmlFor="recipient">
              <FaUser size={14} />
              Favorecido
            </Label>
            <Select
              id="recipient"
              value={recipient}
              onChange={(e) => setRecipient(e.target.value)}
              required
            >
              <option value="">Selecione</option>
              {recipients.map((r) => (
                <option key={r.id} value={r.id}>
                  {r.name}
                </option>
              ))}
            </Select>
          </FieldGroup>

          <FieldGroup>
            <Label htmlFor="bank">
              <FaUniversity size={14} />
              Banco para conversão
            </Label>
            <Select
              id="bank"
              value={bank}
              onChange={(e) => setBank(e.target.value)}
              required
            >
              <option value="">Selecione</option>
              {banks.map((b) => (
                <option key={b.id} value={b.id}>
                  {b.name}
                </option>
              ))}
            </Select>
          </FieldGroup>

          <FieldGroup>
            <Label htmlFor="agency">Agência</Label>
            <Input
              id="agency"
              type="text"
              placeholder="0000"
              value={agency}
              onChange={(e) => setAgency(e.target.value)}
              required
            />
          </FieldGroup>

          <FieldGroup>
            <Label htmlFor="accountNumber">Número da conta</Label>
            <Input
              id="accountNumber"
              type="text"
              placeholder="000000-0"
              value={accountNumber}
              onChange={(e) => setAccountNumber(e.target.value)}
              required
            />
          </FieldGroup>

          {(currency || amount || recipient || bank || agency || accountNumber) && (
            <Summary>
              <SummaryRow>
                <span>Moeda</span>
                <SummaryValue>{currency || '-'}</SummaryValue>
              </SummaryRow>
              <SummaryRow>
                <span>Quantidade</span>
                <SummaryValue>{amount || '0'} {currency || ''}</SummaryValue>
              </SummaryRow>
              <SummaryRow>
                <span>Favorecido</span>
                <SummaryValue>{selectedRecipient?.name || '-'}</SummaryValue>
              </SummaryRow>
              <SummaryRow>
                <span>Banco</span>
                <SummaryValue>{selectedBank?.name || '-'}</SummaryValue>
              </SummaryRow>
              <SummaryRow>
                <span>Agência</span>
                <SummaryValue>{agency || '-'}</SummaryValue>
              </SummaryRow>
              <SummaryRow>
                <span>Conta</span>
                <SummaryValue>{accountNumber || '-'}</SummaryValue>
              </SummaryRow>
            </Summary>
          )}

          <SubmitButton type="submit" disabled={loading || balanceExceeded}>
            {loading ? 'Enviando...' : 'Transferir'}
            {!loading && <FaPaperPlane size={16} />}
          </SubmitButton>

          {balanceExceeded && (
            <ErrorMessage>Saldo insuficiente para essa transferência.</ErrorMessage>
          )}
          {error && <ErrorMessage>{error}</ErrorMessage>}
          {success && (
            <SuccessMessage>
              <FaCheckCircle size={20} />
              Transferência enviada com sucesso!
            </SuccessMessage>
          )}
        </Form>
      </Card>

      {hasSelection ? (
        <DetailsCard>
          <CardHeader>
            <IconBox>
              <FaBuilding size={22} />
            </IconBox>
            <div>
              <CardTitle>Dados da conversão</CardTitle>
              <CardSubtitle>Confira os dados selecionados.</CardSubtitle>
            </div>
          </CardHeader>

          {selectedBalance && (
            <DetailsSection>
              <DetailsSectionTitle>
                <FaCoins size={16} />
                Criptomoeda selecionada
              </DetailsSectionTitle>
              <DetailsItem>
                <DetailsLabel>Moeda</DetailsLabel>
                <DetailsValue>{selectedBalance.currency}</DetailsValue>
              </DetailsItem>
              <DetailsItem>
                <DetailsLabel>Saldo disponível</DetailsLabel>
                <DetailsValue>
                  {selectedBalance.amount} {selectedBalance.currency}
                </DetailsValue>
              </DetailsItem>
              <DetailsItem>
                <DetailsLabel>Quantidade a transferir</DetailsLabel>
                <DetailsValue>
                  {amount || '0'} {selectedBalance.currency}
                </DetailsValue>
              </DetailsItem>
            </DetailsSection>
          )}

          {selectedBank && (
            <DetailsSection>
              <DetailsSectionTitle>
                <FaUniversity size={16} />
                Banco selecionado
              </DetailsSectionTitle>
              <DetailsItem>
                <DetailsLabel>Banco</DetailsLabel>
                <DetailsValue>{selectedBank.name}</DetailsValue>
              </DetailsItem>
              <DetailsItem>
                <DetailsLabel>Código</DetailsLabel>
                <DetailsValue>{selectedBank.code}</DetailsValue>
              </DetailsItem>
              <DetailsItem>
                <DetailsLabel>Agência</DetailsLabel>
                <DetailsValue>{agency}</DetailsValue>
              </DetailsItem>
              <DetailsItem>
                <DetailsLabel>Conta</DetailsLabel>
                <DetailsValue>{accountNumber}</DetailsValue>
              </DetailsItem>
              {selectedRecipient && (
                <DetailsItem>
                  <DetailsLabel>Favorecido</DetailsLabel>
                  <DetailsValue>{selectedRecipient.name}</DetailsValue>
                </DetailsItem>
              )}
            </DetailsSection>
          )}

          {selectedBalance && numericAmount > 0 && unitPrice > 0 && (
            <DetailsSection>
              <DetailsSectionTitle>
                <FaCoins size={16} />
                Previsão da conversão
              </DetailsSectionTitle>
              <PreviewBox>
                <PreviewRow>
                  <PreviewLabel>Valor bruto</PreviewLabel>
                  <PreviewValue>{formatCurrency(grossBrl)}</PreviewValue>
                </PreviewRow>
                <PreviewRow>
                  <PreviewLabel>Taxa de conversão (1%)</PreviewLabel>
                  <PreviewValue>- {formatCurrency(feeBrl)}</PreviewValue>
                </PreviewRow>
                <PreviewRow>
                  <PreviewLabel>Você receberá</PreviewLabel>
                  <NetValue>{formatCurrency(netBrl)}</NetValue>
                </PreviewRow>
              </PreviewBox>
              <HelperText>
                * O valor final pode variar de acordo com a cotação no momento da
                transferência.
              </HelperText>
            </DetailsSection>
          )}
        </DetailsCard>
      ) : (
        <DetailsCard>
          <EmptyState>
            <FaBuilding size={40} />
            <span>Selecione uma criptomoeda e um banco para ver os detalhes.</span>
          </EmptyState>
        </DetailsCard>
      )}
    </Container>
  );
}
