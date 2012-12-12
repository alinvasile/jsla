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

import org.apache.commons.codec.binary.Base64;
import org.jsla.core.Authority;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthorizationHeaderAuthorityProvider implements AuthorityProvider {

private static final Logger logger = LoggerFactory.getLogger(AuthorizationHeaderAuthorityProvider.class);
    
    public Authority retrieveAuthority(ServletRequest request) {

        String user = null;
        
        if (request instanceof HttpServletRequest) {
            
            if(logger.isDebugEnabled()){
                logger.debug("retrieveAuthority");
            }
            
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            
            String authHeader = httpServletRequest.getHeader("authorization");
            if(authHeader != null){
                String encodedValue = authHeader.split(" ")[1];
                byte[] decodedValue = Base64.decodeBase64(encodedValue.getBytes());
                
                user = new String(decodedValue);
                
                user = user.split(":")[0];
                
                if(logger.isDebugEnabled()){
                    logger.debug("Found user: " + user);
                }
                
                return DefaultAuthorityUser.createUsernameOnlyUser(user);
            }
            
        }

        return DefaultAuthorityUser.createAnonymousUser();
    }
    
}
