import { BaseMutationService } from '@/shared/application/service/BaseMutationService';
import { UserAdminGateway, UserDto } from '@/user/domain/port/UserAdminGateway';
import { UpdateUserUseCase } from '@/user/application/port/in/UpdateUserUseCase';
import { UpdateUserCommand } from '@/user/application/command/UpdateUserCommand';

export class UpdateUserService extends BaseMutationService<UpdateUserCommand, UserDto> implements UpdateUserUseCase {
  constructor(private readonly gateway: UserAdminGateway) {
    super();
  }

  protected handleMutation(command: UpdateUserCommand): Promise<UserDto> {
    return this.gateway.update(command.id, command.payload);
  }
}
