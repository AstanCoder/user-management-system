import { AuthSession } from '../../../domain/port/AuthGateway';

export interface CompleteInvitationUseCase {
  execute(token: string, newPassword: string): Promise<AuthSession>;
}
