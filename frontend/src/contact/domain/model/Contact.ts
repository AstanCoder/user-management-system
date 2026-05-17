import { ContactId } from '../valueobject/ContactId';
import { Email } from '../valueobject/Email';
import { PhoneNumber } from '../valueobject/PhoneNumber';

/**
 * Contact aggregate root for the frontend domain layer.
 */
export class Contact {
  private constructor(
    private readonly id: ContactId,
    private readonly firstName: string,
    private readonly lastName: string,
    private readonly email: Email,
    private readonly phone: PhoneNumber | null,
  ) {}

  /**
   * Rehydrates a contact from domain parts.
   * @param id - contact id
   * @param firstName - first name
   * @param lastName - last name
   * @param email - email value object
   * @param phone - optional phone
   * @returns contact instance
   */
  static restore(
    id: ContactId,
    firstName: string,
    lastName: string,
    email: Email,
    phone: PhoneNumber | null,
  ): Contact {
    return new Contact(id, firstName, lastName, email, phone);
  }

  getId(): ContactId {
    return this.id;
  }

  getFirstName(): string {
    return this.firstName;
  }

  getLastName(): string {
    return this.lastName;
  }

  getEmail(): Email {
    return this.email;
  }

  getPhone(): PhoneNumber | null {
    return this.phone;
  }

  /**
   * Returns display full name.
   * @returns combined first and last name
   */
  getFullName(): string {
    return `${this.firstName} ${this.lastName}`;
  }
}
