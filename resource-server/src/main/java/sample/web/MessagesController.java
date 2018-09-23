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

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sample.data.Message;
import sample.data.MessageRepository;
import sample.data.UserProfile;
import sample.data.UserProfileRepository;

import javax.validation.Valid;
import java.util.Calendar;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author Joe Grandja
 */
@RestController
@RequestMapping("/messages")
public class MessagesController {
	private static final String CONTACTS_AUTHORITY = "SCOPE_contacts";
	private final MessageRepository messageRepository;
	private final UserProfileRepository userProfileRepository;

	public MessagesController(
			MessageRepository messageRepository,
			UserProfileRepository userProfileRepository) {
		this.messageRepository = messageRepository;
		this.userProfileRepository = userProfileRepository;
	}

	@GetMapping("/inbox")
	public Iterable<Message> inbox(@AuthenticationPrincipal JwtAuthenticationToken token) {
		Collection<Message> messages = this.messageRepository.getInbox();
		if (hasAuthority(token, CONTACTS_AUTHORITY)) {
			return messages.stream()
					.map(this::addUserInformation)
					.collect(Collectors.toList());
		}
		return messages;
	}

	@GetMapping("/sent")
	public Iterable<Message> sent(@AuthenticationPrincipal JwtAuthenticationToken token) {
		Collection<Message> messages = this.messageRepository.getSent();
		if (hasAuthority(token, CONTACTS_AUTHORITY)) {
			return messages.stream()
					.map(this::addUserInformation)
					.collect(Collectors.toList());
		}
		return messages;
	}

	@GetMapping("/{id}")
	public Message get(@AuthenticationPrincipal JwtAuthenticationToken token,
						@PathVariable Long id) {
		Message message = this.messageRepository.findById(id).orElse(null);
		if (hasAuthority(token, CONTACTS_AUTHORITY)) {
			return addUserInformation(message);
		}
		return message;
	}

	@PostMapping
	public Message save(@AuthenticationPrincipal JwtAuthenticationToken token,
						@Valid @RequestBody Message message) {
		message.setCreated(Calendar.getInstance());
		message = this.messageRepository.save(message);
		if (hasAuthority(token, CONTACTS_AUTHORITY)) {
			return addUserInformation(message);
		}
		return message;
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		this.messageRepository.deleteById(id);
		return;
	}

	private Message addUserInformation(Message message) {
		UserProfile fromUser = this.userProfileRepository.findByUserId(message.getFromId());
		UserProfile toUser = this.userProfileRepository.findByUserId(message.getToId());
		message.setFromId(fromUser.getFirstName());
		message.setToId(toUser.getFirstName());
		return message;
	}

	private boolean hasAuthority(Authentication authentication, String authority) {
		return authentication.getAuthorities().stream()
				.anyMatch(a -> a.getAuthority().equals(authority));
	}
}