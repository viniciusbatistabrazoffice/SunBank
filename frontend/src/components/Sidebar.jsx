import { useState } from 'react'
import styled from 'styled-components'
import { FaChartPie, FaWallet, FaSyncAlt, FaPaperPlane, FaCog, FaSignOutAlt, FaPlus, FaUserCircle } from 'react-icons/fa'
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

const NavItem = styled.a`
  display: flex;
  align-items: center;
  gap: ${({ theme }) => theme.spacing.md};
  padding: ${({ theme }) => `${theme.spacing.md} ${theme.spacing.lg}`};
  border-radius: ${({ theme }) => theme.radii.md};
  border-left: 3px solid
    ${({ theme, $active }) => ($active ? theme.colors.primary : 'transparent')};
  color: ${({ theme, $active }) => ($active ? theme.colors.primary : theme.colors.textMuted)};
  background: ${({ theme, $active }) => ($active ? 'rgba(245, 184, 0, 0.08)' : 'transparent')};
  font-weight: 500;
  font-size: ${({ theme }) => theme.fontSizes.md};
  transition: all 0.2s ease;

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
  { id: 'dashboard', icon: FaChartPie, label: 'Dashboard' },
  { id: 'wallet', icon: FaWallet, label: 'Carteira' },
  { id: 'convert', icon: FaSyncAlt, label: 'Converter' },
  { id: 'transfer', icon: FaPaperPlane, label: 'Transferir' },
  { id: 'settings', icon: FaCog, label: 'Configurações' },
]

export function Sidebar() {
  const [activeId, setActiveId] = useState('dashboard')

  const handleNavClick = (id) => (e) => {
    e.preventDefault()
    setActiveId(id)
  }

  return (
    <SidebarContainer>
      <LogoArea>
        <img src={logo} alt="SunBank" />
        <span>SunBank</span>
      </LogoArea>

      <NewTransaction type="button">
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
        {navItems.map(({ icon: Icon, label, id }) => (
          <NavItem
            key={id}
            href="#"
            $active={activeId === id}
            onClick={handleNavClick(id)}
          >
            <Icon size={20} />
            {label}
          </NavItem>
        ))}
      </Nav>

      <Footer>
        <NavItem as="button" href={undefined} onClick={() => {}}>
          <FaSignOutAlt size={20} />
          Sair
        </NavItem>
      </Footer>
    </SidebarContainer>
  )
}
