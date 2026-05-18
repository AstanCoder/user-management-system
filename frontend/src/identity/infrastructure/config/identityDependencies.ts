import { CompleteInvitationService } from '../../application/service/CompleteInvitationService';
import { LoginService } from '../../application/service/LoginService';
import { RegisterService } from '../../application/service/RegisterService';
import { FetchAuthGateway } from '../http/FetchAuthGateway';

const baseUrl = process.env.NEXT_PUBLIC_API_URL ?? 'http://localhost:8080';
const authGateway = new FetchAuthGateway(baseUrl);

export const identityDependencies = {
  loginUseCase: new LoginService(authGateway),
  registerUseCase: new RegisterService(authGateway),
  completeInvitationUseCase: new CompleteInvitationService(authGateway),
  authGateway,
};
