import { Email } from '../../domain/valueobject/Email';
import { PhoneNumber } from '../../domain/valueobject/PhoneNumber';
import { ContactGateway } from '../../domain/port/ContactGateway';
import { ContactViewModel } from '../command/ContactViewModel';
import { ContactViewModelMapper } from '../mapper/ContactViewModelMapper';
import { UpdateContactCommand } from '../command/UpdateContactCommand';
import { UpdateContactUseCase } from '../port/in/UpdateContactUseCase';

/**
 * Updates a contact through the gateway after client-side validation.
 */
export class UpdateContactService implements UpdateContactUseCase {
  constructor(private readonly gateway: ContactGateway) {}

  /**
   * @inheritdoc
   */
  async execute(command: UpdateContactCommand): Promise<ContactViewModel> {
    Email.create(command.email);
    PhoneNumber.createOptional(command.phone);
    const contact = await this.gateway.update(command);
    return ContactViewModelMapper.toViewModel(contact);
  }
}
