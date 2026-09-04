import { useState } from 'react';
import styled from 'styled-components';
import { FaCoins, FaUniversity, FaCheckCircle } from 'react-icons/fa';
import { useConversion } from '../../hooks/useConversion';

const Form = styled.form`
  display: flex;
  flex-direction: column;
  gap: ${({ theme }) => theme.spacing.lg};
  max-width: 480px;
`;

const Title = styled.h2`
  font-size: ${({ theme }) => theme.fontSizes.xl};
  color: ${({ theme }) => theme.colors.text};
  margin-bottom: ${({ theme }) => theme.spacing.sm};
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

const Input = styled.input`
  width: 100%;
  padding: ${({ theme }) => theme.spacing.md};
  border: 1px solid ${({ theme }) => theme.colors.border};
  border-radius: ${({ theme }) => theme.radii.md};
  background: ${({ theme }) => theme.colors.surfaceLight};
  color: ${({ theme }) => theme.colors.text};
  font-size: ${({ theme }) => theme.fontSizes.md};
  outline: none;

  &:focus {
    border-color: ${({ theme }) => theme.colors.primary};
  }
`;

const Select = styled.select`
  width: 100%;
  padding: ${({ theme }) => theme.spacing.md};
  border: 1px solid ${({ theme }) => theme.colors.border};
  border-radius: ${({ theme }) => theme.radii.md};
  background: ${({ theme }) => theme.colors.surfaceLight};
  color: ${({ theme }) => theme.colors.text};
  font-size: ${({ theme }) => theme.fontSizes.md};
  outline: none;

  &:focus {
    border-color: ${({ theme }) => theme.colors.primary};
  }
`;

const AccountGrid = styled.div`
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: ${({ theme }) => theme.spacing.md};

  @media (max-width: 480px) {
    grid-template-columns: 1fr;
  }
`;

const SubmitButton = styled.button`
  padding: ${({ theme }) => theme.spacing.md};
  border: none;
  border-radius: ${({ theme }) => theme.radii.md};
  background: ${({ theme }) => theme.colors.primary};
  color: ${({ theme }) => theme.colors.background};
  font-size: ${({ theme }) => theme.fontSizes.md};
  font-weight: 700;
  cursor: pointer;

  &:disabled {
    opacity: 0.6;
    cursor: not-allowed;
  }
`;

const QuoteBox = styled.div`
  padding: ${({ theme }) => theme.spacing.md};
  border-radius: ${({ theme }) => theme.radii.md};
  background: ${({ theme }) => theme.colors.surfaceLight};
  border: 1px solid ${({ theme }) => theme.colors.border};
  display: flex;
  flex-direction: column;
  gap: ${({ theme }) => theme.spacing.md};
`;

const QuoteRow = styled.div`
  display: flex;
  justify-content: space-between;
  font-size: ${({ theme }) => theme.fontSizes.sm};
  color: ${({ theme }) => theme.colors.textMuted};
`;

const QuoteValue = styled.span`
  font-weight: 600;
  color: ${({ theme }) => theme.colors.text};
`;

const TotalValue = styled(QuoteValue)`
  color: ${({ theme }) => theme.colors.primary};
  font-size: ${({ theme }) => theme.fontSizes.lg};
`;

const ErrorMessage = styled.p`
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

export function ConversionForm({ balances }) {
  const [from, setFrom] = useState('');
  const [amount, setAmount] = useState('');
  const [accountNumber, setAccountNumber] = useState('');
  const [agency, setAgency] = useState('');
  const [success, setSuccess] = useState(false);
  const { quote, loading, error, getQuote, convert } = useConversion();

  async function handleQuote(e) {
    e.preventDefault();
    setSuccess(false);
    await getQuote({ from, to: 'BRL', amount: Number(amount) });
  }

  async function handleConvert() {
    const result = await convert({
      from,
      to: 'BRL',
      amount: Number(amount),
      accountNumber,
      agency,
    });
    if (result) {
      setFrom('');
      setAmount('');
      setAccountNumber('');
      setAgency('');
      setSuccess(true);
      setTimeout(() => setSuccess(false), 5000);
    }
  }

  const selectedBalance = balances.find((b) => b.currency === from);

  return (
    <Form onSubmit={handleQuote}>
      <Title>Converter cripto para BRL</Title>

      <FieldGroup>
        <Label htmlFor="from">
          <FaCoins size={14} />
          Criptomoeda
        </Label>
        <Select
          id="from"
          value={from}
          onChange={(e) => setFrom(e.target.value)}
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
          <span style={{ fontSize: '0.875rem', color: '#8A929F' }}>
            Saldo disponível: {selectedBalance.amount} {selectedBalance.currency}
          </span>
        )}
      </FieldGroup>

      <FieldGroup>
        <Label htmlFor="amount">Quantidade</Label>
        <Input
          id="amount"
          type="number"
          step="any"
          min="0"
          value={amount}
          onChange={(e) => setAmount(e.target.value)}
          required
        />
      </FieldGroup>

      <FieldGroup>
        <Label htmlFor="agency">
          <FaUniversity size={14} />
          Dados da conta corrente
        </Label>
        <AccountGrid>
          <Input
            id="agency"
            type="text"
            placeholder="Agência"
            value={agency}
            onChange={(e) => setAgency(e.target.value)}
            required
          />
          <Input
            id="accountNumber"
            type="text"
            placeholder="Número da conta"
            value={accountNumber}
            onChange={(e) => setAccountNumber(e.target.value)}
            required
          />
        </AccountGrid>
      </FieldGroup>

      <SubmitButton type="submit" disabled={loading}>
        {loading ? 'Cotando...' : 'Cotar'}
      </SubmitButton>

      {quote && (
        <QuoteBox>
          <QuoteRow>
            <span>Cotação unitária</span>
            <QuoteValue>
              {quote.rate.toLocaleString('pt-BR', {
                style: 'currency',
                currency: 'BRL',
              })}
            </QuoteValue>
          </QuoteRow>
          <QuoteRow>
            <span>Quantidade</span>
            <QuoteValue>
              {quote.amount} {quote.from}
            </QuoteValue>
          </QuoteRow>
          <QuoteRow>
            <span>Agência</span>
            <QuoteValue>{agency || '-'}</QuoteValue>
          </QuoteRow>
          <QuoteRow>
            <span>Conta</span>
            <QuoteValue>{accountNumber || '-'}</QuoteValue>
          </QuoteRow>
          <QuoteRow>
            <span>Você receberá</span>
            <TotalValue>
              {quote.value.toLocaleString('pt-BR', {
                style: 'currency',
                currency: 'BRL',
              })}
            </TotalValue>
          </QuoteRow>
          <SubmitButton type="button" onClick={handleConvert} disabled={loading}>
            {loading ? 'Convertendo...' : 'Confirmar conversão'}
          </SubmitButton>
        </QuoteBox>
      )}

      {error && <ErrorMessage>{error}</ErrorMessage>}
      {success && (
        <SuccessMessage>
          <FaCheckCircle size={20} />
          Conversão realizada com sucesso!
        </SuccessMessage>
      )}
    </Form>
  );
}
