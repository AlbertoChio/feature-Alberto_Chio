import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { inject } from '@angular/core';
import { ROUTE_CONFIG } from '../config/routes.config';

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  const isLoggedIn = !!authService.getToken();
  if (isLoggedIn) {
    return true;
  }

  return router.createUrlTree([ROUTE_CONFIG.login]);
};
