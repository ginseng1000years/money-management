# Money Management Application

## Project Description
This is a Money Management Application consisting of a Vue 3 single-page frontend and a Spring Boot backend. The application supports user authentication via OAuth2 Google Single Sign-On (SSO) and manages financial data stored in a MongoDB database.

## Architecture Overview
The project is divided into three main parts:

- **Frontend (Vue 3 SPA)**:  
  A Vue 3 single-page application with routing for Login, Home, and OAuth2 Callback views. It communicates with the backend for authentication and data operations.

- **Backend (Spring Boot)**:  
  A Spring Boot application configured with OAuth2 Google SSO, JWT validation, and CORS settings. It exposes REST APIs via controllers that interact with the MongoDB database.

- **Services**:  
  MongoDB runs as a container managed by Docker Compose.

## Backend

### Technology Stack
- Java 11
- Spring Boot 2.7.5
- Spring Security with OAuth2 Client
- MongoDB
- Maven for build and dependency management

### Build and Run
1. Ensure you have Java 11 and Maven installed.
2. Navigate to the `backend` directory.
3. Build the project:
   ```bash
   mvn clean install
   ```
4. Run the application:
   ```bash
   mvn spring-boot:run
   ```

## Frontend

### Technology Stack
- Vue 3
- Vue Router
- Axios
- Vue CLI Service

### Build and Run
1. Ensure you have Node.js and npm installed.
2. Navigate to the `frontend` directory.
3. Install dependencies:
   ```bash
   npm install
   ```
4. Run the development server:
   ```bash
   npm run serve
   ```
5. To build for production:
   ```bash
   npm run build
   ```

## Deployment

### MongoDB Service
MongoDB runs as a Docker container managed by Docker Compose.

To start the MongoDB container, run the following command in the `deployment` directory:
```bash
docker-compose up -d
```

This will start MongoDB on port 27017 with data persisted in a Docker volume.

## Documentation
Additional documentation can be found in the `document` directory, including:
- Authentication flow
- Sequence diagrams for category operations
- Architecture diagram (`architecture-diagram.md`)

## License
This project does not currently include a license. Please add one if needed.
