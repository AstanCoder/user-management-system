export interface ChipProps {
  label: string;
}

export function Chip({ label }: ChipProps) {
  return (
    <span className="inline-flex rounded bg-surface-container px-2 py-0.5 text-xs font-semibold text-secondary">
      {label}
    </span>
  );
}
