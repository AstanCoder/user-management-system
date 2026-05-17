import { Contact } from '../model/Contact';
import { ContactId } from '../valueobject/ContactId';
import { CreateContactCommand } from '../../application/command/CreateContactCommand';
import { UpdateContactCommand } from '../../application/command/UpdateContactCommand';

/**
 * Outbound port for contact HTTP persistence operations.
 */
export interface ContactGateway {
  /**
   * Lists all contacts from the API.
   * @returns domain contacts
   */
  list(): Promise<Contact[]>;

  /**
   * Creates a contact via the API.
   * @param command - create command
   * @returns created contact
   */
  create(command: CreateContactCommand): Promise<Contact>;

  /**
   * Updates a contact via the API.
   * @param command - update command
   * @returns updated contact
   */
  update(command: UpdateContactCommand): Promise<Contact>;

  /**
   * Deletes a contact by id.
   * @param id - contact id
   */
  delete(id: ContactId): Promise<void>;
}
