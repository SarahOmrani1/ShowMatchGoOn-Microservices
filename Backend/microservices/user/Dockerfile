# ----------------------------------------------------
# Étape 1 : Phase de compilation (Build avec Maven)
# ----------------------------------------------------
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

# 1. Copier le pom.xml pour télécharger les dépendances
COPY pom.xml .

# 2. Copier le code source
COPY src ./src

# 3. Compiler et générer le fichier .jar (en ignorant les tests)
RUN mvn clean package -DskipTests

# ----------------------------------------------------
# Étape 2 : Phase d'exécution (Image légère Alpine JRE)
# ----------------------------------------------------
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copier uniquement le .jar produit par la première étape
COPY --from=builder /app/target/*.jar app.jar

# Port d'écoute du microservice USER
EXPOSE 8081

# Commande de démarrage de l'application
ENTRYPOINT ["java", "-jar", "app.jar"]