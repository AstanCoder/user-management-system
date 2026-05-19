import { BaseMutationService } from '@/shared/application/service/BaseMutationService';
import { Email } from '../../domain/valueobject/Email';
import { PhoneNumber } from '../../domain/valueobject/PhoneNumber';
import { ContactGateway } from '../../domain/port/ContactGateway';
import { CreateContactCommand } from '../command/CreateContactCommand';
import { ContactViewModel } from '../command/ContactViewModel';
import { ContactViewModelMapper } from '../mapper/ContactViewModelMapper';
import { CreateContactUseCase } from '../port/in/CreateContactUseCase';

export class CreateContactService
  extends BaseMutationService<CreateContactCommand, ContactViewModel>
  implements CreateContactUseCase
{
  constructor(private readonly gateway: ContactGateway) {
    super();
  }

  protected async handleMutation(command: CreateContactCommand): Promise<ContactViewModel> {
    Email.create(command.email);
    PhoneNumber.createOptional(command.phone);
    const contact = await this.gateway.create(command);
    return ContactViewModelMapper.fromDomain(contact);
  }
}
