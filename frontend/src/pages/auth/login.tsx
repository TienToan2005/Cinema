import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import authService from '@/api/auth';
import type { LoginRequest } from '@/interfaces/users';
import { toast } from 'react-hot-toast';
import './Auth.css';

const normalizeToken = (token: string) => token.replace(/^Bearer\s+/i, '').trim();

const Login = () => {
    const navigate = useNavigate();

    const [formData, setFormData] = useState<LoginRequest>({
        username: '',
        password: ''
    });
    const [error, setError] = useState<string | null>(null);
    const [loading, setLoading] = useState(false);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const {name, value} = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
    };
    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        setLoading(true);
        setError(null);
        try{
            const res = await authService.login(formData);
          localStorage.setItem('accessToken', normalizeToken(res.accessToken));
          localStorage.setItem('refreshToken', normalizeToken(res.refreshToken));
            toast.success('Đăng nhập thành công!');
            navigate('/');
        }catch(err: any){
          const errorMessage = (typeof err === 'string' ? err : (err?.userMessage || err?.response?.data?.message))
            || 'Tài khoản hoặc mật khẩu không chính xác';
            setError(errorMessage);
            toast.error(errorMessage);
        }finally{
            setLoading(false);
        }
    };

    return (
        <main className="auth-page">
          <div className="auth-container">
            <h2 className="auth-title">Dang Nhap</h2>
            <p className="auth-subtitle">Tiep tuc trai nghiem dat ve va luu lich su giao dich.</p>
            <form onSubmit={handleSubmit}>
              <div className="input-group full">
                <label>Email / So dien thoai</label>
                <input
                  type="text"
                  name="username"
                  value={formData.username}
                  onChange={handleChange}
                  required
                />
              </div>
              <div className="input-group full">
                <label>Mat khau</label>
                <input
                  type="password"
                  name="password"
                  value={formData.password}
                  onChange={handleChange}
                  required
                />
              </div>
              {error && <p className="error">{error}</p>}
              <button 
                type="submit" 
                className="submit-btn"
                disabled={loading}
              >
                {loading ? 'Dang xac thuc...' : 'Dang Nhap'}
              </button>
            </form>
          </div>
        </main>
    );
};
export default Login;
