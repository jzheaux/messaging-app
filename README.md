# Securing OAuth 2.0 Resources in Spring Security 5.1

## Presentation Outline

1. Demo the Messaging application (complete state)
2. Demo an _"unprotected"_ Resource Server
3. Enable Resource Server (minimal configuration)
4. Protect resources using _"scope-based"_ authorization 
5. Configure `WebClient` for protected resource requests
6. Customize Authorization Request (Authorization Code Grant)
7. JWT Authority Mapping
8. JWT Claims Validation
9. Customize Error Handling
10. Customize Token Response Handling

## Sample Setup

### Initial Setup

- Set the `ext.tomcatHomeDir` in `uaa-server/build.gradle` to the local distribution of Tomcat 8.x
- Download UAA -> `./gradlew downloadUAA`

### Run the Sample

- Build the sample -> `./gradlew clean build`
- Run UAA -> `./gradlew -b uaa-server/build.gradle cargoRunLocal`
- Run the Resource Server -> `./gradlew -b resource-server/build.gradle bootRun`
- Run the Client App -> `./gradlew -b client-app/build.gradle bootRun`
- Go to `http://localhost:8080` and login to UAA using one of the registered users in `uaa.yml` under `scim.users`
