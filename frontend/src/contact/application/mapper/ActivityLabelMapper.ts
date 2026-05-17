export interface ActivityMeta {
  title: string;
  iconName: 'phone' | 'mail' | 'file-text' | 'calendar' | 'edit' | 'activity';
}

const ACTIVITY_MAP: Record<string, ActivityMeta> = {
  CALL: { title: 'Call', iconName: 'phone' },
  EMAIL: { title: 'Email Sent', iconName: 'mail' },
  NOTE: { title: 'Note Added', iconName: 'file-text' },
  MEETING: { title: 'Meeting Scheduled', iconName: 'calendar' },
  EDIT: { title: 'Contact Updated', iconName: 'edit' },
};

export function getActivityMeta(activityType: string): ActivityMeta {
  return ACTIVITY_MAP[activityType.toUpperCase()] ?? { title: activityType, iconName: 'activity' };
}

export function formatActivityTitle(activityType: string): string {
  return getActivityMeta(activityType).title;
}
