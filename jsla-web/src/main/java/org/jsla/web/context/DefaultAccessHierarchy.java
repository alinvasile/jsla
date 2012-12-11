package org.jsla.web.context;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsla.core.AccessMonitor;
import org.jsla.core.Authority;
import org.jsla.core.NoRateDefinedException;
import org.jsla.core.SlaDeniedException;

public class DefaultAccessHierarchy implements AccessHierarchy {

    private Map<String, AccessMonitor> monitorTree = new ConcurrentHashMap<String, AccessMonitor>();
    
    private SortedMap<String, Pattern> patternMap = new TreeMap<String, Pattern>(new Comparator<String>() {

        public int compare(String arg0, String arg1) {
            return arg1.compareTo(arg0);
        }
        
    }); 
    
    /* (non-Javadoc)
     * @see org.jsla.web.context.AccessHierarchy#add(java.lang.String, org.jsla.core.AccessMonitor)
     */
    public synchronized void add(String contextPath, AccessMonitor monitor) {
        monitorTree.put(contextPath,monitor);
        Pattern compile = Pattern.compile(contextPath);        
        patternMap.put(contextPath, compile);
    }
    
    /* (non-Javadoc)
     * @see org.jsla.web.context.AccessHierarchy#grant(java.lang.String, org.jsla.core.Authority)
     */
    public synchronized void grant(String requestURI, Authority authority) throws SlaDeniedException {
        Set<String> set = patternMap.keySet();
        Iterator<String> iterator = set.iterator();
        
        SlaDeniedException lastException = null;
        
        while(iterator.hasNext()){
            String contextPath = iterator.next();
            
            Pattern pattern = patternMap.get(contextPath);
            Matcher matcher = pattern.matcher(requestURI);
            if(matcher.matches()){
                AccessMonitor accessMonitor = monitorTree.get(contextPath);
                try{
                    accessMonitor.grant(authority);
                    return;
                } catch(SlaDeniedException e){
                    lastException = e;
                }
            }
        }
        
        if(lastException != null){
            throw lastException;
        }
        
        throw new NoRateDefinedException();
    }
    
}
