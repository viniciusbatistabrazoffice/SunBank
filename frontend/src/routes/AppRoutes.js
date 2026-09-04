import { Routes, Route } from 'react-router-dom';
import { Header } from '../components/common/Header';
import { Dashboard } from '../pages/Dashboard';
import { Convert } from '../pages/Convert';
import { Transfer } from '../pages/Transfer';

export function AppRoutes() {
  return (
    <>
      <Header />
      <Routes>
        <Route path="/" element={<Dashboard />} />
        <Route path="/convert" element={<Convert />} />
        <Route path="/transfer" element={<Transfer />} />
      </Routes>
    </>
  );
}
