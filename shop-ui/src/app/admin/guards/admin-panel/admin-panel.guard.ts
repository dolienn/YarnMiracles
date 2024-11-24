import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { catchError, map, Observable, of } from 'rxjs';
import { TokenService } from '../../../token/services/token/token.service';
import { RoleService } from '../../../role/services/role/role.service';

export const adminPanelGuard: CanActivateFn = (): Observable<boolean> => {
  const tokenService = inject(TokenService);
  const roleService = inject(RoleService);
  const router = inject(Router);

  const navigateAndDeny = (route: string): Observable<boolean> => {
    router.navigate([route]);
    return of(false);
  };

  if (tokenService.isTokenNotValid()) {
    return navigateAndDeny('page-not-found');
  }

  const user$ = tokenService.getUserByJwtToken();
  if (!user$) {
    return navigateAndDeny('page-not-found');
  }

  return user$.pipe(
    map((user) => {
      if (!roleService.hasAdminRole(user)) {
        router.navigate(['page-not-found']);
        return false;
      }
      return true;
    }),
    catchError(() => navigateAndDeny('login'))
  );
};
