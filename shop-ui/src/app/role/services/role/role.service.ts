import { Injectable } from '@angular/core';
import { User } from '../../../user/models/user/user';
import { Role } from '../../models/role/role';

@Injectable({
  providedIn: 'root',
})
export class RoleService {
  constructor() {}

  hasAdminRole(user: User): boolean {
    return !!user?.roles?.some((role: Role) => role.name === 'ADMIN');
  }
}
