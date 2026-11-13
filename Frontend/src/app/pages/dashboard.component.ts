import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { DecimalPipe } from '@angular/common';
import { ApiService } from '../core/api.service';
import { AuthService } from '../core/auth.service';
import { Content, Notification } from '../core/models';

@Component({
  standalone: true,
  imports: [RouterLink, DecimalPipe],
  template: `
    <section class="hero">
      <div class="hero-content">
        <p class="eyebrow">ShowMatchGoOn · votre espace cinéma</p>
        <h1>Bonjour {{ auth.currentUser()?.username }}<br><em>Prêt à découvrir ?</em></h1>
        <p class="hero-copy">Découvrez les histoires qui vous ressemblent, réservez votre séance et partagez le moment.</p>
        <div class="hero-actions"><a routerLink="/catalogue" class="primary-button">Explorer le catalogue <span>→</span></a><a routerLink="/cinemas" class="secondary-button">Voir les séances</a></div>
      </div>
      <div class="hero-orbit"><div class="orbit-glow"></div><div class="orbit-card"><span>✦</span><small>Des recommandations<br>qui vous ressemblent</small></div></div>
    </section>
    <section class="welcome-strip"><div><span class="status-dot"></span><strong>Votre univers est prêt</strong><span>Des nouveautés choisies pour vous</span></div><a routerLink="/services">Découvrir les services <span>↗</span></a></section>
    <section class="section"><div class="section-heading"><div><p class="eyebrow">Sélection personnalisée</p><h2>Pour vous</h2></div><a routerLink="/catalogue">Voir tout →</a></div>
      @if (loading) { <div class="loading">Chargement de vos recommandations…</div> } @else if (recommendations.length) { <div class="content-grid">@for (item of recommendations; track item.contentId || item.id) { <article class="content-card"><div class="poster"><span>{{ item.category || 'FILM' }}</span><strong>{{ item.title }}</strong></div><div class="card-body"><h3>{{ item.title }}</h3><p>{{ item.reason || item.description || 'Une sélection pensée pour vous.' }}</p><small>★ {{ item.recommendationScore ? (item.recommendationScore | number:'1.0-1') : '—' }}</small></div></article> }</div> } @else { <div class="empty">Aucune recommandation pour le moment. Explorez le catalogue pour commencer.</div> }
    </section>
    <section class="section stats"><div><strong>{{ recommendations.length || '—' }}</strong><span>recommandations</span></div><div><strong>{{ notifications.length || '—' }}</strong><span>notifications</span></div><div><strong>∞</strong><span>histoires à découvrir</span></div></section>
  `
})
export class DashboardComponent implements OnInit {
  recommendations: Content[] = []; notifications: Notification[] = []; loading = true;
  constructor(readonly auth: AuthService, private readonly api: ApiService) {}
  ngOnInit(): void {
    const userId = this.auth.currentUser()?.userId;
    this.api.recommendations(userId).subscribe({ next: value => this.recommendations = value, error: () => {}, complete: () => this.loading = false });
    if (userId) this.api.notifications(userId).subscribe({ next: value => this.notifications = value, error: () => {} });
  }
}
