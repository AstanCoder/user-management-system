import { apiFetch, parseApiError } from '@/shared/lib/apiFetch';
import {
  UpdateUserPayload,
  UserAdminGateway,
  UserDto,
  UserStatsDto,
} from '../../domain/port/UserAdminGateway';

export class FetchUserAdminGateway implements UserAdminGateway {
  constructor(private readonly baseUrl: string) {}

  async list(): Promise<UserDto[]> {
    const response = await apiFetch(this.baseUrl, '/api/users');
    if (!response.ok) throw new Error(await parseApiError(response));
    return (await response.json()) as UserDto[];
  }

  async stats(): Promise<UserStatsDto> {
    const response = await apiFetch(this.baseUrl, '/api/users/stats');
    if (!response.ok) throw new Error(await parseApiError(response));
    return (await response.json()) as UserStatsDto;
  }

  async update(id: string, payload: UpdateUserPayload): Promise<UserDto> {
    const response = await apiFetch(this.baseUrl, `/api/users/${id}`, {
      method: 'PUT',
      body: JSON.stringify(payload),
    });
    if (!response.ok) throw new Error(await parseApiError(response));
    return (await response.json()) as UserDto;
  }

  async delete(id: string): Promise<void> {
    const response = await apiFetch(this.baseUrl, `/api/users/${id}`, { method: 'DELETE' });
    if (!response.ok && response.status !== 204) {
      throw new Error(await parseApiError(response));
    }
  }
}
