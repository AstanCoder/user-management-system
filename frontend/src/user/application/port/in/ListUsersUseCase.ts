import { UserListParams, UserPageDto } from '@/user/domain/port/UserAdminGateway';
import { QueryUseCase } from '@/shared/application/port/in/QueryUseCase';

export type ListUsersUseCase = QueryUseCase<UserListParams, UserPageDto>;
