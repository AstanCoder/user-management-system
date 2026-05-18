'use client';

import Link from 'next/link';
import { Suspense } from 'react';
import { useRouter, useSearchParams } from 'next/navigation';
import { useMutation } from '@tanstack/react-query';
import { useForm } from 'react-hook-form';
import { Button } from '@/shared/ui/Button';
import { Input } from '@/shared/ui/Input';
import { identityDependencies } from '@/identity/infrastructure/config/identityDependencies';

interface ResetPasswordFormData {
  password: string;
}

function ResetPasswordForm() {
  const router = useRouter();
  const searchParams = useSearchParams();
  const token = searchParams.get('token') ?? '';
  const { register, handleSubmit, formState } = useForm<ResetPasswordFormData>({
    defaultValues: { password: '' },
  });

  const resetMutation = useMutation({
    mutationFn: async (form: ResetPasswordFormData) =>
      identityDependencies.authGateway.resetPassword(token, form.password),
    onSuccess: () => {
      router.replace('/login');
    },
  });

  const submit = async (data: ResetPasswordFormData) => {
    if (!token) {
      return;
    }
    try {
      await resetMutation.mutateAsync(data);
    } catch {
      // rendered below
    }
  };

  return (
    <main className="w-full max-w-md rounded-xl border border-outline-variant bg-surface-container-lowest p-8 shadow-sm">
      <h1 className="font-headline-md text-primary">Choose a new password</h1>
      <form onSubmit={(e) => void handleSubmit(submit)(e)} className="mt-6 flex flex-col gap-4">
        <Input
          label="New password"
          type="password"
          {...register('password', { required: true, minLength: 8 })}
          required
          minLength={8}
        />
        {!token && <p className="text-sm text-error">Missing reset token</p>}
        {resetMutation.error instanceof Error && (
          <p className="text-sm text-error">{resetMutation.error.message}</p>
        )}
        <Button
          type="submit"
          disabled={resetMutation.isPending || formState.isSubmitting || !token}
          className="w-full"
        >
          {resetMutation.isPending ? 'Saving…' : 'Update password'}
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

export default function ResetPasswordPage() {
  return (
    <Suspense fallback={<p className="p-8">Loading…</p>}>
      <ResetPasswordForm />
    </Suspense>
  );
}
