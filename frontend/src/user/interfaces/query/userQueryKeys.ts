export const userQueryKeys = {
  all: ['admin', 'users'] as const,
  list: (search: string, page: number, size: number) =>
    ['admin', 'users', 'list', { search, page, size }] as const,
  stats: ['admin', 'users', 'stats'] as const,
};
