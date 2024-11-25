import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { CartService } from '../../../cart/services/cart/cart.service';

export const checkoutGuard: CanActivateFn = () => {
  const cartService = inject(CartService);
  const router = inject(Router);
  if (cartService.cartItems.length == 0) {
    router.navigate(['']);
    return false;
  }
  return true;
};
