import { ButtonHTMLAttributes } from 'react';

type ButtonVariant = 'primary' | 'secondary' | 'ghost' | 'danger';

export interface ButtonProps extends ButtonHTMLAttributes<HTMLButtonElement> {
  variant?: ButtonVariant;
}

const variantClasses: Record<ButtonVariant, string> = {
  primary:
    'bg-primary-container text-on-primary hover:bg-[#0f273f] border border-transparent',
  secondary:
    'bg-surface-container-low text-on-surface border border-outline-variant hover:bg-surface-container',
  ghost: 'bg-transparent text-secondary hover:bg-surface-container-low border border-transparent',
  danger: 'bg-error text-white hover:opacity-90 border border-transparent',
};

export function Button({ variant = 'primary', className = '', children, ...props }: ButtonProps) {
  return (
    <button
      type="button"
      className={`inline-flex items-center justify-center gap-2 rounded px-4 py-2 text-sm font-medium transition-colors disabled:opacity-50 ${variantClasses[variant]} ${className}`}
      {...props}
    >
      {children}
    </button>
  );
}
