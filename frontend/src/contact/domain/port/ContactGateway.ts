import { CreateContactPayload } from '../command/CreateContactPayload';
import { UpdateContactPayload } from '../command/UpdateContactPayload';
import { Contact } from '../model/Contact';
import { ContactId } from '../valueobject/ContactId';

export interface LogActivityPayload {
  activityType: string;
  description?: string | null;
  occurredAt: string;
  confirmed?: boolean;
}

/**
 * Outbound port for contact HTTP persistence operations.
 */
export interface ContactGateway {
  list(): Promise<Contact[]>;
  create(command: CreateContactPayload): Promise<Contact>;
  update(command: UpdateContactPayload): Promise<Contact>;
  delete(id: ContactId): Promise<void>;
  addNote(contactId: string, body: string): Promise<void>;
  deleteNote(contactId: string, noteId: string): Promise<void>;
  addActivity(contactId: string, payload: LogActivityPayload): Promise<void>;
  confirmActivity(contactId: string, activityId: string): Promise<void>;
  deleteActivity(contactId: string, activityId: string): Promise<void>;
  assignTags(contactId: string, tagNames: string[]): Promise<void>;
  uploadAvatar(contactId: string, file: File): Promise<string>;
}
