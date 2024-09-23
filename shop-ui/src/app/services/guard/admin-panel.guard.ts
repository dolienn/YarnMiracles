import { inject } from '@angular/core';
import { CanActivateFn, GuardResult, Router } from '@angular/router';
import { TokenService } from '../token/token.service';
import { catchError, map, Observable, of } from 'rxjs';

export const adminPanelGuard: CanActivateFn = (): Observable<boolean> => {
  const tokenService = inject(TokenService);
  const router = inject(Router);

  const token = tokenService.token;

  if (!token) {
    router.navigate(['page-not-found']);
    return of(false);
  }

  const userInfo = tokenService.getUserInfo();

  if (!userInfo) {
    router.navigate(['page-not-found']);
    return of(false);
  }

  return userInfo.pipe(
    map((data) => {
      const hasAdminRole = data.roles.some((role) => role.name === 'ADMIN');
      if (hasAdminRole) {
        return true;
      } else {
        router.navigate(['page-not-found']);
        return false;
      }
    }),
    catchError(() => {
      router.navigate(['login']);
      return of(false);
    })
  );
};
