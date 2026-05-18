'use client';

import Link from 'next/link';
import { useEffect, useMemo, useRef, useState } from 'react';
import { useParams, useRouter } from 'next/navigation';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { useForm } from 'react-hook-form';
import { X, Camera, User } from 'lucide-react';
import { ContactId } from '@/contact/domain/valueobject/ContactId';
import { contactDependencies } from '@/contact/infrastructure/config/contactDependencies';
import { contactQueryKeys } from '@/contact/interfaces/query/contactQueryKeys';
import { ContactFormData, ContactFormFields } from '@/contact/interfaces/ui/ContactFormFields';
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

export default function EditContactPage() {
  const params = useParams();
  const router = useRouter();
  const queryClient = useQueryClient();
  const id = params.id as string;
  const [avatarUrl, setAvatarUrl] = useState<string | null>(null);
  const [avatarFile, setAvatarFile] = useState<File | null>(null);
  const [error, setError] = useState<string | null>(null);
  const fileRef = useRef<HTMLInputElement>(null);

  const { register, watch, setValue, reset, formState, handleSubmit } = useForm<ContactFormData>({
    defaultValues: EMPTY_FORM,
  });

  const contactQuery = useQuery({
    queryKey: contactQueryKeys.detail(id),
    enabled: Boolean(id),
    queryFn: async () => contactDependencies.contactGateway.getById(id),
  });

  useEffect(() => {
    const dto = contactQuery.data;
    if (!dto) return;
    reset({
      firstName: dto.firstName,
      lastName: dto.lastName,
      email: dto.email,
      phone: dto.phone ?? '',
      company: dto.company ?? '',
      jobTitle: dto.jobTitle ?? '',
      street: dto.street ?? '',
      city: dto.city ?? '',
      postalCode: dto.postalCode ?? '',
      country: dto.country ?? '',
      notes: '',
      tags: (dto.tags ?? []).map((t) => t.name),
    });
    setAvatarUrl(dto.avatarUrl);
  }, [contactQuery.data, reset]);

  const updateMutation = useMutation({
    mutationFn: async (form: ContactFormData) => {
      await contactDependencies.updateContactUseCase.execute({
        id: ContactId.of(id),
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
      await contactDependencies.contactGateway.assignTags(id, form.tags);
      if (avatarFile) {
        const uploadedUrl = await contactDependencies.contactGateway.uploadAvatar(id, avatarFile);
        setAvatarUrl(uploadedUrl);
      }
    },
    onSuccess: async () => {
      await queryClient.invalidateQueries({ queryKey: contactQueryKeys.all });
      await queryClient.invalidateQueries({ queryKey: contactQueryKeys.detail(id) });
      router.push(`/contacts/${id}`);
    },
  });

  const onSubmit = async (form: ContactFormData) => {
    setError(null);
    try {
      await updateMutation.mutateAsync(form);
    } catch (err) {
      const message = err instanceof Error ? err.message : 'Failed to update contact';
      setError(message.includes('409') ? 'Email already in use' : message);
    }
  };

  const previewSrc = useMemo(
    () => (avatarFile ? URL.createObjectURL(avatarFile) : avatarUrl),
    [avatarFile, avatarUrl],
  );

  if (contactQuery.isPending) return <p className="text-on-surface-variant">Loading…</p>;

  return (
    <div className="mx-auto max-w-3xl">
      <div className="mb-6 flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-primary">Edit Contact</h1>
          <p className="mt-1 text-sm text-on-surface-variant">Update contact information.</p>
        </div>
        <Link
          href={`/contacts/${id}`}
          className="flex h-8 w-8 items-center justify-center rounded-lg text-on-surface-variant hover:bg-surface-container"
          aria-label="Cancel"
        >
          <X className="h-5 w-5" />
        </Link>
      </div>

      <form
        onSubmit={(e) => void handleSubmit(onSubmit)(e)}
        className="rounded-xl bg-surface-container-lowest p-6 shadow-sm ring-1 ring-outline-variant"
      >
        <div className="mb-6 flex flex-col items-center gap-3">
          <div className="group relative">
            <div className="flex h-24 w-24 items-center justify-center overflow-hidden rounded-full bg-surface-container ring-2 ring-outline-variant">
              {previewSrc ? (
                // eslint-disable-next-line @next/next/no-img-element
                <img src={previewSrc} alt="Avatar" className="h-full w-full object-cover" />
              ) : (
                <User className="h-10 w-10 text-on-surface-variant" />
              )}
            </div>
            <button
              type="button"
              onClick={() => fileRef.current?.click()}
              className="absolute bottom-0 right-0 flex h-7 w-7 items-center justify-center rounded-full bg-primary text-on-primary"
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

        <ContactFormFields
          register={register}
          watch={watch}
          setValue={setValue}
          errors={formState.errors}
          showTags
          showAddress
        />

        {error && (
          <p className="mt-4 rounded-lg bg-error-container px-3 py-2 text-sm text-on-error-container">
            {error}
          </p>
        )}

        <div className="mt-6 flex justify-end gap-3 border-t border-outline-variant pt-4">
          <Link href={`/contacts/${id}`}>
            <Button variant="secondary" type="button">
              Cancel
            </Button>
          </Link>
          <Button type="submit" disabled={updateMutation.isPending}>
            {updateMutation.isPending ? 'Saving…' : 'Save Changes'}
          </Button>
        </div>
      </form>
    </div>
  );
}
