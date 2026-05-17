import { describe, expect, it } from 'vitest';
import { InvalidContactDataError } from '../exception/InvalidContactDataError';
import { Email } from './Email';

describe('Email', () => {
  it('normalizes email', () => {
    expect(Email.create('  User@Example.COM  ').toString()).toBe('user@example.com');
  });

  it('rejects invalid format', () => {
    expect(() => Email.create('bad')).toThrow(InvalidContactDataError);
  });
});
