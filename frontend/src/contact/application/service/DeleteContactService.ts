import { BaseMutationService } from '@/shared/application/service/BaseMutationService';
import { ContactId } from '../../domain/valueobject/ContactId';
import { ContactGateway } from '../../domain/port/ContactGateway';
import { DeleteContactUseCase } from '../port/in/DeleteContactUseCase';

export class DeleteContactService extends BaseMutationService<ContactId, void> implements DeleteContactUseCase {
  constructor(private readonly gateway: ContactGateway) {
    super();
  }

  protected async handleMutation(id: ContactId): Promise<void> {
    await this.gateway.delete(id);
  }
}
