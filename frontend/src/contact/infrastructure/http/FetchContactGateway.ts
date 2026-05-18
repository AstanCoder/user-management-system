import { apiFetch, parseApiError } from '@/shared/lib/apiFetch';
import { CreateContactCommand } from '../../application/command/CreateContactCommand';
import { UpdateContactCommand } from '../../application/command/UpdateContactCommand';
import { Contact } from '../../domain/model/Contact';
import { ContactGateway, LogActivityPayload } from '../../domain/port/ContactGateway';
import { ContactId } from '../../domain/valueobject/ContactId';
import { ContactApiMapper } from '../mapper/ContactApiMapper';
import {
  AssignTagsApiDto,
  ContactApiDto,
  ContactPageApiDto,
  CreateContactApiDto,
  LogActivityApiDto,
  UpdateContactApiDto,
} from './ContactApiDto';

export interface ContactListParams {
  search?: string;
  page?: number;
  size?: number;
}

export class FetchContactGateway implements ContactGateway {
  constructor(private readonly baseUrl: string) {}

  async list(): Promise<Contact[]> {
    const page = await this.listPaged({ page: 0, size: 500 });
    return ContactApiMapper.toDomainList(page.content);
  }

  async listPaged(params: ContactListParams): Promise<ContactPageApiDto> {
    const query = new URLSearchParams();
    if (params.search) query.set('search', params.search);
    query.set('page', String(params.page ?? 0));
    query.set('size', String(params.size ?? 20));
    const response = await apiFetch(this.baseUrl, `/api/contacts?${query}`);
    if (!response.ok) throw new Error(await parseApiError(response));
    return (await response.json()) as ContactPageApiDto;
  }

  async getById(id: string): Promise<ContactApiDto> {
    const response = await apiFetch(this.baseUrl, `/api/contacts/${id}`);
    if (!response.ok) throw new Error(await parseApiError(response));
    return (await response.json()) as ContactApiDto;
  }

  async create(command: CreateContactCommand): Promise<Contact> {
    const body: CreateContactApiDto = toBody(command);
    const response = await apiFetch(this.baseUrl, '/api/contacts', {
      method: 'POST',
      body: JSON.stringify(body),
    });
    if (!response.ok) throw new Error(await parseApiError(response));
    return ContactApiMapper.toDomain((await response.json()) as ContactApiDto);
  }

  async update(command: UpdateContactCommand): Promise<Contact> {
    const body: UpdateContactApiDto = toBody(command);
    const response = await apiFetch(this.baseUrl, `/api/contacts/${command.id.toString()}`, {
      method: 'PUT',
      body: JSON.stringify(body),
    });
    if (!response.ok) throw new Error(await parseApiError(response));
    return ContactApiMapper.toDomain((await response.json()) as ContactApiDto);
  }

  async delete(id: ContactId): Promise<void> {
    const response = await apiFetch(this.baseUrl, `/api/contacts/${id.toString()}`, {
      method: 'DELETE',
    });
    if (!response.ok && response.status !== 204) {
      throw new Error(await parseApiError(response));
    }
  }

  async addNote(contactId: string, body: string): Promise<void> {
    const response = await apiFetch(this.baseUrl, `/api/contacts/${contactId}/notes`, {
      method: 'POST',
      body: JSON.stringify({ content: body }),
    });
    if (!response.ok) throw new Error(await parseApiError(response));
  }

  async deleteNote(contactId: string, noteId: string): Promise<void> {
    const response = await apiFetch(this.baseUrl, `/api/contacts/${contactId}/notes/${noteId}`, {
      method: 'DELETE',
    });
    if (!response.ok && response.status !== 204) throw new Error(await parseApiError(response));
  }

  async addActivity(contactId: string, payload: LogActivityPayload): Promise<void> {
    const body: LogActivityApiDto = {
      activityType: payload.activityType,
      description: payload.description ?? null,
      occurredAt: payload.occurredAt,
      confirmed: payload.confirmed,
    };
    const response = await apiFetch(this.baseUrl, `/api/contacts/${contactId}/activities`, {
      method: 'POST',
      body: JSON.stringify(body),
    });
    if (!response.ok) throw new Error(await parseApiError(response));
  }

  async confirmActivity(contactId: string, activityId: string): Promise<void> {
    const response = await apiFetch(
      this.baseUrl,
      `/api/contacts/${contactId}/activities/${activityId}/confirm`,
      {
        method: 'PATCH',
      },
    );
    if (!response.ok) throw new Error(await parseApiError(response));
  }

  async deleteActivity(contactId: string, activityId: string): Promise<void> {
    const response = await apiFetch(this.baseUrl, `/api/contacts/${contactId}/activities/${activityId}`, {
      method: 'DELETE',
    });
    if (!response.ok && response.status !== 204) throw new Error(await parseApiError(response));
  }

  async assignTags(contactId: string, tagNames: string[]): Promise<void> {
    const body: AssignTagsApiDto = { tagNames };
    const response = await apiFetch(this.baseUrl, `/api/contacts/${contactId}/tags`, {
      method: 'PUT',
      body: JSON.stringify(body),
    });
    if (!response.ok) throw new Error(await parseApiError(response));
  }

  async uploadAvatar(contactId: string, file: File): Promise<string> {
    const formData = new FormData();
    formData.append('file', file);
    const response = await apiFetch(this.baseUrl, `/api/contacts/${contactId}/avatar`, {
      method: 'POST',
      body: formData,
    });
    if (!response.ok) throw new Error(await parseApiError(response));
    const data = (await response.json()) as { avatarUrl: string };
    return data.avatarUrl;
  }
}

function toBody(command: CreateContactCommand | UpdateContactCommand): CreateContactApiDto {
  return {
    firstName: command.firstName,
    lastName: command.lastName,
    email: command.email,
    phone: command.phone || null,
    company: command.company ?? null,
    jobTitle: command.jobTitle ?? null,
    street: command.street ?? null,
    city: command.city ?? null,
    postalCode: command.postalCode ?? null,
    country: command.country ?? null,
  };
}
