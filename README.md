# Diabetes Risk Assessment App

Application web en architecture microservices permettant d'évaluer le risque de diabète de type 2 chez les patients, à partir de données démographiques et de notes médicales.

## Fonctionnalités principales

-   Création et gestion des patients
-   Ajout de notes médicales
-   Analyse du risque de diabète de type 2 à partir des données
-   Interface web interactive et responsive
-   Authentification sécurisée via JWT

## Exemple de scénario utilisateur

1. L'utilisateur se connecte via le frontend
2. Le `auth-service` retourne un JWT
3. Le frontend stocke ce token et l’utilise pour faire des requêtes
4. La `gateway-service` valide le JWT et route vers le bon service
5. Les services accèdent aux données et retournent les résultats (ex: liste des patients)

## Structure du projet

```plaintext
diabetes-risk-app/
│
├── frontend/                   # Application Angular (v19)
├── gateway-service/            # API Gateway (Spring Boot 3.4.5)
├── auth-service/               # Authentification JWT (Spring Boot 3.4.5)
├── patient-service/            # Gestion des patients (Spring Boot 3.4.5, PostgreSQL v16)
├── notes-service/              # Notes médicales (Spring Boot 3.4.5, MongoDB latest)
├── risk-assessment-service/    # Calcul du risque de diabète (Spring Boot 3.4.5)
└── docker-compose.yml          # Orchestration complète
```

## Authentification et Sécurité

-   Le `auth-service` génère un **JWT** à la connexion via `jsonwebtoken v0.12.6`.
-   La `gateway-service` intercepte toutes les requêtes sortantes du frontend :
    -   Valide le token
    -   Ajoute un en-tête `X-User-Validation: true` avant de transmettre la requête au bon service
-   Chaque microservice backend vérifie :
    -   La présence du JWT dans l’en-tête `Authorization`
    -   L'en-tête `X-User-Validation`

## Lancement avec Docker Compose

### Prérequis

-   Docker & Docker Compose installés

### Lancer l'application

```bash
git clone https://github.com/The-Great-Toad/diabetes-risk-app.git
cd diabetes-risk-app
docker-compose up --build -d
```

Les services seront disponibles à ces adresses :

-   Frontend : http://localhost:4200
-   Gateway : http://localhost:8080
-   Patient Service : http://localhost:8081
-   Notes Service : http://localhost:8082
-   Risk Assessment Service : http://localhost:8083
-   Auth Service : http://localhost:8084

## Documentation (Swagger)

Les documentations des microservices seront disponibles à ces adresse :

-   Patient service : http://localhost:8081/api/swagger-ui/index.html#/
-   Notes service : http://localhost:8082/api/swagger-ui/index.html#/
-   Risk Assessment service : http://localhost:8083/api/swagger-ui/index.html#/

## Tests

Chaque service peut être testé de manière indépendante. Exemple :

```bash
cd patient-service
./mvnw test
```
