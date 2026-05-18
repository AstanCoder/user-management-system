'use client';

import { CSSProperties, useCallback, useEffect, useRef, useState } from 'react';
import { createPortal } from 'react-dom';
import { MoreVertical } from 'lucide-react';
import type { LucideIcon } from 'lucide-react';

export interface DropdownAction {
  label: string;
  icon?: LucideIcon;
  onClick: () => void;
  destructive?: boolean;
  disabled?: boolean;
}

interface DropdownMenuProps {
  actions: DropdownAction[];
  align?: 'left' | 'right';
}

export function DropdownMenu({ actions, align = 'right' }: DropdownMenuProps) {
  const [open, setOpen] = useState(false);
  const ref = useRef<HTMLDivElement>(null);
  const menuRef = useRef<HTMLDivElement>(null);
  const triggerRef = useRef<HTMLButtonElement>(null);
  const [menuStyle, setMenuStyle] = useState<CSSProperties>({ top: 0, left: 0 });

  const updateMenuPosition = useCallback(() => {
    const trigger = triggerRef.current;
    if (!trigger) return;

    const rect = trigger.getBoundingClientRect();
    const menuWidth = 176;
    const viewportPadding = 8;
    const preferredLeft = align === 'right' ? rect.right - menuWidth : rect.left;
    const left = Math.min(
      Math.max(preferredLeft, viewportPadding),
      window.innerWidth - menuWidth - viewportPadding
    );
    const top = Math.min(rect.bottom + 4, window.innerHeight - viewportPadding);

    setMenuStyle({ top, left });
  }, [align]);

  useEffect(() => {
    function handleClick(e: MouseEvent) {
      const target = e.target as Node;
      if (ref.current?.contains(target) || menuRef.current?.contains(target)) return;
      setOpen(false);
    }

    document.addEventListener('mousedown', handleClick);
    return () => document.removeEventListener('mousedown', handleClick);
  }, []);

  useEffect(() => {
    if (!open) return;

    updateMenuPosition();
    const handleViewportChange = () => updateMenuPosition();

    window.addEventListener('resize', handleViewportChange);
    window.addEventListener('scroll', handleViewportChange, true);
    return () => {
      window.removeEventListener('resize', handleViewportChange);
      window.removeEventListener('scroll', handleViewportChange, true);
    };
  }, [open, updateMenuPosition]);

  return (
    <div ref={ref} className="relative">
      <button
        ref={triggerRef}
        onClick={(e) => {
          e.stopPropagation();
          setOpen((v) => !v);
        }}
        className="flex h-8 w-8 items-center justify-center rounded-lg text-on-surface-variant transition-colors hover:bg-surface-container"
        aria-label="More options"
      >
        <MoreVertical className="h-4 w-4" />
      </button>

      {open &&
        typeof document !== 'undefined' &&
        createPortal(
          <div
            ref={menuRef}
            style={menuStyle}
            className="fixed z-50 w-44 rounded-xl border border-outline-variant bg-surface-container-lowest py-1 shadow-lg"
          >
            {actions.map((action) => {
              const Icon = action.icon;
              return (
                <button
                  key={action.label}
                  disabled={action.disabled}
                  onClick={(e) => {
                    e.stopPropagation();
                    setOpen(false);
                    action.onClick();
                  }}
                  className={`flex w-full items-center gap-2 px-3 py-2 text-sm transition-colors disabled:opacity-40 ${
                    action.destructive
                      ? 'text-error hover:bg-error-container'
                      : 'text-on-surface hover:bg-surface-container'
                  }`}
                >
                  {Icon && <Icon className="h-4 w-4 flex-shrink-0" />}
                  {action.label}
                </button>
              );
            })}
          </div>,
          document.body
        )}
    </div>
  );
}
