'use client';

import { useCallback, useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import { useAuth } from '@/identity/interfaces/hooks/useAuth';
import { userDependencies } from '@/user/infrastructure/config/userDependencies';
import { UserDto, UserRole, UserStatsDto } from '@/user/domain/port/UserAdminGateway';
import { Badge, BadgeProps } from '@/shared/ui/Badge';
import { Button } from '@/shared/ui/Button';
import { Card } from '@/shared/ui/Card';

const ROLES: UserRole[] = ['ADMIN', 'EDITOR', 'VIEWER'];

function roleBadgeTone(role: UserRole): BadgeProps['tone'] {
  if (role === 'ADMIN') return 'warning';
  if (role === 'EDITOR') return 'success';
  return 'muted';
}

export default function AdminUsersPage() {
  const { user } = useAuth();
  const router = useRouter();
  const [users, setUsers] = useState<UserDto[]>([]);
  const [stats, setStats] = useState<UserStatsDto | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [busyId, setBusyId] = useState<string | null>(null);

  const load = useCallback(async () => {
    const [u, s] = await Promise.all([
      userDependencies.userAdminGateway.list(),
      userDependencies.userAdminGateway.stats(),
    ]);
    setUsers(u);
    setStats(s);
  }, []);

  useEffect(() => {
    if (user && user.role !== 'ADMIN') {
      router.replace('/contacts');
      return;
    }
    if (user?.role === 'ADMIN') {
      void load().catch((e) => setError(e instanceof Error ? e.message : 'Failed to load users'));
    }
  }, [user, router, load]);

  const handleRoleChange = async (target: UserDto, role: UserRole) => {
    if (target.role === role) return;
    setBusyId(target.id);
    setError(null);
    try {
      const updated = await userDependencies.userAdminGateway.update(target.id, {
        firstName: target.firstName,
        lastName: target.lastName,
        role,
        status: target.status,
      });
      setUsers((prev) => prev.map((u) => (u.id === updated.id ? updated : u)));
    } catch (e) {
      setError(e instanceof Error ? e.message : 'Failed to update role');
    } finally {
      setBusyId(null);
    }
  };

  const handleDelete = async (target: UserDto) => {
    if (target.id === user?.userId) {
      setError('You cannot delete your own account');
      return;
    }
    if (!window.confirm(`Delete ${target.firstName} ${target.lastName}?`)) {
      return;
    }
    setBusyId(target.id);
    setError(null);
    try {
      await userDependencies.userAdminGateway.delete(target.id);
      await load();
    } catch (e) {
      setError(e instanceof Error ? e.message : 'Failed to delete user');
    } finally {
      setBusyId(null);
    }
  };

  if (!stats) return <p>Loading…</p>;

  return (
    <div>
      <h1 className="mb-2 text-2xl font-bold text-primary">User management</h1>
      <p className="mb-6 text-on-surface-variant">Manage team access and roles.</p>
      {error && <p className="mb-4 text-sm text-error">{error}</p>}
      <div className="mb-8 grid gap-4 sm:grid-cols-3">
        <Card>
          <p className="text-xs uppercase text-on-surface-variant">Total users</p>
          <p className="text-2xl font-bold">{stats.totalUsers}</p>
        </Card>
        <Card>
          <p className="text-xs uppercase text-on-surface-variant">Active</p>
          <p className="text-2xl font-bold">{stats.activeUsers}</p>
        </Card>
        <Card>
          <p className="text-xs uppercase text-on-surface-variant">Invited</p>
          <p className="text-2xl font-bold">{stats.invitedUsers}</p>
        </Card>
      </div>
      <table className="w-full border-collapse text-sm">
        <thead>
          <tr className="border-b border-outline-variant text-left">
            <th className="py-2">User</th>
            <th>Role</th>
            <th>Status</th>
            <th className="py-2 text-right">Actions</th>
          </tr>
        </thead>
        <tbody>
          {users.map((u) => {
            const isSelf = u.id === user?.userId;
            const disabled = busyId === u.id;
            return (
              <tr key={u.id} className="border-b border-outline-variant">
                <td className="py-3">
                  <p className="font-medium">
                    {u.firstName} {u.lastName}
                    {isSelf && <span className="ml-2 text-xs text-secondary">(you)</span>}
                  </p>
                  <p className="text-on-surface-variant">{u.email}</p>
                </td>
                <td>
                  <div className="flex flex-col gap-2">
                    <Badge label={u.role} tone={roleBadgeTone(u.role)} />
                    <select
                      value={u.role}
                      disabled={disabled}
                      onChange={(e) => void handleRoleChange(u, e.target.value as UserRole)}
                      className="rounded border border-outline-variant bg-surface-container-lowest px-2 py-1 text-sm"
                      aria-label={`Change role for ${u.email}`}
                    >
                      {ROLES.map((r) => (
                        <option key={r} value={r}>
                          {r}
                        </option>
                      ))}
                    </select>
                  </div>
                </td>
                <td>
                  <Badge label={u.status} tone="muted" />
                </td>
                <td className="py-3 text-right">
                  <Button
                    variant="danger"
                    disabled={disabled || isSelf}
                    onClick={() => void handleDelete(u)}
                    className="text-xs"
                  >
                    Delete
                  </Button>
                </td>
              </tr>
            );
          })}
        </tbody>
      </table>
    </div>
  );
}
