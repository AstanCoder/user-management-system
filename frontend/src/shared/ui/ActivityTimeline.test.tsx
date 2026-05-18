import { render, screen } from '@testing-library/react';
import { describe, expect, it } from 'vitest';
import { ActivityTimeline } from './ActivityTimeline';

describe('ActivityTimeline', () => {
  it('renders activities in order', () => {
    render(
      <ActivityTimeline
        activities={[
          {
            id: 'a1',
            activityType: 'CALL',
            description: 'Call started',
            authorUserId: 'u1',
            occurredAt: '2026-05-18T03:00:00Z',
            createdAt: '2026-05-18T03:00:00Z',
            confirmed: true,
          },
          {
            id: 'a2',
            activityType: 'EMAIL',
            description: 'Email sent',
            authorUserId: 'u1',
            occurredAt: '2026-05-18T03:01:00Z',
            createdAt: '2026-05-18T03:01:00Z',
            confirmed: true,
          },
        ]}
      />,
    );

    expect(screen.getByText('Call started')).toBeInTheDocument();
    expect(screen.getByText('Email sent')).toBeInTheDocument();
  });
});
