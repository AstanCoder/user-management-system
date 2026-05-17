import { AuthSession, LoginCommand } from '../../../domain/port/AuthGateway';

export interface LoginUseCase {
  execute(command: LoginCommand): Promise<AuthSession>;
}
