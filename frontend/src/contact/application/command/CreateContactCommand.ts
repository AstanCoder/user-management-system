/**
 * Application command to create a contact.
 */
export interface CreateContactCommand {
  firstName: string;
  lastName: string;
  email: string;
  phone?: string | null;
}
