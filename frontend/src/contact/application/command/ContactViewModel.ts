export interface TagViewModel {
  id: string;
  name: string;
}

export interface NoteViewModel {
  id: string;
  body: string;
  authorId: string;
  createdAt: string;
}

export interface ActivityViewModel {
  id: string;
  activityType: string;
  description: string | null;
  authorUserId: string | null;
  occurredAt: string;
  createdAt: string;
}

export interface ContactViewModel {
  id: string;
  fullName: string;
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
  tags: TagViewModel[];
  notes: NoteViewModel[];
  activities: ActivityViewModel[];
}

export interface ContactPageViewModel {
  items: ContactViewModel[];
  totalElements: number;
  totalPages: number;
  page: number;
  size: number;
}
