export type UserRole = 'ADMIN' | 'EDITOR' | 'VIEWER';
export type UserStatus = 'ACTIVE' | 'PENDING' | 'INACTIVE';

export interface UserDto {
  id: string;
  email: string;
  firstName: string;
  lastName: string;
  role: UserRole;
  status: UserStatus;
}

export interface UserStatsDto {
  totalUsers: number;
  activeUsers: number;
  invitedUsers: number;
  disabledUsers: number;
}

export interface UpdateUserPayload {
  firstName: string;
  lastName: string;
  role: UserRole;
  status: UserStatus;
}

export interface UserAdminGateway {
  list(): Promise<UserDto[]>;
  stats(): Promise<UserStatsDto>;
  update(id: string, payload: UpdateUserPayload): Promise<UserDto>;
  delete(id: string): Promise<void>;
}
