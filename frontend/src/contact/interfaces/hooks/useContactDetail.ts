'use client';

import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { ContactViewModelMapper } from '../../application/mapper/ContactViewModelMapper';
import { contactDependencies } from '../../infrastructure/config/contactDependencies';
import { contactQueryKeys } from '../query/contactQueryKeys';

export function useContactDetail(contactId: string) {
  const queryClient = useQueryClient();

  const detailQuery = useQuery({
    queryKey: contactQueryKeys.detail(contactId),
    enabled: Boolean(contactId),
    queryFn: async () => {
      const dto = await contactDependencies.contactGateway.getById(contactId);
      return ContactViewModelMapper.fromApi(dto);
    },
  });

  const invalidateDetail = async () => {
    await queryClient.invalidateQueries({ queryKey: contactQueryKeys.detail(contactId) });
  };

  const addNoteMutation = useMutation({
    mutationFn: async (body: string) => {
      await contactDependencies.contactGateway.addNote(contactId, body);
    },
    onSuccess: invalidateDetail,
  });

  const deleteNoteMutation = useMutation({
    mutationFn: async (noteId: string) => {
      await contactDependencies.contactGateway.deleteNote(contactId, noteId);
    },
    onSuccess: invalidateDetail,
  });

  const addActivityMutation = useMutation({
    mutationFn: async (payload: { activityType: string; description?: string; confirmed?: boolean }) => {
      await contactDependencies.contactGateway.addActivity(contactId, {
        activityType: payload.activityType,
        description: payload.description ?? null,
        occurredAt: new Date().toISOString(),
        confirmed: payload.confirmed,
      });
    },
    onSuccess: invalidateDetail,
  });

  const confirmActivityMutation = useMutation({
    mutationFn: async (activityId: string) => {
      await contactDependencies.contactGateway.confirmActivity(contactId, activityId);
    },
    onSuccess: invalidateDetail,
  });

  const deleteActivityMutation = useMutation({
    mutationFn: async (activityId: string) => {
      await contactDependencies.contactGateway.deleteActivity(contactId, activityId);
    },
    onSuccess: invalidateDetail,
  });

  const assignTagsMutation = useMutation({
    mutationFn: async (tagNames: string[]) => {
      await contactDependencies.contactGateway.assignTags(contactId, tagNames);
    },
    onSuccess: async () => {
      await invalidateDetail();
      await queryClient.invalidateQueries({ queryKey: contactQueryKeys.all });
    },
  });

  const uploadAvatarMutation = useMutation({
    mutationFn: async (file: File) => contactDependencies.contactGateway.uploadAvatar(contactId, file),
    onSuccess: async () => {
      await invalidateDetail();
      await queryClient.invalidateQueries({ queryKey: contactQueryKeys.all });
    },
  });

  return {
    contact: detailQuery.data ?? null,
    loading: detailQuery.isPending,
    error: detailQuery.error instanceof Error ? detailQuery.error.message : null,
    refetch: () => {
      void detailQuery.refetch();
    },
    addNote: async (body: string) => {
      await addNoteMutation.mutateAsync(body);
    },
    deleteNote: async (noteId: string) => {
      await deleteNoteMutation.mutateAsync(noteId);
    },
    addActivity: async (activityType: string, description?: string, confirmed = true) => {
      await addActivityMutation.mutateAsync({ activityType, description, confirmed });
    },
    confirmActivity: async (activityId: string) => {
      await confirmActivityMutation.mutateAsync(activityId);
    },
    deleteActivity: async (activityId: string) => {
      await deleteActivityMutation.mutateAsync(activityId);
    },
    assignTags: async (tagNames: string[]) => {
      await assignTagsMutation.mutateAsync(tagNames);
    },
    uploadAvatar: async (file: File): Promise<string> => uploadAvatarMutation.mutateAsync(file),
  };
}
