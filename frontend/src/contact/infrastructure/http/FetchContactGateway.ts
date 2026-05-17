import { Contact } from '../../domain/model/Contact';
import { ContactId } from '../../domain/valueobject/ContactId';
import { ContactGateway } from '../../domain/port/ContactGateway';
import { CreateContactCommand } from '../../application/command/CreateContactCommand';
import { UpdateContactCommand } from '../../application/command/UpdateContactCommand';
import { ContactApiMapper } from '../mapper/ContactApiMapper';
import { ContactApiDto } from './ContactApiDto';

/**
 * HTTP adapter implementing {@link ContactGateway} using fetch.
 */
export class FetchContactGateway implements ContactGateway {
  constructor(private readonly baseUrl: string) {}

  /**
   * @inheritdoc
   */
  async list(): Promise<Contact[]> {
    const response = await fetch(`${this.baseUrl}/api/contacts`);
    if (!response.ok) {
      throw new Error(`Failed to list contacts: ${response.status}`);
    }
    const dtos = (await response.json()) as ContactApiDto[];
    return ContactApiMapper.toDomainList(dtos);
  }

  /**
   * @inheritdoc
   */
  async create(command: CreateContactCommand): Promise<Contact> {
    const response = await fetch(`${this.baseUrl}/api/contacts`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        firstName: command.firstName,
        lastName: command.lastName,
        email: command.email,
        phone: command.phone ?? null,
      }),
    });
    if (!response.ok) {
      const message = await this.extractError(response);
      throw new Error(message);
    }
    const dto = (await response.json()) as ContactApiDto;
    return ContactApiMapper.toDomain(dto);
  }

  /**
   * @inheritdoc
   */
  async update(command: UpdateContactCommand): Promise<Contact> {
    const response = await fetch(`${this.baseUrl}/api/contacts/${command.id.toString()}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        firstName: command.firstName,
        lastName: command.lastName,
        email: command.email,
        phone: command.phone ?? null,
      }),
    });
    if (!response.ok) {
      const message = await this.extractError(response);
      throw new Error(message);
    }
    const dto = (await response.json()) as ContactApiDto;
    return ContactApiMapper.toDomain(dto);
  }

  /**
   * @inheritdoc
   */
  async delete(id: ContactId): Promise<void> {
    const response = await fetch(`${this.baseUrl}/api/contacts/${id.toString()}`, {
      method: 'DELETE',
    });
    if (!response.ok && response.status !== 204) {
      const message = await this.extractError(response);
      throw new Error(message);
    }
  }

  private async extractError(response: Response): Promise<string> {
    try {
      const body = (await response.json()) as { message?: string };
      return body.message ?? `Request failed with status ${response.status}`;
    } catch {
      return `Request failed with status ${response.status}`;
    }
  }
}
