'use client';

import Link from 'next/link';
import { usePathname, useRouter } from 'next/navigation';
import { ReactNode } from 'react';
import { clearStoredAuth, StoredAuth } from '@/shared/lib/authStorage';
import { Avatar } from './Avatar';
import { PageFooter } from './PageFooter';

export interface AppShellProps {
  user: StoredAuth;
  children: ReactNode;
}

const navItems = [
  { href: '/contacts', label: 'Contacts', icon: 'contact_page', adminOnly: false },
  { href: '/admin/users', label: 'Admin Panel', icon: 'security', adminOnly: true },
];

const bottomNavItems = [
  { href: '/contacts', label: 'Directory' },
  { href: '/admin/users', label: 'Admin', adminOnly: true },
];

function formatRole(role: StoredAuth['role']): string {
  switch (role) {
    case 'ADMIN':
      return 'Global Admin';
    case 'EDITOR':
      return 'Editor';
    case 'VIEWER':
      return 'Viewer';
    default:
      return role;
  }
}

export function AppShell({ user, children }: AppShellProps) {
  const pathname = usePathname();
  const router = useRouter();
  const fullName = `${user.firstName} ${user.lastName}`.trim();

  const handleLogout = async () => {
    const baseUrl = process.env.NEXT_PUBLIC_API_URL ?? 'http://localhost:8080';
    try {
      await fetch(`${baseUrl}/api/auth/logout`, { method: 'POST' });
    } catch {
      /* ignore */
    }
    clearStoredAuth();
    router.replace('/login');
  };

  const visibleNav = navItems.filter((item) => !item.adminOnly || user.role === 'ADMIN');
  const visibleBottom = bottomNavItems.filter((item) => !item.adminOnly || user.role === 'ADMIN');

  return (
    <div className="flex min-h-screen bg-background text-on-surface">
      <aside className="hidden w-64 shrink-0 flex-col border-r border-outline-variant bg-surface lg:flex">
        <div className="border-b border-outline-variant p-6">
          <div className="flex items-center gap-3">
            <Avatar name={fullName} size="md" />
            <div>
              <p className="text-sm font-semibold text-primary">{fullName}</p>
              <p className="text-xs text-on-surface-variant">{formatRole(user.role)}</p>
            </div>
          </div>
        </div>
        <nav className="flex-1 space-y-1 p-4">
          {visibleNav.map((item) => {
            const active = pathname.startsWith(item.href);
            return (
              <Link
                key={item.href}
                href={item.href}
                className={`flex items-center gap-3 rounded-r-full px-4 py-3 text-sm font-medium transition-colors ${
                  active
                    ? 'bg-secondary-container text-on-secondary-container'
                    : 'text-on-surface-variant hover:bg-surface-container-low'
                }`}
              >
                <span className="material-symbols-outlined text-xl">{item.icon}</span>
                {item.label}
              </Link>
            );
          })}
        </nav>
        <div className="border-t border-outline-variant p-4">
          <button
            type="button"
            onClick={() => void handleLogout()}
            className="w-full rounded px-3 py-2 text-left text-sm text-on-surface-variant hover:bg-surface-container-low"
          >
            Sign out
          </button>
        </div>
      </aside>

      <div className="flex min-w-0 flex-1 flex-col">
        <header className="flex items-center justify-between border-b border-outline-variant bg-surface px-4 py-3 lg:px-10">
          <div className="flex items-center gap-2">
            <span className="material-symbols-outlined text-primary">contacts</span>
            <span className="text-lg font-semibold text-primary">Nexus CRM</span>
          </div>
          <div className="flex items-center gap-3 lg:hidden">
            <Avatar name={fullName} size="sm" />
          </div>
        </header>

        <main className="mx-auto w-full max-w-app flex-1 overflow-y-auto p-4 pb-24 lg:p-10 lg:pb-10">
          {children}
        </main>

        <PageFooter />

        <nav className="fixed bottom-0 left-0 right-0 flex justify-around border-t border-outline-variant bg-surface px-4 py-2 lg:hidden">
          {visibleBottom.map((item) => {
            const active = pathname.startsWith(item.href);
            return (
              <Link
                key={item.href}
                href={item.href}
                className={`rounded-full px-4 py-1 text-xs font-medium ${
                  active ? 'bg-secondary-container text-on-secondary-container' : 'text-on-surface-variant'
                }`}
              >
                {item.label}
              </Link>
            );
          })}
        </nav>
      </div>
    </div>
  );
}
