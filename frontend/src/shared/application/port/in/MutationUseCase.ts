export interface MutationUseCase<TCommand, TResult> {
  execute(command: TCommand): Promise<TResult>;
}
