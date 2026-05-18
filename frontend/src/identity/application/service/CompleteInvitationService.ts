import { AuthGateway, AuthSession } from '../../domain/port/AuthGateway';
import { CompleteInvitationUseCase } from '../port/in/CompleteInvitationUseCase';

export class CompleteInvitationService implements CompleteInvitationUseCase {
  constructor(private readonly gateway: AuthGateway) {}

  execute(token: string, newPassword: string): Promise<AuthSession> {
    return this.gateway.completeInvitation(token, newPassword);
  }
}
