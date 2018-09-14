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

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import sample.data.UserRepository;
import sample.security.CurrentUser;
import sample.security.CustomUserDetails;

import javax.validation.Valid;
import java.util.Collections;

/**
 * @author Joe Grandja
 */
@RestController
@RequestMapping(value = "/messages")
public class MessageController {
	private final UserRepository userRepository;

	public MessageController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@RequestMapping(value = "/inbox")
	public Iterable<Message> inbox() {
		// TODO
		return Collections.emptyList();
	}

	@RequestMapping(value = "/sent")
	public Iterable<Message> sent() {
		// TODO
		return Collections.emptyList();
	}

	@RequestMapping(value = "/{id}")
	public Message get(@PathVariable Long id) {
		// TODO
		return new Message();
	}

	@RequestMapping(method = RequestMethod.POST)
	public Message save(@Valid @RequestBody Message message, @CurrentUser CustomUserDetails currentUser) {
		message.setToUserId(this.userRepository.findByEmail(message.getToUserId()).getEmail());
		message.setFromUserId(this.userRepository.findByEmail(currentUser.getUsername()).getEmail());

		// TODO POST
		return message;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable Long id) {
		// TODO
	}
}