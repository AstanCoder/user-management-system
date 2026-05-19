import { BaseMutationService } from '@/shared/application/service/BaseMutationService';
import { UserAdminGateway } from '@/user/domain/port/UserAdminGateway';
import { ResendInvitationUseCase } from '@/user/application/port/in/ResendInvitationUseCase';

export class ResendInvitationService extends BaseMutationService<string, void> implements ResendInvitationUseCase {
  constructor(private readonly gateway: UserAdminGateway) {
    super();
  }

  protected handleMutation(command: string): Promise<void> {
    return this.gateway.resendInvitation(command);
  }
}
