/**
 * Anti-corruption DTO matching backend ContactResponse JSON shape.
 */
export interface ContactApiDto {
  id: string;
  firstName: string;
  lastName: string;
  email: string;
  phone: string | null;
  createdAt: string;
  updatedAt: string;
}

/**
 * Request body for creating a contact.
 */
export interface CreateContactApiDto {
  firstName: string;
  lastName: string;
  email: string;
  phone?: string | null;
}

/**
 * Request body for updating a contact.
 */
export interface UpdateContactApiDto {
  firstName: string;
  lastName: string;
  email: string;
  phone?: string | null;
}
