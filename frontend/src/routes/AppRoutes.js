import { Routes, Route } from 'react-router-dom';
import { Header } from '../components/common/Header';
import { Sidebar } from '../components/common/Sidebar';
import { Dashboard } from '../pages/Dashboard';
import { Convert } from '../pages/Convert';
import { Transfer } from '../pages/Transfer';

export function AppRoutes() {
  return (
    <>
      <Header />
      <div className="app-layout">
        <Sidebar />
        <div className="app-content">
          <Routes>
            <Route path="/" element={<Dashboard />} />
            <Route path="/convert" element={<Convert />} />
            <Route path="/transfer" element={<Transfer />} />
          </Routes>
        </div>
      </div>
    </>
  );
}
