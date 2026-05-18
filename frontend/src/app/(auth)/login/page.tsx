'use client';

import Link from 'next/link';
import { useRouter } from 'next/navigation';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { useForm } from 'react-hook-form';
import { ContactRound } from 'lucide-react';
import { Button } from '@/shared/ui/Button';
import { Input } from '@/shared/ui/Input';
import { identityDependencies } from '@/identity/infrastructure/config/identityDependencies';
import { authQueryKeys } from '@/identity/interfaces/query/authQueryKeys';

interface LoginFormData {
  email: string;
  password: string;
}

export default function LoginPage() {
  const router = useRouter();
  const queryClient = useQueryClient();
  const { register, handleSubmit, formState } = useForm<LoginFormData>({
    defaultValues: { email: '', password: '' },
  });

  const loginMutation = useMutation({
    mutationFn: async (form: LoginFormData) => identityDependencies.loginUseCase.execute(form),
    onSuccess: async () => {
      await queryClient.invalidateQueries({ queryKey: authQueryKeys.session });
      router.replace('/contacts');
    },
  });

  const onSubmit = async (form: LoginFormData) => {
    try {
      await loginMutation.mutateAsync(form);
    } catch {
      // rendered below
    }
  };

  return (
    <main className="w-full max-w-md rounded-xl border border-outline-variant bg-surface-container-lowest p-8 shadow-sm">
      <div className="mb-6 text-center">
        <ContactRound className="mx-auto h-10 w-10 text-primary" />
        <h1 className="mt-2 text-xl font-semibold text-primary">Nexus CRM</h1>
        <p className="text-sm text-on-surface-variant">Secure access to your contact portal</p>
      </div>
      <form onSubmit={(e) => void handleSubmit(onSubmit)(e)} className="flex flex-col gap-4">
        <Input label="Email" type="email" {...register('email', { required: true })} required />
        <Input
          label="Password"
          type="password"
          {...register('password', { required: true })}
          required
        />
        <p className="text-right text-sm">
          <Link href="/forgot-password" className="text-secondary">
            Forgot password?
          </Link>
        </p>
        {loginMutation.error instanceof Error && (
          <p className="text-sm text-error">{loginMutation.error.message}</p>
        )}
        <Button type="submit" disabled={loginMutation.isPending || formState.isSubmitting} className="w-full">
          {loginMutation.isPending ? 'Signing in…' : 'Sign in'}
        </Button>
      </form>
      <p className="mt-4 text-center text-sm text-on-surface-variant">
        No account?{' '}
        <Link href="/register" className="font-medium text-secondary">
          Register
        </Link>
      </p>
    </main>
  );
}
