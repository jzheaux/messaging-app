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

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sample.data.User;
import sample.data.UserRepository;
import sample.security.CurrentUser;
import sample.security.CustomUserDetails;

import javax.validation.Valid;
import java.util.Collections;

/**
 * @author Joe Grandja
 */
@RestController
@RequestMapping("/messages")
public class MessageController {
	private final UserRepository userRepository;

	public MessageController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@GetMapping("/inbox")
	public Iterable<Message> inbox() {
		// TODO
		return Collections.emptyList();
	}

	@GetMapping("/sent")
	public Iterable<Message> sent() {
		// TODO
		return Collections.emptyList();
	}

	@GetMapping("/{id}")
	public Message get(@PathVariable Long id) {
		// TODO
		return new Message();
	}

	@PostMapping
	public Message save(@Valid @RequestBody Message message, @CurrentUser CustomUserDetails currentUser) {
		User toUser = this.userRepository.findByEmail(message.getToId());
		message.setToId(toUser.getEmail());
		User fromUser = this.userRepository.findByEmail(currentUser.getUsername());
		message.setFromId(fromUser.getEmail());

		// TODO POST
		return message;
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		// TODO
	}
}