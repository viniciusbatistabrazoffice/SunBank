import { NavLink, useNavigate } from 'react-router-dom'
import styled from 'styled-components'
import { FaChartPie, FaWallet, FaSyncAlt, FaPaperPlane, FaCreditCard, FaCog, FaSignOutAlt, FaPlus, FaUserCircle } from 'react-icons/fa'
import logo from '../assets/logo.svg'

const SidebarContainer = styled.aside`
  width: 260px;
  height: 100vh;
  position: fixed;
  left: 0;
  top: 0;
  display: flex;
  flex-direction: column;
  padding: ${({ theme }) => theme.spacing.lg};
  background: ${({ theme }) => theme.colors.surface};
  border-right: 1px solid ${({ theme }) => theme.colors.border};
  box-shadow: ${({ theme }) => theme.shadows.lg};
  z-index: 50;
`

const LogoArea = styled.div`
  display: flex;
  align-items: center;
  gap: ${({ theme }) => theme.spacing.md};
  margin-bottom: ${({ theme }) => theme.spacing.xl};

  img {
    width: 40px;
    height: 40px;
  }

  span {
    font-size: ${({ theme }) => theme.fontSizes.lg};
    font-weight: 700;
    color: ${({ theme }) => theme.colors.text};
    letter-spacing: -0.5px;
  }
`

const NewTransaction = styled.button`
  display: flex;
  align-items: center;
  justify-content: center;
  gap: ${({ theme }) => theme.spacing.sm};
  width: 100%;
  padding: ${({ theme }) => theme.spacing.md};
  border: none;
  border-radius: ${({ theme }) => theme.radii.md};
  background: ${({ theme }) => theme.colors.primary};
  color: ${({ theme }) => theme.colors.surface};
  font-weight: 600;
  font-size: ${({ theme }) => theme.fontSizes.md};
  margin-bottom: ${({ theme }) => theme.spacing.xl};
  transition: all 0.2s ease;

  &:hover {
    background: ${({ theme }) => theme.colors.primaryHover};
    box-shadow: ${({ theme }) => theme.shadows.glow};
    transform: translateY(-1px);
  }
`

const UserCard = styled.div`
  display: flex;
  align-items: center;
  gap: ${({ theme }) => theme.spacing.md};
  padding: ${({ theme }) => theme.spacing.md};
  border-radius: ${({ theme }) => theme.radii.md};
  background: ${({ theme }) => theme.colors.surfaceLight};
  border: 1px solid ${({ theme }) => theme.colors.border};
  margin-bottom: ${({ theme }) => theme.spacing.xl};
`

const UserInfo = styled.div`
  display: flex;
  flex-direction: column;

  span:first-child {
    font-weight: 600;
    color: ${({ theme }) => theme.colors.text};
    font-size: ${({ theme }) => theme.fontSizes.sm};
  }

  span:last-child {
    color: ${({ theme }) => theme.colors.textMuted};
    font-size: ${({ theme }) => theme.fontSizes.xs};
  }
`

const SectionTitle = styled.p`
  font-size: ${({ theme }) => theme.fontSizes.xs};
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 1px;
  color: ${({ theme }) => theme.colors.textMuted};
  margin-bottom: ${({ theme }) => theme.spacing.md};
  padding-left: ${({ theme }) => theme.spacing.md};
`

const Nav = styled.nav`
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: ${({ theme }) => theme.spacing.xs};
`

const NavItem = styled(NavLink)`
  display: flex;
  align-items: center;
  gap: ${({ theme }) => theme.spacing.md};
  padding: ${({ theme }) => `${theme.spacing.md} ${theme.spacing.lg}`};
  border-radius: ${({ theme }) => theme.radii.md};
  border-left: 3px solid transparent;
  color: ${({ theme }) => theme.colors.textMuted};
  background: transparent;
  font-weight: 500;
  font-size: ${({ theme }) => theme.fontSizes.md};
  transition: all 0.2s ease;
  text-decoration: none;

  &:hover {
    background: ${({ theme }) => theme.colors.surfaceLight};
    color: ${({ theme }) => theme.colors.text};
    transform: translateX(4px);
  }

  &.active {
    background: rgba(245, 184, 0, 0.08);
    color: ${({ theme }) => theme.colors.primary};
    border-left-color: ${({ theme }) => theme.colors.primary};
  }
`

const LogoutButton = styled.button`
  display: flex;
  align-items: center;
  gap: ${({ theme }) => theme.spacing.md};
  width: 100%;
  padding: ${({ theme }) => `${theme.spacing.md} ${theme.spacing.lg}`};
  border: none;
  border-radius: ${({ theme }) => theme.radii.md};
  border-left: 3px solid transparent;
  background: transparent;
  color: ${({ theme }) => theme.colors.textMuted};
  font-weight: 500;
  font-size: ${({ theme }) => theme.fontSizes.md};
  transition: all 0.2s ease;
  text-align: left;

  &:hover {
    background: ${({ theme }) => theme.colors.surfaceLight};
    color: ${({ theme }) => theme.colors.text};
    transform: translateX(4px);
  }
`

const Footer = styled.div`
  margin-top: auto;
  padding-top: ${({ theme }) => theme.spacing.lg};
  border-top: 1px solid ${({ theme }) => theme.colors.border};
`

const navItems = [
  { to: '/', label: 'Dashboard', icon: FaChartPie, end: true },
  { to: '/wallet', label: 'Carteira', icon: FaWallet },
  { to: '/virtual-card', label: 'Cartão Virtual', icon: FaCreditCard },
  { to: '/convert', label: 'Converter', icon: FaSyncAlt },
  { to: '/transfer', label: 'Transferir', icon: FaPaperPlane },
  { to: '/settings', label: 'Configurações', icon: FaCog },
]

export function Sidebar() {
  const navigate = useNavigate()

  return (
    <SidebarContainer>
      <LogoArea>
        <img src={logo} alt="SunBank" />
        <span>SunBank</span>
      </LogoArea>

      <NewTransaction type="button" onClick={() => navigate('/transfer')}>
        <FaPlus size={18} />
        Nova transação
      </NewTransaction>

      <UserCard>
        <FaUserCircle size={40} color="#F5B800" />
        <UserInfo>
          <span>Usuário SunBank</span>
          <span>Bem-vindo de volta</span>
        </UserInfo>
      </UserCard>

      <SectionTitle>Menu</SectionTitle>

      <Nav>
        {navItems.map(({ icon: Icon, label, to, end }) => (
          <NavItem key={to} to={to} end={end}>
            <Icon size={20} />
            {label}
          </NavItem>
        ))}
      </Nav>

      <Footer>
        <LogoutButton type="button" onClick={() => {}}>
          <FaSignOutAlt size={20} />
          Sair
        </LogoutButton>
      </Footer>
    </SidebarContainer>
  )
}
