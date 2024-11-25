import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { TokenService } from '../../../token/services/token/token.service';

export const loggedInGuard: CanActivateFn = () => {
  const tokenService = inject(TokenService);
  const router = inject(Router);
  if (tokenService.isTokenValid()) {
    router.navigate(['']);
    return false;
  }
  return true;
};
