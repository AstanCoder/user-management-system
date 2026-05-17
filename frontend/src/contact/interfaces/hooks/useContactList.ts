'use client';

import { useCallback, useEffect, useState } from 'react';
import { ContactViewModel } from '../../application/command/ContactViewModel';
import { ContactDependencies } from '../../infrastructure/config/contactDependencies';

/**
 * Loads and exposes the contact list via the list use case.
 * @param deps - wired contact dependencies
 * @returns list state and refetch handler
 */
export function useContactList(deps: ContactDependencies) {
  const [items, setItems] = useState<ContactViewModel[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const refetch = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const result = await deps.listContactsUseCase.execute();
      setItems(result);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to load contacts');
    } finally {
      setLoading(false);
    }
  }, [deps]);

  useEffect(() => {
    void refetch();
  }, [refetch]);

  return { items, loading, error, refetch };
}
