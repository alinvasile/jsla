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
package org.jsla.web.authority;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.jsla.core.Authority;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpBasicAuthorityProvider implements AuthorityProvider {

    private static final Logger logger = LoggerFactory.getLogger(HttpBasicAuthorityProvider.class);
    
    public Authority retrieveAuthority(ServletRequest request) {

        if (request instanceof HttpServletRequest) {
            
            if(logger.isDebugEnabled()){
                logger.debug("retrieveAuthority");
            }
            
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            
            String user = httpServletRequest.getRemoteUser();
            
            if(logger.isDebugEnabled()){
                logger.debug("Found user: " + user);
            }
            
            
            if (user != null) {
                return DefaultAuthorityUser.createUsernameOnlyUser(user);
            }
        }

        return DefaultAuthorityUser.createAnonymousUser();
    }

}
