package com.github.alinvasile.jsla.web.util;

import org.apache.commons.codec.binary.Base64;

public class Util {

    public static String extractUsernameFromAuthorizationHeader(String headerValue){
        String encodedValue = headerValue.split(" ")[1];
        byte[] decodedValue = Base64.decodeBase64(encodedValue.getBytes());
        
        String user = new String(decodedValue);
        
        user = user.split(":")[0];
        
        return user;
    }
    
}
