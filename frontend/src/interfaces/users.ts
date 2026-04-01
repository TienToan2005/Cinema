export interface UserBase {
  id: number;
  fullName: string;
  email: string;
  phoneNumber: string;
  role: string;
  birthday: string;
  region: string;
  district: string;
}

export interface LoginRequest {
  username?: string;
  email?: string;
  password: string;
}

export interface RegisterRequest {
  username?: string;
  email?: string;
  password: string;
  fullName: string;
  phoneNumber?: string;
  birthday: string;
  district: string;
  region: string;
  favoriteCinema?: string;
}

export interface TokenResponse {
  accessToken: string;
  refreshToken: string;
}

export interface UserResponse extends UserBase {
  username?: string;
}

