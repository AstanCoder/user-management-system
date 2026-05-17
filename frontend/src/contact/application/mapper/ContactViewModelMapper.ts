import { Contact } from '../../domain/model/Contact';
import { ContactViewModel } from '../command/ContactViewModel';

/**
 * Maps domain contacts to presentation view models.
 */
export class ContactViewModelMapper {
  /**
   * Maps a single domain contact to a view model.
   * @param contact - domain contact
   * @returns view model for UI
   */
  static toViewModel(contact: Contact): ContactViewModel {
    return {
      id: contact.getId().toString(),
      fullName: contact.getFullName(),
      firstName: contact.getFirstName(),
      lastName: contact.getLastName(),
      email: contact.getEmail().toString(),
      phone: contact.getPhone()?.toString() ?? null,
    };
  }

  /**
   * Maps a list of domain contacts to view models.
   * @param contacts - domain contacts
   * @returns view models
   */
  static toViewModels(contacts: Contact[]): ContactViewModel[] {
    return contacts.map(ContactViewModelMapper.toViewModel);
  }
}
