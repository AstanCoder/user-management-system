import { ContactViewModel } from '../../command/ContactViewModel';
import { UpdateContactCommand } from '../../command/UpdateContactCommand';

/**
 * Inbound port for updating a contact.
 */
export interface UpdateContactUseCase {
  /**
   * Updates a contact and returns the view model.
   * @param command - update command
   * @returns updated contact view model
   */
  execute(command: UpdateContactCommand): Promise<ContactViewModel>;
}
