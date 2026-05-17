import { ContactViewModel } from '../../command/ContactViewModel';
import { CreateContactCommand } from '../../command/CreateContactCommand';

/**
 * Inbound port for creating a contact.
 */
export interface CreateContactUseCase {
  /**
   * Creates a contact and returns the view model.
   * @param command - create command
   * @returns created contact view model
   */
  execute(command: CreateContactCommand): Promise<ContactViewModel>;
}
