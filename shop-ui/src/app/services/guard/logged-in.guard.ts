import { CanActivateFn, Router } from '@angular/router';
import { TokenService } from '../token/token.service';
import { inject } from '@angular/core';

export const loggedInGuard: CanActivateFn = () => {
  const tokenService = inject(TokenService);
  const router = inject(Router);
  if (tokenService.isTokenValid()) {
    router.navigate(['']);
    return false;
  }
  return true;
};
