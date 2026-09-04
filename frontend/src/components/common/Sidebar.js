import { NavLink } from 'react-router-dom';
import './Sidebar.css';

const menuItems = [
  { to: '/', label: 'Dashboard', end: true },
  { to: '/convert', label: 'Converter' },
  { to: '/transfer', label: 'Transferir' },
];

export function Sidebar() {
  return (
    <aside className="sidebar">
      <nav className="sidebar-nav">
        {menuItems.map((item) => (
          <NavLink
            key={item.to}
            to={item.to}
            end={item.end}
            className={({ isActive }) =>
              `sidebar-link${isActive ? ' sidebar-link--active' : ''}`
            }
          >
            {item.label}
          </NavLink>
        ))}
      </nav>
    </aside>
  );
}
