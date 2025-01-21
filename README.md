# MediLabo

MediLabo est une application modulaire construite avec une architecture de microservices. Elle a pour but de faciliter la gestion des données liées à des patients, tout en offrant une interface utilisateur moderne et intuitive.

---

## 🌐 Architecture du projet

Le projet MediLabo est composé des éléments suivants :

### 🏗️ 1. **Projet principal : MediLabo**

- Contient le fichier `docker-compose.yml` pour orchestrer les microservices et d'autres ressources (comme les bases de données et les services).

### 🔧 2. **Microservices :**

#### 🛠️ a. **config-server**
   - **Rôle** : Fournit une configuration centralisée pour tous les microservices.
   - **Fonctionnalités** :
     - Chargement dynamique des paramètres depuis des dépôts Git ([dépôt des configurations](https://github.com/DarkMammut/Medilabo-Config-Server.git)).

#### 🗂️ b. **eureka-server**
   - **Rôle** : Serveur de registre pour les microservices.
   - **Fonctionnalités** :
     - Gestion de la découverte des services.

#### 🌉 c. **api-gateway**
   - **Rôle** : Sert de passerelle entre les différents microservices et le frontend.
   - **Fonctionnalités** :
     - Routage des requêtes.
     - Gestion de la sécurité (authentification et autorisation).

#### 👩‍⚕️ d. **patient-service**
   - **Rôle** : Gère les opérations métiers liées aux patients.
   - **Fonctionnalités** :
     - CRUD des données des patients.
     - Connecté à une base de données SQL via Docker.
   - **Remarque** : Ce microservice ne contient pas encore de fichier `application.properties` ou `application.yml` configuré.

#### 📝 e. **notes-service**
   - **Rôle** : Gère les notes médicales des patients.
   - **Fonctionnalités** :
     - CRUD des notes.
     - Connecté à une base de données MongoDB (`notes-db`).
   - **Remarque** : La base de données MongoDB est configurée pour stocker toutes les notes des patients.

#### ⚠️ f. **risk-service**
   - **Rôle** : Évalue les risques médicaux des patients.
   - **Fonctionnalités** :
     - Analyse des données patient pour déterminer les risques.

#### 💻 g. **frontend**
   - **Rôle** : Application web construite avec React pour consommer les API via l'`api-gateway`.
   - **Fonctionnalités** :
     - Interface utilisateur pour interagir avec les données des patients.
     - Communication avec l'`api-gateway` pour récupérer et envoyer les données.

---

## ⚙️ Configuration

### 🐳 Docker Compose
Le fichier `docker-compose.yml` dans le projet principal est utilisé pour :
- Lancer les conteneurs des microservices.
- Configurer les bases de données et les dépendances requises.

### 🗄️ Base de données
- Le microservice `patient-service` utilise une base de données SQL, exécutée via un conteneur Docker.
- Le microservice `notes-service` utilise une base de données MongoDB (`notes-db`) pour stocker les notes des patients.
- Les paramètres de connexion aux bases de données doivent être définis dans des fichiers de configuration (non inclus par défaut).

---

## 📋 Prérequis

- **Docker** et **Docker Compose** installés sur votre machine.
- **Node.js** et **npm** (ou **yarn**) pour le développement du frontend.
- **JDK 21** pour exécuter les microservices backend.

---

## 🚀 Instructions d'installation

1. Clonez le dépôt :
   ```bash
   git clone https://github.com/DarkMammut/P9-MediLabo.git
   cd MediLabo
   ```

2. Lancez les services avec Docker Compose :
   ```bash
   docker-compose up --build
   ```

3. Accédez à l'application frontend :
   - Ouvrez un navigateur et rendez-vous sur [http://localhost:3000](http://localhost:3000) (par défaut).

4. Testez les microservices (API) :
   - L'API Gateway est accessible sur [http://localhost:8080](http://localhost:8080).

---

## 🛠️ Développement

Pour travailler sur un microservice ou le frontend :

### 🎨 Frontend
1. Allez dans le dossier `frontend` :
   ```bash
   cd frontend
   ```
2. Installez les dépendances :
   ```bash
   npm install
   ```
3. Lancez le serveur de développement :
   ```bash
   npm start
   ```

### ⚙️ Backend (Exemple : patient-service)
1. Allez dans le dossier du microservice :
   ```bash
   cd patient-service
   ```
2. Configurez un fichier `application.properties` ou `application.yml` avec les paramètres nécessaires.
3. Lancez l'application :
   ```bash
   ./mvnw spring-boot:run
   ```

---

Pour toute question ou assistance, veuillez contacter l'équipe de développement ou ouvrir une issue sur le dépôt.
