/*
 * Copyright 2012 Alin Vasile
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
package org.jsla.web.jee5;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.jsla.core.AccessMonitor;
import org.jsla.core.Authority;
import org.jsla.core.SlaDeniedException;
import org.jsla.web.MonitorFactory;
import org.jsla.web.authority.AuthorityProvider;

public class SlaFilter implements Filter {

    private AccessMonitor monitor;
    
    private AuthorityProvider authorityProvider;

    public void destroy() {

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {

        Authority authority = authorityProvider.retrieveAuthority(request);
        
        try{
            monitor.grant(authority);
        } catch(SlaDeniedException e){
            e.printStackTrace();
        }
        
        chain.doFilter(request, response);

    }

    public void init(FilterConfig config) throws ServletException {
        String monitorProviderClass = config.getInitParameter("sla.monitor.provider");
        String authorityProviderClass = config.getInitParameter("sla.authority.provider");
        
        try {
            MonitorFactory factory = (MonitorFactory) (Class.forName(monitorProviderClass).newInstance());
            this.monitor = factory.buildAccessMonitor(config.getServletContext());
            this.authorityProvider = (AuthorityProvider) (Class.forName(authorityProviderClass).newInstance());
        } catch (Exception e) {
            throw new ServletException(e);
        } 
        
        
       
    }

}
