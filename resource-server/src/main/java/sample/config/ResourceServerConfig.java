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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
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
import sample.security.AudienceValidator;
import sample.security.GrantedAuthoritiesExtractor;
import sample.security.MoreInformativeAuthenticationEntryPoint;

/**
 * @author Joe Grandja
 */
@EnableWebSecurity
public class ResourceServerConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private OAuth2ResourceServerProperties resourceServerProperties;

	// @formatter:off
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.mvcMatchers("/messages/**").access("hasAuthority('messages')")
				.mvcMatchers("/contacts/**").access("hasAuthority('contacts')")
				.anyRequest().authenticated()
				.and()
			.oauth2ResourceServer()
				.authenticationEntryPoint(new MoreInformativeAuthenticationEntryPoint())
				.jwt()
					.jwtAuthenticationConverter(new GrantedAuthoritiesExtractor())
					.decoder(jwtDecoder());
	}
	// @formatter:on

	private JwtDecoder jwtDecoder() {
		String issuerUri = this.resourceServerProperties.getJwt().getIssuerUri();

		NimbusJwtDecoderJwkSupport jwtDecoder =
				(NimbusJwtDecoderJwkSupport) JwtDecoders.fromOidcIssuerLocation(issuerUri);

		OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuerUri);
		OAuth2TokenValidator<Jwt> withAudience = new DelegatingOAuth2TokenValidator<>(
				withIssuer, new AudienceValidator());
		jwtDecoder.setJwtValidator(withAudience);

		return jwtDecoder;
	}
}