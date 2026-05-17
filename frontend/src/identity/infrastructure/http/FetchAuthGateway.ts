import { apiFetch, parseApiError } from '@/shared/lib/apiFetch';
import { setStoredAuth, StoredAuth } from '@/shared/lib/authStorage';
import { AuthGateway, AuthSession, LoginCommand, RegisterCommand } from '../../domain/port/AuthGateway';
import { AuthApiResponse, CurrentUserApiResponse } from './AuthApiDto';

function toSession(dto: AuthApiResponse): AuthSession {
  return {
    token: dto.token,
    userId: dto.userId,
    email: dto.email,
    firstName: dto.firstName,
    lastName: dto.lastName,
    role: dto.role,
  };
}

function persist(session: AuthSession): AuthSession {
  const stored: StoredAuth = {
    token: session.token,
    userId: session.userId,
    email: session.email,
    firstName: session.firstName,
    lastName: session.lastName,
    role: session.role,
  };
  setStoredAuth(stored);
  return session;
}

export class FetchAuthGateway implements AuthGateway {
  constructor(private readonly baseUrl: string) {}

  async login(command: LoginCommand): Promise<AuthSession> {
    const response = await apiFetch(this.baseUrl, '/api/auth/login', {
      method: 'POST',
      body: JSON.stringify(command),
    });
    if (!response.ok) {
      throw new Error(await parseApiError(response));
    }
    const dto = (await response.json()) as AuthApiResponse;
    return persist(toSession(dto));
  }

  async register(command: RegisterCommand): Promise<AuthSession> {
    const response = await apiFetch(this.baseUrl, '/api/auth/register', {
      method: 'POST',
      body: JSON.stringify(command),
    });
    if (!response.ok) {
      throw new Error(await parseApiError(response));
    }
    const dto = (await response.json()) as AuthApiResponse;
    return persist(toSession(dto));
  }

  async getCurrentUser(): Promise<AuthSession> {
    const response = await apiFetch(this.baseUrl, '/api/auth/me');
    if (!response.ok) {
      throw new Error(await parseApiError(response));
    }
    const dto = (await response.json()) as CurrentUserApiResponse;
    const auth = getStoredAuthFromMe(dto);
    setStoredAuth(auth);
    return {
      token: auth.token,
      userId: auth.userId,
      email: auth.email,
      firstName: auth.firstName,
      lastName: auth.lastName,
      role: auth.role,
    };
  }

  async forgotPassword(email: string): Promise<void> {
    const response = await apiFetch(this.baseUrl, '/api/auth/forgot-password', {
      method: 'POST',
      body: JSON.stringify({ email }),
    });
    if (!response.ok) {
      throw new Error(await parseApiError(response));
    }
  }

  async resetPassword(token: string, newPassword: string): Promise<void> {
    const response = await apiFetch(this.baseUrl, '/api/auth/reset-password', {
      method: 'POST',
      body: JSON.stringify({ token, newPassword }),
    });
    if (!response.ok) {
      throw new Error(await parseApiError(response));
    }
  }
}

import { getStoredAuth } from '@/shared/lib/authStorage';

function getStoredAuthFromMe(dto: CurrentUserApiResponse): StoredAuth {
  const token = getStoredAuth()?.token ?? '';
  return {
    token,
    userId: dto.id,
    email: dto.email,
    firstName: dto.firstName,
    lastName: dto.lastName,
    role: dto.role,
  };
}
