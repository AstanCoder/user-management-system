import { describe, expect, it, vi } from 'vitest';
import { LoginService } from './LoginService';
import { AuthGateway } from '../../domain/port/AuthGateway';

describe('LoginService', () => {
  it('delegates login to gateway', async () => {
    const gateway: AuthGateway = {
      login: vi.fn().mockResolvedValue({
        token: 't',
        userId: '1',
        email: 'a@b.com',
        firstName: 'A',
        lastName: 'B',
        role: 'ADMIN',
      }),
      register: vi.fn(),
      getCurrentUser: vi.fn(),
      forgotPassword: vi.fn(),
      resetPassword: vi.fn(),
    };
    const service = new LoginService(gateway);
    await service.execute({ email: 'a@b.com', password: 'secret' });
    expect(gateway.login).toHaveBeenCalledWith({ email: 'a@b.com', password: 'secret' });
  });
});
