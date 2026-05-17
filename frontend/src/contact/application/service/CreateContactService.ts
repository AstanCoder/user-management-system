import { Email } from '../../domain/valueobject/Email';
import { PhoneNumber } from '../../domain/valueobject/PhoneNumber';
import { ContactGateway } from '../../domain/port/ContactGateway';
import { CreateContactCommand } from '../command/CreateContactCommand';
import { ContactViewModel } from '../command/ContactViewModel';
import { ContactViewModelMapper } from '../mapper/ContactViewModelMapper';
import { CreateContactUseCase } from '../port/in/CreateContactUseCase';

/**
 * Creates a contact through the gateway after client-side validation.
 */
export class CreateContactService implements CreateContactUseCase {
  constructor(private readonly gateway: ContactGateway) {}

  /**
   * @inheritdoc
   */
  async execute(command: CreateContactCommand): Promise<ContactViewModel> {
    Email.create(command.email);
    PhoneNumber.createOptional(command.phone);
    const contact = await this.gateway.create(command);
    return ContactViewModelMapper.fromDomain(contact);
  }
}
