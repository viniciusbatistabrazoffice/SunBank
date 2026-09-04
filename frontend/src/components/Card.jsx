import styled from 'styled-components'

export const Card = styled.div`
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
`

export const CardTitle = styled.h3`
  font-size: ${({ theme }) => theme.fontSizes.sm};
  color: ${({ theme }) => theme.colors.textMuted};
  font-weight: 500;
  margin-bottom: ${({ theme }) => theme.spacing.sm};
`

export const CardValue = styled.div`
  font-size: ${({ theme }) => theme.fontSizes.xl};
  font-weight: 700;
  color: ${({ theme }) => theme.colors.text};
`
