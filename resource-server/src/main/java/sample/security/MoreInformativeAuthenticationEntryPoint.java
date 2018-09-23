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
package sample.security;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

public class MoreInformativeAuthenticationEntryPoint implements AuthenticationEntryPoint {
	private BearerTokenAuthenticationEntryPoint delegate = new BearerTokenAuthenticationEntryPoint();

	private ObjectMapper mapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
						 AuthenticationException reason) throws IOException, ServletException {

		this.delegate.commence(request, response, reason);

		if (reason.getCause() instanceof JwtValidationException) {
			JwtValidationException validationException = (JwtValidationException) reason.getCause();
			Collection<OAuth2Error> errors = validationException.getErrors();
			this.mapper.writeValue(response.getWriter(), errors);
		}
	}
}
