'use client';

import Link from 'next/link';
import { useState } from 'react';
import { useRouter } from 'next/navigation';
import { Search, UserPlus, Users, Shield, PenLine } from 'lucide-react';
import { useAuth } from '@/identity/interfaces/hooks/useAuth';
import { useUserAdmin } from '@/user/interfaces/hooks/useUserAdmin';
import { UserDto, UserRole } from '@/user/domain/port/UserAdminGateway';
import { Avatar } from '@/shared/ui/Avatar';
import { Badge, BadgeProps } from '@/shared/ui/Badge';
import { Button } from '@/shared/ui/Button';
import { DropdownMenu } from '@/shared/ui/DropdownMenu';
import { Pagination } from '@/shared/ui/Pagination';
import { StatCard } from '@/shared/ui/StatCard';
import { formatRelativeTime } from '@/shared/lib/relativeTime';

const PAGE_SIZE = 20;
const ROLES: UserRole[] = ['ADMIN', 'EDITOR', 'VIEWER'];

function roleBadgeTone(role: UserRole): BadgeProps['tone'] {
  if (role === 'ADMIN') return 'warning';
  if (role === 'EDITOR') return 'success';
  return 'muted';
}

function statusDotColor(status: UserDto['status']): string {
  if (status === 'ACTIVE') return 'bg-[#2E7D32]';
  if (status === 'INVITED') return 'bg-amber-500';
  return 'bg-error';
}

function statusLabel(status: UserDto['status']): string {
  if (status === 'ACTIVE') return 'Active';
  if (status === 'INVITED') return 'Invited';
  return 'Disabled';
}

export default function AdminUsersPage() {
  const { user } = useAuth();
  const router = useRouter();
  const [search, setSearch] = useState('');
  const [page, setPage] = useState(0);
  const [busyId, setBusyId] = useState<string | null>(null);
  const [actionError, setActionError] = useState<string | null>(null);

  const { users, stats, loading, error, totalElements, totalPages, updateUser, deleteUser } = useUserAdmin({
    search,
    page,
    size: PAGE_SIZE,
  });

  if (user && user.role !== 'ADMIN') {
    router.replace('/contacts');
    return null;
  }

  const handleRoleChange = async (target: UserDto, role: UserRole) => {
    if (target.role === role) return;
    setBusyId(target.id);
    setActionError(null);
    try {
      await updateUser(target.id, {
        firstName: target.firstName,
        lastName: target.lastName,
        role,
        status: target.status,
      });
    } catch (e) {
      setActionError(e instanceof Error ? e.message : 'Failed to update role');
    } finally {
      setBusyId(null);
    }
  };

  const handleDelete = async (target: UserDto) => {
    if (target.id === user?.userId) {
      setActionError('You cannot delete your own account');
      return;
    }
    if (!window.confirm(`Delete ${target.firstName} ${target.lastName}?`)) return;
    setBusyId(target.id);
    setActionError(null);
    try {
      await deleteUser(target.id);
    } catch (e) {
      setActionError(e instanceof Error ? e.message : 'Failed to delete user');
    } finally {
      setBusyId(null);
    }
  };

  return (
    <div className="space-y-6">
      <div className="flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
        <div>
          <h1 className="text-2xl font-bold tracking-tight text-primary">User Management</h1>
          <p className="mt-1 text-sm text-on-surface-variant">Manage team access and roles.</p>
        </div>
        <Link href="/admin/users/invite">
          <Button className="flex items-center gap-1.5">
            <UserPlus className="h-4 w-4" />
            Invite User
          </Button>
        </Link>
      </div>

      {(error || actionError) && (
        <div className="rounded-xl bg-error-container p-4 text-sm text-on-error-container">
          {error ?? actionError}
        </div>
      )}

      {stats && (
        <div className="grid gap-4 sm:grid-cols-3">
          <StatCard
            label="Total Users"
            value={stats.totalUsers}
            subtitle={`+${stats.usersCreatedLast7Days} this week · ${stats.invitedPendingCount} Pending`}
            icon={Users}
          />
          <StatCard label="Active Admins" value={stats.adminCount} icon={Shield} />
          <StatCard label="System Editors" value={stats.editorCount} icon={PenLine} />
        </div>
      )}

      <div className="flex gap-3">
        <div className="relative flex-1 max-w-sm">
          <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-on-surface-variant" />
          <input
            type="search"
            placeholder="Search by name or email…"
            value={search}
            onChange={(e) => {
              setSearch(e.target.value);
              setPage(0);
            }}
            className="h-10 w-full rounded-lg border border-outline-variant bg-surface-container-lowest pl-9 pr-3 text-sm focus:outline-none focus:ring-2 focus:ring-primary"
          />
        </div>
      </div>

      <div className="overflow-hidden rounded-xl bg-surface-container-lowest shadow-sm ring-1 ring-outline-variant">
        {loading ? (
          <div className="p-8 text-center text-on-surface-variant">Loading users…</div>
        ) : (
          <table className="w-full text-sm">
            <thead>
              <tr className="border-b border-outline-variant bg-surface-container-low text-left">
                <th className="px-4 py-3 font-medium text-on-surface-variant">User</th>
                <th className="px-4 py-3 font-medium text-on-surface-variant">Role</th>
                <th className="px-4 py-3 font-medium text-on-surface-variant">Status</th>
                <th className="px-4 py-3 font-medium text-on-surface-variant">Last Active</th>
                <th className="px-4 py-3" />
              </tr>
            </thead>
            <tbody>
              {users.map((u) => {
                const isSelf = u.id === user?.userId;
                const disabled = busyId === u.id;
                const fullName = `${u.firstName} ${u.lastName}`;
                return (
                  <tr key={u.id} className="border-b border-outline-variant last:border-0">
                    <td className="px-4 py-3">
                      <div className="flex items-center gap-3">
                        <Avatar name={fullName} size="sm" />
                        <div>
                          <p className="font-medium text-on-surface">
                            {fullName}
                            {isSelf && <span className="ml-2 text-xs text-secondary">(you)</span>}
                          </p>
                          <p className="text-xs text-on-surface-variant">{u.email}</p>
                        </div>
                      </div>
                    </td>
                    <td className="px-4 py-3">
                      <Badge label={u.role} tone={roleBadgeTone(u.role)} />
                    </td>
                    <td className="px-4 py-3">
                      <div className="flex items-center gap-2">
                        <span className={`h-2 w-2 rounded-full ${statusDotColor(u.status)}`} />
                        <span className="text-on-surface">{statusLabel(u.status)}</span>
                      </div>
                    </td>
                    <td className="px-4 py-3 text-on-surface-variant">
                      {u.lastActiveAt ? formatRelativeTime(u.lastActiveAt) : 'Never'}
                    </td>
                    <td className="px-4 py-3 text-right">
                      <DropdownMenu
                        actions={[
                          ...ROLES.map((role) => ({
                            label: `Set as ${role}`,
                            onClick: () => void handleRoleChange(u, role),
                            disabled: disabled || u.role === role,
                          })),
                          {
                            label: 'Delete',
                            onClick: () => void handleDelete(u),
                            destructive: true,
                            disabled: disabled || isSelf,
                          },
                        ]}
                      />
                    </td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        )}
      </div>

      {totalPages > 1 && (
        <Pagination
          page={page}
          totalPages={totalPages}
          totalElements={totalElements}
          pageSize={PAGE_SIZE}
          onPageChange={setPage}
        />
      )}
    </div>
  );
}
