'use client';

import Link from 'next/link';
import { FormEvent, useRef, useState } from 'react';
import { useRouter } from 'next/navigation';
import { X, Camera, User } from 'lucide-react';
import { contactDependencies } from '@/contact/infrastructure/config/contactDependencies';
import {
  ContactFormData,
  ContactFormFields,
} from '@/contact/interfaces/ui/ContactFormFields';
import { Button } from '@/shared/ui/Button';

const EMPTY_FORM: ContactFormData = {
  firstName: '',
  lastName: '',
  email: '',
  phone: '',
  company: '',
  jobTitle: '',
  street: '',
  city: '',
  postalCode: '',
  country: '',
  notes: '',
  tags: [],
};

export default function NewContactPage() {
  const router = useRouter();
  const [form, setForm] = useState<ContactFormData>(EMPTY_FORM);
  const [error, setError] = useState<string | null>(null);
  const [submitting, setSubmitting] = useState(false);
  const [avatarFile, setAvatarFile] = useState<File | null>(null);
  const fileRef = useRef<HTMLInputElement>(null);

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setError(null);
    setSubmitting(true);
    try {
      const contact = await contactDependencies.createContactUseCase.execute({
        firstName: form.firstName,
        lastName: form.lastName,
        email: form.email,
        phone: form.phone || null,
        company: form.company || null,
        jobTitle: form.jobTitle || null,
        street: form.street || null,
        city: form.city || null,
        postalCode: form.postalCode || null,
        country: form.country || null,
      });
      const contactId = contact.id;

      if (form.tags.length > 0) {
        await contactDependencies.contactGateway.assignTags(contactId, form.tags);
      }
      if (form.notes.trim()) {
        await contactDependencies.contactGateway.addNote(contactId, form.notes.trim());
      }
      if (avatarFile) {
        await contactDependencies.contactGateway.uploadAvatar(contactId, avatarFile);
      }

      router.push(`/contacts/${contactId}`);
    } catch (err) {
      const message = err instanceof Error ? err.message : 'Failed to create contact';
      setError(message.includes('409') ? 'Email already in use' : message);
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="mx-auto max-w-3xl">
      <div className="mb-6 flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-primary">Add Contact</h1>
          <p className="mt-1 text-sm text-on-surface-variant">Create a new contact in your directory.</p>
        </div>
        <Link
          href="/contacts"
          className="flex h-8 w-8 items-center justify-center rounded-lg text-on-surface-variant hover:bg-surface-container"
          aria-label="Cancel"
        >
          <X className="h-5 w-5" />
        </Link>
      </div>

      <form
        onSubmit={(e) => void handleSubmit(e)}
        className="rounded-xl bg-surface-container-lowest p-6 shadow-sm ring-1 ring-outline-variant"
      >
        <div className="mb-6 flex flex-col items-center gap-3">
          <div className="group relative">
            <div className="flex h-24 w-24 items-center justify-center rounded-full bg-surface-container ring-2 ring-outline-variant">
              {avatarFile ? (
                // eslint-disable-next-line @next/next/no-img-element
                <img
                  src={URL.createObjectURL(avatarFile)}
                  alt="Avatar preview"
                  className="h-full w-full rounded-full object-cover"
                />
              ) : (
                <User className="h-10 w-10 text-on-surface-variant" />
              )}
            </div>
            <button
              type="button"
              onClick={() => fileRef.current?.click()}
              className="absolute bottom-0 right-0 flex h-7 w-7 items-center justify-center rounded-full bg-primary text-on-primary opacity-0 transition-opacity group-hover:opacity-100"
            >
              <Camera className="h-4 w-4" />
            </button>
          </div>
          <input
            ref={fileRef}
            type="file"
            accept="image/jpeg,image/png,image/gif"
            className="hidden"
            onChange={(e) => setAvatarFile(e.target.files?.[0] ?? null)}
          />
          <button
            type="button"
            onClick={() => fileRef.current?.click()}
            className="text-sm font-medium text-primary hover:underline"
          >
            Upload Photo
          </button>
        </div>

        <ContactFormFields form={form} onChange={setForm} showNotes showTags showAddress />

        {error && (
          <p className="mt-4 rounded-lg bg-error-container px-3 py-2 text-sm text-on-error-container">
            {error}
          </p>
        )}

        <div className="mt-6 flex justify-end gap-3 border-t border-outline-variant pt-4">
          <Link href="/contacts">
            <Button variant="secondary" type="button">
              Cancel
            </Button>
          </Link>
          <Button type="submit" disabled={submitting}>
            {submitting ? 'Saving…' : 'Save Contact'}
          </Button>
        </div>
      </form>
    </div>
  );
}
