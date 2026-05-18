import { Contact } from '../../domain/model/Contact';
import { ContactApiDto } from '../../infrastructure/http/ContactApiDto';
import { ContactViewModel } from '../command/ContactViewModel';

export class ContactViewModelMapper {
  static fromDomain(contact: Contact): ContactViewModel {
    return {
      id: contact.getId().toString(),
      firstName: contact.getFirstName(),
      lastName: contact.getLastName(),
      fullName: contact.getFullName(),
      email: contact.getEmail().toString(),
      phone: contact.getPhone()?.toString() ?? null,
      company: null,
      jobTitle: null,
      street: null,
      city: null,
      postalCode: null,
      country: null,
      avatarUrl: null,
      tags: [],
      notes: [],
      activities: [],
    };
  }

  static fromDomainList(contacts: Contact[]): ContactViewModel[] {
    return contacts.map(ContactViewModelMapper.fromDomain);
  }

  static fromApi(dto: ContactApiDto): ContactViewModel {
    return {
      id: dto.id,
      firstName: dto.firstName,
      lastName: dto.lastName,
      fullName: `${dto.firstName} ${dto.lastName}`.trim(),
      email: dto.email,
      phone: dto.phone,
      company: dto.company,
      jobTitle: dto.jobTitle,
      street: dto.street,
      city: dto.city,
      postalCode: dto.postalCode,
      country: dto.country,
      avatarUrl: dto.avatarUrl,
      tags: (dto.tags ?? []).map((t) => ({ id: t.id, name: t.name })),
      notes: (dto.notes ?? []).map((n) => ({
        id: n.id,
        body: n.content,
        authorUserId: n.authorUserId,
        createdAt: n.createdAt,
        updatedAt: n.updatedAt,
      })),
      activities: (dto.activities ?? []).map((a) => ({
        id: a.id,
        activityType: a.activityType,
        description: a.description,
        authorUserId: a.authorUserId,
        occurredAt: a.occurredAt,
        createdAt: a.createdAt,
        confirmed: a.confirmed,
      })),
    };
  }
}
