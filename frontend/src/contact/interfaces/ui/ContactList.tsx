import { ContactViewModel } from '../../application/command/ContactViewModel';

export interface ContactListProps {
  items: ContactViewModel[];
  loading: boolean;
  error: string | null;
  onRetry: () => void;
  onEdit: (contact: ContactViewModel) => void;
  onDelete: (contact: ContactViewModel) => void;
}

/**
 * Renders the contact table with loading, empty, and error states.
 */
export function ContactList({
  items,
  loading,
  error,
  onRetry,
  onEdit,
  onDelete,
}: ContactListProps) {
  if (loading) {
    return <p className="status">Loading contacts…</p>;
  }

  if (error) {
    return (
      <div className="error-panel">
        <p>{error}</p>
        <button type="button" className="btn btn-secondary" onClick={onRetry}>
          Retry
        </button>
      </div>
    );
  }

  if (items.length === 0) {
    return <p className="status">No contacts yet. Add your first contact.</p>;
  }

  return (
    <table className="contact-table">
      <thead>
        <tr>
          <th>Name</th>
          <th>Email</th>
          <th>Phone</th>
          <th aria-label="Actions" />
        </tr>
      </thead>
      <tbody>
        {items.map((contact) => (
          <tr key={contact.id}>
            <td>{contact.fullName}</td>
            <td>{contact.email}</td>
            <td>{contact.phone ?? '—'}</td>
            <td className="actions">
              <button type="button" className="btn btn-link" onClick={() => onEdit(contact)}>
                Edit
              </button>
              <button type="button" className="btn btn-link danger" onClick={() => onDelete(contact)}>
                Delete
              </button>
            </td>
          </tr>
        ))}
      </tbody>
    </table>
  );
}
