
import axios from 'axios';

const normalizeToken = (token: string) => token.replace(/^Bearer\s+/i, '').trim();

const api = axios.create({
    // Use Vite proxy in dev by default to avoid browser CORS issues.
    baseURL: import.meta.env.VITE_API_URL || '/api',
    timeout: 10000,
    headers: {
        'Content-Type': 'application/json'
    }
});

api.interceptors.request.use((config) => {
    const token = localStorage.getItem('accessToken');

    if (token) {
        config.headers.Authorization = `Bearer ${normalizeToken(token)}`;
    }

    return config;
});

api.interceptors.response.use(
    (response) => response,
    (error) => {
        const serverMessage = error.response?.data?.message;
        const message = typeof serverMessage === 'string' && serverMessage.trim()
            ? serverMessage
            : (error.message || 'Da co loi xay ra');

        if (error.response?.status === 401) {
            console.error('Token het han hoac khong hop le');
        }

        error.userMessage = message;
        return Promise.reject(error);
    }
);

export default api;