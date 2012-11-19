package org.jsla.example;

import org.jsla.core.Authority;

public class AnonymousUser implements Authority {

    public String getUsername() {
        return null;
    }

    public String getGroup() {
        return null;
    }

    public boolean isAnonymous() {
        return true;
    }

	public String[] getRoles() {
		return null;
	}

}
