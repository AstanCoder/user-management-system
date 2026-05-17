'use client';

import Link from 'next/link';
import { FormEvent, useState } from 'react';
import { useRouter } from 'next/navigation';
import { contactDependencies } from '@/contact/infrastructure/config/contactDependencies';
import { Button } from '@/shared/ui/Button';
import { Input } from '@/shared/ui/Input';

export default function NewContactPage() {
  const router = useRouter();
  const [form, setForm] = useState({
    firstName: '',
    lastName: '',
    email: '',
    phone: '',
    company: '',
    jobTitle: '',
  });
  const [error, setError] = useState<string | null>(null);

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setError(null);
    try {
      const contact = await contactDependencies.createContactUseCase.execute({
        ...form,
        phone: form.phone || null,
        company: form.company || null,
        jobTitle: form.jobTitle || null,
      });
      router.push(`/contacts/${contact.id}`);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to create contact');
    }
  };

  return (
    <div className="mx-auto max-w-lg">
      <Link href="/contacts" className="text-sm text-secondary">← Cancel</Link>
      <h1 className="my-4 text-xl font-bold text-primary">Add contact</h1>
      <form onSubmit={(e) => void handleSubmit(e)} className="flex flex-col gap-4">
        <Input label="First name" value={form.firstName} onChange={(e) => setForm({ ...form, firstName: e.target.value })} required />
        <Input label="Last name" value={form.lastName} onChange={(e) => setForm({ ...form, lastName: e.target.value })} required />
        <Input label="Company" value={form.company} onChange={(e) => setForm({ ...form, company: e.target.value })} />
        <Input label="Job title" value={form.jobTitle} onChange={(e) => setForm({ ...form, jobTitle: e.target.value })} />
        <Input label="Email" type="email" value={form.email} onChange={(e) => setForm({ ...form, email: e.target.value })} required />
        <Input label="Phone" value={form.phone} onChange={(e) => setForm({ ...form, phone: e.target.value })} />
        {error && <p className="text-error">{error}</p>}
        <Button type="submit">Save contact</Button>
      </form>
    </div>
  );
}
