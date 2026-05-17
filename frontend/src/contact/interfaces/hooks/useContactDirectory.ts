'use client';

import { useCallback, useEffect, useState } from 'react';
import { ContactViewModel } from '../../application/command/ContactViewModel';
import { ContactViewModelMapper } from '../../application/mapper/ContactViewModelMapper';
import { contactDependencies } from '../../infrastructure/config/contactDependencies';

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
}

export function useContactDirectory(options: UseContactDirectoryOptions = {}): UseContactDirectoryResult {
  const { search, size = 20 } = options;
  const [page, setPage] = useState(options.page ?? 0);
  const [items, setItems] = useState<ContactViewModel[]>([]);
  const [totalElements, setTotalElements] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const refetch = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const result = await contactDependencies.contactGateway.listPaged({
        search: search || undefined,
        page,
        size,
      });
      setItems(result.content.map(ContactViewModelMapper.fromApi));
      setTotalElements(result.totalElements);
      setTotalPages(result.totalPages);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to load contacts');
    } finally {
      setLoading(false);
    }
  }, [search, page, size]);

  useEffect(() => {
    const timer = setTimeout(() => void refetch(), 300);
    return () => clearTimeout(timer);
  }, [refetch]);

  return { items, loading, error, page, totalPages, totalElements, setPage, refetch };
}
