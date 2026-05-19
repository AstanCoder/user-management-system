import { NextResponse } from 'next/server';
import type { NextRequest } from 'next/server';
const AUTH_COOKIE_NAME = 'nexus_auth';

const publicPaths = ['/login', '/register', '/forgot-password', '/reset-password', '/accept-invitation'];
const protectedPrefixes = ['/contacts', '/admin'];

export function middleware(request: NextRequest) {
  const { pathname } = request.nextUrl;

  if (publicPaths.some((p) => pathname === p || pathname.startsWith(`${p}/`))) {
    return NextResponse.next();
  }

  const isProtected = protectedPrefixes.some((p) => pathname.startsWith(p));
  if (isProtected && !request.cookies.get(AUTH_COOKIE_NAME)?.value) {
    const loginUrl = new URL('/login', request.url);
    loginUrl.searchParams.set('from', pathname);
    return NextResponse.redirect(loginUrl);
  }

  return NextResponse.next();
}

export const config = {
  matcher: ['/((?!_next/static|_next/image|favicon.ico|logo.png|api).*)'],
};
