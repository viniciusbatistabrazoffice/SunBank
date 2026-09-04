import { FaArrowUp, FaArrowDown, FaDollarSign, FaCoins } from 'react-icons/fa'
import { Button } from '../components/Button'
import { Card, CardTitle, CardValue } from '../components/Card'
import { Header, PageTitle, Grid } from '../components/Layout'

const stats = [
  { title: 'Saldo Total', value: 'R$ 124.590,00', icon: FaDollarSign, positive: true },
  { title: 'Receitas', value: 'R$ 18.430,00', icon: FaArrowUp, positive: true },
  { title: 'Despesas', value: 'R$ 4.120,00', icon: FaArrowDown, positive: false },
  { title: 'Investimentos', value: 'R$ 32.150,00', icon: FaCoins, positive: true },
]

export function Dashboard() {
  return (
    <>
      <Header>
        <PageTitle>Dashboard</PageTitle>
        <Button $variant="primary">Nova Transferência</Button>
      </Header>

      <Grid>
        {stats.map(({ title, value, icon: Icon, positive }) => (
          <Card key={title}>
            <CardTitle>
              <Icon
                size={14}
                style={{
                  marginRight: '8px',
                  color: positive ? '#22C55E' : '#EF4444',
                }}
              />
              {title}
            </CardTitle>
            <CardValue>{value}</CardValue>
          </Card>
        ))}
      </Grid>
    </>
  )
}
