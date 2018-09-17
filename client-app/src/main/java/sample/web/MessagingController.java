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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import sample.data.User;
import sample.data.UserRepository;
import sample.security.CurrentUser;
import sample.security.CustomUserDetails;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;

/**
 * @author Joe Grandja
 */
@RestController
@RequestMapping("/messages")
public class MessagingController {
	private final WebClient webClient;
	private final String messagesBaseUri;
	private final UserRepository userRepository;

	public MessagingController(WebClient webClient,
								@Value("${oauth2.resource.messages-base-uri}") String messagesBaseUri,
								UserRepository userRepository) {
		this.webClient = webClient;
		this.messagesBaseUri = messagesBaseUri;
		this.userRepository = userRepository;
	}

	@GetMapping("/inbox")
	public Iterable<Message> inbox(@RegisteredOAuth2AuthorizedClient("messaging") OAuth2AuthorizedClient messagingClient) {
		return getMessages(messagingClient, this.messagesBaseUri + "/inbox");
	}

	@GetMapping("/sent")
	public Iterable<Message> sent(@RegisteredOAuth2AuthorizedClient("messaging") OAuth2AuthorizedClient messagingClient) {
		return getMessages(messagingClient, this.messagesBaseUri + "/sent");
	}

	private List<Message> getMessages(OAuth2AuthorizedClient messagingClient, String messagesUri) {
		ParameterizedTypeReference<List<Message>> typeRef = new ParameterizedTypeReference<List<Message>>() {};
		return this.webClient
				.get()
				.uri(messagesUri)
				.attributes(oauth2AuthorizedClient(messagingClient))
				.retrieve()
				.bodyToMono(typeRef)
				.block();
	}

	@GetMapping("/{id}")
	public Message get(@PathVariable Long id,
						@RegisteredOAuth2AuthorizedClient("messaging") OAuth2AuthorizedClient messagingClient) {
		return this.webClient
				.get()
				.uri(this.messagesBaseUri + "/" + id)
				.attributes(oauth2AuthorizedClient(messagingClient))
				.retrieve()
				.bodyToMono(Message.class)
				.block();
	}

	@PostMapping
	public Message save(@Valid @RequestBody Message message,
						@CurrentUser CustomUserDetails currentUser,
						@RegisteredOAuth2AuthorizedClient("messaging") OAuth2AuthorizedClient messagingClient) {
		User toUser = this.userRepository.findByEmail(message.getToId());
		message.setToId(toUser.getEmail());
		User fromUser = this.userRepository.findByEmail(currentUser.getUsername());
		message.setFromId(fromUser.getEmail());

		return this.webClient
				.post()
				.uri(this.messagesBaseUri)
				.contentType(MediaType.APPLICATION_JSON)
				.syncBody(message)
				.attributes(oauth2AuthorizedClient(messagingClient))
				.retrieve()
				.bodyToMono(Message.class)
				.block();
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id,
						@RegisteredOAuth2AuthorizedClient("messaging") OAuth2AuthorizedClient messagingClient) {
		this.webClient
			.delete()
			.uri(this.messagesBaseUri + "/" + id)
			.attributes(oauth2AuthorizedClient(messagingClient))
			.retrieve()
			.bodyToMono(Void.class)
			.block();
		return;
	}
}