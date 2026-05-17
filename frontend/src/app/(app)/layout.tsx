'use client';

import { useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { AppShell } from '@/shared/ui/AppShell';
import { useAuth } from '@/identity/interfaces/hooks/useAuth';

export default function AppLayout({ children }: { children: React.ReactNode }) {
  const { user, loading, refresh } = useAuth();
  const router = useRouter();

  useEffect(() => {
    if (!loading && !user) {
      void refresh().then((u) => {
        if (!u) router.replace('/login');
      });
    }
  }, [loading, user, refresh, router]);

  if (loading || !user) {
    return <div className="flex min-h-screen items-center justify-center">Loading…</div>;
  }

  return <AppShell user={user}>{children}</AppShell>;
}
