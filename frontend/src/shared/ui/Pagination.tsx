'use client';

import { ChevronLeft, ChevronRight } from 'lucide-react';

interface PaginationProps {
  page: number;
  totalPages: number;
  totalElements: number;
  pageSize: number;
  onPageChange: (page: number) => void;
}

export function Pagination({ page, totalPages, totalElements, pageSize, onPageChange }: PaginationProps) {
  const from = totalElements === 0 ? 0 : page * pageSize + 1;
  const to = Math.min((page + 1) * pageSize, totalElements);

  const pages = buildPageRange(page, totalPages);

  return (
    <div className="flex flex-col items-center justify-between gap-3 sm:flex-row">
      <span className="text-sm text-on-surface-variant">
        Showing {from} to {to} of {totalElements}
      </span>
      <div className="flex items-center gap-1">
        <button
          onClick={() => onPageChange(page - 1)}
          disabled={page === 0}
          aria-label="Previous page"
          className="flex h-8 w-8 items-center justify-center rounded-lg border border-outline-variant text-on-surface-variant transition-colors hover:bg-surface-container disabled:cursor-not-allowed disabled:opacity-40"
        >
          <ChevronLeft className="h-4 w-4" />
        </button>

        {pages.map((entry, idx) =>
          entry === '...' ? (
            <span key={`ellipsis-${idx}`} className="px-1 text-sm text-on-surface-variant">
              …
            </span>
          ) : (
            <button
              key={entry}
              onClick={() => onPageChange(Number(entry) - 1)}
              aria-current={page + 1 === entry ? 'page' : undefined}
              className={`flex h-8 w-8 items-center justify-center rounded-lg text-sm transition-colors ${
                page + 1 === entry
                  ? 'bg-primary text-on-primary'
                  : 'border border-outline-variant text-on-surface hover:bg-surface-container'
              }`}
            >
              {entry}
            </button>
          ),
        )}

        <button
          onClick={() => onPageChange(page + 1)}
          disabled={page >= totalPages - 1}
          aria-label="Next page"
          className="flex h-8 w-8 items-center justify-center rounded-lg border border-outline-variant text-on-surface-variant transition-colors hover:bg-surface-container disabled:cursor-not-allowed disabled:opacity-40"
        >
          <ChevronRight className="h-4 w-4" />
        </button>
      </div>
    </div>
  );
}

function buildPageRange(current: number, total: number): (number | '...')[] {
  if (total <= 7) return Array.from({ length: total }, (_, i) => i + 1);

  const c = current + 1;
  const result: (number | '...')[] = [];

  if (c <= 4) {
    result.push(1, 2, 3, 4, 5, '...', total);
  } else if (c >= total - 3) {
    result.push(1, '...', total - 4, total - 3, total - 2, total - 1, total);
  } else {
    result.push(1, '...', c - 1, c, c + 1, '...', total);
  }

  return result;
}
