'use client';

import { useCallback, useMemo, useState } from 'react';
import { ContactViewModel } from '@/contact/application/command/ContactViewModel';
import { ContactId } from '@/contact/domain/valueobject/ContactId';
import { contactDependencies } from '@/contact/infrastructure/config/contactDependencies';
import { useContactList } from '@/contact/interfaces/hooks/useContactList';
import { ConfirmDeleteDialog } from '@/contact/interfaces/ui/ConfirmDeleteDialog';
import { ContactForm } from '@/contact/interfaces/ui/ContactForm';
import { ContactList } from '@/contact/interfaces/ui/ContactList';

type FormMode = 'create' | 'edit' | null;

export default function ContactsPage() {
  const deps = useMemo(() => contactDependencies, []);
  const { items, loading, error, refetch } = useContactList(deps);
  const [formMode, setFormMode] = useState<FormMode>(null);
  const [editing, setEditing] = useState<ContactViewModel | null>(null);
  const [deleting, setDeleting] = useState<ContactViewModel | null>(null);

  const handleCreate = useCallback(
    async (data: { firstName: string; lastName: string; email: string; phone: string }) => {
      await deps.createContactUseCase.execute(data);
      setFormMode(null);
      await refetch();
    },
    [deps, refetch],
  );

  const handleUpdate = useCallback(
    async (data: { firstName: string; lastName: string; email: string; phone: string }) => {
      if (!editing) return;
      await deps.updateContactUseCase.execute({
        id: ContactId.of(editing.id),
        ...data,
      });
      setFormMode(null);
      setEditing(null);
      await refetch();
    },
    [deps, editing, refetch],
  );

  const handleDelete = useCallback(async () => {
    if (!deleting) return;
    await deps.deleteContactUseCase.execute(ContactId.of(deleting.id));
    setDeleting(null);
    await refetch();
  }, [deps, deleting, refetch]);

  return (
    <main>
      <header className="page-header">
        <h1>Contacts</h1>
        <button type="button" className="btn btn-primary" onClick={() => setFormMode('create')}>
          Add contact
        </button>
      </header>

      <ContactList
        items={items}
        loading={loading}
        error={error}
        onRetry={() => void refetch()}
        onEdit={(c) => {
          setEditing(c);
          setFormMode('edit');
        }}
        onDelete={setDeleting}
      />

      {formMode === 'create' && (
        <ContactForm onSubmit={handleCreate} onCancel={() => setFormMode(null)} />
      )}
      {formMode === 'edit' && editing && (
        <ContactForm initial={editing} onSubmit={handleUpdate} onCancel={() => setFormMode(null)} />
      )}
      {deleting && (
        <ConfirmDeleteDialog
          contactName={deleting.fullName}
          onConfirm={() => void handleDelete()}
          onCancel={() => setDeleting(null)}
        />
      )}
    </main>
  );
}
