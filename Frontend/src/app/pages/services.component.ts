import { Component, OnInit } from '@angular/core';
import { JsonPipe } from '@angular/common';
import { ApiService } from '../core/api.service';
import { API } from '../core/api.config';
import { ServiceRecord } from '../core/models';
import { catchError, map, of } from 'rxjs';

interface ServicePanel {
  key: string;
  label: string;
  service: string;
  path: string;
  items: ServiceRecord[];
  available: boolean;
  error?: string;
}

@Component({
  standalone: true,
  imports: [JsonPipe],
  template: `
    <section class="page-header">
      <p class="eyebrow">Architecture connectée</p>
      <h1>Tous les services</h1>
      <p>Cette vue vérifie les communications frontend → API Gateway → microservices.</p>
    </section>
    <section class="service-grid">
      @for (panel of panels; track panel.key) {
        <article class="service-panel">
          <div class="service-panel-heading">
            <div><span class="service-dot" [class.offline]="!panel.available"></span><h2>{{ panel.label }}</h2></div>
            <small>{{ panel.service }}</small>
          </div>
          <p class="service-path">{{ panel.path }}</p>
          @if (loading) {
            <div class="service-state">Vérification…</div>
          } @else if (panel.available) {
            <strong class="service-count">{{ panel.items.length }}</strong><span class="service-caption">élément(s) reçu(s) via Gateway</span>
            @if (panel.items.length) { <pre>{{ panel.items[0] | json }}</pre> }
          } @else {
            <div class="service-state offline-text">{{ panel.error }}</div>
          }
        </article>
      }
    </section>
  `
})
export class ServicesComponent implements OnInit {
  loading = true;
  panels: ServicePanel[] = [
    { key: 'users', label: 'Utilisateurs', service: 'USER', path: API.users, items: [], available: false },
    { key: 'contents', label: 'Contenus', service: 'CONTENT-NOTIFICATION', path: API.contents, items: [], available: false },
    { key: 'cinemas', label: 'Cinémas', service: 'CINEMA-RESERVATION', path: API.cinemas, items: [], available: false },
    { key: 'salles', label: 'Salles', service: 'CINEMA-RESERVATION', path: API.salles, items: [], available: false },
    { key: 'seances', label: 'Séances', service: 'CINEMA-RESERVATION', path: API.seances, items: [], available: false },
    { key: 'reservations', label: 'Réservations', service: 'CINEMA-RESERVATION', path: API.reservations, items: [], available: false },
    { key: 'posts', label: 'Forum', service: 'FORUM-PROMOTION', path: API.posts, items: [], available: false },
    { key: 'comments', label: 'Commentaires', service: 'FORUM-PROMOTION', path: API.comments, items: [], available: false },
    { key: 'promotions', label: 'Promotions', service: 'FORUM-PROMOTION', path: API.promotions, items: [], available: false },
    { key: 'watchparties', label: 'Watch Parties', service: 'WATCHPARTY-CHAT', path: API.watchparty, items: [], available: false },
    { key: 'feedback', label: 'Feedback', service: 'WATCHPARTY-CHAT', path: API.feedback, items: [], available: false },
    { key: 'abonnements', label: 'Abonnements', service: 'SUBSCRIPTION-LOYALTY', path: API.abonnements, items: [], available: false },
    { key: 'fidelities', label: 'Fidélité', service: 'SUBSCRIPTION-LOYALTY', path: API.fidelities, items: [], available: false }
  ];

  constructor(private readonly api: ApiService) {}

  ngOnInit(): void {
    this.panels.forEach(panel => {
      this.api.collection(panel.path).pipe(
        map(items => ({ items, available: true })),
        catchError(() => of({ items: [], available: false }))
      ).subscribe(result => {
        panel.items = result.items;
        panel.available = result.available;
        if (!result.available) panel.error = 'Service non disponible ou route non exposée par la Gateway.';
      });
    });
    setTimeout(() => this.loading = false, 700);
  }
}
