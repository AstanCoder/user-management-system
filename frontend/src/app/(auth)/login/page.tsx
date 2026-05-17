'use client';

import Link from 'next/link';
import { FormEvent, useState } from 'react';
import { useRouter } from 'next/navigation';
import { Button } from '@/shared/ui/Button';
import { Input } from '@/shared/ui/Input';
import { identityDependencies } from '@/identity/infrastructure/config/identityDependencies';

export default function LoginPage() {
  const router = useRouter();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setError(null);
    setLoading(true);
    try {
      await identityDependencies.loginUseCase.execute({ email, password });
      router.replace('/contacts');
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Login failed');
    } finally {
      setLoading(false);
    }
  };

  return (
    <main className="w-full max-w-md rounded-xl border border-outline-variant bg-surface-container-lowest p-8 shadow-sm">
      <div className="mb-6 text-center">
        <span className="material-symbols-outlined text-4xl text-primary">contacts</span>
        <h1 className="mt-2 text-xl font-semibold text-primary">Nexus CRM</h1>
        <p className="text-sm text-on-surface-variant">Secure access to your contact portal</p>
      </div>
      <form onSubmit={(e) => void handleSubmit(e)} className="flex flex-col gap-4">
        <Input label="Email" type="email" value={email} onChange={(e) => setEmail(e.target.value)} required />
        <Input label="Password" type="password" value={password} onChange={(e) => setPassword(e.target.value)} required />
        <p className="text-right text-sm">
          <Link href="/forgot-password" className="text-secondary">
            Forgot password?
          </Link>
        </p>
        {error && <p className="text-sm text-error">{error}</p>}
        <Button type="submit" disabled={loading} className="w-full">
          {loading ? 'Signing in…' : 'Sign in'}
        </Button>
      </form>
      <p className="mt-4 text-center text-sm text-on-surface-variant">
        No account? <Link href="/register" className="font-medium text-secondary">Register</Link>
      </p>
    </main>
  );
}
