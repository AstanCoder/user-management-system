export interface QueryUseCase<TQuery, TResult> {
  execute(...args: TQuery extends void ? [] : [query: TQuery]): Promise<TResult>;
}
