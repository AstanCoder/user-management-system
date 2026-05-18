'use client';

import Link from 'next/link';
import { FormEvent, useState } from 'react';
import { useRouter } from 'next/navigation';
import { ChevronRight } from 'lucide-react';
import { useAuth } from '@/identity/interfaces/hooks/useAuth';
import { userDependencies } from '@/user/infrastructure/config/userDependencies';
import { UserRole } from '@/user/domain/port/UserAdminGateway';
import { Button } from '@/shared/ui/Button';

const ROLE_OPTIONS: { value: UserRole; label: string; description: string }[] = [
  {
    value: 'ADMIN',
    label: 'Admin',
    description: 'Full access to all contacts, users, and system settings.',
  },
  {
    value: 'EDITOR',
    label: 'Editor',
    description: 'Can create, edit, and delete contacts. Cannot manage users.',
  },
  {
    value: 'VIEWER',
    label: 'Viewer',
    description: 'Read-only access to contacts and activity logs.',
  },
];

export default function InviteUserPage() {
  const { user } = useAuth();
  const router = useRouter();
  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');
  const [email, setEmail] = useState('');
  const [role, setRole] = useState<UserRole>('EDITOR');
  const [error, setError] = useState<string | null>(null);
  const [submitting, setSubmitting] = useState(false);

  if (user && user.role !== 'ADMIN') {
    router.replace('/contacts');
    return null;
  }

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setError(null);
    setSubmitting(true);
    try {
      await userDependencies.userAdminGateway.invite({
        email: email.trim(),
        firstName: firstName.trim(),
        lastName: lastName.trim(),
        role,
      });
      router.push('/admin/users');
    } catch (err) {
      const message = err instanceof Error ? err.message : 'Failed to send invitation';
      setError(
        message.toLowerCase().includes('already') || message.includes('409')
          ? 'This email is already registered'
          : message,
      );
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="mx-auto max-w-2xl space-y-6">
      <nav className="flex items-center gap-1 text-sm text-on-surface-variant">
        <Link href="/admin/users" className="hover:text-primary">
          Admin Panel
        </Link>
        <ChevronRight className="h-3.5 w-3.5" />
        <Link href="/admin/users" className="hover:text-primary">
          Users
        </Link>
        <ChevronRight className="h-3.5 w-3.5" />
        <span className="text-on-surface">Invite User</span>
      </nav>

      <div>
        <h1 className="text-2xl font-bold text-primary">Invite User</h1>
        <p className="mt-1 text-sm text-on-surface-variant">
          Send an invitation to join your Nexus CRM workspace.
        </p>
      </div>

      <form
        onSubmit={(e) => void handleSubmit(e)}
        className="rounded-xl bg-surface-container-lowest p-6 shadow-sm ring-1 ring-outline-variant"
      >
        <div className="space-y-6">
          <div className="grid gap-4 sm:grid-cols-2">
            <div>
              <label htmlFor="firstName" className="mb-1.5 block text-sm font-medium text-on-surface">
                First Name
              </label>
              <input
                id="firstName"
                type="text"
                required
                value={firstName}
                onChange={(e) => setFirstName(e.target.value)}
                placeholder="Jane"
                className="w-full rounded-lg border border-outline-variant bg-surface-container-low px-3 py-2.5 text-sm focus:outline-none focus:ring-2 focus:ring-primary"
              />
            </div>
            <div>
              <label htmlFor="lastName" className="mb-1.5 block text-sm font-medium text-on-surface">
                Last Name
              </label>
              <input
                id="lastName"
                type="text"
                required
                value={lastName}
                onChange={(e) => setLastName(e.target.value)}
                placeholder="Doe"
                className="w-full rounded-lg border border-outline-variant bg-surface-container-low px-3 py-2.5 text-sm focus:outline-none focus:ring-2 focus:ring-primary"
              />
            </div>
          </div>

          <div>
            <label htmlFor="email" className="mb-1.5 block text-sm font-medium text-on-surface">
              Email Address
            </label>
            <input
              id="email"
              type="email"
              required
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder="colleague@company.com"
              className="w-full rounded-lg border border-outline-variant bg-surface-container-low px-3 py-2.5 text-sm focus:outline-none focus:ring-2 focus:ring-primary"
            />
          </div>

          <fieldset>
            <legend className="mb-3 text-sm font-medium text-on-surface">Role</legend>
            <div className="space-y-3">
              {ROLE_OPTIONS.map((option) => (
                <label
                  key={option.value}
                  className={`flex cursor-pointer items-start gap-3 rounded-xl border p-4 transition-colors ${
                    role === option.value
                      ? 'border-primary bg-primary-container/10 ring-1 ring-primary'
                      : 'border-outline-variant hover:bg-surface-container-low'
                  }`}
                >
                  <input
                    type="radio"
                    name="role"
                    value={option.value}
                    checked={role === option.value}
                    onChange={() => setRole(option.value)}
                    className="mt-0.5 accent-primary"
                  />
                  <div>
                    <p className="text-sm font-semibold text-on-surface">{option.label}</p>
                    <p className="mt-0.5 text-xs text-on-surface-variant">{option.description}</p>
                  </div>
                </label>
              ))}
            </div>
          </fieldset>

          {error && (
            <p className="rounded-lg bg-error-container px-3 py-2 text-sm text-on-error-container">
              {error}
            </p>
          )}

          <div className="flex justify-end gap-3 border-t border-outline-variant pt-4">
            <Link href="/admin/users">
              <Button variant="secondary" type="button">
                Cancel
              </Button>
            </Link>
            <Button type="submit" disabled={submitting}>
              {submitting ? 'Sending…' : 'Send Invitation'}
            </Button>
          </div>
        </div>
      </form>
    </div>
  );
}
