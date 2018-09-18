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
package sample.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.WebAttributes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

/**
 * @author Joe Grandja
 */
@RestController
public class SecurityController {

	@GetMapping("/principal")
	public ResponseEntity<User> currentPrincipal(@AuthenticationPrincipal OidcUser oidcUser) {
		User user = new User();
		user.setUserId(oidcUser.getClaimAsString("user_name"));
		user.setFirstName(oidcUser.getGivenName());
		user.setLastName(oidcUser.getFamilyName());
		user.setEmail(oidcUser.getEmail());
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@GetMapping(path = "/login", params = "error")
	public ResponseEntity<String> loginError(@SessionAttribute(WebAttributes.AUTHENTICATION_EXCEPTION) AuthenticationException authEx) {
		String errorMsg = authEx != null ? authEx.getMessage() : "[unknown error]";
		return new ResponseEntity<>(errorMsg, HttpStatus.UNAUTHORIZED);
	}
}