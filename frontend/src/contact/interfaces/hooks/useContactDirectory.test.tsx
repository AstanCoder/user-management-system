import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { renderHook, waitFor } from '@testing-library/react';
import { ReactNode } from 'react';
import { beforeEach, describe, expect, it, vi } from 'vitest';

const { listPagedMock } = vi.hoisted(() => ({
  listPagedMock: vi.fn(),
}));

vi.mock('@/contact/infrastructure/config/contactDependencies', () => ({
  contactDependencies: {
    contactGateway: {
      listPaged: listPagedMock,
      delete: vi.fn(),
    },
  },
}));

import { useContactDirectory } from './useContactDirectory';

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

describe('useContactDirectory', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    listPagedMock.mockResolvedValue({
      content: [],
      totalElements: 1200,
      totalPages: 60,
      page: 0,
      size: 20,
    });
  });

  it('synchronizes internal page when controlled page changes', async () => {
    const wrapper = createWrapper();
    const { rerender } = renderHook(
      ({ page }) =>
        useContactDirectory({
          page,
          size: 20,
        }),
      {
        wrapper,
        initialProps: { page: 0 },
      },
    );

    await waitFor(() => {
      expect(listPagedMock).toHaveBeenCalledWith(expect.objectContaining({ page: 0, size: 20 }));
    });

    rerender({ page: 2 });

    await waitFor(() => {
      expect(listPagedMock).toHaveBeenCalledWith(expect.objectContaining({ page: 2, size: 20 }));
    });
  });
});
