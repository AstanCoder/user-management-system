import { CreateContactService } from '../../application/service/CreateContactService';
import { DeleteContactService } from '../../application/service/DeleteContactService';
import { ListContactsService } from '../../application/service/ListContactsService';
import { UpdateContactService } from '../../application/service/UpdateContactService';
import { CreateContactUseCase } from '../../application/port/in/CreateContactUseCase';
import { DeleteContactUseCase } from '../../application/port/in/DeleteContactUseCase';
import { ListContactsUseCase } from '../../application/port/in/ListContactsUseCase';
import { UpdateContactUseCase } from '../../application/port/in/UpdateContactUseCase';
import { FetchContactGateway } from '../http/FetchContactGateway';

/**
 * Composition root wiring gateway and use cases for the contact bounded context.
 */
export interface ContactDependencies {
  contactGateway: FetchContactGateway;
  listContactsUseCase: ListContactsUseCase;
  createContactUseCase: CreateContactUseCase;
  updateContactUseCase: UpdateContactUseCase;
  deleteContactUseCase: DeleteContactUseCase;
}

/**
 * Creates contact dependencies with the configured API base URL.
 * @param apiBaseUrl - backend base URL (e.g. http://localhost:8080)
 * @returns wired use cases
 */
export function createContactDependencies(apiBaseUrl: string): ContactDependencies {
  const gateway = new FetchContactGateway(apiBaseUrl);
  return {
    contactGateway: gateway,
    listContactsUseCase: new ListContactsService(gateway),
    createContactUseCase: new CreateContactService(gateway),
    updateContactUseCase: new UpdateContactService(gateway),
    deleteContactUseCase: new DeleteContactService(gateway),
  };
}

/**
 * Default dependencies using NEXT_PUBLIC_API_URL.
 */
export const contactDependencies = createContactDependencies(
  process.env.NEXT_PUBLIC_API_URL ?? 'http://localhost:8080',
);
