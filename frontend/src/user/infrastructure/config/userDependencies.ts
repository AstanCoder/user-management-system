import { FetchUserAdminGateway } from '../http/FetchUserAdminGateway';

const baseUrl = process.env.NEXT_PUBLIC_API_URL ?? 'http://localhost:8080';

export const userDependencies = {
  userAdminGateway: new FetchUserAdminGateway(baseUrl),
};
