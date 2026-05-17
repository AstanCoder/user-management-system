import { InvalidContactDataError } from '../exception/InvalidContactDataError';

const PHONE_PATTERN = /^[+0-9]{7,30}$/;

/**
 * Optional phone number normalized to digits and leading plus.
 */
export class PhoneNumber {
  private constructor(private readonly value: string) {}

  /**
   * Creates a phone number from raw input.
   * @param raw - user input; blank yields null
   * @returns phone or null when absent
   * @throws InvalidContactDataError when format is invalid
   */
  static createOptional(raw: string | null | undefined): PhoneNumber | null {
    if (!raw?.trim()) {
      return null;
    }
    const normalized = raw.trim().replace(/[\s-]/g, '');
    if (!PHONE_PATTERN.test(normalized)) {
      throw new InvalidContactDataError('Invalid phone number format');
    }
    return new PhoneNumber(normalized);
  }

  /**
   * Rehydrates from stored value.
   * @param stored - persisted phone
   * @returns phone instance
   */
  static fromStored(stored: string): PhoneNumber {
    return new PhoneNumber(stored);
  }

  /**
   * Returns the phone string value.
   * @returns phone digits
   */
  toString(): string {
    return this.value;
  }
}
