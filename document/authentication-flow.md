# JSESSIONID Lifecycle Flow in Money Management Application

This diagram visualizes the flow of generating, using, and expiring the JSESSIONID session cookie in the application.

```mermaid
sequenceDiagram
    participant User as User (Browser)
    participant Frontend as Frontend (Vue.js)
    participant Backend as Backend (Spring Security + Servlet Container)

    User->>Frontend: Click "Login with Google"
    Frontend->>Backend: Redirect to OAuth2 Google Login
    Backend->>Google: OAuth2 Authentication Request
    Google->>Backend: OAuth2 Authentication Response (Success)
    Backend->>Backend: Create HTTP Session
    Backend->>User: Set-Cookie: JSESSIONID (Session Cookie)
    User->>Frontend: Store JSESSIONID Cookie
    Frontend->>Backend: API Requests with JSESSIONID Cookie (withCredentials: true)
    Backend->>Backend: Validate Session using JSESSIONID
    Backend->>Frontend: Return Protected Resources
    User->>Frontend: Logout
    Frontend->>Backend: Logout Request
    Backend->>Backend: Invalidate HTTP Session (JSESSIONID invalidated)
    Backend->>User: Set-Cookie: JSESSIONID expired/removed

    Note over Backend: Session expires after timeout period (default or configured)
    Note over Backend: After expiration, JSESSIONID is invalid and user must re-authenticate
```

## Explanation

- **JSESSIONID Generation:** When the user successfully logs in via OAuth2 (Google login), Spring Security creates an HTTP session. The servlet container generates the JSESSIONID cookie and sends it to the client.
- **Using JSESSIONID:** The frontend sends the JSESSIONID cookie with every API request (enabled by `withCredentials: true` in axios). The backend uses this cookie to identify and validate the user's session.
- **JSESSIONID Expiration:** The session (and thus JSESSIONID) expires after a configured timeout or when the user logs out. After expiration, the session is invalidated, and the user must log in again to get a new JSESSIONID.

This flow ensures secure session-based authentication without requiring an Authorization header on every request.
