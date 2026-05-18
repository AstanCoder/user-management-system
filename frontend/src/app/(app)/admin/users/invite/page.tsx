'use client';

import Link from 'next/link';
import { useRouter } from 'next/navigation';
import { ChevronRight } from 'lucide-react';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { useForm } from 'react-hook-form';
import { useAuth } from '@/identity/interfaces/hooks/useAuth';
import { userDependencies } from '@/user/infrastructure/config/userDependencies';
import { UserRole } from '@/user/domain/port/UserAdminGateway';
import { userQueryKeys } from '@/user/interfaces/query/userQueryKeys';
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

interface InviteFormData {
  firstName: string;
  lastName: string;
  email: string;
  role: UserRole;
}

export default function InviteUserPage() {
  const { user } = useAuth();
  const router = useRouter();
  const queryClient = useQueryClient();
  const { register, watch, setValue, handleSubmit, formState } = useForm<InviteFormData>({
    defaultValues: {
      firstName: '',
      lastName: '',
      email: '',
      role: 'EDITOR',
    },
  });

  const inviteMutation = useMutation({
    mutationFn: async (form: InviteFormData) =>
      userDependencies.userAdminGateway.invite({
        email: form.email.trim(),
        firstName: form.firstName.trim(),
        lastName: form.lastName.trim(),
        role: form.role,
      }),
    onSuccess: async () => {
      await queryClient.invalidateQueries({ queryKey: userQueryKeys.all });
      router.push('/admin/users');
    },
  });

  if (user && user.role !== 'ADMIN') {
    router.replace('/contacts');
    return null;
  }

  const role = watch('role');

  const submit = async (data: InviteFormData) => {
    try {
      await inviteMutation.mutateAsync(data);
    } catch {}
  };

  const errorMessage =
    inviteMutation.error instanceof Error
      ? inviteMutation.error.message.toLowerCase().includes('already') ||
        inviteMutation.error.message.includes('409')
        ? 'This email is already registered'
        : inviteMutation.error.message
      : null;

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
        onSubmit={(e) => void handleSubmit(submit)(e)}
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
                {...register('firstName', { required: true })}
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
                {...register('lastName', { required: true })}
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
              {...register('email', { required: true })}
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
                    value={option.value}
                    checked={role === option.value}
                    onChange={() => setValue('role', option.value, { shouldDirty: true })}
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

          {errorMessage && (
            <p className="rounded-lg bg-error-container px-3 py-2 text-sm text-on-error-container">
              {errorMessage}
            </p>
          )}

          <div className="flex justify-end gap-3 border-t border-outline-variant pt-4">
            <Link href="/admin/users">
              <Button variant="secondary" type="button">
                Cancel
              </Button>
            </Link>
            <Button type="submit" disabled={inviteMutation.isPending || formState.isSubmitting}>
              {inviteMutation.isPending ? 'Sending…' : 'Send Invitation'}
            </Button>
          </div>
        </div>
      </form>
    </div>
  );
}
