export interface BadgeProps {
  label: string;
  tone?: 'default' | 'success' | 'warning' | 'muted';
}

const toneClasses = {
  default: 'bg-secondary-container text-on-secondary-container',
  success: 'bg-emerald-100 text-emerald-800',
  warning: 'bg-amber-100 text-amber-800',
  muted: 'bg-surface-container text-on-surface-variant',
};

export function Badge({ label, tone = 'default' }: BadgeProps) {
  return (
    <span
      className={`inline-flex rounded-full px-2 py-0.5 text-xs font-semibold ${toneClasses[tone]}`}
    >
      {label}
    </span>
  );
}
