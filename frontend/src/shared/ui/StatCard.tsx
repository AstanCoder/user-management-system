import type { LucideIcon } from 'lucide-react';

interface StatCardProps {
  label: string;
  value: string | number;
  subtitle?: string;
  icon?: LucideIcon;
}

export function StatCard({ label, value, subtitle, icon: Icon }: StatCardProps) {
  return (
    <div className="rounded-xl bg-surface-container-lowest p-5 shadow-sm ring-1 ring-outline-variant">
      <div className="flex items-start justify-between">
        <div>
          <p className="text-sm font-medium text-on-surface-variant">{label}</p>
          <p className="mt-1 text-3xl font-bold tracking-tight text-on-surface">{value}</p>
          {subtitle && <p className="mt-1 text-xs text-on-surface-variant">{subtitle}</p>}
        </div>
        {Icon && (
          <div className="rounded-lg bg-primary-container p-2">
            <Icon className="h-5 w-5 text-on-primary-container" />
          </div>
        )}
      </div>
    </div>
  );
}
