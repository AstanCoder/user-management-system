import type { UserRole } from '../model/UserRole';

export interface LoginCommand {
  email: string;
  password: string;
}

export interface RegisterCommand {
  email: string;
  password: string;
  firstName: string;
  lastName: string;
}

export interface AuthSession {
  token: string;
  userId: string;
  email: string;
  firstName: string;
  lastName: string;
  role: UserRole;
}

export interface AuthGateway {
  login(command: LoginCommand): Promise<AuthSession>;
  register(command: RegisterCommand): Promise<AuthSession>;
  getCurrentUser(): Promise<AuthSession>;
  forgotPassword(email: string): Promise<void>;
  resetPassword(token: string, newPassword: string): Promise<void>;
}
