import { Contact } from '../../domain/model/Contact';
import { ContactId } from '../../domain/valueobject/ContactId';
import { Email } from '../../domain/valueobject/Email';
import { PhoneNumber } from '../../domain/valueobject/PhoneNumber';
import { ContactApiDto } from '../http/ContactApiDto';

/**
 * Maps API JSON DTOs to domain contacts (anti-corruption layer).
 */
export class ContactApiMapper {
  /**
   * Converts an API DTO to a domain contact.
   * @param dto - API response body
   * @returns domain contact
   */
  static toDomain(dto: ContactApiDto): Contact {
    return Contact.restore(
      ContactId.of(dto.id),
      dto.firstName,
      dto.lastName,
      Email.fromStored(dto.email),
      dto.phone ? PhoneNumber.fromStored(dto.phone) : null,
    );
  }

  /**
   * Converts a list of API DTOs to domain contacts.
   * @param dtos - API response list
   * @returns domain contacts
   */
  static toDomainList(dtos: ContactApiDto[]): Contact[] {
    return dtos.map(ContactApiMapper.toDomain);
  }
}
