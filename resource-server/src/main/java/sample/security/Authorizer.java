package sample.security;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class Authorizer {
	public boolean complainingHasAuthority(Authentication authentication, String scope) {
		if (!hasAuthority(authentication, scope)) {
			throw new OAuth2AccessDeniedException("Access is denied", scope);
		}

		return true;
	}

	private boolean hasAuthority(Authentication authentication, String authority) {
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(authority);
		return authorities.contains(grantedAuthority);
	}
}
