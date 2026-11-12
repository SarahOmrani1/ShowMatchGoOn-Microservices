# 1. Image de base officielle Python légère
FROM python:3.11-slim

# 2. Répertoire de travail dans le conteneur
WORKDIR /app

# 3. Variables d'environnement pour optimiser Python dans Docker
ENV PYTHONDONTWRITEBYTECODE=1
ENV PYTHONUNBUFFERED=1

# 4. Copier les dépendances et les installer
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# 5. Copier tout le code du microservice (main.py, etc.)
COPY . .

# Port enregistré dans Eureka et utilisé par FastAPI
EXPOSE 8087

# Lancement du serveur Uvicorn
CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8087"]