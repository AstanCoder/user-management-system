import { BaseMutationService } from '@/shared/application/service/BaseMutationService';
import { InviteUserPayload, UserAdminGateway, UserDto } from '@/user/domain/port/UserAdminGateway';
import { InviteUserUseCase } from '@/user/application/port/in/InviteUserUseCase';

export class InviteUserService extends BaseMutationService<InviteUserPayload, UserDto> implements InviteUserUseCase {
  constructor(private readonly gateway: UserAdminGateway) {
    super();
  }

  protected handleMutation(command: InviteUserPayload): Promise<UserDto> {
    return this.gateway.invite(command);
  }
}
