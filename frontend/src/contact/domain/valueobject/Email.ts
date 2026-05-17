import { InvalidContactDataError } from '../exception/InvalidContactDataError';

const EMAIL_PATTERN = /^[\w.+-]+@[\w.-]+\.[A-Za-z]{2,}$/;

/**
 * Email value object with normalization to lowercase trimmed form.
 */
export class Email {
  private constructor(private readonly value: string) {}

  /**
   * Creates an email from raw input after validation and normalization.
   * @param raw - user input
   * @returns normalized email
   * @throws InvalidContactDataError when blank or malformed
   */
  static create(raw: string): Email {
    if (!raw?.trim()) {
      throw new InvalidContactDataError('Email must not be blank');
    }
    const normalized = raw.trim().toLowerCase();
    if (!EMAIL_PATTERN.test(normalized)) {
      throw new InvalidContactDataError('Invalid email format');
    }
    return new Email(normalized);
  }

  /**
   * Rehydrates from stored value without re-validation.
   * @param stored - persisted email
   * @returns email instance
   */
  static fromStored(stored: string): Email {
    return new Email(stored);
  }

  /**
   * Returns the email string value.
   * @returns email address
   */
  toString(): string {
    return this.value;
  }
}
