import { AuthGateway, RegisterCommand } from '../../domain/port/AuthGateway';
import { RegisterUseCase } from '../port/in/RegisterUseCase';

export class RegisterService implements RegisterUseCase {
  constructor(private readonly gateway: AuthGateway) {}

  execute(command: RegisterCommand) {
    return this.gateway.register(command);
  }
}
