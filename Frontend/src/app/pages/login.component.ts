import { Component } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../core/auth.service';

@Component({
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  template: `
    <section class="auth-page">
      <div class="auth-card">
        <p class="eyebrow">Bienvenue sur ShowMatchGoOn</p><h1>Votre cinéma,<br><em>autrement.</em></h1>
        <p class="muted">Connectez-vous pour retrouver vos recommandations et vos réservations.</p>
        <form [formGroup]="form" (ngSubmit)="submit()">
          <label>Nom d'utilisateur<input formControlName="username" autocomplete="username"></label>
          <label>Mot de passe<input type="password" formControlName="password" autocomplete="current-password"></label>
          @if (error) { <p class="error">{{ error }}</p> }
          <button class="primary-button full" [disabled]="form.invalid || loading">{{ loading ? 'Connexion…' : 'Se connecter' }}</button>
        </form>
        <p class="switch">Pas encore de compte ? <a routerLink="/inscription">Créer un compte</a></p>
      </div>
    </section>
  `
})
export class LoginComponent {
  readonly form = this.fb.nonNullable.group({ username: ['', Validators.required], password: ['', Validators.required] });
  loading = false;
  error = '';
  constructor(private readonly fb: FormBuilder, private readonly auth: AuthService, private readonly router: Router) {}
  submit(): void {
    if (this.form.invalid) return;
    this.loading = true; this.error = '';
    this.auth.login(this.form.getRawValue()).subscribe({
      next: () => this.router.navigateByUrl('/'),
      error: (response: HttpErrorResponse) => {
        this.error = this.getErrorMessage(response);
        this.loading = false;
      }
    });
  }

  private getErrorMessage(response: HttpErrorResponse): string {
    if (response.status === 0) return 'API Gateway indisponible. Vérifiez que le backend est démarré.';
    if (response.status === 401 || response.status === 403) return 'Nom d’utilisateur ou mot de passe incorrect.';
    if (response.status >= 500) return 'Le service utilisateur rencontre une erreur. Consultez ses logs.';
    if (response.error?.message) return response.error.message;
    return `Connexion impossible (erreur ${response.status}).`;
  }
}
