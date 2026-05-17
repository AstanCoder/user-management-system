import { ContactId } from '../../../domain/valueobject/ContactId';

/**
 * Inbound port for deleting a contact.
 */
export interface DeleteContactUseCase {
  /**
   * Deletes a contact by id.
   * @param id - contact id
   */
  execute(id: ContactId): Promise<void>;
}
