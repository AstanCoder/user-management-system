import { BaseQueryService } from '@/shared/application/service/BaseQueryService';
import { UserAdminGateway, UserStatsDto } from '@/user/domain/port/UserAdminGateway';
import { GetUserStatsUseCase } from '@/user/application/port/in/GetUserStatsUseCase';

export class GetUserStatsService extends BaseQueryService<void, UserStatsDto> implements GetUserStatsUseCase {
  constructor(private readonly gateway: UserAdminGateway) {
    super();
  }

  protected handleQuery(): Promise<UserStatsDto> {
    return this.gateway.stats();
  }
}
