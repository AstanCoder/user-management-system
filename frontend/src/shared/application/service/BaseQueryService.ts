import { QueryUseCase } from '../port/in/QueryUseCase';

export abstract class BaseQueryService<TQuery, TResult> implements QueryUseCase<TQuery, TResult> {
  async execute(...args: TQuery extends void ? [] : [query: TQuery]): Promise<TResult> {
    const query = args[0] as TQuery;
    return this.handleQuery(query);
  }

  protected abstract handleQuery(query: TQuery): Promise<TResult>;
}
