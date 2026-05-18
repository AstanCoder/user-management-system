'use client';

import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { useEffect, useMemo, useState } from 'react';
import { userDependencies } from '../../infrastructure/config/userDependencies';
import { InviteUserPayload, UpdateUserPayload } from '../../domain/port/UserAdminGateway';
import { userQueryKeys } from '../query/userQueryKeys';

export interface UseUserAdminOptions {
  search?: string;
  page?: number;
  size?: number;
}

export function useUserAdmin(options: UseUserAdminOptions = {}) {
  const { search, size = 20 } = options;
  const [page, setPage] = useState(options.page ?? 0);
  const queryClient = useQueryClient();

  const normalizedSearch = useMemo(() => (search ?? '').trim(), [search]);
  const [debouncedSearch, setDebouncedSearch] = useState(normalizedSearch);

  useEffect(() => {
    const timer = setTimeout(() => setDebouncedSearch(normalizedSearch), 300);
    return () => clearTimeout(timer);
  }, [normalizedSearch]);

  const usersQuery = useQuery({
    queryKey: userQueryKeys.list(debouncedSearch, page, size),
    queryFn: async () =>
      userDependencies.userAdminGateway.list({
        page,
        size,
        search: debouncedSearch || undefined,
      }),
  });

  const statsQuery = useQuery({
    queryKey: userQueryKeys.stats,
    queryFn: async () => userDependencies.userAdminGateway.stats(),
  });

  const invalidateUsers = async () => {
    await queryClient.invalidateQueries({ queryKey: userQueryKeys.all });
  };

  const updateUserMutation = useMutation({
    mutationFn: async ({ id, payload }: { id: string; payload: UpdateUserPayload }) =>
      userDependencies.userAdminGateway.update(id, payload),
    onSuccess: invalidateUsers,
  });

  const deleteUserMutation = useMutation({
    mutationFn: async (id: string) => userDependencies.userAdminGateway.delete(id),
    onSuccess: invalidateUsers,
  });

  const inviteUserMutation = useMutation({
    mutationFn: async (payload: InviteUserPayload) => userDependencies.userAdminGateway.invite(payload),
    onSuccess: invalidateUsers,
  });

  const resendInvitationMutation = useMutation({
    mutationFn: async (id: string) => userDependencies.userAdminGateway.resendInvitation(id),
    onSuccess: invalidateUsers,
  });

  return {
    users: usersQuery.data?.content ?? [],
    stats: statsQuery.data ?? null,
    loading: usersQuery.isPending || statsQuery.isPending,
    error:
      (usersQuery.error instanceof Error ? usersQuery.error.message : null) ??
      (statsQuery.error instanceof Error ? statsQuery.error.message : null),
    page,
    totalElements: usersQuery.data?.totalElements ?? 0,
    totalPages: usersQuery.data?.totalPages ?? 0,
    setPage,
    refetch: () => {
      void usersQuery.refetch();
      void statsQuery.refetch();
    },
    updateUser: async (id: string, payload: UpdateUserPayload) =>
      updateUserMutation.mutateAsync({ id, payload }),
    deleteUser: async (id: string) => deleteUserMutation.mutateAsync(id),
    inviteUser: async (payload: InviteUserPayload) => inviteUserMutation.mutateAsync(payload),
    resendInvitation: async (id: string) => resendInvitationMutation.mutateAsync(id),
  };
}
