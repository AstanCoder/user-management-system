import { ContactGateway } from '../../domain/port/ContactGateway';
import { ContactViewModel } from '../command/ContactViewModel';
import { ContactViewModelMapper } from '../mapper/ContactViewModelMapper';
import { ListContactsUseCase } from '../port/in/ListContactsUseCase';

/**
 * Lists contacts via the outbound gateway port.
 */
export class ListContactsService implements ListContactsUseCase {
  constructor(private readonly gateway: ContactGateway) {}

  /**
   * @inheritdoc
   */
  async execute(): Promise<ContactViewModel[]> {
    const contacts = await this.gateway.list();
    return ContactViewModelMapper.toViewModels(contacts);
  }
}
