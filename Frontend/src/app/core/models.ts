export interface AuthResponse {
  token: string;
  tokenType: string;
  userId: number;
  username: string;
  email: string;
  role: string;
}

export interface AuthRequest {
  username: string;
  password: string;
  email?: string;
  firstName?: string;
  lastName?: string;
  phoneNumber?: string;
}

export interface Content {
  id: string;
  contentId?: string;
  title: string;
  description?: string;
  category?: string;
  contentType?: string;
  status?: string;
  viewCount?: number;
  genres?: string[];
  genreIds?: string[];
  recommendationScore?: number;
  reason?: string;
}

export interface Cinema {
  id: string;
  nom: string;
  adresse?: string;
  ville?: string;
  latitude?: number;
  longitude?: number;
}

export interface Seance {
  id: string;
  filmId?: string;
  cinemaId?: string;
  date?: string;
  heure?: string;
  prix?: number;
}

export interface Reservation {
  id: string;
  userId?: number;
  seanceId?: string;
  nombrePlaces?: number;
  statut?: string;
}

export interface Notification {
  id: string;
  userId?: number;
  title?: string;
  message?: string;
  read?: boolean;
  createdAt?: string;
}

export type ServiceRecord = Record<string, unknown>;
