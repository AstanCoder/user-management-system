import { UpdateUserPayload } from '@/user/domain/port/UserAdminGateway';

export interface UpdateUserCommand {
  id: string;
  payload: UpdateUserPayload;
}
