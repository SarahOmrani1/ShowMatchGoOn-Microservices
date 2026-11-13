import { Routes } from '@angular/router';
import { authGuard } from './core/auth.guard';
import { DashboardComponent } from './pages/dashboard.component';
import { LoginComponent } from './pages/login.component';
import { RegisterComponent } from './pages/register.component';
import { CatalogComponent } from './pages/catalog.component';
import { CinemasComponent } from './pages/cinemas.component';
import { ServicesComponent } from './pages/services.component';

export const routes: Routes = [
  { path: '', component: DashboardComponent, canActivate: [authGuard] },
  { path: 'catalogue', component: CatalogComponent, canActivate: [authGuard] },
  { path: 'cinemas', component: CinemasComponent, canActivate: [authGuard] },
  { path: 'services', component: ServicesComponent, canActivate: [authGuard] },
  { path: 'connexion', component: LoginComponent },
  { path: 'inscription', component: RegisterComponent },
  { path: '**', redirectTo: '' }
];
