import { cleanup, fireEvent, render, screen, waitFor } from '@testing-library/react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { afterEach, beforeEach, describe, expect, it, vi } from 'vitest';
import AcceptInvitationPage from './page';

const { mockReplace, mockExecute, state } = vi.hoisted(() => ({
  mockReplace: vi.fn(),
  mockExecute: vi.fn().mockResolvedValue({
    token: 'jwt',
    userId: 'u-1',
    email: 'invitee@nexuscrm.com',
    firstName: 'Invitee',
    lastName: 'User',
    role: 'EDITOR',
  }),
  state: { token: 'invite-token' },
}));

vi.mock('next/navigation', () => ({
  useRouter: () => ({ replace: mockReplace }),
  useSearchParams: () => ({
    get: (key: string) => (key === 'token' ? state.token : null),
  }),
}));

vi.mock('@/identity/infrastructure/config/identityDependencies', () => ({
  identityDependencies: {
    completeInvitationUseCase: {
      execute: mockExecute,
    },
  },
}));

vi.mock('@/identity/interfaces/query/authQueryKeys', () => ({
  authQueryKeys: {
    session: ['auth', 'session'],
  },
}));

describe('AcceptInvitationPage', () => {
  afterEach(() => {
    cleanup();
  });

  beforeEach(() => {
    vi.clearAllMocks();
    state.token = 'invite-token';
  });

  function renderPage() {
    const queryClient = new QueryClient();
    render(
      <QueryClientProvider client={queryClient}>
        <AcceptInvitationPage />
      </QueryClientProvider>,
    );
  }

  it('shows missing token error when token is absent', async () => {
    state.token = '';
    renderPage();

    expect(await screen.findByText('Missing invitation token')).toBeInTheDocument();
  });

  it('completes invitation and redirects to contacts', async () => {
    renderPage();

    fireEvent.change(screen.getByLabelText('Password'), {
      target: { value: 'StrongPass123!' },
    });
    fireEvent.change(screen.getByLabelText('Confirm password'), {
      target: { value: 'StrongPass123!' },
    });
    fireEvent.click(screen.getByRole('button', { name: 'Complete registration' }));

    await waitFor(() => {
      expect(mockExecute).toHaveBeenCalledWith('invite-token', 'StrongPass123!');
    });
    await waitFor(() => {
      expect(mockReplace).toHaveBeenCalledWith('/contacts');
    });
  });
});
