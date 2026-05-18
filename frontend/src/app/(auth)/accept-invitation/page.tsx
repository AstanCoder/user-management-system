'use client';

import Link from 'next/link';
import { Suspense } from 'react';
import { useRouter, useSearchParams } from 'next/navigation';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { useForm } from 'react-hook-form';
import { authQueryKeys } from '@/identity/interfaces/query/authQueryKeys';
import { identityDependencies } from '@/identity/infrastructure/config/identityDependencies';
import { Button } from '@/shared/ui/Button';
import { Input } from '@/shared/ui/Input';

interface AcceptInvitationFormData {
  password: string;
  confirmPassword: string;
}

function AcceptInvitationForm() {
  const router = useRouter();
  const queryClient = useQueryClient();
  const searchParams = useSearchParams();
  const token = searchParams.get('token') ?? '';
  const { register, handleSubmit, setError, formState } = useForm<AcceptInvitationFormData>({
    defaultValues: {
      password: '',
      confirmPassword: '',
    },
  });

  const completeMutation = useMutation({
    mutationFn: async (form: AcceptInvitationFormData) =>
      identityDependencies.completeInvitationUseCase.execute(token, form.password),
    onSuccess: async () => {
      await queryClient.invalidateQueries({ queryKey: authQueryKeys.session });
      router.replace('/contacts');
    },
  });

  const submit = async (data: AcceptInvitationFormData) => {
    if (!token) {
      return;
    }
    if (data.password !== data.confirmPassword) {
      setError('confirmPassword', { message: 'Passwords do not match' });
      return;
    }
    try {
      await completeMutation.mutateAsync(data);
    } catch {
      // rendered below
    }
  };

  return (
    <main className="w-full max-w-md rounded-xl border border-outline-variant bg-surface-container-lowest p-8 shadow-sm">
      <h1 className="font-headline-md text-primary">Complete your invitation</h1>
      <p className="mt-2 text-sm text-on-surface-variant">
        Choose your password to activate your account.
      </p>
      <form onSubmit={(e) => void handleSubmit(submit)(e)} className="mt-6 flex flex-col gap-4">
        <Input
          label="Password"
          type="password"
          {...register('password', { required: true, minLength: 8 })}
          required
          minLength={8}
        />
        <Input
          label="Confirm password"
          type="password"
          {...register('confirmPassword', { required: true, minLength: 8 })}
          required
          minLength={8}
          error={formState.errors.confirmPassword?.message}
        />
        {!token && <p className="text-sm text-error">Missing invitation token</p>}
        {completeMutation.error instanceof Error && (
          <p className="text-sm text-error">{completeMutation.error.message}</p>
        )}
        <Button
          type="submit"
          disabled={completeMutation.isPending || formState.isSubmitting || !token}
          className="w-full"
        >
          {completeMutation.isPending ? 'Completing…' : 'Complete registration'}
        </Button>
      </form>
      <p className="mt-4 text-center text-sm">
        <Link href="/login" className="text-secondary">
          Back to sign in
        </Link>
      </p>
    </main>
  );
}

export default function AcceptInvitationPage() {
  return (
    <Suspense fallback={<p className="p-8">Loading…</p>}>
      <AcceptInvitationForm />
    </Suspense>
  );
}
