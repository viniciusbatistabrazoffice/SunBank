import styled from 'styled-components'

export const Main = styled.main`
  margin-left: 260px;
  min-height: 100vh;
  padding: ${({ theme }) => theme.spacing.xl};
  background: ${({ theme }) => theme.colors.background};
`

export const Header = styled.header`
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: ${({ theme }) => theme.spacing.xl};
`

export const PageTitle = styled.h1`
  font-size: ${({ theme }) => theme.fontSizes.xxl};
  font-weight: 700;
  color: ${({ theme }) => theme.colors.text};
`

export const Grid = styled.div`
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));
  gap: ${({ theme }) => theme.spacing.lg};
`
