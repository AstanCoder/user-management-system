'use client';

import { FormEvent, useEffect, useState } from 'react';
import { ContactViewModel } from '../../application/command/ContactViewModel';
import { InvalidContactDataError } from '../../domain/exception/InvalidContactDataError';
import { Email } from '../../domain/valueobject/Email';

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

/**
 * Modal form for creating or editing a contact.
 */
export function ContactForm({ initial, onSubmit, onCancel }: ContactFormProps) {
  const [firstName, setFirstName] = useState(initial?.firstName ?? '');
  const [lastName, setLastName] = useState(initial?.lastName ?? '');
  const [email, setEmail] = useState(initial?.email ?? '');
  const [phone, setPhone] = useState(initial?.phone ?? '');
  const [error, setError] = useState<string | null>(null);
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    setFirstName(initial?.firstName ?? '');
    setLastName(initial?.lastName ?? '');
    setEmail(initial?.email ?? '');
    setPhone(initial?.phone ?? '');
    setError(null);
  }, [initial]);

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setError(null);
    try {
      Email.create(email);
      setSubmitting(true);
      await onSubmit({
        firstName: firstName.trim(),
        lastName: lastName.trim(),
        email: email.trim(),
        phone: phone.trim(),
      });
    } catch (err) {
      setError(err instanceof InvalidContactDataError ? err.message : 'Failed to save contact');
    } finally {
      setSubmitting(false);
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
        <form onSubmit={handleSubmit} className="contact-form">
          <label>
            First name
            <input value={firstName} onChange={(e) => setFirstName(e.target.value)} required />
          </label>
          <label>
            Last name
            <input value={lastName} onChange={(e) => setLastName(e.target.value)} required />
          </label>
          <label>
            Email
            <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} required />
          </label>
          <label>
            Phone (optional)
            <input value={phone} onChange={(e) => setPhone(e.target.value)} />
          </label>
          {error && <p className="form-error">{error}</p>}
          <footer className="form-actions">
            <button type="button" className="btn btn-secondary" onClick={onCancel}>
              Cancel
            </button>
            <button type="submit" className="btn btn-primary" disabled={submitting}>
              {submitting ? 'Saving…' : 'Save'}
            </button>
          </footer>
        </form>
      </section>
    </section>
  );
}
