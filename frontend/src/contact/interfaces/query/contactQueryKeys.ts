export const contactQueryKeys = {
  all: ['contacts'] as const,
  directory: (
    search: string,
    page: number,
    size: number,
    email: string,
    phone: string,
    tagNames: string[],
  ) => ['contacts', 'directory', { search, page, size, email, phone, tagNames }] as const,
  detail: (contactId: string) => ['contacts', 'detail', contactId] as const,
  activities: (contactId: string, activityType: string) =>
    ['contacts', 'activities', contactId, activityType] as const,
  allList: ['contacts', 'list', 'all'] as const,
};
