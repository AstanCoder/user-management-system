import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { renderHook, waitFor } from '@testing-library/react';
import { ReactNode } from 'react';
import { beforeEach, describe, expect, it, vi } from 'vitest';

const { listUsersExecuteMock, getStatsExecuteMock } = vi.hoisted(() => ({
  listUsersExecuteMock: vi.fn(),
  getStatsExecuteMock: vi.fn(),
}));

vi.mock('@/user/infrastructure/config/userDependencies', () => ({
  userDependencies: {
    listUsersUseCase: {
      execute: listUsersExecuteMock,
    },
    getUserStatsUseCase: {
      execute: getStatsExecuteMock,
    },
    updateUserUseCase: {
      execute: vi.fn(),
    },
    deleteUserUseCase: {
      execute: vi.fn(),
    },
    inviteUserUseCase: {
      execute: vi.fn(),
    },
    resendInvitationUseCase: {
      execute: vi.fn(),
    },
  },
}));

import { useUserAdmin } from './useUserAdmin';

function createWrapper() {
  const queryClient = new QueryClient({
    defaultOptions: {
      queries: {
        retry: false,
      },
    },
  });

  return function Wrapper({ children }: { children: ReactNode }) {
    return <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>;
  };
}

describe('useUserAdmin', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    listUsersExecuteMock.mockResolvedValue({
      content: [],
      totalElements: 120,
      totalPages: 6,
      page: 0,
      size: 20,
    });
    getStatsExecuteMock.mockResolvedValue({
      totalUsers: 120,
      activeUsers: 100,
      invitedUsers: 10,
      disabledUsers: 10,
      adminCount: 4,
      editorCount: 80,
      viewerCount: 36,
      invitedPendingCount: 10,
      usersCreatedLast7Days: 6,
    });
  });

  it('synchronizes internal page when controlled page changes', async () => {
    const wrapper = createWrapper();
    const { rerender } = renderHook(
      ({ page }) =>
        useUserAdmin({
          page,
          size: 20,
        }),
      {
        wrapper,
        initialProps: { page: 0 },
      },
    );

    await waitFor(() => {
      expect(listUsersExecuteMock).toHaveBeenCalledWith(expect.objectContaining({ page: 0, size: 20 }));
    });

    rerender({ page: 2 });

    await waitFor(() => {
      expect(listUsersExecuteMock).toHaveBeenCalledWith(expect.objectContaining({ page: 2, size: 20 }));
    });
  });
});
