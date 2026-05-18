'use client';

import { Phone, Mail, FileText, Calendar, Edit, Activity } from 'lucide-react';
import type { LucideIcon } from 'lucide-react';
import { getActivityMeta } from '@/contact/application/mapper/ActivityLabelMapper';
import { formatRelativeTime } from '@/shared/lib/relativeTime';
import type { ActivityViewModel } from '@/contact/application/command/ContactViewModel';

const ICON_MAP: Record<string, LucideIcon> = {
  phone: Phone,
  mail: Mail,
  'file-text': FileText,
  calendar: Calendar,
  edit: Edit,
  activity: Activity,
};

interface ActivityTimelineProps {
  activities: ActivityViewModel[];
  filter?: string;
  canManage?: boolean;
  onConfirm?: (activityId: string) => Promise<void>;
  onDelete?: (activityId: string) => Promise<void>;
}

export function ActivityTimeline({
  activities,
  filter,
  canManage = false,
  onConfirm,
  onDelete,
}: ActivityTimelineProps) {
  const filtered = filter
    ? activities.filter((a) => a.activityType.toUpperCase() === filter.toUpperCase())
    : activities;

  if (filtered.length === 0) {
    return (
      <div className="flex flex-col items-center justify-center py-12 text-on-surface-variant">
        <Activity className="mb-2 h-8 w-8 opacity-40" />
        <p className="text-sm">No activity yet.</p>
      </div>
    );
  }

  return (
    <ol className="space-y-3">
      {filtered.map((activity) => {
        const meta = getActivityMeta(activity.activityType);
        const Icon = ICON_MAP[meta.iconName] ?? Activity;
        return (
          <li key={activity.id} className="flex gap-3">
            <div className="mt-0.5 flex h-8 w-8 flex-shrink-0 items-center justify-center rounded-full bg-primary-container">
              <Icon className="h-4 w-4 text-on-primary-container" />
            </div>
            <div className="min-w-0 flex-1 rounded-xl bg-surface-container-low p-3">
              <div className="flex items-center justify-between gap-2">
                <span className="text-sm font-semibold text-on-surface">{meta.title}</span>
                <div className="flex items-center gap-2">
                  {!activity.confirmed && (
                    <span className="rounded-full bg-warning/15 px-2 py-0.5 text-[10px] font-semibold uppercase tracking-wide text-warning">
                      Unconfirmed
                    </span>
                  )}
                  <span className="flex-shrink-0 text-xs text-on-surface-variant">
                    {formatRelativeTime(activity.occurredAt)}
                  </span>
                </div>
              </div>
              {activity.description && (
                <p className="mt-1 text-sm text-on-surface-variant">{activity.description}</p>
              )}
              {canManage && !activity.confirmed && (
                <div className="mt-2 flex gap-2">
                  <button
                    type="button"
                    onClick={() => void onConfirm?.(activity.id)}
                    className="rounded-md bg-primary px-2.5 py-1 text-xs font-medium text-on-primary transition-opacity hover:opacity-90"
                  >
                    Confirm
                  </button>
                  <button
                    type="button"
                    onClick={() => void onDelete?.(activity.id)}
                    className="rounded-md border border-outline-variant px-2.5 py-1 text-xs font-medium text-on-surface transition-colors hover:bg-surface-container"
                  >
                    Delete
                  </button>
                </div>
              )}
            </div>
          </li>
        );
      })}
    </ol>
  );
}
