import { InviteUserPayload, UserDto } from '@/user/domain/port/UserAdminGateway';
import { MutationUseCase } from '@/shared/application/port/in/MutationUseCase';

export type InviteUserUseCase = MutationUseCase<InviteUserPayload, UserDto>;
