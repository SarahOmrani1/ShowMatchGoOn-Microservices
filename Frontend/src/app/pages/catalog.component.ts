import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../core/api.service';
import { Content } from '../core/models';

@Component({
  standalone: true,
  imports: [FormsModule],
  template: `
    <section class="page-header"><p class="eyebrow">Bibliothèque</p><h1>Le catalogue</h1><p>Films, séries et documentaires à découvrir.</p></section>
    <section class="toolbar"><input [(ngModel)]="query" placeholder="Rechercher un titre…"><div class="filter-pills"><button [class.selected]="filter === 'ALL'" (click)="filter='ALL'">Tout</button><button [class.selected]="filter === 'FILM'" (click)="filter='FILM'">Films</button><button [class.selected]="filter === 'SERIES'" (click)="filter='SERIES'">Séries</button></div></section>
    <section class="content-grid catalog-grid">@for (item of filtered; track item.id) { <article class="content-card"><div class="poster"><span>{{ item.contentType || item.category || 'CONTENU' }}</span><strong>{{ item.title }}</strong></div><div class="card-body"><h3>{{ item.title }}</h3><p>{{ item.description || 'Découvrez ce contenu sur ShowMatchGoOn.' }}</p><small>{{ item.viewCount || 0 }} vues</small></div></article> } @empty { <div class="empty">Aucun contenu ne correspond à votre recherche.</div> }</section>
  `
})
export class CatalogComponent implements OnInit {
  items: Content[] = []; query = ''; filter = 'ALL';
  constructor(private readonly api: ApiService) {}
  get filtered(): Content[] {
    const term = this.query.toLowerCase();
    return this.items.filter(item => (!term || item.title.toLowerCase().includes(term)) && (this.filter === 'ALL' || item.contentType === this.filter));
  }
  ngOnInit(): void { this.api.contents().subscribe({ next: value => this.items = value, error: () => {} }); }
}
