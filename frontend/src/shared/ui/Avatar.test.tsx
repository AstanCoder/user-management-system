import { render, screen } from '@testing-library/react';
import { describe, expect, it } from 'vitest';
import { Avatar } from './Avatar';

describe('Avatar', () => {
  it('renders initials when no image', () => {
    render(<Avatar name="Sarah Jenkins" />);
    expect(screen.getByText('SJ')).toBeInTheDocument();
  });
});
