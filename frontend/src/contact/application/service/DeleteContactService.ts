import { ContactId } from '../../domain/valueobject/ContactId';
import { ContactGateway } from '../../domain/port/ContactGateway';
import { DeleteContactUseCase } from '../port/in/DeleteContactUseCase';

/**
 * Deletes a contact through the gateway.
 */
export class DeleteContactService implements DeleteContactUseCase {
  constructor(private readonly gateway: ContactGateway) {}

  /**
   * @inheritdoc
   */
  async execute(id: ContactId): Promise<void> {
    await this.gateway.delete(id);
  }
}
