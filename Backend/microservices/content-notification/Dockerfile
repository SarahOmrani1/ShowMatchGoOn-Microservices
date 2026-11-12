# ----------------------------------------------------
# Étape 1 : compilation du microservice avec Maven (JDK 17)
# ----------------------------------------------------
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /workspace

# 1. Copier d'abord le pom.xml et télécharger les dépendances
#    (cette couche est mise en cache tant que le pom.xml ne change pas)
COPY pom.xml .

# 2. Copier le code source et générer le .jar (sans les tests)
COPY src ./src
RUN mvn -B package -DskipTests

# ----------------------------------------------------
# Étape 2 : image d'exécution légère (JRE 17 uniquement)
# ----------------------------------------------------
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copier uniquement le .jar produit par l'étape 1
COPY --from=build /workspace/target/content-notification-0.0.1-SNAPSHOT.jar app.jar

# Port d'écoute du microservice content-notification
EXPOSE 8082

# Démarrage de l'application
ENTRYPOINT ["java", "-jar", "app.jar"]