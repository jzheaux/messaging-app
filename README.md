# Securing OAuth 2.0 Resources with Spring Security 5.1

# Sample Demo

## Initial Setup

- Set the `ext.tomcatHomeDir` in `uaa-server/build.gradle` to the local distribution of Tomcat 8.x
- Download UAA -> `./gradlew downloadUAA`

## Run the Sample

- Build the sample -> `./gradlew clean build`
- Run UAA -> `./gradlew -b uaa-server/build.gradle cargoRunLocal`
- Run the Resource Server -> `./gradlew -b resource-server/build.gradle bootRun`
- Run the Client App -> `./gradlew -b client-app/build.gradle bootRun`
- Go to http://localhost:8080 and login to UAA using credentials *springsec5 / password*


# Presentation Outline

1. A basic Authorization Code grant flow

Joe would hypothetically have a vanilla UAA running, with users already added. The configured apps on UAA may have scopes and audiences also already configured.

Then, it's a matter of adding the appropriate configuration to the client and doing a Hello, World.

2. Add in Resource Server

Joe could then maybe show some badness, like that if he connects via WebClient to a certain (unprotected) RESTful service, his client app can show privileged information.

But, if Josh adds Resource Server protection, then folks at least need to have a legit OAuth cred, or even better, an OAuth cred with the correct scope (the admin can see the data, but not a plain user). I'm thinking that we begin with just the issuer spring boot configuration, and then add some filter security.

3. Not everything is happy, though

This works great for a basic OAuth 2.0 server with no curious nuances or deviations. There are places where we are not fully compliant yet; there are popular Authorization Servers out there that have additional requirements or data.

One of the most exciting aspects of Spring Security 5.1 is places where we are now able to customize the flow.

  1. Custom claims (we might think about integrating with Keycloak instead of or in addition to UAA. It's also local AND it does deviate from the spec a bit with the way it expresses authority)
  
  On the client side... magical things that only Joe knows.
  
  Then, on the Resource Server side, we break down Jwts into two pieces. The first is going from the serialized and encoded token to a decoded Java object called "Jwt". The second is going from this "Jwt" to an Authentication. Since this scenario is about generating GrantedAuthorities for our Authentication object, let's choose the second injection point. We can wire a custom JwtAuthenticationConverter whose job it is to convert a Jwt into a Spring Security Authentication.
  
  2. Custom validation
  
  Now, just because a user's authority is scoped for a particular family of activities, we may need to further narrow their authority to certain endpoints. 
  
  Client side.
  
  For Resource Server, issuer validation comes by default when we use the default issuer configuration. But, we can also easily add our own validation steps to this to make sure that the audience matches what we'd expect...
  
  3. Custom error handling

  And we all know what our least favorite thing of all is about error messaging right? When there are seven problems, but the UI will only tell us about them one at a time? The tricky bit is... OAuth 2 spec limitations... so, you can also customize the error handling that resource server does....
  