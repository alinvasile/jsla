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
package com.github.alinvasile.jsla.web.jee5;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.github.alinvasile.jsla.core.Authority;
import com.github.alinvasile.jsla.core.SlaDeniedException;
import com.github.alinvasile.jsla.core.logging.AtomicRequestId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.github.alinvasile.jsla.web.AccessEngineFactory;
import com.github.alinvasile.jsla.web.authority.AuthorityProvider;
import com.github.alinvasile.jsla.web.context.AccessHierarchy;

public class SlaFilter implements Filter {

    private AccessHierarchy monitor;

    private AuthorityProvider authorityProvider;

    private static final Logger logger = LoggerFactory.getLogger(SlaFilter.class);

    public void destroy() {

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
    ServletException {

        MDC.put("SLA_REQUEST_ID", AtomicRequestId.nextRequestId());

        try{

            Authority authority = authorityProvider.retrieveAuthority(request);

            String fullRequestURI = ((HttpServletRequest)request).getRequestURI();
            String contextPath =  ((HttpServletRequest)request).getContextPath();

            String requestURI = fullRequestURI.substring(fullRequestURI.indexOf(contextPath) + contextPath.length());

            if(logger.isDebugEnabled()){
                logger.debug("fullRequestURI: " + fullRequestURI);
                logger.debug("contextPath: " + contextPath);
                logger.debug("requestURI: " + requestURI);
            }

            logger.info("Authorise " + authority + " for <" + requestURI + ">");

            try{
                monitor.grant(requestURI,authority);
            } catch(SlaDeniedException e){
                //e.printStackTrace();
                throw new ServletException(e);
                //TODO define an error page
            }

            chain.doFilter(request, response);
        } finally {
            MDC.remove("SLA_REQUEST_ID");
        }

    }

    public void init(FilterConfig config) throws ServletException {
        String monitorProviderClass = config.getInitParameter("sla.monitor.provider");
        String authorityProviderClass = config.getInitParameter("sla.authority.provider");

        try {
            AccessEngineFactory factory = (AccessEngineFactory) (Class.forName(monitorProviderClass).newInstance());
            this.monitor = factory.buildAccessMonitor(config.getServletContext());
            this.authorityProvider = (AuthorityProvider) (Class.forName(authorityProviderClass).newInstance());
        } catch (Exception e) {
            throw new ServletException(e);
        } 



    }

}
