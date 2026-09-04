import { Routes, Route } from 'react-router-dom';
import { Sidebar } from '../components/Sidebar';
import { Main } from '../components/Layout';
import { Dashboard } from '../pages/Dashboard.jsx';
import { Wallet } from '../pages/Wallet';
import { Convert } from '../pages/Convert';
import { Transfer } from '../pages/Transfer';
import { Settings } from '../pages/Settings';

export function AppRoutes() {
  return (
    <>
      <Sidebar />
      <Main as="div">
        <Routes>
          <Route path="/" element={<Dashboard />} />
          <Route path="/wallet" element={<Wallet />} />
          <Route path="/convert" element={<Convert />} />
          <Route path="/transfer" element={<Transfer />} />
          <Route path="/settings" element={<Settings />} />
        </Routes>
      </Main>
    </>
  );
}
