import { ReactNode } from 'react';

export interface CardProps {
  children: ReactNode;
  className?: string;
  onClick?: () => void;
}

export function Card({ children, className = '', onClick }: CardProps) {
  const interactive = onClick ? 'cursor-pointer hover:bg-surface-container-low transition-colors' : '';
  return (
    <article
      className={`rounded-lg border border-outline-variant bg-surface-container-lowest p-4 shadow-sm ${interactive} ${className}`}
      onClick={onClick}
      onKeyDown={onClick ? (e) => e.key === 'Enter' && onClick() : undefined}
      role={onClick ? 'button' : undefined}
      tabIndex={onClick ? 0 : undefined}
    >
      {children}
    </article>
  );
}
