import { Component } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../core/auth.service';

@Component({
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  template: `
    <section class="auth-page"><div class="auth-card">
      <p class="eyebrow">Rejoignez la communauté</p><h1>Créez votre<br><em>expérience.</em></h1>
      <form [formGroup]="form" (ngSubmit)="submit()">
        <div class="form-grid"><label>Prénom<input formControlName="firstName"></label><label>Nom<input formControlName="lastName"></label></div>
        <label>Nom d'utilisateur<input formControlName="username"></label><label>Email<input type="email" formControlName="email"></label>
        <label>Mot de passe<input type="password" formControlName="password"></label>
        @if (error) { <p class="error">{{ error }}</p> }
        <button class="primary-button full" [disabled]="form.invalid || loading">{{ loading ? 'Création…' : 'Créer mon compte' }}</button>
      </form>
      <p class="switch">Déjà membre ? <a routerLink="/connexion">Se connecter</a></p>
    </div></section>
  `
})
export class RegisterComponent {
  readonly form = this.fb.nonNullable.group({
    firstName: ['', Validators.required], lastName: ['', Validators.required], username: ['', Validators.required],
    email: ['', [Validators.required, Validators.email]], password: ['', [Validators.required, Validators.minLength(6)]]
  });
  loading = false; error = '';
  constructor(private readonly fb: FormBuilder, private readonly auth: AuthService, private readonly router: Router) {}
  submit(): void {
    if (this.form.invalid) return;
    this.loading = true; this.auth.register(this.form.getRawValue()).subscribe({
      next: () => this.router.navigateByUrl('/'),
      error: (response: HttpErrorResponse) => {
        this.error = this.getErrorMessage(response);
        this.loading = false;
      }
    });
  }

  private getErrorMessage(response: HttpErrorResponse): string {
    if (response.status === 0) return 'API Gateway indisponible. Vérifiez que le backend est démarré.';
    if (response.status === 409) return 'Ce nom d’utilisateur ou cet email existe déjà.';
    if (typeof response.error === 'string' && response.error.trim()) return response.error;
    if (response.error?.message) return response.error.message;
    return `Création impossible (erreur ${response.status}).`;
  }
}
