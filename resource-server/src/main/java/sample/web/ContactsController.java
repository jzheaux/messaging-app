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

import java.util.ArrayList;
import java.util.List;

import sample.data.UserProfile;
import sample.data.UserProfileRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Joe Grandja
 */
@RestController
@RequestMapping("/contacts")
public class ContactsController {
	private final UserProfileRepository userProfileRepository;

	public ContactsController(UserProfileRepository userProfileRepository) {
		this.userProfileRepository = userProfileRepository;
	}

	@GetMapping
	public Iterable<Contact> getContacts() {
		List<Contact> contacts = new ArrayList<>();
		this.userProfileRepository.findByUserContacts()
				.forEach(userProfile -> contacts.add(asContact(userProfile)));
		return contacts;
	}

	@GetMapping("/{id}")
	public Contact getContact(@PathVariable String id) {
		UserProfile userProfile = this.userProfileRepository.findByUserId(id);
		return userProfile != null ? asContact(userProfile) : null;
	}

	private Contact asContact(UserProfile userProfile) {
		Contact contact = new Contact();
		contact.setUserId(userProfile.getUserId());
		contact.setFirstName(userProfile.getFirstName());
		contact.setLastName(userProfile.getLastName());
		contact.setEmail(userProfile.getEmail());
		return contact;
	}
}