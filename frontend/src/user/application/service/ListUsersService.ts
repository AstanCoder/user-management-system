import { BaseQueryService } from '@/shared/application/service/BaseQueryService';
import { UserAdminGateway, UserListParams, UserPageDto } from '@/user/domain/port/UserAdminGateway';
import { ListUsersUseCase } from '@/user/application/port/in/ListUsersUseCase';

export class ListUsersService
  extends BaseQueryService<UserListParams, UserPageDto>
  implements ListUsersUseCase
{
  constructor(private readonly gateway: UserAdminGateway) {
    super();
  }

  protected handleQuery(query: UserListParams): Promise<UserPageDto> {
    return this.gateway.list(query);
  }
}
