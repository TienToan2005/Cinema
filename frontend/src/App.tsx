import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { Toaster } from 'react-hot-toast';
import Navbar from '@/components/Navbar';
import HomePage from '@/pages/HomePage';
import MovieDetail from '@/pages/MovieDetail';
import SeatSelection from '@/pages/SeatSelection';
import Login from '@/pages/auth/login';
import Register from '@/pages/auth/register';
import './App.css';

function App() {
  return (
    <Router>
      <div className="app-shell">
        <Navbar />
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/movies/:movieId" element={<MovieDetail />} />
          <Route path="/seats/:scheduleId" element={<SeatSelection />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
        <Toaster
          position="top-right"
          toastOptions={{
            style: {
              borderRadius: '12px',
              border: '1px solid rgba(255,255,255,0.15)',
              background: '#112034',
              color: '#f7fbff',
            },
          }}
        />
      </div>
    </Router>
  );
}

export default App;
