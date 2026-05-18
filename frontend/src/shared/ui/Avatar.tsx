import Image from 'next/image';

export interface AvatarProps {
  name?: string | null;
  src?: string | null;
  size?: 'sm' | 'md' | 'lg' | 'xl';
}

function normalizeName(name?: string | null): string {
  if (typeof name !== 'string') {
    return '';
  }
  return name.trim();
}

function initials(name?: string | null): string {
  const normalizedName = normalizeName(name);
  const parts = normalizedName.split(/\s+/).filter(Boolean);
  if (parts.length === 0) {
    return '?';
  }
  if (parts.length === 1) {
    return parts[0].slice(0, 2).toUpperCase();
  }
  return `${parts[0][0]}${parts[parts.length - 1][0]}`.toUpperCase();
}

const sizeClasses = {
  sm: 'h-8 w-8 text-xs',
  md: 'h-10 w-10 text-sm',
  lg: 'h-16 w-16 text-lg',
  xl: 'h-32 w-32 text-3xl',
};

const sizePixels = {
  sm: 32,
  md: 40,
  lg: 64,
  xl: 128,
};

export function Avatar({ name, src, size = 'md' }: AvatarProps) {
  const normalizedName = normalizeName(name);
  const alt = normalizedName || 'Avatar';

  if (src) {
    return (
      <Image
        src={src}
        alt={alt}
        width={sizePixels[size]}
        height={sizePixels[size]}
        unoptimized
        className={`${sizeClasses[size]} shrink-0 rounded-full object-cover border border-outline-variant`}
      />
    );
  }
  return (
    <span
      className={`${sizeClasses[size]} inline-flex shrink-0 items-center justify-center rounded-full bg-surface-tint font-medium text-on-primary`}
      aria-hidden
    >
      {initials(name)}
    </span>
  );
}
