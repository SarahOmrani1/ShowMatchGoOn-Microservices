import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { forkJoin, Observable } from 'rxjs';
import { API, API_BASE_URL } from './api.config';
import { Cinema, Content, Notification, Reservation, Seance, ServiceRecord } from './models';

@Injectable({ providedIn: 'root' })
export class ApiService {
  constructor(private readonly http: HttpClient) {}

  contents(): Observable<Content[]> {
    return this.http.get<Content[]>(`${API_BASE_URL}${API.contents}`);
  }

  recommendations(userId?: number): Observable<Content[]> {
    let params = new HttpParams({ fromObject: { limit: '6' } });
    if (userId) params = params.set('userId', userId);
    return this.http.get<Content[]>(`${API_BASE_URL}${API.contents}/recommendations`, { params });
  }

  cinemas(): Observable<Cinema[]> {
    return this.http.get<Cinema[]>(`${API_BASE_URL}${API.cinemas}`);
  }

  seances(): Observable<Seance[]> {
    return this.http.get<Seance[]>(`${API_BASE_URL}${API.seances}`);
  }

  reservations(): Observable<Reservation[]> {
    return this.http.get<Reservation[]>(`${API_BASE_URL}${API.reservations}`);
  }

  notifications(userId: number): Observable<Notification[]> {
    return this.http.get<Notification[]>(`${API_BASE_URL}${API.notifications}/user/${userId}`);
  }

  collection(path: string): Observable<ServiceRecord[]> {
    return this.http.get<ServiceRecord[]>(`${API_BASE_URL}${path}`);
  }

  serviceOverview(): Observable<Record<string, ServiceRecord[]>> {
    return forkJoin({
      users: this.collection(API.users),
      contents: this.collection(API.contents),
      cinemas: this.collection(API.cinemas),
      salles: this.collection(API.salles),
      seances: this.collection(API.seances),
      reservations: this.collection(API.reservations),
      posts: this.collection(API.posts),
      comments: this.collection(API.comments),
      promotions: this.collection(API.promotions),
      watchparties: this.collection(API.watchparty),
      feedback: this.collection(API.feedback),
      abonnements: this.collection(API.abonnements),
      fidelities: this.collection(API.fidelities)
    });
  }
}
