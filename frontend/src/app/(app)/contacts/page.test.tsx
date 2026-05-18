import { cleanup, fireEvent, render, screen, waitFor } from '@testing-library/react';
import { afterEach, beforeEach, describe, expect, it, vi } from 'vitest';

const { mockUseContactDirectory } = vi.hoisted(() => ({
  mockUseContactDirectory: vi.fn(),
}));

vi.mock('next/navigation', () => ({
  useRouter: () => ({ push: vi.fn() }),
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

vi.mock('@/contact/interfaces/hooks/useContactDirectory', () => ({
  useContactDirectory: (options: unknown) => mockUseContactDirectory(options),
}));

import ContactsDirectoryPage from './page';

describe('ContactsDirectoryPage', () => {
  afterEach(() => {
    cleanup();
  });

  beforeEach(() => {
    vi.clearAllMocks();
    mockUseContactDirectory.mockReturnValue({
      items: [],
      loading: false,
      error: null,
      page: 0,
      totalPages: 0,
      totalElements: 0,
      setPage: vi.fn(),
      refetch: vi.fn(),
      deleteContact: vi.fn(),
    });
  });

  it('shows advanced filters action', () => {
    render(<ContactsDirectoryPage />);
    expect(screen.getByText('Advanced Filters')).toBeInTheDocument();
  });

  it('applies advanced filters to directory hook options', async () => {
    render(<ContactsDirectoryPage />);

    fireEvent.click(screen.getAllByText('Advanced Filters')[0]);
    fireEvent.change(screen.getByPlaceholderText('Contains email'), {
      target: { value: 'albert@outlook.com' },
    });
    fireEvent.change(screen.getByPlaceholderText('Contains phone'), {
      target: { value: '786' },
    });
    fireEvent.change(screen.getByPlaceholderText('typescript, sales'), {
      target: { value: 'typescript, sales' },
    });
    fireEvent.click(screen.getByText('Apply'));

    await waitFor(() => {
      const lastCall = mockUseContactDirectory.mock.calls.at(-1)?.[0] as {
        email?: string;
        phone?: string;
        tagNames?: string[];
      };
      expect(lastCall.email).toBe('albert@outlook.com');
      expect(lastCall.phone).toBe('786');
      expect(lastCall.tagNames).toEqual(['typescript', 'sales']);
    });
  });
});
