import { BaseMutationService } from '@/shared/application/service/BaseMutationService';
import { Email } from '../../domain/valueobject/Email';
import { PhoneNumber } from '../../domain/valueobject/PhoneNumber';
import { ContactGateway } from '../../domain/port/ContactGateway';
import { ContactViewModel } from '../command/ContactViewModel';
import { ContactViewModelMapper } from '../mapper/ContactViewModelMapper';
import { UpdateContactCommand } from '../command/UpdateContactCommand';
import { UpdateContactUseCase } from '../port/in/UpdateContactUseCase';

export class UpdateContactService
  extends BaseMutationService<UpdateContactCommand, ContactViewModel>
  implements UpdateContactUseCase
{
  constructor(private readonly gateway: ContactGateway) {
    super();
  }

  protected async handleMutation(command: UpdateContactCommand): Promise<ContactViewModel> {
    Email.create(command.email);
    PhoneNumber.createOptional(command.phone);
    const contact = await this.gateway.update(command);
    return ContactViewModelMapper.fromDomain(contact);
  }
}
