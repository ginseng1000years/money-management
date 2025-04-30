# Project Architecture Diagram

```mermaid
graph TD
  subgraph Frontend (Vue 3 SPA)
    A1[App.vue (Root Component)]
    A2[Vue Router]
    A3[Login View]
    A4[Home View]
    A5[Callback View]
    A1 --> A2
    A2 --> A3
    A2 --> A4
    A2 --> A5
  end

  subgraph Backend (Spring Boot)
    B1[MoneyManagementApplication.java (Main App)]
    B2[SecurityConfig (OAuth2 Google SSO)]
    B3[JwtConfig (JWT Validation)]
    B4[CorsConfig (CORS Settings)]
    B5[UserController (REST API)]
    B6[MongoDB Database]
    B1 --> B2
    B1 --> B3
    B1 --> B4
    B1 --> B5
    B5 --> B6
  end

  subgraph Services
    S1[MongoDB Container (docker-compose)]
  end

  %% Connections
  A3 -->|OAuth2 Login| B2
  A5 -->|OAuth2 Callback| B2
  A4 -->|API Requests| B5
  B5 -->|Data Storage| S1
```

## Description

- The **Frontend** is a Vue 3 single-page application with routing for Login, Home, and OAuth2 Callback views.
- The **Backend** is a Spring Boot application configured with OAuth2 Google SSO, JWT validation, and CORS settings.
- The backend exposes REST APIs via controllers (e.g., UserController) that interact with the MongoDB database.
- MongoDB runs as a container managed by docker-compose.
- The frontend communicates with the backend for authentication and data operations.
