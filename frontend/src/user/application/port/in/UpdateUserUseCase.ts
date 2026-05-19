import { MutationUseCase } from '@/shared/application/port/in/MutationUseCase';
import { UserDto } from '@/user/domain/port/UserAdminGateway';
import { UpdateUserCommand } from '@/user/application/command/UpdateUserCommand';

export type UpdateUserUseCase = MutationUseCase<UpdateUserCommand, UserDto>;
