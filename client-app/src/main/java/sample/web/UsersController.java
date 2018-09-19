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

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;

/**
 * @author Joe Grandja
 */
@RestController
@RequestMapping("/users")
public class UsersController {
	private final WebClient webClient;
	private final String contactsBaseUri;

	public UsersController(WebClient webClient,
							@Value("${oauth2.resource.contacts-base-uri}") String contactsBaseUri) {
		this.webClient = webClient;
		this.contactsBaseUri = contactsBaseUri;
	}

	@GetMapping
	public Iterable<User> getUsers(@RegisteredOAuth2AuthorizedClient("messaging") OAuth2AuthorizedClient contactsClient) {
		ParameterizedTypeReference<List<User>> typeRef = new ParameterizedTypeReference<List<User>>() {};
		return this.webClient
				.get()
				.uri(this.contactsBaseUri)
				.attributes(oauth2AuthorizedClient(contactsClient))
				.retrieve()
				.bodyToMono(typeRef)
				.block();
	}

	@GetMapping("/{id}")
	public User getUser(@PathVariable Long id,
						@RegisteredOAuth2AuthorizedClient("messaging") OAuth2AuthorizedClient contactsClient) {
		return this.webClient
				.get()
				.uri(this.contactsBaseUri + "/" + id)
				.attributes(oauth2AuthorizedClient(contactsClient))
				.retrieve()
				.bodyToMono(User.class)
				.block();
	}
}