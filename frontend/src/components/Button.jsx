import styled, { css } from 'styled-components'

const variants = {
  primary: css`
    background: ${({ theme }) => theme.colors.primary};
    color: ${({ theme }) => theme.colors.background};
    &:hover {
      background: ${({ theme }) => theme.colors.primaryHover};
      box-shadow: ${({ theme }) => theme.shadows.glow};
    }
  `,
  secondary: css`
    background: ${({ theme }) => theme.colors.surfaceLight};
    color: ${({ theme }) => theme.colors.text};
    border: 1px solid ${({ theme }) => theme.colors.border};
    &:hover {
      background: ${({ theme }) => theme.colors.border};
    }
  `,
  ghost: css`
    background: transparent;
    color: ${({ theme }) => theme.colors.textMuted};
    &:hover {
      color: ${({ theme }) => theme.colors.text};
      background: ${({ theme }) => theme.colors.surfaceLight};
    }
  `,
}

export const Button = styled.button`
  padding: ${({ theme }) => `${theme.spacing.sm} ${theme.spacing.lg}`};
  border-radius: ${({ theme }) => theme.radii.md};
  border: none;
  font-weight: 600;
  font-size: ${({ theme }) => theme.fontSizes.sm};
  transition: all 0.2s ease;
  display: inline-flex;
  align-items: center;
  gap: ${({ theme }) => theme.spacing.sm};
  ${({ $variant = 'primary' }) => variants[$variant]}
`
