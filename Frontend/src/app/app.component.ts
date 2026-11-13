import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { AuthService } from './core/auth.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  template: `
    <header class="topbar">
      <a routerLink="/" class="brand"><span class="brand-mark">S</span><span>ShowMatch<span>GoOn</span></span></a>
      @if (auth.isAuthenticated()) {
        <nav>
          <a routerLink="/" routerLinkActive="active" [routerLinkActiveOptions]="{exact: true}">Accueil</a>
          <a routerLink="/catalogue" routerLinkActive="active">Catalogue</a>
          <a routerLink="/cinemas" routerLinkActive="active">Cinémas</a>
          <a routerLink="/services" routerLinkActive="active">Tous les services</a>
        </nav>
        <div class="user-menu"><span>{{ auth.currentUser()?.username }}</span><button class="ghost-button" (click)="auth.logout()">Déconnexion</button></div>
      } @else {
        <div class="user-menu"><a routerLink="/connexion" class="ghost-button">Connexion</a><a routerLink="/inscription" class="primary-button">Créer un compte</a></div>
      }
    </header>
    <main><router-outlet /></main>
    <footer>ShowMatchGoOn <span>•</span> Votre prochaine séance commence ici.</footer>
  `
})
export class AppComponent {
  constructor(readonly auth: AuthService) {}
}
