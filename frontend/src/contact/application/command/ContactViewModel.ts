/**
 * Presentation-ready contact data for the UI layer.
 */
export interface ContactViewModel {
  id: string;
  fullName: string;
  firstName: string;
  lastName: string;
  email: string;
  phone: string | null;
}
