import { ContactId } from '../valueobject/ContactId';

export interface UpdateContactPayload {
  id: ContactId;
  firstName: string;
  lastName: string;
  email: string;
  phone?: string | null;
  company?: string | null;
  jobTitle?: string | null;
  street?: string | null;
  city?: string | null;
  postalCode?: string | null;
  country?: string | null;
}
