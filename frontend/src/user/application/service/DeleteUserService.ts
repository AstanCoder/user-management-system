import { BaseMutationService } from '@/shared/application/service/BaseMutationService';
import { UserAdminGateway } from '@/user/domain/port/UserAdminGateway';
import { DeleteUserUseCase } from '@/user/application/port/in/DeleteUserUseCase';

export class DeleteUserService extends BaseMutationService<string, void> implements DeleteUserUseCase {
  constructor(private readonly gateway: UserAdminGateway) {
    super();
  }

  protected handleMutation(command: string): Promise<void> {
    return this.gateway.delete(command);
  }
}
