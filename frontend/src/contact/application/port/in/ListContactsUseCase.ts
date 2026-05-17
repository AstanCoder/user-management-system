import { ContactViewModel } from '../../command/ContactViewModel';

/**
 * Inbound port for listing contacts.
 */
export interface ListContactsUseCase {
  /**
   * Returns all contacts as view models.
   * @returns contact view models
   */
  execute(): Promise<ContactViewModel[]>;
}
