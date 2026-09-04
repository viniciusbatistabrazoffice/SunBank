import { FaUserCircle } from 'react-icons/fa';
import styled from 'styled-components';
import { Card, CardTitle, CardValue } from '../../components/Card';
import { Header, PageTitle, Grid } from '../../components/Layout';
import { useAuth } from '../../contexts/AuthContext';

const Page = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: ${({ theme }) => theme.spacing.xl};
`;

const ProfileCard = styled(Card)`
  display: flex;
  align-items: center;
  gap: ${({ theme }) => theme.spacing.xl};
  max-width: 600px;
`;

const Info = styled.div`
  display: flex;
  flex-direction: column;
  gap: ${({ theme }) => theme.spacing.md};
`;

export function Profile() {
  const { user } = useAuth();

  return (
    <Page>
      <Header>
        <PageTitle>Perfil</PageTitle>
      </Header>

      <Grid>
        <ProfileCard>
          <FaUserCircle size={72} color="#F5B800" />
          <Info>
            <div>
              <CardTitle>Nome</CardTitle>
              <CardValue>{user?.name || 'Usuário SunBank'}</CardValue>
            </div>
            <div>
              <CardTitle>Email</CardTitle>
              <CardValue>{user?.email || 'Bem-vindo de volta'}</CardValue>
            </div>
          </Info>
        </ProfileCard>
      </Grid>
    </Page>
  );
}
