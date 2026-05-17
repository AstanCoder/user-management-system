import { AuthSession, RegisterCommand } from '../../../domain/port/AuthGateway';

export interface RegisterUseCase {
  execute(command: RegisterCommand): Promise<AuthSession>;
}
