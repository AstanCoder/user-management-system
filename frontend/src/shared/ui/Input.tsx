import { InputHTMLAttributes, forwardRef } from 'react';

export interface InputProps extends InputHTMLAttributes<HTMLInputElement> {
  label?: string;
  error?: string;
}

export const Input = forwardRef<HTMLInputElement, InputProps>(
  ({ label, error, className = '', id, ...props }, ref) => {
    const inputId = id ?? label?.toLowerCase().replace(/\s+/g, '-');
    return (
      <label className="flex flex-col gap-1 text-sm">
        {label && (
          <span className="font-medium text-on-surface">{label}</span>
        )}
        <input
          ref={ref}
          id={inputId}
          className={`rounded border border-outline-variant bg-surface-container-lowest px-3 py-2 text-on-surface placeholder:text-outline focus:border-secondary focus:outline-none focus:ring-2 focus:ring-secondary/10 ${className}`}
          {...props}
        />
        {error && <span className="text-xs text-error">{error}</span>}
      </label>
    );
  },
);

Input.displayName = 'Input';
