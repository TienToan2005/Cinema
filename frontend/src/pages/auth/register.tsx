import { useState, type FormEvent, type ChangeEvent } from 'react';
import { useNavigate } from 'react-router-dom';
import authService from '@/api/auth';
import type { RegisterRequest } from '@/interfaces/users';
import { toast } from 'react-hot-toast';
import './Auth.css';

const Register = () => {
    const navigate = useNavigate();
    const [formData, setFormData] = useState<RegisterRequest>({
        username: '',
        password: '',
        fullName: '',
        district: '',
        region: '',
        birthday: '',
        favoriteCinema: ''
    });
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);
    const [passwordConfirm, setPasswordConfirm] = useState('');
    const handleChange = (e: ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        } as RegisterRequest));
    };

    const handlePasswordConfirmChange = (e: ChangeEvent<HTMLInputElement>) => {
        setPasswordConfirm(e.target.value);
    };

    const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        setLoading(true);
        setError('');

        // Basic validation
        if (formData.password.length < 6) {
            setError('Mật khẩu phải ít nhất 6 ký tự');
            setLoading(false);
            return;
        }
        if (formData.password !== passwordConfirm) {
            setError('Mật khẩu xác nhận không khớp');
            setLoading(false);
            return;
        }

        try {
            await authService.register(formData);
            toast.success("Đăng ký thành công! Vui lòng đăng nhập.");
            navigate('/login');
        } catch (err: unknown) {
          const apiError = err as { userMessage?: string; response?: { data?: { message?: string } } };
          const errorMessage = (typeof err === 'string' ? err : (apiError.userMessage || apiError.response?.data?.message))
            || "Đăng ký thất bại, vui lòng kiểm tra lại.";
            setError(errorMessage);
        } finally {
            setLoading(false);
        }
    };
    return (
      <main className="auth-page">
        <div className="auth-container">
          <h2 className="auth-title">Dang Ky Thanh Vien</h2>
          <p className="auth-subtitle">Tao tai khoan de dat ve nhanh hon va nhan uu dai.</p>
          <form onSubmit={handleSubmit}>
            <div className="auth-grid">
              <div className="input-group full">
                <label>Email / So dien thoai</label>
                <input 
                  name="username" 
                  value={formData.username} 
                  onChange={handleChange} 
                  required 
                />
              </div>
              <div className="input-group">
                <label>Mat khau</label>
                <input 
                  name="password" 
                  type="password" 
                  value={formData.password} 
                  onChange={handleChange} 
                  required 
                />
              </div>
              <div className="input-group">
                <label>Xac nhan mat khau</label>
                <input 
                  type="password" 
                  value={passwordConfirm} 
                  onChange={handlePasswordConfirmChange} 
                  required 
                />
              </div>
            </div>

            <hr className="hr-divider" />

            <div className="auth-grid">
              <div className="input-group full">
                <label>Ho va ten</label>
                <input name="fullName" value={formData.fullName} onChange={handleChange} required />
              </div>
              <div className="input-group">
                <label>Ngay sinh</label>
                <input name="birthday" type="date" value={formData.birthday} onChange={handleChange} required />
              </div>
              <div className="input-group">
                <label>Khu vuc</label>
                <select name="region" value={formData.region} onChange={handleChange} required>
                  <option value="">Chon tinh thanh</option>
                  <option value="Ha Noi">Ha Noi</option>
                  <option value="TP.HCM">TP.HCM</option>
                  <option value="Da Nang">Da Nang</option>
                </select>
              </div>
              <div className="input-group">
                <label>Quan/Huyen</label>
                <input name="district" value={formData.district} onChange={handleChange} required />
              </div>
              <div className="input-group">
                <label>Rap yeu thich</label>
                <input name="favoriteCinema" value={formData.favoriteCinema} onChange={handleChange} />
              </div>
            </div>

            {error && <p className="error">{error}</p>}
            <button 
              type="submit" 
              className="submit-btn"
              disabled={loading}
            >
              {loading ? 'Dang dang ky...' : 'Dang Ky Ngay'}
            </button>
          </form>
        </div>
      </main>
    );
};

export default Register;
