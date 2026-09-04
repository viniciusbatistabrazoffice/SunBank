import { ThemeProvider } from 'styled-components'
import { theme } from './styles/theme'
import { GlobalStyles } from './styles/GlobalStyles'
import { Sidebar } from './components/Sidebar'
import { Main } from './components/Layout'
import { Dashboard } from './pages/Dashboard'

function App() {
  return (
    <ThemeProvider theme={theme}>
      <GlobalStyles />
      <Sidebar />
      <Main>
        <Dashboard />
      </Main>
    </ThemeProvider>
  )
}

export default App
