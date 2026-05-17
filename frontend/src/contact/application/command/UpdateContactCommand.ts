import { ContactId } from '../../domain/valueobject/ContactId';

/**
 * Application command to update a contact.
 */
export interface UpdateContactCommand {
  id: ContactId;
  firstName: string;
  lastName: string;
  email: string;
  phone?: string | null;
}
