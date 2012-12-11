package org.jsla.web.context;

public class Regexizer {

    public static String escape(String pattern){
        String returnPattern = pattern;
        
        returnPattern = returnPattern.replaceAll("\\/", "\\\\/");
        returnPattern = returnPattern.replaceAll("\\*", "(.*)");     
        
        return returnPattern;        
    }
    
}
