'use client';

import { Building2, Briefcase, Mail, Phone, MapPin } from 'lucide-react';
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
  form: ContactFormData;
  onChange: (form: ContactFormData) => void;
  showNotes?: boolean;
  showAddress?: boolean;
  showTags?: boolean;
}

function IconInput({
  icon: Icon,
  label,
  ...props
}: React.ComponentProps<'input'> & { icon: React.ComponentType<{ className?: string }>; label: string }) {
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
    </div>
  );
}

export function ContactFormFields({
  form,
  onChange,
  showNotes = false,
  showAddress = true,
  showTags = true,
}: ContactFormFieldsProps) {
  const set = (patch: Partial<ContactFormData>) => onChange({ ...form, ...patch });

  return (
    <div className="space-y-5">
      <div className="grid gap-4 sm:grid-cols-2">
        <Input
          label="First Name"
          value={form.firstName}
          onChange={(e) => set({ firstName: e.target.value })}
          required
        />
        <Input
          label="Last Name"
          value={form.lastName}
          onChange={(e) => set({ lastName: e.target.value })}
          required
        />
      </div>

      <IconInput
        icon={Building2}
        label="Company"
        value={form.company}
        onChange={(e) => set({ company: e.target.value })}
      />
      <IconInput
        icon={Briefcase}
        label="Position"
        value={form.jobTitle}
        onChange={(e) => set({ jobTitle: e.target.value })}
      />
      <IconInput
        icon={Mail}
        label="Email"
        type="email"
        value={form.email}
        onChange={(e) => set({ email: e.target.value })}
        required
      />
      <IconInput
        icon={Phone}
        label="Phone"
        type="tel"
        value={form.phone}
        onChange={(e) => set({ phone: e.target.value })}
      />

      {showTags && (
        <div>
          <label className="mb-1.5 block text-sm font-medium text-on-surface">Tags</label>
          <TagEditor tags={form.tags} onChange={(tags) => set({ tags })} />
        </div>
      )}

      {showNotes && (
        <div>
          <label htmlFor="notes" className="mb-1.5 block text-sm font-medium text-on-surface">
            Notes
          </label>
          <textarea
            id="notes"
            value={form.notes}
            onChange={(e) => set({ notes: e.target.value })}
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
            <Input
              label="Street"
              value={form.street}
              onChange={(e) => set({ street: e.target.value })}
            />
            <div className="grid gap-3 sm:grid-cols-2">
              <Input label="City" value={form.city} onChange={(e) => set({ city: e.target.value })} />
              <Input
                label="Postal Code"
                value={form.postalCode}
                onChange={(e) => set({ postalCode: e.target.value })}
              />
            </div>
            <Input
              label="Country"
              value={form.country}
              onChange={(e) => set({ country: e.target.value })}
            />
          </div>
        </fieldset>
      )}
    </div>
  );
}
