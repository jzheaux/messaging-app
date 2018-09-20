/*
 * Copyright 2002-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package sample.config;

import sample.security.AudienceValidator;
import sample.security.GrantedAuthoritiesExtractor;
import sample.security.MoreInformativeAccessDeniedHandler;
import sample.security.MoreInformativeAuthenticationEntryPoint;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoderJwkSupport;

/**
 * @author Josh Cummings
 */
@EnableWebSecurity
public class ResourceServerConfiguration extends WebSecurityConfigurerAdapter {

	@Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
	String issuerUri;

	// @formatter:off
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.mvcMatchers("/messages/**").access("@authorizer.complainingHasAuthority(authentication, 'messages')")
				.mvcMatchers("/contacts/**").access("@authorizer.complainingHasAuthority(authentication, 'contacts')")
				.anyRequest().authenticated()
				.and()
			.oauth2ResourceServer()
				.accessDeniedHandler(new MoreInformativeAccessDeniedHandler())
				.authenticationEntryPoint(new MoreInformativeAuthenticationEntryPoint())
				.jwt()
					.jwtAuthenticationConverter(new GrantedAuthoritiesExtractor());
	}
	// @formatter:on

	@Bean
	JwtDecoder jwtDecoder() {
		NimbusJwtDecoderJwkSupport decoder = (NimbusJwtDecoderJwkSupport)
				JwtDecoders.fromOidcIssuerLocation(this.issuerUri);

		OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(this.issuerUri);
		OAuth2TokenValidator<Jwt> withAudience =
				new DelegatingOAuth2TokenValidator<>(withIssuer, new AudienceValidator());
		decoder.setJwtValidator(withAudience);

		return decoder;
	}
}