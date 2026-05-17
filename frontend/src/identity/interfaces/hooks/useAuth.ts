'use client';

import { useCallback, useEffect, useState } from 'react';
import { getStoredAuth, StoredAuth, clearStoredAuth } from '@/shared/lib/authStorage';
import { identityDependencies } from '../../infrastructure/config/identityDependencies';

export function useAuth() {
  const [user, setUser] = useState<StoredAuth | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    setUser(getStoredAuth());
    setLoading(false);
  }, []);

  const refresh = useCallback(async () => {
    const stored = getStoredAuth();
    if (!stored?.token) {
      setUser(null);
      return null;
    }
    try {
      await identityDependencies.authGateway.getCurrentUser();
      setUser(getStoredAuth());
      return getStoredAuth();
    } catch {
      clearStoredAuth();
      setUser(null);
      return null;
    }
  }, []);

  return { user, loading, refresh, setUser };
}
