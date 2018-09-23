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

import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.BearerTokenError;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class AudienceValidator implements OAuth2TokenValidator<Jwt> {
	private static final BearerTokenError MISSING_AUDIENCE =
			new BearerTokenError("invalid_token", HttpStatus.UNAUTHORIZED,
					"The token is missing a required audience.", null);

	@Override
	public OAuth2TokenValidatorResult validate(Jwt token) {
		List<String> audience = token.getAudience();
		if (!CollectionUtils.isEmpty(audience) && audience.contains("messaging")) {
			return OAuth2TokenValidatorResult.success();
		} else {
			return OAuth2TokenValidatorResult.failure(MISSING_AUDIENCE);
		}
	}
}