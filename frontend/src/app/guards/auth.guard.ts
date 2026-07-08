import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.isLoggedIn()) {
    const expectedRole = route.data['role'];
    if (expectedRole && authService.getRole() !== expectedRole) {
      if (authService.getRole() === 'ROLE_ADMIN') {
        router.navigate(['/admin']);
      } else {
        router.navigate(['/catalog']);
      }
      return false;
    }
    return true;
  }

  router.navigate(['/login']);
  return false;
};
