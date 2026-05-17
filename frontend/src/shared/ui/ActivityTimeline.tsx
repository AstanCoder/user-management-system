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
}

export function ActivityTimeline({ activities, filter }: ActivityTimelineProps) {
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
                <span className="flex-shrink-0 text-xs text-on-surface-variant">
                  {formatRelativeTime(activity.occurredAt)}
                </span>
              </div>
              {activity.description && (
                <p className="mt-1 text-sm text-on-surface-variant">{activity.description}</p>
              )}
            </div>
          </li>
        );
      })}
    </ol>
  );
}
