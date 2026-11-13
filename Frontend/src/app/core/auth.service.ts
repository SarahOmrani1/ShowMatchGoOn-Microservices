import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { API, API_BASE_URL } from './api.config';
import { AuthRequest, AuthResponse } from './models';

const SESSION_KEY = 'showmatchgoon.session';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly session = signal<AuthResponse | null>(this.readSession());

  constructor(private readonly http: HttpClient) {}

  currentUser(): AuthResponse | null {
    return this.session();
  }

  isAuthenticated(): boolean {
    return Boolean(this.session()?.token);
  }

  login(request: AuthRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${API_BASE_URL}${API.auth}/login`, request).pipe(
      tap(response => this.saveSession(response))
    );
  }

  register(request: AuthRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${API_BASE_URL}${API.auth}/register`, request).pipe(
      tap(response => this.saveSession(response))
    );
  }

  logout(): void {
    sessionStorage.removeItem(SESSION_KEY);
    this.session.set(null);
  }

  token(): string | null {
    return this.session()?.token ?? null;
  }

  private saveSession(response: AuthResponse): void {
    sessionStorage.setItem(SESSION_KEY, JSON.stringify(response));
    this.session.set(response);
  }

  private readSession(): AuthResponse | null {
    const raw = sessionStorage.getItem(SESSION_KEY);
    if (!raw) return null;
    try {
      return JSON.parse(raw) as AuthResponse;
    } catch {
      sessionStorage.removeItem(SESSION_KEY);
      return null;
    }
  }
}
