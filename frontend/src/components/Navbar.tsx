import { Link, useNavigate } from 'react-router-dom';
import './Navbar.css';

const Navbar = () => {
  const navigate = useNavigate();
  const token = localStorage.getItem('accessToken');

  const handleLogout = () => {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    navigate('/login');
  };

  return (
    <nav className="navbar">
      <div className="navbar-inner">
        <Link to="/" className="navbar-logo">
          <span className="navbar-logo-mark">CINEMAX</span>
          <span className="navbar-logo-text">BOOK TICKET EXPERIENCE</span>
        </Link>

        <ul className="navbar-links">
          <li><Link to="/" className="navbar-link">Trang chu</Link></li>
          <li><Link to="/" className="navbar-link">Phim</Link></li>

          {!token ? (
            <>
              <li><Link to="/login" className="navbar-link">Dang nhap</Link></li>
              <li><Link to="/register" className="navbar-link navbar-cta">Dang ky</Link></li>
            </>
          ) : (
            <li>
              <button type="button" onClick={handleLogout} className="navbar-button">
                Dang xuat
              </button>
            </li>
          )}
        </ul>
      </div>
    </nav>
  );
};

export default Navbar;