package org.jsla.web.context;

import org.jsla.core.AccessMonitor;
import org.jsla.core.Authority;
import org.jsla.core.SlaDeniedException;

public interface AccessHierarchy {

    public abstract void add(String contextPath, AccessMonitor monitor);

    public abstract void grant(String requestURI, Authority authority) throws SlaDeniedException;

}