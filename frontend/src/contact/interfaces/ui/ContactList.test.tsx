import { render, screen } from '@testing-library/react';
import { describe, expect, it, vi } from 'vitest';
import { ContactViewModel } from '../../application/command/ContactViewModel';
import { ContactList } from './ContactList';

const base: Omit<ContactViewModel, 'id' | 'fullName' | 'firstName' | 'lastName' | 'email'> = {
  phone: null,
  company: null,
  jobTitle: null,
  street: null,
  city: null,
  postalCode: null,
  country: null,
  avatarUrl: null,
  tags: [],
  notes: [],
  activities: [],
};

describe('ContactList', () => {
  it('renders contact names', () => {
    render(
      <ContactList
        items={[
          { id: '1', fullName: 'Jane Doe', firstName: 'Jane', lastName: 'Doe', email: 'jane@example.com', ...base },
          {
            id: '2',
            fullName: 'John Smith',
            firstName: 'John',
            lastName: 'Smith',
            email: 'john@example.com',
            ...base,
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
