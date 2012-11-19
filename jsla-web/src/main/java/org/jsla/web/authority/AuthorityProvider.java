package org.jsla.web.authority;

import javax.servlet.ServletRequest;

import org.jsla.core.Authority;

public interface AuthorityProvider {

	Authority retrieveAuthority(ServletRequest request);
	
}
