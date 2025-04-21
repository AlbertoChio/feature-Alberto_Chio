import { Component, inject } from '@angular/core';
import {
  ActivatedRoute,
  NavigationEnd,
  Router,
  RouterLink,
  RouterOutlet,
} from '@angular/router';
import { AplazoButtonComponent } from '@apz/shared-ui/button';
import { AplazoDashboardComponents } from '@apz/shared-ui/dashboard';
import { AplazoSidenavLinkComponent } from '@apz/shared-ui/sidenav';
import { ROUTE_CONFIG } from '../config/routes.config';
import { AuthService } from '../services/auth.service';
import { filter, map } from 'rxjs';
import { CommonModule } from '@angular/common';

@Component({
  standalone: true,
  selector: 'app-layout',
  templateUrl: './layout.component.html',
  imports: [
    AplazoDashboardComponents,
    AplazoButtonComponent,
    AplazoSidenavLinkComponent,
    RouterOutlet,
    RouterLink,
    CommonModule,
  ],
})
export class LayoutComponent {
  readonly #router = inject(Router);
  readonly #route = inject(ActivatedRoute);
  readonly #authService: AuthService = inject(AuthService);

  readonly breadcrumb$ = this.#router.events.pipe(
    filter((event) => event instanceof NavigationEnd),
    map(() => {
      let currentRoute = this.#route.root;
      let breadcrumb = '';

      while (currentRoute?.firstChild) {
        currentRoute = currentRoute?.firstChild;
        if (currentRoute?.snapshot?.data?.['breadcrumb']) {
          breadcrumb = currentRoute?.snapshot?.data?.['breadcrumb'];
        }
      }
      return breadcrumb;
    })
  );
  readonly appRoutes = ROUTE_CONFIG;

  clickLogo(): void {
    this.#router.navigate([ROUTE_CONFIG.app, ROUTE_CONFIG.home]);
  }

  logOut() {
    this.#authService.clearUser();
    this.#router.navigate([ROUTE_CONFIG.login]);
  }
}
