import { Component, OnInit } from '@angular/core';
import { ApiService } from '../core/api.service';
import { Cinema, Seance } from '../core/models';

@Component({
  standalone: true,
  template: `
    <section class="page-header"><p class="eyebrow">Près de chez vous</p><h1>Cinémas & séances</h1><p>Trouvez votre prochaine projection.</p></section>
    <section class="cinema-layout"><div><h2>Salles partenaires</h2>@for (cinema of cinemas; track cinema.id) { <article class="cinema-card"><div class="cinema-icon">▣</div><div><h3>{{ cinema.nom }}</h3><p>{{ cinema.adresse || 'Adresse non renseignée' }}<br>{{ cinema.ville || '' }}</p></div><span>→</span></article> } @empty { <div class="empty">Aucun cinéma disponible.</div> }</div><div><h2>Prochaines séances</h2>@for (seance of seances; track seance.id) { <article class="session-card"><div><strong>{{ seance.date || 'Date à confirmer' }}</strong><span>{{ seance.heure || 'Horaire à confirmer' }}</span></div><b>{{ seance.prix ? (seance.prix + ' DT') : 'Tarif standard' }}</b></article> } @empty { <div class="empty">Aucune séance disponible.</div> }</div></section>
  `
})
export class CinemasComponent implements OnInit {
  cinemas: Cinema[] = []; seances: Seance[] = [];
  constructor(private readonly api: ApiService) {}
  ngOnInit(): void {
    this.api.cinemas().subscribe({ next: value => this.cinemas = value, error: () => {} });
    this.api.seances().subscribe({ next: value => this.seances = value, error: () => {} });
  }
}
