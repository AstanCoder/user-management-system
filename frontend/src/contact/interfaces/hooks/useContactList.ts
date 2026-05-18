'use client';

import { useQuery } from '@tanstack/react-query';
import { ContactViewModel } from '../../application/command/ContactViewModel';
import { ContactDependencies } from '../../infrastructure/config/contactDependencies';
import { contactQueryKeys } from '../query/contactQueryKeys';

export function useContactList(deps: ContactDependencies) {
  const listQuery = useQuery<ContactViewModel[]>({
    queryKey: contactQueryKeys.allList,
    queryFn: async () => deps.listContactsUseCase.execute(),
  });

  return {
    items: listQuery.data ?? [],
    loading: listQuery.isPending,
    error: listQuery.error instanceof Error ? listQuery.error.message : null,
    refetch: () => {
      void listQuery.refetch();
    },
  };
}
