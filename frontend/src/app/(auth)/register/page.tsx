'use client';

import Link from 'next/link';
import { useRouter } from 'next/navigation';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { useForm } from 'react-hook-form';
import { Button } from '@/shared/ui/Button';
import { Input } from '@/shared/ui/Input';
import { identityDependencies } from '@/identity/infrastructure/config/identityDependencies';
import { authQueryKeys } from '@/identity/interfaces/query/authQueryKeys';

interface RegisterFormData {
  firstName: string;
  lastName: string;
  email: string;
  password: string;
}

export default function RegisterPage() {
  const router = useRouter();
  const queryClient = useQueryClient();
  const { register, handleSubmit, formState } = useForm<RegisterFormData>({
    defaultValues: {
      firstName: '',
      lastName: '',
      email: '',
      password: '',
    },
  });

  const registerMutation = useMutation({
    mutationFn: async (form: RegisterFormData) => identityDependencies.registerUseCase.execute(form),
    onSuccess: async () => {
      await queryClient.invalidateQueries({ queryKey: authQueryKeys.session });
      router.replace('/contacts');
    },
  });

  const onSubmit = async (form: RegisterFormData) => {
    try {
      await registerMutation.mutateAsync(form);
    } catch {
      // rendered below
    }
  };

  return (
    <main className="w-full max-w-md rounded-xl border border-outline-variant bg-surface-container-lowest p-8 shadow-sm">
      <h1 className="mb-6 text-xl font-semibold text-primary">Create account</h1>
      <form onSubmit={(e) => void handleSubmit(onSubmit)(e)} className="flex flex-col gap-4">
        <Input label="First name" {...register('firstName', { required: true })} required />
        <Input label="Last name" {...register('lastName', { required: true })} required />
        <Input label="Email" type="email" {...register('email', { required: true })} required />
        <Input
          label="Password"
          type="password"
          {...register('password', { required: true, minLength: 8 })}
          required
        />
        {registerMutation.error instanceof Error && (
          <p className="text-sm text-error">{registerMutation.error.message}</p>
        )}
        <Button type="submit" disabled={registerMutation.isPending || formState.isSubmitting} className="w-full">
          {registerMutation.isPending ? 'Creating…' : 'Register'}
        </Button>
      </form>
      <p className="mt-4 text-center text-sm">
        <Link href="/login" className="text-secondary">
          Back to login
        </Link>
      </p>
    </main>
  );
}
