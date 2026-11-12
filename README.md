# 🎬 ShowMatchGoOn - Microservices

## 📌 Description

**ShowMatchGoOn** est une plateforme intelligente de divertissement développée
dans le cadre d'un projet académique.

La plateforme permet aux utilisateurs de découvrir des films et des séries,
réserver des billets de cinéma, participer à des sessions Watch Party,
gagner des points de fidélité et bénéficier de recommandations personnalisées
basées sur l'intelligence artificielle.

Le projet évolue progressivement d'une architecture monolithique vers une
**architecture Microservices**, afin d'améliorer la modularité, la
maintenabilité, l'évolutivité et l'indépendance des différents domaines métier.

---

## 🎯 Objectifs du projet

Les principaux objectifs sont :

- Concevoir une architecture distribuée basée sur les Microservices.
- Décomposer l'application selon ses différents domaines métier.
- Mettre en place un **Service Discovery**.
- Mettre en place une **API Gateway** comme point d'entrée de l'application.
- Définir une communication cohérente entre les différents services.
- Sécuriser les communications et les endpoints.
- Intégrer progressivement les services d'intelligence artificielle.
- Préparer la conteneurisation et le déploiement avec Docker.
- Documenter les choix architecturaux et techniques.
# 🎬 ShowMatchGoOn - Microservices

## 📌 Description

**ShowMatchGoOn** est une plateforme intelligente de divertissement développée
dans le cadre d'un projet académique.

La plateforme permet aux utilisateurs de découvrir des films et des séries,
réserver des billets de cinéma, participer à des sessions Watch Party,
gagner des points de fidélité et bénéficier de recommandations personnalisées
basées sur l'intelligence artificielle.

Le projet évolue progressivement d'une architecture monolithique vers une
**architecture Microservices**, afin d'améliorer la modularité, la
maintenabilité, l'évolutivité et l'indépendance des différents domaines métier.

---

## 🎯 Objectifs du projet

Les principaux objectifs sont :

- Concevoir une architecture distribuée basée sur les Microservices.
- Décomposer l'application selon ses différents domaines métier.
- Mettre en place un **Service Discovery**.
- Mettre en place une **API Gateway** comme point d'entrée de l'application.
- Définir une communication cohérente entre les différents services.
- Sécuriser les communications et les endpoints.
- Intégrer progressivement les services d'intelligence artificielle.
- Préparer la conteneurisation et le déploiement avec Docker.
- Documenter les choix architecturaux et techniques.

---

## ✨ Fonctionnalités principales

- 🎬 Découverte de films et séries
- 🤖 Recommandations personnalisées basées sur l'IA
- 🎟️ Réservation de billets de cinéma
- 🔐 Authentification et autorisation
- 🏢 Gestion des cinémas
- 🏠 Gestion des salles
- 📅 Gestion des séances
- 🎉 Participation aux Watch Parties
- ⭐ Système de points de fidélité
- 💬 Gestion des feedbacks
- 📊 Consultation des données et historiques

---

## 🏗️ Architecture du projet

Le projet est conçu autour d'une architecture **Microservices**.

L'architecture cible comprend notamment :
---

## ✨ Fonctionnalités principales

- 🎬 Découverte de films et séries
- 🤖 Recommandations personnalisées basées sur l'IA
- 🎟️ Réservation de billets de cinéma
- 🔐 Authentification et autorisation
- 🏢 Gestion des cinémas
- 🏠 Gestion des salles
- 📅 Gestion des séances
- 🎉 Participation aux Watch Parties
- ⭐ Système de points de fidélité
- 💬 Gestion des feedbacks
- 📊 Consultation des données et historiques

---

🧩 Microservices prévus
Microservice	Responsabilité
Discovery Server	Découverte et enregistrement des services
API Gateway	Point d'entrée et routage des requêtes
Auth Service	Authentification, utilisateurs et sécurité
Movie Service	Films, séries et informations associées
Cinema Service	Cinémas, salles et séances
Reservation Service	Gestion des réservations
Recommendation Service	Gestion des recommandations personnalisées
Watch Party Service	Gestion des sessions Watch Party
Loyalty Service	Gestion des points et de la fidélité
AI Service	Modèles Machine Learning / Deep Learning


🛠️ Technologies
Frontend
- Angular
- TypeScript
- Angular CLI
Backend
- Spring Boot
- Spring Cloud
- REST API
- JWT
- Maven
Base de données
- MongoDB
Intelligence Artificielle
- Python
- Machine Learning
- Deep Learning
Architecture distribuée
- Spring Cloud
- Eureka / Service Discovery
- API Gateway
- Communication interservices
DevOps
- Git / GitHub
- Docker
- Docker Compose
- Jenkins
📚 Documentation
La documentation du projet est organisée en plusieurs parties :
documentation/
│
├── 01-description/
│   ├── contexte.md
│   ├── objectifs.md
│   ├── fonctionnalites.md
│   └── acteurs.md
│
├── 02-conception/
│   ├── use-cases.md
│   ├── class-diagram.md
│   └── sequence-diagrams.md
│
├── 03-architecture/
│   ├── architecture-microservices.md
│   ├── communication.md
│   └── security.md
│
└── images/
    ├── use-case.png
    ├── class-diagram.png
    ├── architecture-globale.png
    ├── architecture-microservices.png
    ├── sequence-login.png
    ├── sequence-reservation.png
    ├── sequence-recommendation.png
    └── sequence-watchparty.png

Description
Cette partie présente :
- le contexte du projet ;
- le problème à résoudre ;
- les objectifs ;
- les acteurs ;
- les fonctionnalités principales.
Conception
Cette partie présente :
- les diagrammes de cas d'utilisation ;
- le diagramme de classes ;
- les diagrammes de séquence ;
- les principaux flux métier.
Architecture
Cette partie présente :
- l'architecture globale ;
- la décomposition en Microservices ;
- le rôle de chaque service ;
- la communication interservices ;
- la sécurité ;
- les choix technologiques.
📊 Évolution du projet
Le projet sera réalisé progressivement selon les étapes suivantes :
Analyse de l'application existante
            ↓
       Conception
            ↓
   Architecture cible
            ↓
     Service Discovery
            ↓
       API Gateway
            ↓
    Microservices métier
            ↓
 Communication interservices
            ↓
        Sécurité
            ↓
    Intelligence Artificielle
            ↓
      Conteneurisation
            ↓
       CI / CD

🚧 État actuel
Le repository est actuellement consacré à :
- la description du projet ;
- l'analyse des besoins ;
- la conception ;
- les diagrammes ;
- la définition de l'architecture Microservices.
L'intégration du code et des différents services sera réalisée progressivement
dans les prochaines étapes du projet.

## Lancement avec Docker Compose

Docker Compose démarre Eureka, la Gateway, les microservices, MongoDB, MySQL et RabbitMQ. Le fichier `.env` à la racine contient des identifiants réservés au développement local ; ne les réutilisez pas en production.

```powershell
docker compose config
docker compose up --build -d
docker compose ps
```

La Gateway est disponible sur `http://localhost:8888`, Eureka sur `http://localhost:8761` et la console RabbitMQ sur `http://localhost:15672`. Les API des microservices sont aussi publiées sur leurs ports `8081` à `8087`.

Pour consulter les journaux : `docker compose logs -f eureka gateway`. Pour arrêter les services sans supprimer les données persistées : `docker compose down`. La suppression des volumes avec `docker compose down -v` efface les bases locales.

👥 Contributors
- Omrani Sarra
- Rezgui Wafa
- Mejri Elee
- Thlibi Ranim
- Essid Iyed
- BenSalem Rania
🎓 Contexte académique
Projet réalisé dans le cadre d'un projet académique à :
ESPRIT – École Supérieure Privée d'Ingénierie et de Technologies
Le projet s'inscrit dans une démarche d'étude des architectures Web
distribuées, de la conception Microservices et du développement
d'applications Cloud Native.
