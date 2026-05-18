'use client';

import Link from 'next/link';
import { useMutation } from '@tanstack/react-query';
import { useForm } from 'react-hook-form';
import { Button } from '@/shared/ui/Button';
import { Input } from '@/shared/ui/Input';
import { identityDependencies } from '@/identity/infrastructure/config/identityDependencies';

interface ForgotPasswordFormData {
  email: string;
}

export default function ForgotPasswordPage() {
  const { register, handleSubmit, formState } = useForm<ForgotPasswordFormData>({
    defaultValues: { email: '' },
  });

  const forgotMutation = useMutation({
    mutationFn: async (form: ForgotPasswordFormData) =>
      identityDependencies.authGateway.forgotPassword(form.email),
  });

  const submit = async (form: ForgotPasswordFormData) => {
    try {
      await forgotMutation.mutateAsync(form);
    } catch {
      // rendered below
    }
  };

  const sent = forgotMutation.isSuccess;

  return (
    <main className="w-full max-w-md rounded-xl border border-outline-variant bg-surface-container-lowest p-8 shadow-sm">
      <h1 className="font-headline-md text-primary">Reset password</h1>
      <p className="mt-2 text-sm text-on-surface-variant">
        Enter your email and we will send reset instructions if an account exists.
      </p>
      {sent ? (
        <p className="mt-6 text-sm text-secondary">
          If the email is registered, you will receive a reset link shortly.
        </p>
      ) : (
        <form onSubmit={(e) => void handleSubmit(submit)(e)} className="mt-6 flex flex-col gap-4">
          <Input label="Email" type="email" {...register('email', { required: true })} required />
          {forgotMutation.error instanceof Error && (
            <p className="text-sm text-error">{forgotMutation.error.message}</p>
          )}
          <Button type="submit" disabled={forgotMutation.isPending || formState.isSubmitting} className="w-full">
            {forgotMutation.isPending ? 'Sending…' : 'Send reset link'}
          </Button>
        </form>
      )}
      <p className="mt-4 text-center text-sm">
        <Link href="/login" className="text-secondary">
          Back to sign in
        </Link>
      </p>
    </main>
  );
}
