import { MutationUseCase } from '../port/in/MutationUseCase';

export abstract class BaseMutationService<TCommand, TResult> implements MutationUseCase<TCommand, TResult> {
  async execute(command: TCommand): Promise<TResult> {
    return this.handleMutation(command);
  }

  protected abstract handleMutation(command: TCommand): Promise<TResult>;
}
