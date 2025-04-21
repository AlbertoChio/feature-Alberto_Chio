import { Routes } from '@angular/router';
import { ROUTE_CONFIG } from './config/routes.config';
import { LayoutComponent } from './layout/layout.component';
import { HistorialComponent } from './pages/historial/historial.component';
import { HomeComponent } from './pages/home/home.component';
import { RegisterComponent } from './pages/register/register.component';
import { LoginComponent } from './pages/login/login.component';
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    redirectTo: ROUTE_CONFIG.login
  },
  {
    path: ROUTE_CONFIG.login,
    component: LoginComponent,
  },
  {
    path: ROUTE_CONFIG.register,
    component: RegisterComponent,
  },
  {
    path: ROUTE_CONFIG.app,
    component: LayoutComponent,
    children: [
      {
        path: '',
        pathMatch: 'full',
        redirectTo: ROUTE_CONFIG.home,
      },
      {
        path: ROUTE_CONFIG.home,
        component: HomeComponent,
        data: { breadcrumb: 'Home' },
        canActivate: [authGuard],
      },
      {
        path: ROUTE_CONFIG.historial,
        component: HistorialComponent,
        data: { breadcrumb: 'Historial' },
        canActivate: [authGuard],
      }
    ],
  },
  {
    path: '**',
    pathMatch: 'full',
    redirectTo: ROUTE_CONFIG.login,
  },
];
