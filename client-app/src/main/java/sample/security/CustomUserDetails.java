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

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import sample.data.User;
import sample.data.UserAuthority;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Joe Grandja
 */
public class CustomUserDetails implements UserDetails {
	private static final long serialVersionUID = 5639683223516504866L;
	private final User user;
	private final List<GrantedAuthority> authorities;

	public CustomUserDetails(User user) {
		this.user = user;
		this.authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(
				user.getAuthorities().stream().map(UserAuthority::getAuthority).collect(Collectors.joining(", ")));
	}

	public Long getId() {
		return this.user.getId();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	@Override
	public String getPassword() {
		return this.user.getPassword();
	}

	@Override
	public String getUsername() {
		return this.user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}