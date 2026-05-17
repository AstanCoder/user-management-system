import { cleanup, fireEvent, render, screen, waitFor, within } from '@testing-library/react';
import { afterEach, beforeEach, describe, expect, it, vi } from 'vitest';

const { mockUpdate, mockDelete, mockList, mockStats } = vi.hoisted(() => ({
  mockUpdate: vi.fn().mockResolvedValue({
    id: '2',
    firstName: 'Jane',
    lastName: 'Doe',
    email: 'jane@example.com',
    role: 'VIEWER',
    status: 'ACTIVE',
  }),
  mockDelete: vi.fn().mockResolvedValue(undefined),
  mockList: vi.fn().mockResolvedValue([
    { id: '1', firstName: 'Admin', lastName: 'User', email: 'admin@nexuscrm.com', role: 'ADMIN', status: 'ACTIVE' },
    { id: '2', firstName: 'Jane', lastName: 'Doe', email: 'jane@example.com', role: 'EDITOR', status: 'ACTIVE' },
  ]),
  mockStats: vi.fn().mockResolvedValue({ totalUsers: 2, activeUsers: 2, invitedUsers: 0, disabledUsers: 0 }),
}));

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

vi.mock('@/user/infrastructure/config/userDependencies', () => ({
  userDependencies: {
    userAdminGateway: {
      list: mockList,
      stats: mockStats,
      update: mockUpdate,
      delete: mockDelete,
    },
  },
}));

import AdminUsersPage from './page';

describe('AdminUsersPage', () => {
  afterEach(() => {
    cleanup();
  });

  beforeEach(() => {
    vi.clearAllMocks();
    mockList.mockResolvedValue([
      { id: '1', firstName: 'Admin', lastName: 'User', email: 'admin@nexuscrm.com', role: 'ADMIN', status: 'ACTIVE' },
      { id: '2', firstName: 'Jane', lastName: 'Doe', email: 'jane@example.com', role: 'EDITOR', status: 'ACTIVE' },
    ]);
    mockStats.mockResolvedValue({ totalUsers: 2, activeUsers: 2, invitedUsers: 0, disabledUsers: 0 });
  });

  it('renders stats and user rows', async () => {
    render(<AdminUsersPage />);
    expect(await screen.findByText('User management')).toBeInTheDocument();
    expect(await screen.findByText('Jane Doe')).toBeInTheDocument();
    expect(screen.getByLabelText(/Change role for jane@example.com/)).toBeInTheDocument();
  });

  it('updates role on change', async () => {
    render(<AdminUsersPage />);
    const select = await screen.findByLabelText(/Change role for jane@example.com/);
    fireEvent.change(select, { target: { value: 'VIEWER' } });
    await waitFor(() => {
      expect(mockUpdate).toHaveBeenCalledWith('2', {
        firstName: 'Jane',
        lastName: 'Doe',
        role: 'VIEWER',
        status: 'ACTIVE',
      });
    });
  });

  it('disables delete for current admin', async () => {
    const view = render(<AdminUsersPage />);
    await waitFor(() => {
      const deleteButtons = within(view.container).getAllByRole('button', { name: 'Delete' });
      expect(deleteButtons).toHaveLength(2);
      expect(deleteButtons[0]).toBeDisabled();
      expect(deleteButtons[1]).not.toBeDisabled();
    });
  });
});
