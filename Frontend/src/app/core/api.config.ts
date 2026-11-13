export const API_BASE_URL = `http://${window.location.hostname}:8888`;

export const API = {
  auth: '/api/auth',
  users: '/api/users',
  contents: '/api/contents',
  notifications: '/api/notifications',
  cinemas: '/api/cinemas',
  seances: '/api/seances',
  reservations: '/api/reservations',
  salles: '/api/salles',
  posts: '/api/posts',
  comments: '/api/commentaires',
  promotions: '/api/promotions',
  watchparty: '/watchparty',
  feedback: '/feedback',
  subscriptions: '/api/subscriptions/premium',
  abonnements: '/api/abonnements',
  fidelities: '/api/fidelities'
} as const;
