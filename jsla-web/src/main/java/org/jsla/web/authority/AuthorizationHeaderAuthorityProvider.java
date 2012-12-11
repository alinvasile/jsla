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
