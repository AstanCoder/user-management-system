export type UserRole = 'ADMIN' | 'EDITOR' | 'VIEWER';
export type UserStatus = 'ACTIVE' | 'INVITED' | 'DISABLED';

export interface UserDto {
  id: string;
  email: string;
  firstName: string;
  lastName: string;
  role: UserRole;
  status: UserStatus;
  lastActiveAt: string | null;
}

export interface UserPageDto {
  content: UserDto[];
  totalElements: number;
  totalPages: number;
  page: number;
  size: number;
}

export interface UserStatsDto {
  totalUsers: number;
  activeUsers: number;
  invitedUsers: number;
  disabledUsers: number;
  adminCount: number;
  editorCount: number;
  viewerCount: number;
  invitedPendingCount: number;
  usersCreatedLast7Days: number;
}

export interface UpdateUserPayload {
  firstName: string;
  lastName: string;
  role: UserRole;
  status: UserStatus;
}

export interface InviteUserPayload {
  email: string;
  firstName: string;
  lastName: string;
  role: UserRole;
}

export interface UserListParams {
  page?: number;
  size?: number;
  search?: string;
}

export interface UserAdminGateway {
  list(params: UserListParams): Promise<UserPageDto>;
  stats(): Promise<UserStatsDto>;
  invite(payload: InviteUserPayload): Promise<UserDto>;
  resendInvitation(id: string): Promise<void>;
  update(id: string, payload: UpdateUserPayload): Promise<UserDto>;
  delete(id: string): Promise<void>;
}
