import { render, screen } from '@testing-library/react';
import { describe, expect, it, vi } from 'vitest';
import { ContactList } from './ContactList';

describe('ContactList', () => {
  it('renders contact names', () => {
    render(
      <ContactList
        items={[
          {
            id: '1',
            fullName: 'Jane Doe',
            firstName: 'Jane',
            lastName: 'Doe',
            email: 'jane@example.com',
            phone: null,
          },
          {
            id: '2',
            fullName: 'John Smith',
            firstName: 'John',
            lastName: 'Smith',
            email: 'john@example.com',
            phone: '+1234567890',
          },
        ]}
        loading={false}
        error={null}
        onRetry={vi.fn()}
        onEdit={vi.fn()}
        onDelete={vi.fn()}
      />,
    );
    expect(screen.getByText('Jane Doe')).toBeInTheDocument();
    expect(screen.getByText('John Smith')).toBeInTheDocument();
  });
});
