package org.github.jsla.example;

import org.jsla.core.authority.Authority;

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

}
