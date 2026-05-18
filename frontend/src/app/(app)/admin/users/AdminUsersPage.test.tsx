import { cleanup, fireEvent, render, screen, waitFor } from '@testing-library/react';
import { afterEach, beforeEach, describe, expect, it, vi } from 'vitest';

const { mockUpdateUser, mockDeleteUser, mockRefetch } = vi.hoisted(() => ({
  mockUpdateUser: vi.fn().mockResolvedValue(undefined),
  mockDeleteUser: vi.fn().mockResolvedValue(undefined),
  mockRefetch: vi.fn().mockResolvedValue(undefined),
}));

const mockUsers = [
  {
    id: '1',
    firstName: 'Admin',
    lastName: 'User',
    email: 'admin@nexuscrm.com',
    role: 'ADMIN' as const,
    status: 'ACTIVE' as const,
    lastActiveAt: null,
  },
  {
    id: '2',
    firstName: 'Jane',
    lastName: 'Doe',
    email: 'jane@example.com',
    role: 'EDITOR' as const,
    status: 'ACTIVE' as const,
    lastActiveAt: '2026-05-17T10:00:00Z',
  },
];

vi.mock('next/navigation', () => ({
  useRouter: () => ({ replace: vi.fn() }),
}));

vi.mock('@/identity/interfaces/hooks/useAuth', () => ({
  useAuth: () => ({
    user: {
      role: 'ADMIN',
      firstName: 'Admin',
      lastName: 'User',
      email: 'admin@nexuscrm.com',
      token: 't',
      userId: '1',
    },
    loading: false,
    refresh: vi.fn(),
  }),
}));

vi.mock('@/user/interfaces/hooks/useUserAdmin', () => ({
  useUserAdmin: () => ({
    users: mockUsers,
    stats: {
      totalUsers: 2,
      activeUsers: 2,
      invitedUsers: 0,
      disabledUsers: 0,
      adminCount: 1,
      editorCount: 1,
      viewerCount: 0,
      invitedPendingCount: 0,
      usersCreatedLast7Days: 1,
    },
    loading: false,
    error: null,
    page: 0,
    totalElements: 2,
    totalPages: 1,
    setPage: vi.fn(),
    refetch: mockRefetch,
    updateUser: mockUpdateUser,
    deleteUser: mockDeleteUser,
  }),
}));

import AdminUsersPage from './page';

describe('AdminUsersPage', () => {
  afterEach(() => {
    cleanup();
  });

  beforeEach(() => {
    vi.clearAllMocks();
    vi.stubGlobal('confirm', vi.fn(() => true));
  });

  it('renders stats and user rows', async () => {
    render(<AdminUsersPage />);
    expect(await screen.findByText('User Management')).toBeInTheDocument();
    expect(await screen.findByText('Jane Doe')).toBeInTheDocument();
    expect(screen.getByText('Invite User')).toBeInTheDocument();
  });

  it('shows pending count in total users card only', async () => {
    render(<AdminUsersPage />);
    await screen.findByText('User Management');
    expect(screen.getByText('+1 this week · 0 Pending')).toBeInTheDocument();
    expect(screen.queryByText('0 Pending')).not.toBeInTheDocument();
  });

  it('shows role change via dropdown menu', async () => {
    render(<AdminUsersPage />);
    await screen.findByText('Jane Doe');
    const menuButtons = screen.getAllByLabelText('More options');
    fireEvent.click(menuButtons[1]);
    const table = screen.getByRole('table');
    const tableContainer = table.parentElement;
    expect(tableContainer).not.toBeNull();
    expect(tableContainer?.textContent).not.toContain('Set as VIEWER');
    expect(screen.getByText('Set as VIEWER')).toBeInTheDocument();
    fireEvent.click(screen.getByText('Set as VIEWER'));
    await waitFor(() => {
      expect(mockUpdateUser).toHaveBeenCalledWith('2', {
        firstName: 'Jane',
        lastName: 'Doe',
        role: 'VIEWER',
        status: 'ACTIVE',
      });
    });
  });
});
