package org.jsla.web.authority;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.jsla.core.authority.Authority;

public class HttpBasicAuthorityProvider implements AuthorityProvider {

	public Authority retrieveAuthority(ServletRequest request) {
		
		if(request instanceof HttpServletRequest){
			HttpServletRequest httpServletRequest = (HttpServletRequest)request;
			String user = httpServletRequest.getRemoteUser();
			if(user != null){
				return DefaultAuthorityUser.createUsernameOnlyUser(user);
			}
		}
		
		return DefaultAuthorityUser.createAnonymousUser();
	}

}
