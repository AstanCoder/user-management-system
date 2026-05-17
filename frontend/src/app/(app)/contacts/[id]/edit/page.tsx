'use client';

import Link from 'next/link';
import { FormEvent, useCallback, useEffect, useState } from 'react';
import { useParams, useRouter } from 'next/navigation';
import { ContactId } from '@/contact/domain/valueobject/ContactId';
import { contactDependencies } from '@/contact/infrastructure/config/contactDependencies';
import { Button } from '@/shared/ui/Button';
import { Input } from '@/shared/ui/Input';

export default function EditContactPage() {
  const params = useParams();
  const router = useRouter();
  const id = params.id as string;
  const [form, setForm] = useState({
    firstName: '',
    lastName: '',
    email: '',
    phone: '',
    company: '',
    jobTitle: '',
  });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const load = useCallback(async () => {
    setLoading(true);
    try {
      const dto = await contactDependencies.contactGateway.getById(id);
      setForm({
        firstName: dto.firstName,
        lastName: dto.lastName,
        email: dto.email,
        phone: dto.phone ?? '',
        company: dto.company ?? '',
        jobTitle: dto.jobTitle ?? '',
      });
    } finally {
      setLoading(false);
    }
  }, [id]);

  useEffect(() => {
    void load();
  }, [load]);

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setError(null);
    try {
      await contactDependencies.updateContactUseCase.execute({
        id: ContactId.of(id),
        firstName: form.firstName,
        lastName: form.lastName,
        email: form.email,
        phone: form.phone || null,
        company: form.company || null,
        jobTitle: form.jobTitle || null,
      });
      router.push(`/contacts/${id}`);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to update contact');
    }
  };

  if (loading) return <p>Loading…</p>;

  return (
    <div className="mx-auto max-w-lg">
      <Link href={`/contacts/${id}`} className="text-sm text-secondary">
        ← Cancel
      </Link>
      <h1 className="my-4 text-xl font-bold text-primary">Edit contact</h1>
      <form onSubmit={(e) => void handleSubmit(e)} className="flex flex-col gap-4">
        <Input label="First name" value={form.firstName} onChange={(e) => setForm({ ...form, firstName: e.target.value })} required />
        <Input label="Last name" value={form.lastName} onChange={(e) => setForm({ ...form, lastName: e.target.value })} required />
        <Input label="Company" value={form.company} onChange={(e) => setForm({ ...form, company: e.target.value })} />
        <Input label="Job title" value={form.jobTitle} onChange={(e) => setForm({ ...form, jobTitle: e.target.value })} />
        <Input label="Email" type="email" value={form.email} onChange={(e) => setForm({ ...form, email: e.target.value })} required />
        <Input label="Phone" value={form.phone} onChange={(e) => setForm({ ...form, phone: e.target.value })} />
        {error && <p className="text-error">{error}</p>}
        <Button type="submit">Save changes</Button>
      </form>
    </div>
  );
}
