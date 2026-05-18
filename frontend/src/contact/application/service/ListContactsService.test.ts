import { describe, expect, it, vi } from 'vitest';
import { Contact } from '../../domain/model/Contact';
import { ContactId } from '../../domain/valueobject/ContactId';
import { Email } from '../../domain/valueobject/Email';
import { ContactGateway } from '../../domain/port/ContactGateway';
import { ListContactsService } from './ListContactsService';

describe('ListContactsService', () => {
  it('returns view models from gateway', async () => {
    const contact = Contact.restore(
      ContactId.of('550e8400-e29b-41d4-a716-446655440000'),
      'Jane',
      'Doe',
      Email.fromStored('jane@example.com'),
      null,
    );
    const gateway: ContactGateway = {
      list: vi.fn().mockResolvedValue([contact]),
      create: vi.fn(),
      update: vi.fn(),
      delete: vi.fn(),
      addNote: vi.fn(),
      deleteNote: vi.fn(),
      addActivity: vi.fn(),
      confirmActivity: vi.fn(),
      deleteActivity: vi.fn(),
      assignTags: vi.fn(),
      uploadAvatar: vi.fn(),
    };
    const service = new ListContactsService(gateway);
    const result = await service.execute();
    expect(result).toHaveLength(1);
    expect(result[0].fullName).toBe('Jane Doe');
  });
});
