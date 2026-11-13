# ShowMatchGoOn Frontend

Frontend Angular standalone pour ShowMatchGoOn. Toutes les requêtes passent par
l'API Gateway sur `http://localhost:8888`; le frontend ne contacte jamais
directement les microservices.

## Démarrage

```powershell
cd Frontend
npm install
npm start
```

Ouvrir ensuite `http://localhost:4200`. Eureka, la Gateway et les services
nécessaires doivent être démarrés côté backend.

Les routes utilisées par la Gateway sont centralisées dans
`src/app/core/api.config.ts`. Le token JWT est conservé dans la session du
navigateur et ajouté automatiquement aux appels par l'intercepteur HTTP.

## Périmètre couvert

La navigation `Tous les services` vérifie les collections exposées par la
Gateway pour les domaines suivants : utilisateurs, contenus et notifications,
cinémas/salles/séances/réservations, forum/commentaires/promotions,
watch-party/feedback et abonnements/fidélité. Une carte rouge signifie que le
microservice correspondant n'est pas démarré ou qu'il n'est pas encore
enregistré dans Eureka ; cela n'empêche pas les autres domaines de fonctionner.
