'use client';

import { useEffect, useMemo, useState } from 'react';
import { useForm } from 'react-hook-form';
import { ContactViewModel } from '../../application/command/ContactViewModel';
import { InvalidContactDataError } from '../../domain/exception/InvalidContactDataError';
import { Email } from '../../domain/valueobject/Email';

interface ContactModalFormData {
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
}

export interface ContactFormProps {
  initial?: ContactViewModel | null;
  onSubmit: (data: {
    firstName: string;
    lastName: string;
    email: string;
    phone: string;
  }) => Promise<void>;
  onCancel: () => void;
}

export function ContactForm({ initial, onSubmit, onCancel }: ContactFormProps) {
  const [error, setError] = useState<string | null>(null);

  const defaults = useMemo<ContactModalFormData>(
    () => ({
      firstName: initial?.firstName ?? '',
      lastName: initial?.lastName ?? '',
      email: initial?.email ?? '',
      phone: initial?.phone ?? '',
    }),
    [initial],
  );

  const { register, reset, formState, handleSubmit } = useForm<ContactModalFormData>({
    defaultValues: defaults,
  });

  useEffect(() => {
    reset(defaults);
    setError(null);
  }, [defaults, reset]);

  const submit = async (data: ContactModalFormData) => {
    setError(null);
    try {
      Email.create(data.email);
      await onSubmit({
        firstName: data.firstName.trim(),
        lastName: data.lastName.trim(),
        email: data.email.trim(),
        phone: data.phone.trim(),
      });
    } catch (err) {
      setError(err instanceof InvalidContactDataError ? err.message : 'Failed to save contact');
    }
  };

  return (
    <section className="modal-backdrop" role="presentation" onClick={onCancel}>
      <section
        className="modal"
        role="dialog"
        aria-modal="true"
        aria-labelledby="contact-form-title"
        onClick={(e) => e.stopPropagation()}
      >
        <h2 id="contact-form-title">{initial ? 'Edit contact' : 'Add contact'}</h2>
        <form onSubmit={(e) => void handleSubmit(submit)(e)} className="contact-form">
          <label>
            First name
            <input {...register('firstName', { required: true })} required />
          </label>
          <label>
            Last name
            <input {...register('lastName', { required: true })} required />
          </label>
          <label>
            Email
            <input type="email" {...register('email', { required: true })} required />
          </label>
          <label>
            Phone (optional)
            <input {...register('phone')} />
          </label>
          {error && <p className="form-error">{error}</p>}
          <footer className="form-actions">
            <button type="button" className="btn btn-secondary" onClick={onCancel}>
              Cancel
            </button>
            <button type="submit" className="btn btn-primary" disabled={formState.isSubmitting}>
              {formState.isSubmitting ? 'Saving…' : 'Save'}
            </button>
          </footer>
        </form>
      </section>
    </section>
  );
}
