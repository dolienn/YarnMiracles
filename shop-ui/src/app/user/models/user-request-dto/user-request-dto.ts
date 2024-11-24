import { Role } from '../../../role/models/role/role';

export class UserRequestDTO {
  constructor(
    public id: number = 0,
    public firstname: string = '',
    public lastname: string = '',
    public email: string = '',
    public password: string = '',
    public dateOfBirth: Date = new Date(),
    public accountLocked: boolean = false,
    public roles: Role[] = []
  ) {}
}
