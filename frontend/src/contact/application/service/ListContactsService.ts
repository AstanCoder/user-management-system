import { BaseQueryService } from '@/shared/application/service/BaseQueryService';
import { ContactGateway } from '../../domain/port/ContactGateway';
import { ContactViewModel } from '../command/ContactViewModel';
import { ContactViewModelMapper } from '../mapper/ContactViewModelMapper';
import { ListContactsUseCase } from '../port/in/ListContactsUseCase';

export class ListContactsService extends BaseQueryService<void, ContactViewModel[]> implements ListContactsUseCase {
  constructor(private readonly gateway: ContactGateway) {
    super();
  }

  protected async handleQuery(): Promise<ContactViewModel[]> {
    const contacts = await this.gateway.list();
    return ContactViewModelMapper.fromDomainList(contacts);
  }
}
