import { DeleteUserUseCase } from '@/user/application/port/in/DeleteUserUseCase';
import { GetUserStatsUseCase } from '@/user/application/port/in/GetUserStatsUseCase';
import { InviteUserUseCase } from '@/user/application/port/in/InviteUserUseCase';
import { ListUsersUseCase } from '@/user/application/port/in/ListUsersUseCase';
import { ResendInvitationUseCase } from '@/user/application/port/in/ResendInvitationUseCase';
import { UpdateUserUseCase } from '@/user/application/port/in/UpdateUserUseCase';
import { DeleteUserService } from '@/user/application/service/DeleteUserService';
import { GetUserStatsService } from '@/user/application/service/GetUserStatsService';
import { InviteUserService } from '@/user/application/service/InviteUserService';
import { ListUsersService } from '@/user/application/service/ListUsersService';
import { ResendInvitationService } from '@/user/application/service/ResendInvitationService';
import { UpdateUserService } from '@/user/application/service/UpdateUserService';
import { UserAdminGateway } from '@/user/domain/port/UserAdminGateway';
import { FetchUserAdminGateway } from '../http/FetchUserAdminGateway';

const baseUrl = process.env.NEXT_PUBLIC_API_URL ?? 'http://localhost:8080';

export interface UserDependencies {
  userAdminGateway: UserAdminGateway;
  listUsersUseCase: ListUsersUseCase;
  getUserStatsUseCase: GetUserStatsUseCase;
  updateUserUseCase: UpdateUserUseCase;
  deleteUserUseCase: DeleteUserUseCase;
  inviteUserUseCase: InviteUserUseCase;
  resendInvitationUseCase: ResendInvitationUseCase;
}

export function createUserDependencies(apiBaseUrl: string): UserDependencies {
  const gateway = new FetchUserAdminGateway(apiBaseUrl);
  return {
    userAdminGateway: gateway,
    listUsersUseCase: new ListUsersService(gateway),
    getUserStatsUseCase: new GetUserStatsService(gateway),
    updateUserUseCase: new UpdateUserService(gateway),
    deleteUserUseCase: new DeleteUserService(gateway),
    inviteUserUseCase: new InviteUserService(gateway),
    resendInvitationUseCase: new ResendInvitationService(gateway),
  };
}

export const userDependencies = createUserDependencies(baseUrl);
