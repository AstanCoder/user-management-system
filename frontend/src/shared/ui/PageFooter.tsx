export function PageFooter() {
  return (
    <footer className="border-t border-outline-variant bg-surface-container-lowest px-6 py-4">
      <div className="mx-auto flex max-w-app flex-col items-center justify-between gap-2 sm:flex-row">
        <span className="text-xs text-on-surface-variant">
          © 2024 Nexus Contact Management. Data-driven clarity.
        </span>
        <nav className="flex gap-4">
          {(['Terms', 'Privacy', 'Support'] as const).map((label) => (
            <a
              key={label}
              href="#"
              className="text-xs text-on-surface-variant transition-colors hover:text-on-surface"
            >
              {label}
            </a>
          ))}
        </nav>
      </div>
    </footer>
  );
}
