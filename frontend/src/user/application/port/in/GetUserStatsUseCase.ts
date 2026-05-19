import { QueryUseCase } from '@/shared/application/port/in/QueryUseCase';
import { UserStatsDto } from '@/user/domain/port/UserAdminGateway';

export type GetUserStatsUseCase = QueryUseCase<void, UserStatsDto>;
