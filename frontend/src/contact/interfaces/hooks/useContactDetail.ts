'use client';

import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { useInfiniteQuery } from '@tanstack/react-query';
import { ContactViewModelMapper } from '../../application/mapper/ContactViewModelMapper';
import { contactDependencies } from '../../infrastructure/config/contactDependencies';
import { contactQueryKeys } from '../query/contactQueryKeys';
import type { ActivityViewModel } from '../../application/command/ContactViewModel';

export function useContactDetail(contactId: string, activityType = '') {
  const queryClient = useQueryClient();

  const detailQuery = useQuery({
    queryKey: contactQueryKeys.detail(contactId),
    enabled: Boolean(contactId),
    queryFn: async () => {
      const dto = await contactDependencies.contactGateway.getById(contactId);
      return ContactViewModelMapper.fromApi(dto);
    },
  });

  const activitiesQuery = useInfiniteQuery({
    queryKey: contactQueryKeys.activities(contactId, activityType),
    enabled: Boolean(contactId),
    initialPageParam: 0,
    queryFn: async ({ pageParam }) => {
      return contactDependencies.contactGateway.listActivities(contactId, {
        page: pageParam,
        size: 10,
        activityType: activityType || undefined,
      });
    },
    getNextPageParam: (lastPage) => {
      const nextPage = lastPage.page + 1;
      return nextPage < lastPage.totalPages ? nextPage : undefined;
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
    onSuccess: async () => {
      await invalidateDetail();
      await queryClient.invalidateQueries({ queryKey: contactQueryKeys.activities(contactId, activityType) });
    },
  });

  const confirmActivityMutation = useMutation({
    mutationFn: async (activityId: string) => {
      await contactDependencies.contactGateway.confirmActivity(contactId, activityId);
    },
    onSuccess: async () => {
      await invalidateDetail();
      await queryClient.invalidateQueries({ queryKey: contactQueryKeys.activities(contactId, activityType) });
    },
  });

  const deleteActivityMutation = useMutation({
    mutationFn: async (activityId: string) => {
      await contactDependencies.contactGateway.deleteActivity(contactId, activityId);
    },
    onSuccess: async () => {
      await invalidateDetail();
      await queryClient.invalidateQueries({ queryKey: contactQueryKeys.activities(contactId, activityType) });
    },
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
    activities: activitiesQuery.data?.pages.flatMap((page) =>
      page.content.map(
        (activity): ActivityViewModel => ({
          id: activity.id,
          activityType: activity.activityType,
          description: activity.description,
          authorUserId: activity.authorUserId,
          occurredAt: activity.occurredAt,
          createdAt: activity.createdAt,
          confirmed: activity.confirmed,
        }),
      ),
    ) ?? [],
    hasMoreActivities: activitiesQuery.hasNextPage,
    loadingMoreActivities: activitiesQuery.isFetchingNextPage,
    loadMoreActivities: async () => {
      if (activitiesQuery.hasNextPage) {
        await activitiesQuery.fetchNextPage();
      }
    },
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
