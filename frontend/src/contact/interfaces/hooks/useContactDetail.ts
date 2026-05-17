'use client';

import { useCallback, useEffect, useState } from 'react';
import { ContactViewModel } from '../../application/command/ContactViewModel';
import { ContactViewModelMapper } from '../../application/mapper/ContactViewModelMapper';
import { contactDependencies } from '../../infrastructure/config/contactDependencies';

export function useContactDetail(contactId: string) {
  const [contact, setContact] = useState<ContactViewModel | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const refetch = useCallback(async () => {
    if (!contactId) return;
    setLoading(true);
    setError(null);
    try {
      const dto = await contactDependencies.contactGateway.getById(contactId);
      setContact(ContactViewModelMapper.fromApi(dto));
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to load contact');
      setContact(null);
    } finally {
      setLoading(false);
    }
  }, [contactId]);

  useEffect(() => {
    void refetch();
  }, [refetch]);

  const addNote = useCallback(
    async (body: string) => {
      await contactDependencies.contactGateway.addNote(contactId, body);
      await refetch();
    },
    [contactId, refetch],
  );

  const addActivity = useCallback(
    async (activityType: string, description?: string) => {
      await contactDependencies.contactGateway.addActivity(contactId, {
        activityType,
        description: description ?? null,
        occurredAt: new Date().toISOString(),
      });
      await refetch();
    },
    [contactId, refetch],
  );

  const assignTags = useCallback(
    async (tagNames: string[]) => {
      await contactDependencies.contactGateway.assignTags(contactId, tagNames);
      await refetch();
    },
    [contactId, refetch],
  );

  const uploadAvatar = useCallback(
    async (file: File): Promise<string> => {
      const url = await contactDependencies.contactGateway.uploadAvatar(contactId, file);
      await refetch();
      return url;
    },
    [contactId, refetch],
  );

  return { contact, loading, error, refetch, addNote, addActivity, assignTags, uploadAvatar };
}
