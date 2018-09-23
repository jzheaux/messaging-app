# Securing OAuth 2.0 Resources with Spring Security 5.1

## Initial Setup

- Set the `ext.tomcatHomeDir` in `uaa-server/build.gradle` to the local distribution of Tomcat 8.x
- Download UAA -> `./gradlew downloadUAA`

## Run the Sample

- Build the sample -> `./gradlew clean build`
- Run UAA -> `./gradlew -b uaa-server/build.gradle cargoRunLocal`
- Run the Resource Server -> `./gradlew -b resource-server/build.gradle bootRun`
- Run the Client App -> `./gradlew -b client-app/build.gradle bootRun`
- Go to `http://localhost:8080` and login to UAA using one of the registered users in `uaa.yml` under `scim.users`
