import { useState, useRef, useEffect } from 'react';
import { NavLink, useNavigate } from 'react-router-dom';
import styled from 'styled-components';
import { FaUserCircle, FaSearch, FaCog, FaSignOutAlt } from 'react-icons/fa';
import { useAuth } from '../../contexts/AuthContext';

const HeaderContainer = styled.header`
  position: fixed;
  top: 0;
  left: 260px;
  right: 0;
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 ${({ theme }) => theme.spacing.xl};
  background: ${({ theme }) => theme.colors.surface};
  border-bottom: 1px solid ${({ theme }) => theme.colors.border};
  box-shadow: ${({ theme }) => theme.shadows.md};
  z-index: 100;
`;

const Logo = styled(NavLink)`
  margin-right: auto;
  font-size: ${({ theme }) => theme.fontSizes.xl};
  font-weight: 700;
  color: ${({ theme }) => theme.colors.text};
  text-decoration: none;
  letter-spacing: -0.5px;
`;

const SearchContainer = styled.div`
  position: relative;
  display: flex;
  align-items: center;
  width: 420px;
`;

const SearchIcon = styled(FaSearch).attrs({ size: 16 })`
  position: absolute;
  left: ${({ theme }) => theme.spacing.md};
  color: ${({ theme }) => theme.colors.textMuted};
  pointer-events: none;
`;

const SearchInput = styled.input`
  width: 100%;
  padding: ${({ theme }) => `${theme.spacing.sm} ${theme.spacing.md}`};
  padding-left: 2.5rem;
  border: 1px solid ${({ theme }) => theme.colors.border};
  border-radius: ${({ theme }) => theme.radii.md};
  background: ${({ theme }) => theme.colors.surfaceLight};
  color: ${({ theme }) => theme.colors.text};
  font-size: ${({ theme }) => theme.fontSizes.sm};
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

const Profile = styled.div`
  margin-left: auto;
  position: relative;
  display: flex;
  align-items: center;
`;

const ProfileButton = styled.button`
  display: flex;
  align-items: center;
  gap: ${({ theme }) => theme.spacing.md};
  padding: ${({ theme }) => `${theme.spacing.sm} ${theme.spacing.md}`};
  border-radius: ${({ theme }) => theme.radii.md};
  background: ${({ theme }) => theme.colors.surfaceLight};
  border: 1px solid ${({ theme }) => theme.colors.border};
  cursor: pointer;
  transition: all 0.2s ease;

  &:hover {
    border-color: ${({ theme }) => theme.colors.primary};
    background: ${({ theme }) => theme.colors.background};
  }
`;

const ProfileInfo = styled.div`
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  max-width: 160px;
`;

const ProfileName = styled.span`
  font-size: ${({ theme }) => theme.fontSizes.sm};
  font-weight: 600;
  color: ${({ theme }) => theme.colors.text};
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 100%;
`;

const ProfileRole = styled.span`
  font-size: ${({ theme }) => theme.fontSizes.xs};
  color: ${({ theme }) => theme.colors.textMuted};
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 100%;
`;

const Dropdown = styled.div`
  position: absolute;
  top: calc(100% + ${({ theme }) => theme.spacing.sm});
  right: 0;
  min-width: 180px;
  padding: ${({ theme }) => theme.spacing.sm};
  border-radius: ${({ theme }) => theme.radii.md};
  background: ${({ theme }) => theme.colors.surface};
  border: 1px solid ${({ theme }) => theme.colors.border};
  box-shadow: ${({ theme }) => theme.shadows.lg};
  z-index: 110;
`;

const DropdownItem = styled.button`
  display: flex;
  align-items: center;
  gap: ${({ theme }) => theme.spacing.md};
  width: 100%;
  padding: ${({ theme }) => `${theme.spacing.md} ${theme.spacing.lg}`};
  border: none;
  border-radius: ${({ theme }) => theme.radii.md};
  background: transparent;
  color: ${({ theme }) => theme.colors.textMuted};
  font-size: ${({ theme }) => theme.fontSizes.sm};
  font-weight: 500;
  text-align: left;
  cursor: pointer;
  transition: all 0.2s ease;

  &:hover {
    background: ${({ theme }) => theme.colors.surfaceLight};
    color: ${({ theme }) => theme.colors.text};
  }
`;

export function Header() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const [query, setQuery] = useState('');
  const [open, setOpen] = useState(false);
  const profileRef = useRef(null);

  const toggle = () => setOpen((prev) => !prev);

  const goToSettings = () => {
    setOpen(false);
    navigate('/settings');
  };

  const handleLogout = () => {
    setOpen(false);
    logout();
    navigate('/');
  };

  useEffect(() => {
    const close = (e) => {
      if (profileRef.current && !profileRef.current.contains(e.target)) {
        setOpen(false);
      }
    };
    document.addEventListener('mousedown', close);
    return () => document.removeEventListener('mousedown', close);
  }, []);

  return (
    <HeaderContainer>
      <Logo to="/" end>
        SunBank
      </Logo>
      <SearchContainer>
        <SearchIcon />
        <SearchInput
          type="text"
          placeholder="Buscar..."
          value={query}
          onChange={(e) => setQuery(e.target.value)}
        />
      </SearchContainer>
      <Profile ref={profileRef}>
        <ProfileButton type="button" onClick={toggle}>
          <ProfileInfo>
            <ProfileName>{user?.name || 'Usuário SunBank'}</ProfileName>
            <ProfileRole>{user?.email || 'Bem-vindo de volta'}</ProfileRole>
          </ProfileInfo>
          <FaUserCircle size={34} color="#F5B800" />
        </ProfileButton>
        {open && (
          <Dropdown>
            <DropdownItem type="button" onClick={goToSettings}>
              <FaCog size={16} />
              Configurações
            </DropdownItem>
            <DropdownItem type="button" onClick={handleLogout}>
              <FaSignOutAlt size={16} />
              Sair
            </DropdownItem>
          </Dropdown>
        )}
      </Profile>
    </HeaderContainer>
  );
}
