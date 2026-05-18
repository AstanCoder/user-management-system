export const contactQueryKeys = {
  all: ['contacts'] as const,
  directory: (search: string, page: number, size: number) =>
    ['contacts', 'directory', { search, page, size }] as const,
  detail: (contactId: string) => ['contacts', 'detail', contactId] as const,
  allList: ['contacts', 'list', 'all'] as const,
};
