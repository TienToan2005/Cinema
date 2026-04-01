import api from '@/services/api';
import type { LoginRequest, RegisterRequest, TokenResponse, UserResponse } from '@/interfaces/users';

type LoginApiResponse = TokenResponse | { data?: TokenResponse };

const extractTokenResponse = (payload: LoginApiResponse): TokenResponse => {
    const direct = payload as TokenResponse;
    if (direct?.accessToken) {
        return direct;
    }

    const nested = (payload as { data?: TokenResponse })?.data;
    if (nested?.accessToken) {
        return nested;
    }

    throw new Error('Khong doc duoc token tu phan hoi dang nhap');
};

const authService = {

    login: async (data: LoginRequest): Promise<TokenResponse> => {
        const identity = (data.username ?? data.email ?? '').trim();
        const payload: LoginRequest = {
            username: identity,
            password: data.password,
            ...(identity.includes('@') ? { email: identity } : {})
        };

        const res = await api.post<LoginApiResponse>("/auth/login" , payload);
        return extractTokenResponse(res.data);
    },

    register: async (data: RegisterRequest): Promise<UserResponse> => {
        const res = await api.post<UserResponse>("/auth/register", data);
        return res.data;
    },

    logout: () => {
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
        window.location.href = '/login';
    },

    getCurrentUser: async (): Promise<UserResponse> => {
        const res = await api.get<UserResponse>("/auth/me");
        return res.data;
    }
};

export default authService;
