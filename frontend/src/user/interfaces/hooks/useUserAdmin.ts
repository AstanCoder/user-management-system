'use client';

import { useCallback, useEffect, useState } from 'react';
import { userDependencies } from '../../infrastructure/config/userDependencies';
import { UserDto, UserStatsDto } from '../../domain/port/UserAdminGateway';

export interface UseUserAdminOptions {
  search?: string;
  page?: number;
  size?: number;
}

export function useUserAdmin(options: UseUserAdminOptions = {}) {
  const { search, size = 20 } = options;
  const [page, setPage] = useState(options.page ?? 0);
  const [users, setUsers] = useState<UserDto[]>([]);
  const [stats, setStats] = useState<UserStatsDto | null>(null);
  const [totalElements, setTotalElements] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const refetch = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const [pageResult, statsResult] = await Promise.all([
        userDependencies.userAdminGateway.list({ page, size, search: search || undefined }),
        userDependencies.userAdminGateway.stats(),
      ]);
      setUsers(pageResult.content);
      setTotalElements(pageResult.totalElements);
      setTotalPages(pageResult.totalPages);
      setStats(statsResult);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to load users');
    } finally {
      setLoading(false);
    }
  }, [page, size, search]);

  useEffect(() => {
    const timer = setTimeout(() => void refetch(), 300);
    return () => clearTimeout(timer);
  }, [refetch]);

  return {
    users,
    stats,
    loading,
    error,
    page,
    totalElements,
    totalPages,
    setPage,
    refetch,
  };
}
