import type { Metadata } from 'next';
import './globals.css';

export const metadata: Metadata = {
  title: 'Contact List',
  description: 'Manage your contacts',
};

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="en">
      <body>{children}</body>
    </html>
  );
}
