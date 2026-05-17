'use client';

import Link from 'next/link';
import { FormEvent, ReactNode, useState } from 'react';
import { useParams } from 'next/navigation';
import { useContactDetail } from '@/contact/interfaces/hooks/useContactDetail';
import { Avatar } from '@/shared/ui/Avatar';
import { Button } from '@/shared/ui/Button';
import { Card } from '@/shared/ui/Card';
import { Chip } from '@/shared/ui/Chip';

type DetailTab = 'activity' | 'notes' | 'company';

function formatWhen(iso: string): string {
  return new Date(iso).toLocaleString(undefined, { dateStyle: 'medium', timeStyle: 'short' });
}

function DetailTabButton({
  active,
  children,
  onClick,
}: {
  active: boolean;
  children: ReactNode;
  onClick: () => void;
}) {
  return (
    <button
      type="button"
      onClick={onClick}
      className={`px-4 py-4 text-sm font-medium transition-colors ${
        active
          ? 'border-b-2 border-primary font-semibold text-primary'
          : 'border-b-2 border-transparent text-on-surface-variant hover:border-outline-variant hover:text-on-surface'
      }`}
    >
      {children}
    </button>
  );
}

function ProfileField({ label, children }: { label: string; children: ReactNode }) {
  return (
    <div className="flex flex-col">
      <span className="mb-1 text-xs font-semibold uppercase tracking-wider text-outline">{label}</span>
      <div className="text-sm text-on-surface">{children}</div>
    </div>
  );
}

export default function ContactDetailPage() {
  const params = useParams();
  const id = params.id as string;
  const { contact, loading, error, addNote } = useContactDetail(id);
  const [note, setNote] = useState('');
  const [tab, setTab] = useState<DetailTab>('activity');
  const [savingNote, setSavingNote] = useState(false);

  const handleAddNote = async (e: FormEvent) => {
    e.preventDefault();
    if (!note.trim()) return;
    setSavingNote(true);
    try {
      await addNote(note.trim());
      setNote('');
    } finally {
      setSavingNote(false);
    }
  };

  if (loading) return <p className="text-on-surface-variant">Loading…</p>;
  if (error || !contact) return <p className="text-error">{error ?? 'Contact not found'}</p>;

  const address = [contact.street, contact.city, contact.postalCode, contact.country].filter(Boolean).join(', ');
  const roleLine = [contact.jobTitle, contact.company].filter(Boolean).join(' at ');

  return (
    <div>
      <Link
        href="/contacts"
        className="mb-6 inline-flex items-center gap-1 text-sm text-on-surface-variant transition-colors hover:text-primary"
      >
        <span className="material-symbols-outlined text-xl">arrow_back</span>
        Back to Directory
      </Link>

      <div className="grid grid-cols-1 gap-6 lg:grid-cols-12 lg:gap-6">
        <div className="flex flex-col gap-6 lg:col-span-4">
          <Card className="relative overflow-hidden p-0 text-center">
            <div
              className="absolute inset-x-0 top-0 h-24 bg-gradient-to-r from-surface-container-high to-surface-container opacity-50"
              aria-hidden
            />
            <div className="relative px-6 pb-6 pt-4">
              <div className="relative z-10 mx-auto mt-2 w-fit rounded-full border-4 border-surface-container-lowest shadow-sm">
                <Avatar name={contact.fullName} src={contact.avatarUrl} size="xl" />
              </div>
              <h1 className="relative z-10 mt-4 font-headline-md text-on-surface">{contact.fullName}</h1>
              {roleLine && (
                <p className="relative z-10 mt-1 text-base text-on-surface-variant">{roleLine}</p>
              )}

              <div className="relative z-10 mt-6 flex w-full gap-3">
                {contact.phone ? (
                  <a
                    href={`tel:${contact.phone}`}
                    className="flex flex-1 items-center justify-center gap-2 rounded-lg bg-primary-container px-3 py-3 text-sm font-medium text-on-primary transition-colors hover:bg-[#0f273f]"
                  >
                    <span className="material-symbols-outlined text-xl">call</span>
                    Call
                  </a>
                ) : (
                  <Button variant="primary" className="flex-1 py-3" disabled>
                    Call
                  </Button>
                )}
                <a
                  href={`mailto:${contact.email}`}
                  className="flex flex-1 items-center justify-center gap-2 rounded-lg border border-outline-variant bg-surface-container-high px-3 py-3 text-sm font-medium text-on-surface transition-colors hover:bg-surface-container"
                >
                  <span className="material-symbols-outlined text-xl">mail</span>
                  Email
                </a>
                <Link
                  href={`/contacts/${id}/edit`}
                  className="flex w-12 shrink-0 items-center justify-center rounded-lg border border-outline-variant bg-surface-container-lowest text-on-surface-variant transition-colors hover:bg-surface-container-low"
                  aria-label="Edit contact"
                >
                  <span className="material-symbols-outlined text-xl">edit</span>
                </Link>
              </div>

              <div className="relative z-10 mt-6 flex w-full flex-col gap-4 text-left">
                {contact.phone && (
                  <ProfileField label="Direct line">{contact.phone}</ProfileField>
                )}
                <ProfileField label="Work email">
                  <a href={`mailto:${contact.email}`} className="text-primary hover:underline">
                    {contact.email}
                  </a>
                </ProfileField>
                {address && <ProfileField label="Location">{address}</ProfileField>}
              </div>
            </div>
          </Card>

          {contact.tags.length > 0 && (
            <Card>
              <h2 className="mb-3 text-sm font-bold text-on-surface">Segmentation tags</h2>
              <div className="flex flex-wrap gap-2">
                {contact.tags.map((t) => (
                  <Chip key={t.id} label={t.name} />
                ))}
              </div>
            </Card>
          )}
        </div>

        <div className="flex flex-col lg:col-span-8">
          <Card className="flex flex-1 flex-col overflow-hidden p-0">
            <div className="flex border-b border-outline-variant bg-surface">
              <DetailTabButton active={tab === 'activity'} onClick={() => setTab('activity')}>
                Activity Log
              </DetailTabButton>
              <DetailTabButton active={tab === 'notes'} onClick={() => setTab('notes')}>
                Notes
              </DetailTabButton>
              <DetailTabButton active={tab === 'company'} onClick={() => setTab('company')}>
                Company Intel
              </DetailTabButton>
            </div>

            <div className="flex-1 overflow-y-auto p-6">
              {tab === 'activity' && (
                <>
                  <h2 className="mb-6 text-xl font-semibold text-on-surface">Recent interactions</h2>
                  {contact.activities.length === 0 ? (
                    <p className="text-sm text-on-surface-variant">No activities logged yet.</p>
                  ) : (
                    <ul className="relative flex flex-col gap-8 border-l-2 border-surface-container-high pl-6">
                      {contact.activities.map((item, index) => (
                        <li key={item.id} className="relative">
                          <span
                            className={`absolute -left-[31px] top-1 flex h-6 w-6 items-center justify-center rounded-full border-2 bg-surface-container-lowest ${
                              index === 0 ? 'border-primary' : 'border-outline-variant'
                            }`}
                            aria-hidden
                          >
                            <span
                              className={`h-2 w-2 rounded-full ${index === 0 ? 'bg-primary' : 'bg-outline-variant'}`}
                            />
                          </span>
                          <div className="flex flex-col gap-1">
                            <div className="flex flex-wrap items-center gap-2">
                              <span className="text-sm font-bold text-on-surface">{item.title}</span>
                              <span className="text-xs text-outline">{formatWhen(item.occurredAt)}</span>
                            </div>
                            {item.description && (
                              <p className="mt-2 rounded-lg border border-outline-variant/50 bg-surface-container-low p-3 text-sm text-on-surface-variant">
                                {item.description}
                              </p>
                            )}
                          </div>
                        </li>
                      ))}
                    </ul>
                  )}
                </>
              )}

              {tab === 'notes' && (
                <>
                  <h2 className="mb-6 text-xl font-semibold text-on-surface">Notes</h2>
                  {contact.notes.length === 0 ? (
                    <p className="text-sm text-on-surface-variant">No notes yet. Add one below.</p>
                  ) : (
                    <ul className="space-y-4">
                      {contact.notes.map((item) => (
                        <li
                          key={item.id}
                          className="rounded-lg border border-outline-variant/50 border-l-4 border-l-secondary bg-surface p-4 text-sm text-on-surface-variant"
                        >
                          <p>{item.body}</p>
                          <p className="mt-2 text-xs text-outline">{formatWhen(item.createdAt)}</p>
                        </li>
                      ))}
                    </ul>
                  )}
                </>
              )}

              {tab === 'company' && (
                <div className="space-y-4">
                  <h2 className="text-xl font-semibold text-on-surface">Company intel</h2>
                  <p className="text-base font-medium text-on-surface">
                    {contact.company ?? 'No company on file'}
                  </p>
                  {contact.jobTitle && (
                    <p className="text-sm text-on-surface-variant">
                      <span className="font-medium text-on-surface">Role:</span> {contact.jobTitle}
                    </p>
                  )}
                  {address && (
                    <p className="text-sm text-on-surface-variant">
                      <span className="font-medium text-on-surface">Address:</span> {address}
                    </p>
                  )}
                </div>
              )}
            </div>

            {tab === 'notes' && (
              <form
                onSubmit={(e) => void handleAddNote(e)}
                className="border-t border-outline-variant bg-surface-container-lowest p-4"
              >
                <label htmlFor="add-note" className="mb-2 block text-sm font-medium text-on-surface">
                  Add note
                </label>
                <div className="flex flex-col gap-3 sm:flex-row sm:items-end">
                  <textarea
                    id="add-note"
                    value={note}
                    onChange={(e) => setNote(e.target.value)}
                    rows={3}
                    placeholder="Write a note about this contact…"
                    className="min-h-[88px] flex-1 resize-y rounded-lg border border-outline-variant bg-surface-container-low px-3 py-2 text-sm text-on-surface placeholder:text-outline focus:border-secondary focus:outline-none focus:ring-2 focus:ring-secondary/10"
                  />
                  <Button type="submit" disabled={savingNote || !note.trim()} className="shrink-0 sm:min-w-[96px]">
                    {savingNote ? 'Saving…' : 'Save'}
                  </Button>
                </div>
              </form>
            )}
          </Card>
        </div>
      </div>
    </div>
  );
}
