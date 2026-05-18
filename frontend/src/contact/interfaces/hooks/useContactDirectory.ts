'use client';

import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { useEffect, useMemo, useState } from 'react';
import { ContactViewModel } from '../../application/command/ContactViewModel';
import { ContactViewModelMapper } from '../../application/mapper/ContactViewModelMapper';
import { ContactId } from '../../domain/valueobject/ContactId';
import { contactDependencies } from '../../infrastructure/config/contactDependencies';
import { contactQueryKeys } from '../query/contactQueryKeys';

export interface UseContactDirectoryOptions {
  search?: string;
  page?: number;
  size?: number;
}

export interface UseContactDirectoryResult {
  items: ContactViewModel[];
  loading: boolean;
  error: string | null;
  page: number;
  totalPages: number;
  totalElements: number;
  setPage: (page: number) => void;
  refetch: () => void;
  deleteContact: (contactId: string) => Promise<void>;
}

export function useContactDirectory(options: UseContactDirectoryOptions = {}): UseContactDirectoryResult {
  const { search, size = 20 } = options;
  const [page, setPage] = useState(options.page ?? 0);
  const queryClient = useQueryClient();

  const normalizedSearch = useMemo(() => (search ?? '').trim(), [search]);
  const [debouncedSearch, setDebouncedSearch] = useState(normalizedSearch);

  useEffect(() => {
    const timer = setTimeout(() => setDebouncedSearch(normalizedSearch), 300);
    return () => clearTimeout(timer);
  }, [normalizedSearch]);

  const directoryQuery = useQuery({
    queryKey: contactQueryKeys.directory(debouncedSearch, page, size),
    queryFn: async () =>
      contactDependencies.contactGateway.listPaged({
        search: debouncedSearch || undefined,
        page,
        size,
      }),
  });

  const deleteContactMutation = useMutation({
    mutationFn: async (contactId: string) => {
      await contactDependencies.contactGateway.delete(ContactId.of(contactId));
    },
    onSuccess: async () => {
      await queryClient.invalidateQueries({ queryKey: contactQueryKeys.all });
    },
  });

  return {
    items: (directoryQuery.data?.content ?? []).map(ContactViewModelMapper.fromApi),
    loading: directoryQuery.isPending,
    error: directoryQuery.error instanceof Error ? directoryQuery.error.message : null,
    page,
    totalPages: directoryQuery.data?.totalPages ?? 0,
    totalElements: directoryQuery.data?.totalElements ?? 0,
    setPage,
    refetch: () => {
      void directoryQuery.refetch();
    },
    deleteContact: async (contactId: string) => {
      await deleteContactMutation.mutateAsync(contactId);
    },
  };
}
