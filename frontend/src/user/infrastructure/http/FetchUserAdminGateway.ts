import { apiFetch, parseApiError } from '@/shared/lib/apiFetch';
import {
  InviteUserPayload,
  UpdateUserPayload,
  UserAdminGateway,
  UserDto,
  UserListParams,
  UserPageDto,
  UserStatsDto,
} from '../../domain/port/UserAdminGateway';

export class FetchUserAdminGateway implements UserAdminGateway {
  constructor(private readonly baseUrl: string) {}

  async list(params: UserListParams): Promise<UserPageDto> {
    const query = new URLSearchParams();
    query.set('page', String(params.page ?? 0));
    query.set('size', String(params.size ?? 20));
    if (params.search) query.set('search', params.search);
    const response = await apiFetch(this.baseUrl, `/api/users?${query}`);
    if (!response.ok) throw new Error(await parseApiError(response));
    return (await response.json()) as UserPageDto;
  }

  async stats(): Promise<UserStatsDto> {
    const response = await apiFetch(this.baseUrl, '/api/users/stats');
    if (!response.ok) throw new Error(await parseApiError(response));
    return (await response.json()) as UserStatsDto;
  }

  async invite(payload: InviteUserPayload): Promise<UserDto> {
    const response = await apiFetch(this.baseUrl, '/api/users/invite', {
      method: 'POST',
      body: JSON.stringify(payload),
    });
    if (!response.ok) throw new Error(await parseApiError(response));
    return (await response.json()) as UserDto;
  }

  async resendInvitation(id: string): Promise<void> {
    const response = await apiFetch(this.baseUrl, `/api/users/${id}/resend-invitation`, {
      method: 'POST',
    });
    if (!response.ok && response.status !== 204) {
      throw new Error(await parseApiError(response));
    }
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
