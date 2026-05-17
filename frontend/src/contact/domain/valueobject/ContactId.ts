/**
 * Typed identity for a contact aggregate.
 */
export class ContactId {
  private constructor(private readonly value: string) {}

  /**
   * Creates a contact id from a UUID string.
   * @param raw - UUID string
   * @returns contact id instance
   */
  static of(raw: string): ContactId {
    return new ContactId(raw);
  }

  /**
   * Returns the raw UUID string.
   * @returns uuid string
   */
  toString(): string {
    return this.value;
  }
}
