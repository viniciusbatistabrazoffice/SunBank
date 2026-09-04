import { Routes, Route } from 'react-router-dom';
import { Header } from '../components/common/Header';
import { Sidebar } from '../components/Sidebar';
import { Main } from '../components/Layout';
import { Dashboard } from '../pages/Dashboard.jsx';
import { Wallet } from '../pages/Wallet';
import { Transfer } from '../pages/Transfer';
import { VirtualCard } from '../pages/VirtualCard';
import { Settings } from '../pages/Settings';

export function AppRoutes() {
  return (
    <>
      <Header />
      <Sidebar />
      <Main as="div">
        <Routes>
          <Route path="/" element={<Dashboard />} />
          <Route path="/wallet" element={<Wallet />} />
          <Route path="/transfer" element={<Transfer />} />
          <Route path="/virtual-card" element={<VirtualCard />} />
          <Route path="/settings" element={<Settings />} />
        </Routes>
      </Main>
    </>
  );
}
