'use client';

import { Building2, Briefcase, Mail, Phone, MapPin } from 'lucide-react';
import {
  FieldErrors,
  UseFormRegister,
  UseFormSetValue,
  UseFormWatch,
} from 'react-hook-form';
import { TagEditor } from '@/shared/ui/TagEditor';
import { Input } from '@/shared/ui/Input';

export interface ContactFormData {
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  company: string;
  jobTitle: string;
  street: string;
  city: string;
  postalCode: string;
  country: string;
  notes: string;
  tags: string[];
}

interface ContactFormFieldsProps {
  register: UseFormRegister<ContactFormData>;
  watch: UseFormWatch<ContactFormData>;
  setValue: UseFormSetValue<ContactFormData>;
  errors: FieldErrors<ContactFormData>;
  showNotes?: boolean;
  showAddress?: boolean;
  showTags?: boolean;
}

function IconInput({
  icon: Icon,
  label,
  error,
  ...props
}: React.ComponentProps<'input'> & {
  icon: React.ComponentType<{ className?: string }>;
  label: string;
  error?: string;
}) {
  return (
    <div>
      <label className="mb-1.5 block text-sm font-medium text-on-surface">{label}</label>
      <div className="relative">
        <Icon className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-on-surface-variant" />
        <input
          {...props}
          className="w-full rounded-lg border border-outline-variant bg-surface-container-low py-2.5 pl-9 pr-3 text-sm text-on-surface focus:outline-none focus:ring-2 focus:ring-primary"
        />
      </div>
      {error && <p className="mt-1 text-xs text-error">{error}</p>}
    </div>
  );
}

export function ContactFormFields({
  register,
  watch,
  setValue,
  errors,
  showNotes = false,
  showAddress = true,
  showTags = true,
}: ContactFormFieldsProps) {
  const tags = watch('tags') ?? [];

  return (
    <div className="space-y-5">
      <div className="grid gap-4 sm:grid-cols-2">
        <Input
          label="First Name"
          {...register('firstName', { required: 'First name is required' })}
          error={errors.firstName?.message}
        />
        <Input
          label="Last Name"
          {...register('lastName', { required: 'Last name is required' })}
          error={errors.lastName?.message}
        />
      </div>

      <IconInput icon={Building2} label="Company" {...register('company')} error={errors.company?.message} />
      <IconInput icon={Briefcase} label="Position" {...register('jobTitle')} error={errors.jobTitle?.message} />
      <IconInput
        icon={Mail}
        label="Email"
        type="email"
        {...register('email', { required: 'Email is required' })}
        error={errors.email?.message}
      />
      <IconInput icon={Phone} label="Phone" type="tel" {...register('phone')} error={errors.phone?.message} />

      {showTags && (
        <div>
          <label className="mb-1.5 block text-sm font-medium text-on-surface">Tags</label>
          <TagEditor
            tags={tags}
            onChange={(nextTags) => setValue('tags', nextTags, { shouldDirty: true })}
          />
        </div>
      )}

      {showNotes && (
        <div>
          <label htmlFor="notes" className="mb-1.5 block text-sm font-medium text-on-surface">
            Notes
          </label>
          <textarea
            id="notes"
            {...register('notes')}
            rows={4}
            placeholder="Add initial notes about this contact…"
            className="w-full resize-y rounded-lg border border-outline-variant bg-surface-container-low px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-primary"
          />
        </div>
      )}

      {showAddress && (
        <fieldset>
          <legend className="mb-3 flex items-center gap-1.5 text-sm font-medium text-on-surface">
            <MapPin className="h-4 w-4" />
            Address
          </legend>
          <div className="space-y-3">
            <Input label="Street" {...register('street')} error={errors.street?.message} />
            <div className="grid gap-3 sm:grid-cols-2">
              <Input label="City" {...register('city')} error={errors.city?.message} />
              <Input
                label="Postal Code"
                {...register('postalCode')}
                error={errors.postalCode?.message}
              />
            </div>
            <Input label="Country" {...register('country')} error={errors.country?.message} />
          </div>
        </fieldset>
      )}
    </div>
  );
}
