package sample.security;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandler;

public class MoreInformativeAccessDeniedHandler implements AccessDeniedHandler {
	private AccessDeniedHandler delegate = new BearerTokenAccessDeniedHandler();

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
		this.delegate.handle(request, response, e);

		if ( e instanceof OAuth2AccessDeniedException ) {
			response.getWriter().println("The missing scope is " + ((OAuth2AccessDeniedException) e).getScope());
		}
	}
}
