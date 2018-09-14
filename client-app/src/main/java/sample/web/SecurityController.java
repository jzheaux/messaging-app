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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sample.data.User;
import sample.data.UserRepository;
import sample.security.CurrentUser;
import sample.security.CustomUserDetails;

/**
 * @author Joe Grandja
 */
@RestController
public class SecurityController {
	private final UserRepository userRepository;

	public SecurityController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@RequestMapping(value = "/principal")
	public ResponseEntity<User> currentPrincipal(@CurrentUser CustomUserDetails currentUser) {
		User user = this.userRepository.findById(currentUser.getId()).orElse(null);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}
}