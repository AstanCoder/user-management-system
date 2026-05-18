'use client';

import { useQuery, useQueryClient } from '@tanstack/react-query';
import {
  StoredAuth,
  clearStoredAuth,
  getStoredAuth,
  setStoredAuth,
} from '@/shared/lib/authStorage';
import { identityDependencies } from '../../infrastructure/config/identityDependencies';
import { authQueryKeys } from '../query/authQueryKeys';

export function useAuth() {
  const queryClient = useQueryClient();

  const sessionQuery = useQuery<StoredAuth | null>({
    queryKey: authQueryKeys.session,
    queryFn: async () => {
      const stored = getStoredAuth();
      if (!stored?.token) {
        return null;
      }
      try {
        const current = await identityDependencies.authGateway.getCurrentUser();
        return {
          token: current.token,
          userId: current.userId,
          email: current.email,
          firstName: current.firstName,
          lastName: current.lastName,
          role: current.role,
        };
      } catch {
        clearStoredAuth();
        return null;
      }
    },
    initialData: getStoredAuth(),
    staleTime: 60_000,
  });

  const setUser = (user: StoredAuth | null) => {
    if (user) {
      setStoredAuth(user);
    } else {
      clearStoredAuth();
    }
    queryClient.setQueryData(authQueryKeys.session, user);
  };

  return {
    user: sessionQuery.data ?? null,
    loading: sessionQuery.isPending,
    refresh: async () => {
      const result = await sessionQuery.refetch();
      return result.data ?? null;
    },
    setUser,
  };
}
