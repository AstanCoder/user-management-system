'use client';

export interface ConfirmDeleteDialogProps {
  /** Contact display name */
  contactName: string;
  /** Confirm delete */
  onConfirm: () => void;
  /** Cancel */
  onCancel: () => void;
}

/**
 * Confirmation dialog before deleting a contact.
 */
export function ConfirmDeleteDialog({ contactName, onConfirm, onCancel }: ConfirmDeleteDialogProps) {
  return (
    <section className="modal-backdrop" role="presentation" onClick={onCancel}>
      <section
        className="modal modal-sm"
        role="alertdialog"
        aria-modal="true"
        aria-labelledby="delete-title"
        onClick={(e) => e.stopPropagation()}
      >
        <h2 id="delete-title">Delete contact</h2>
        <p>
          Are you sure you want to delete <strong>{contactName}</strong>? This cannot be undone.
        </p>
        <footer className="form-actions">
          <button type="button" className="btn btn-secondary" onClick={onCancel}>
            Cancel
          </button>
          <button type="button" className="btn btn-danger" onClick={onConfirm}>
            Delete
          </button>
        </footer>
      </section>
    </section>
  );
}
