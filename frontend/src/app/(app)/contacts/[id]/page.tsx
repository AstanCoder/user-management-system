'use client';

import { useContactDetail } from '@/contact/interfaces/hooks/useContactDetail';
import { useAuth } from '@/identity/interfaces/hooks/useAuth';
import { ActivityTimeline } from '@/shared/ui/ActivityTimeline';
import { Avatar } from '@/shared/ui/Avatar';
import { Button } from '@/shared/ui/Button';
import { Chip } from '@/shared/ui/Chip';
import {
    ArrowLeft,
    Building2,
    ChevronDown,
    Edit,
    Filter,
    Mail,
    MapPin,
    Phone
} from 'lucide-react';
import Link from 'next/link';
import { useParams } from 'next/navigation';
import { FormEvent, ReactNode, useEffect, useRef, useState } from 'react';

type DetailTab = 'activity' | 'company';

const ACTIVITY_FILTERS = [
  { value: '', label: 'All' },
  { value: 'CALL', label: 'Calls' },
  { value: 'EMAIL', label: 'Emails' },
  { value: 'NOTE', label: 'Notes' },
  { value: 'MEETING', label: 'Meetings' },
];

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
  const { user } = useAuth();
  const [logText, setLogText] = useState('');
  const [logType, setLogType] = useState('NOTE');
  const [tab, setTab] = useState<DetailTab>('activity');
  const [activityFilter, setActivityFilter] = useState('');
  const [filterOpen, setFilterOpen] = useState(false);
  const [savingLog, setSavingLog] = useState(false);
  const filterRef = useRef<HTMLDivElement>(null);
  const loadMoreRef = useRef<HTMLDivElement>(null);
  const {
    contact,
    activities,
    hasMoreActivities,
    loadingMoreActivities,
    loadMoreActivities,
    loading,
    error,
    addActivity,
    confirmActivity,
    deleteActivity,
  } = useContactDetail(id, activityFilter);

  const canEdit = user?.role === 'ADMIN' || user?.role === 'EDITOR';

  useEffect(() => {
    const target = loadMoreRef.current;
    if (!target) {
      return;
    }
    const observer = new IntersectionObserver(
      (entries) => {
        const [entry] = entries;
        if (entry?.isIntersecting && hasMoreActivities && !loadingMoreActivities) {
          void loadMoreActivities();
        }
      },
      { rootMargin: '200px' },
    );
    observer.observe(target);
    return () => observer.disconnect();
  }, [hasMoreActivities, loadMoreActivities, loadingMoreActivities]);

  const handleAddLog = async (e: FormEvent) => {
    e.preventDefault();
    if (!logText.trim()) return;
    setSavingLog(true);
    try {
      await addActivity(logType, logText.trim(), !(logType === 'CALL' || logType === 'EMAIL'));
      setLogText('');
    } finally {
      setSavingLog(false);
    }
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center py-20 text-on-surface-variant">
        Loading…
      </div>
    );
  }

  if (error || !contact) {
    return <p className="text-error">{error ?? 'Contact not found'}</p>;
  }

  const address = [contact.street, contact.city, contact.postalCode, contact.country]
    .filter(Boolean)
    .join(', ');
  const roleLine = [contact.jobTitle, contact.company].filter(Boolean).join(' at ');
  const userInitials = user ? `${user.firstName[0]}${user.lastName[0]}`.toUpperCase() : 'U';

  return (
    <div>
      <Link
        href="/contacts"
        className="mb-6 inline-flex items-center gap-1.5 text-sm text-on-surface-variant transition-colors hover:text-primary"
      >
        <ArrowLeft className="h-4 w-4" />
        Back to Directory
      </Link>

      <div className="grid grid-cols-1 gap-6 lg:grid-cols-12">
        <div className="flex flex-col gap-4 lg:col-span-4">
          <div className="relative overflow-hidden rounded-xl bg-surface-container-lowest shadow-sm ring-1 ring-outline-variant">
            <div
              className="absolute inset-x-0 top-0 h-24 bg-gradient-to-r from-primary-container to-secondary-container opacity-60"
              aria-hidden
            />
            <div className="relative px-6 pb-6 pt-4 text-center">
              <div className="mx-auto mt-2 w-fit rounded-full border-4 border-surface-container-lowest shadow-sm">
                <Avatar name={contact.fullName} src={contact.avatarUrl} size="xl" />
              </div>
              <h1 className="mt-4 text-xl font-bold text-on-surface">{contact.fullName}</h1>
              {roleLine && <p className="mt-1 text-sm text-on-surface-variant">{roleLine}</p>}

              <div className="mt-6 flex gap-2">
                {contact.phone ? (
                  <a
                    href={`tel:${contact.phone}`}
                    onClick={() =>
                      void addActivity('CALL', `Call started with ${contact.fullName}`, false)
                    }
                    className="flex flex-1 items-center justify-center gap-1.5 rounded-lg bg-primary-container py-2.5 text-sm font-medium text-on-primary transition-colors hover:opacity-90"
                  >
                    <Phone className="h-4 w-4" />
                    Call
                  </a>
                ) : (
                  <button
                    disabled
                    className="flex flex-1 items-center justify-center gap-1.5 rounded-lg bg-surface-container py-2.5 text-sm text-on-surface-variant"
                  >
                    <Phone className="h-4 w-4" />
                    Call
                  </button>
                )}
                <a
                  href={`mailto:${contact.email}`}
                  onClick={() =>
                    void addActivity('EMAIL', `Email started with ${contact.fullName}`, false)
                  }
                  className="flex flex-1 items-center justify-center gap-1.5 rounded-lg border border-outline-variant bg-surface-container-high py-2.5 text-sm font-medium text-on-surface transition-colors hover:bg-surface-container"
                >
                  <Mail className="h-4 w-4" />
                  Email
                </a>
                {canEdit && (
                  <Link
                    href={`/contacts/${id}/edit`}
                    className="flex w-10 shrink-0 items-center justify-center rounded-lg border border-outline-variant bg-surface-container-lowest text-on-surface-variant transition-colors hover:bg-surface-container-low"
                    aria-label="Edit contact"
                  >
                    <Edit className="h-4 w-4" />
                  </Link>
                )}
              </div>

              <div className="mt-6 space-y-4 text-left">
                {contact.phone && <ProfileField label="Direct Line">{contact.phone}</ProfileField>}
                <ProfileField label="Work Email">
                  <a href={`mailto:${contact.email}`} className="text-primary hover:underline">
                    {contact.email}
                  </a>
                </ProfileField>
                {address && <ProfileField label="Location">{address}</ProfileField>}
              </div>
            </div>
          </div>

          <div className="rounded-xl bg-surface-container-lowest p-5 shadow-sm ring-1 ring-outline-variant">
            <h2 className="mb-3 text-sm font-bold text-on-surface">Segmentation Tags</h2>
            {contact.tags.length > 0 ? (
              <div className="flex flex-wrap gap-2">
                {contact.tags.map((t) => (
                  <Chip key={t.id} label={t.name} />
                ))}
              </div>
            ) : (
              <p className="text-xs text-on-surface-variant">No tags assigned.</p>
            )}
          </div>
        </div>

        <div className="flex flex-col lg:col-span-8">
          <div className="flex flex-1 flex-col overflow-hidden rounded-xl bg-surface-container-lowest shadow-sm ring-1 ring-outline-variant">
            <div className="flex border-b border-outline-variant">
              <DetailTabButton active={tab === 'activity'} onClick={() => setTab('activity')}>
                Activity Log
              </DetailTabButton>
              <DetailTabButton active={tab === 'company'} onClick={() => setTab('company')}>
                Company Intel
              </DetailTabButton>
            </div>

            <div className="flex-1 overflow-y-auto p-6">
              {tab === 'activity' && (
                <>
                  <div className="mb-4 flex items-center justify-between">
                    <h2 className="text-lg font-semibold text-on-surface">Recent Interactions</h2>
                    <div className="relative" ref={filterRef}>
                      <button
                        type="button"
                        onClick={() => setFilterOpen((v) => !v)}
                        className="flex items-center gap-1.5 rounded-lg border border-outline-variant px-3 py-1.5 text-sm text-on-surface-variant transition-colors hover:bg-surface-container"
                      >
                        <Filter className="h-3.5 w-3.5" />
                        Filter
                        <ChevronDown className="h-3.5 w-3.5" />
                      </button>
                      {filterOpen && (
                        <div className="absolute right-0 z-10 mt-1 w-36 rounded-xl border border-outline-variant bg-surface-container-lowest py-1 shadow-lg">
                          {ACTIVITY_FILTERS.map((f) => (
                            <button
                              key={f.value}
                              type="button"
                              onClick={() => {
                                setActivityFilter(f.value);
                                setFilterOpen(false);
                              }}
                              className={`w-full px-3 py-2 text-left text-sm transition-colors hover:bg-surface-container ${
                                activityFilter === f.value
                                  ? 'font-semibold text-primary'
                                  : 'text-on-surface'
                              }`}
                            >
                              {f.label}
                            </button>
                          ))}
                        </div>
                      )}
                    </div>
                  </div>
                  <ActivityTimeline
                    activities={activities}
                    canManage={canEdit}
                    onConfirm={confirmActivity}
                    onDelete={deleteActivity}
                  />
                  <div ref={loadMoreRef} className="h-6" />
                  {loadingMoreActivities && (
                    <p className="mt-2 text-center text-xs text-on-surface-variant">Loading more activities...</p>
                  )}
                </>
              )}

              {tab === 'company' && (
                <CompanyIntelTab contact={contact} address={address} />
              )}
            </div>

            {tab === 'activity' && canEdit && (
              <form
                onSubmit={(e) => void handleAddLog(e)}
                className="border-t border-outline-variant p-4"
              >
                <div className="flex items-start gap-3">
                  <div className="flex h-8 w-8 flex-shrink-0 items-center justify-center rounded-full bg-primary-container text-xs font-bold text-on-primary">
                    {userInitials}
                  </div>
                  <div className="flex flex-1 gap-2">
                    <select
                      value={logType}
                      onChange={(e) => setLogType(e.target.value)}
                      className="rounded-lg border border-outline-variant bg-surface-container-low px-2 py-2 text-sm text-on-surface focus:outline-none focus:ring-2 focus:ring-primary"
                    >
                      {ACTIVITY_FILTERS.filter((f) => f.value).map((f) => (
                        <option key={f.value} value={f.value}>
                          {f.label.slice(0, -1)}
                        </option>
                      ))}
                    </select>
                    <input
                      value={logText}
                      onChange={(e) => setLogText(e.target.value)}
                      placeholder={`Log ${logType.toLowerCase()} details...`}
                      className="flex-1 rounded-lg border border-outline-variant bg-surface-container-low px-3 py-2 text-sm text-on-surface placeholder:text-on-surface-variant focus:outline-none focus:ring-2 focus:ring-primary"
                    />
                    <Button type="submit" disabled={savingLog || !logText.trim()} className="shrink-0">
                      {savingLog ? 'Saving…' : 'Add Log'}
                    </Button>
                  </div>
                </div>
              </form>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}

function CompanyIntelTab({
  contact,
  address,
}: {
  contact: { company: string | null; jobTitle: string | null; city: string | null; country: string | null };
  address: string;
}) {
  const location = [contact.city, contact.country].filter(Boolean).join(', ');

  if (!contact.company && !contact.jobTitle && !address) {
    return (
      <div className="flex flex-col items-center justify-center py-16 text-on-surface-variant">
        <Building2 className="mb-3 h-10 w-10 opacity-40" />
        <p className="text-sm font-medium">No company information on file</p>
        <p className="mt-1 text-xs">Edit the contact to add company details.</p>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div className="rounded-xl bg-surface-container-low p-5">
        <div className="flex items-start gap-4">
          <div className="flex h-12 w-12 flex-shrink-0 items-center justify-center rounded-xl bg-primary-container">
            <Building2 className="h-6 w-6 text-on-primary-container" />
          </div>
          <div>
            <h2 className="text-lg font-bold text-on-surface">{contact.company ?? '—'}</h2>
            {contact.jobTitle && (
              <p className="mt-0.5 text-sm text-on-surface-variant">{contact.jobTitle}</p>
            )}
          </div>
        </div>
      </div>

      <div>
        <h3 className="mb-3 text-sm font-bold uppercase tracking-wider text-outline">Key Details</h3>
        <div className="grid gap-3 sm:grid-cols-2">
          <IntelField icon={MapPin} label="Location" value={location || '—'} />
          <IntelField icon={Building2} label="Status" value="Active Contact" />
        </div>
      </div>
    </div>
  );
}

function IntelField({
  icon: Icon,
  label,
  value,
}: {
  icon: React.ComponentType<{ className?: string }>;
  label: string;
  value: string;
}) {
  return (
    <div className="flex items-center gap-3 rounded-lg bg-surface-container-low p-3">
      <Icon className="h-4 w-4 flex-shrink-0 text-on-surface-variant" />
      <div>
        <p className="text-xs text-on-surface-variant">{label}</p>
        <p className="text-sm font-medium text-on-surface">{value}</p>
      </div>
    </div>
  );
}
