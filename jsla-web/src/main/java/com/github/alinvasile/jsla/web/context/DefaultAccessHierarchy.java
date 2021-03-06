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
package com.github.alinvasile.jsla.web.context;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.alinvasile.jsla.core.AccessMonitor;
import com.github.alinvasile.jsla.core.Authority;
import com.github.alinvasile.jsla.core.NoRateDefinedException;
import com.github.alinvasile.jsla.core.SlaDeniedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultAccessHierarchy implements AccessHierarchy {

    private static final Logger logger = LoggerFactory.getLogger(DefaultAccessHierarchy.class);
    
    private Map<String, AccessMonitor> monitorTree = new ConcurrentHashMap<String, AccessMonitor>();
    
    private SortedMap<String, Pattern> patternMap = new TreeMap<String, Pattern>(new Comparator<String>() {

        public int compare(String arg0, String arg1) {
            return arg1.compareTo(arg0);
        }
        
    }); 
    
    /* (non-Javadoc)
     * @see com.github.alinvasile.jsla.web.context.AccessHierarchy#add(java.lang.String, com.github.alinvasile.jsla.core.AccessMonitor)
     */
    public synchronized void add(String contextPath, AccessMonitor monitor) {
        if(logger.isDebugEnabled()){
            logger.debug("Add context path: " + contextPath);
        }
        
        String escape = Regexizer.escape(contextPath);
        Pattern compile = Pattern.compile(escape);     
        
        if(logger.isDebugEnabled()){
            logger.debug("Escaped context path: " + escape);
        }
        
        monitorTree.put(contextPath,monitor);
        patternMap.put(contextPath, compile);
    }
    
    /* (non-Javadoc)
     * @see com.github.alinvasile.jsla.web.context.AccessHierarchy#grant(java.lang.String, com.github.alinvasile.jsla.core.Authority)
     */
    public synchronized void grant(String requestURI, Authority authority) throws SlaDeniedException {
        Set<String> set = patternMap.keySet();
        
        if(logger.isDebugEnabled()){
            logger.debug("Configured context paths: " + set);
        }
        
        Iterator<String> iterator = set.iterator();
        
        SlaDeniedException lastException = null;
        
        while(iterator.hasNext()){
            String contextPath = iterator.next();
            
            if(logger.isDebugEnabled()){
                logger.debug("Evaluating: " + contextPath);
            }
            
            Pattern pattern = patternMap.get(contextPath);
            Matcher matcher = pattern.matcher(requestURI);
            boolean matches = matcher.matches();
            
            if(logger.isDebugEnabled()){
                logger.debug("MATCH: " + matches);
            }
            
            if(matches){
                AccessMonitor accessMonitor = monitorTree.get(contextPath);
                try{
                    accessMonitor.grant(authority);
                    return;
                } catch(SlaDeniedException e){
                    if(logger.isDebugEnabled()){
                        logger.debug("Resulted exception: ", e.getMessage());
                    }
                    lastException = e;
                }
            }
        }
        
        if(lastException != null){
            logger.error("About to throw: ", lastException);
            throw lastException;
        }
        
        if(logger.isInfoEnabled()){
            logger.debug("No SLA activated for " + authority + ", context path is <" + requestURI + ">");
        }
        
        throw new NoRateDefinedException();
    }
    
}
