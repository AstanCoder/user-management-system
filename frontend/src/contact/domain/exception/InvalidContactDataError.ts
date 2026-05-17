/**
 * Thrown when contact value objects or invariants are violated on the client.
 */
export class InvalidContactDataError extends Error {
  constructor(message: string) {
    super(message);
    this.name = 'InvalidContactDataError';
  }
}
