import { AuthGateway, LoginCommand } from '../../domain/port/AuthGateway';
import { LoginUseCase } from '../port/in/LoginUseCase';

export class LoginService implements LoginUseCase {
  constructor(private readonly gateway: AuthGateway) {}

  execute(command: LoginCommand) {
    return this.gateway.login(command);
  }
}
