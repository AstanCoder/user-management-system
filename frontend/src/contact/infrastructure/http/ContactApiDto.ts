export interface TagApiDto {
  id: string;
  name: string;
}

export interface NoteApiDto {
  id: string;
  content: string;
  authorUserId: string;
  createdAt: string;
  updatedAt: string;
}

export interface ActivityApiDto {
  id: string;
  activityType: string;
  description: string | null;
  authorUserId: string | null;
  occurredAt: string;
  createdAt: string;
  confirmed: boolean;
}

export interface LogActivityApiDto {
  activityType: string;
  description?: string | null;
  occurredAt: string;
  confirmed?: boolean;
}

export interface AssignTagsApiDto {
  tagNames: string[];
}

export interface ContactApiDto {
  id: string;
  firstName: string;
  lastName: string;
  email: string;
  phone: string | null;
  company: string | null;
  jobTitle: string | null;
  street: string | null;
  city: string | null;
  postalCode: string | null;
  country: string | null;
  avatarUrl: string | null;
  status: string | null;
  createdAt: string;
  updatedAt: string;
  tags?: TagApiDto[];
  notes?: NoteApiDto[];
  activities?: ActivityApiDto[];
}

export interface ContactPageApiDto {
  content: ContactApiDto[];
  totalElements: number;
  totalPages: number;
  page: number;
  size: number;
}

export interface CreateContactApiDto {
  firstName: string;
  lastName: string;
  email: string;
  phone?: string | null;
  company?: string | null;
  jobTitle?: string | null;
  street?: string | null;
  city?: string | null;
  postalCode?: string | null;
  country?: string | null;
}

export type UpdateContactApiDto = CreateContactApiDto;
