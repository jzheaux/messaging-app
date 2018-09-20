package sample.security;

import org.springframework.security.access.AccessDeniedException;

public class OAuth2AccessDeniedException extends AccessDeniedException {
	private String scope;

	public OAuth2AccessDeniedException(String message, String scope) {
		super(message);
		this.scope = scope;
	}

	public String getScope() {
		return scope;
	}
}
