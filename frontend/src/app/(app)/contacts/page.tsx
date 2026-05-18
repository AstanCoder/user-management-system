'use client';

import Link from 'next/link';
import { useRouter } from 'next/navigation';
import { useState } from 'react';
import { Mail, Building2, Plus, Search, Filter } from 'lucide-react';
import { useContactDirectory } from '@/contact/interfaces/hooks/useContactDirectory';
import { useAuth } from '@/identity/interfaces/hooks/useAuth';
import { Avatar } from '@/shared/ui/Avatar';
import { Button } from '@/shared/ui/Button';
import { Chip } from '@/shared/ui/Chip';
import { DropdownMenu } from '@/shared/ui/DropdownMenu';
import { Pagination } from '@/shared/ui/Pagination';
import type { ContactViewModel } from '@/contact/application/command/ContactViewModel';

const PAGE_SIZE = 20;

export default function ContactsDirectoryPage() {
  const router = useRouter();
  const { user } = useAuth();
  const [search, setSearch] = useState('');
  const [page, setPage] = useState(0);
  const [filtersOpen, setFiltersOpen] = useState(false);
  const [emailFilter, setEmailFilter] = useState('');
  const [phoneFilter, setPhoneFilter] = useState('');
  const [tagFilters, setTagFilters] = useState<string[]>([]);
  const [draftEmailFilter, setDraftEmailFilter] = useState('');
  const [draftPhoneFilter, setDraftPhoneFilter] = useState('');
  const [draftTagFiltersText, setDraftTagFiltersText] = useState('');
  const { items, loading, error, totalElements, totalPages, deleteContact } = useContactDirectory({
    search,
    email: emailFilter,
    phone: phoneFilter,
    tagNames: tagFilters,
    page,
    size: PAGE_SIZE,
  });

  const canEdit = user?.role === 'ADMIN' || user?.role === 'EDITOR';

  async function handleDelete(contact: ContactViewModel) {
    if (!confirm(`Delete "${contact.fullName}"? This cannot be undone.`)) return;
    try {
      await deleteContact(contact.id);
    } catch {
      alert('Failed to delete contact.');
    }
  }

  const activeFiltersCount =
    (emailFilter ? 1 : 0) + (phoneFilter ? 1 : 0) + (tagFilters.length > 0 ? 1 : 0);

  const applyAdvancedFilters = () => {
    setEmailFilter(draftEmailFilter.trim());
    setPhoneFilter(draftPhoneFilter.trim());
    setTagFilters(
      draftTagFiltersText
        .split(',')
        .map((tagName) => tagName.trim())
        .filter(Boolean),
    );
    setPage(0);
    setFiltersOpen(false);
  };

  const clearAllFilters = () => {
    setSearch('');
    setEmailFilter('');
    setPhoneFilter('');
    setTagFilters([]);
    setDraftEmailFilter('');
    setDraftPhoneFilter('');
    setDraftTagFiltersText('');
    setPage(0);
    setFiltersOpen(false);
  };

  return (
    <div className="space-y-6">
      <div className="flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
        <div>
          <h1 className="text-2xl font-bold tracking-tight text-primary">Contact Directory</h1>
          <p className="mt-1 text-sm text-on-surface-variant">
            Manage and organize your professional network.
          </p>
        </div>
        <div className="flex gap-3">
          <Button variant="secondary" className="flex items-center gap-1.5" onClick={() => setFiltersOpen((v) => !v)}>
            <Filter className="h-4 w-4" />
            Advanced Filters{activeFiltersCount > 0 ? ` (${activeFiltersCount})` : ''}
          </Button>
          <div className="relative">
            <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-on-surface-variant" />
            <input
              type="search"
              placeholder="Search contacts or phone…"
              value={search}
              onChange={(e) => {
                setSearch(e.target.value);
                setPage(0);
              }}
              className="h-10 rounded-lg border border-outline-variant bg-surface-container-lowest pl-9 pr-3 text-sm text-on-surface placeholder:text-on-surface-variant focus:outline-none focus:ring-2 focus:ring-primary"
            />
          </div>
          {canEdit && (
            <Link href="/contacts/new">
              <Button className="flex items-center gap-1.5">
                <Plus className="h-4 w-4" />
                Add Contact
              </Button>
            </Link>
          )}
        </div>
      </div>

      {filtersOpen && (
        <div className="rounded-xl bg-surface-container-lowest p-4 shadow-sm ring-1 ring-outline-variant">
          <div className="grid gap-3 md:grid-cols-3">
            <label className="text-sm text-on-surface-variant">
              Email
              <input
                value={draftEmailFilter}
                onChange={(e) => setDraftEmailFilter(e.target.value)}
                placeholder="Contains email"
                className="mt-1 h-10 w-full rounded-lg border border-outline-variant bg-surface-container-lowest px-3 text-sm text-on-surface focus:outline-none focus:ring-2 focus:ring-primary"
              />
            </label>
            <label className="text-sm text-on-surface-variant">
              Phone
              <input
                value={draftPhoneFilter}
                onChange={(e) => setDraftPhoneFilter(e.target.value)}
                placeholder="Contains phone"
                className="mt-1 h-10 w-full rounded-lg border border-outline-variant bg-surface-container-lowest px-3 text-sm text-on-surface focus:outline-none focus:ring-2 focus:ring-primary"
              />
            </label>
            <label className="text-sm text-on-surface-variant">
              Segmentation Tags
              <input
                value={draftTagFiltersText}
                onChange={(e) => setDraftTagFiltersText(e.target.value)}
                placeholder="typescript, sales"
                className="mt-1 h-10 w-full rounded-lg border border-outline-variant bg-surface-container-lowest px-3 text-sm text-on-surface focus:outline-none focus:ring-2 focus:ring-primary"
              />
            </label>
          </div>
          <div className="mt-3 flex justify-end gap-2">
            <Button variant="secondary" onClick={clearAllFilters}>
              Clear
            </Button>
            <Button onClick={applyAdvancedFilters}>Apply</Button>
          </div>
        </div>
      )}

      {loading && (
        <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4">
          {Array.from({ length: 8 }).map((_, i) => (
            <div key={i} className="h-44 animate-pulse rounded-xl bg-surface-container" />
          ))}
        </div>
      )}

      {error && (
        <div className="rounded-xl bg-error-container p-4 text-sm text-on-error-container">{error}</div>
      )}

      {!loading && !error && items.length === 0 && (
        <div className="flex flex-col items-center justify-center py-20 text-on-surface-variant">
          <p className="text-base font-medium">No contacts found.</p>
          {search && (
            <p className="mt-1 text-sm">
              Try adjusting your filters, or{' '}
              <button className="underline hover:text-on-surface" onClick={clearAllFilters}>
                clear filters
              </button>
              .
            </p>
          )}
        </div>
      )}

      {!loading && items.length > 0 && (
        <>
          <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4">
            {items.map((contact) => (
              <ContactCard
                key={contact.id}
                contact={contact}
                canEdit={canEdit}
                onView={() => router.push(`/contacts/${contact.id}`)}
                onEdit={() => router.push(`/contacts/${contact.id}/edit`)}
                onDelete={() => void handleDelete(contact)}
              />
            ))}
          </div>

          {totalPages > 1 && (
            <Pagination
              page={page}
              totalPages={totalPages}
              totalElements={totalElements}
              pageSize={PAGE_SIZE}
              onPageChange={setPage}
            />
          )}
        </>
      )}
    </div>
  );
}

interface ContactCardProps {
  contact: ContactViewModel;
  canEdit: boolean;
  onView: () => void;
  onEdit: () => void;
  onDelete: () => void;
}

function ContactCard({ contact, canEdit, onView, onEdit, onDelete }: ContactCardProps) {
  const actions = [
    { label: 'View', onClick: onView },
    ...(canEdit ? [{ label: 'Edit', onClick: onEdit }] : []),
    ...(canEdit ? [{ label: 'Delete', onClick: onDelete, destructive: true as const }] : []),
  ];

  return (
    <div
      className="group relative flex cursor-pointer flex-col rounded-xl bg-surface-container-lowest p-5 shadow-sm ring-1 ring-outline-variant transition-shadow hover:shadow-md"
      onClick={onView}
    >
      <div className="absolute right-3 top-3 opacity-0 transition-opacity group-hover:opacity-100">
        <DropdownMenu actions={actions} />
      </div>

      <div className="flex flex-col items-center gap-3 text-center">
        <Avatar name={contact.fullName} src={contact.avatarUrl} size="lg" />
        <div className="min-w-0 w-full">
          <p className="font-semibold text-on-surface">{contact.fullName}</p>
          {contact.jobTitle && <p className="mt-0.5 text-xs text-on-surface-variant">{contact.jobTitle}</p>}
        </div>
      </div>

      <div className="mt-4 space-y-1.5">
        <div className="flex items-center gap-2 truncate">
          <Mail className="h-3.5 w-3.5 flex-shrink-0 text-on-surface-variant" />
          <span className="truncate text-xs text-on-surface-variant">{contact.email}</span>
        </div>
        {contact.company && (
          <div className="flex items-center gap-2 truncate">
            <Building2 className="h-3.5 w-3.5 flex-shrink-0 text-on-surface-variant" />
            <span className="truncate text-xs text-on-surface-variant">{contact.company}</span>
          </div>
        )}
      </div>

      {contact.tags.length > 0 && (
        <div className="mt-3 flex flex-wrap gap-1">
          {contact.tags.slice(0, 3).map((tag) => (
            <Chip key={tag.id} label={tag.name} />
          ))}
          {contact.tags.length > 3 && <Chip label={`+${contact.tags.length - 3}`} />}
        </div>
      )}
    </div>
  );
}
