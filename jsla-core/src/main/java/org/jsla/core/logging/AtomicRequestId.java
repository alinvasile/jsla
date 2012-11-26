package org.jsla.core.logging;

import java.util.concurrent.atomic.AtomicLong;

public class AtomicRequestId {

    private static final AtomicLong requestId = new AtomicLong(0);
    
    public static String nextRequestId(){
        long value = requestId.incrementAndGet();
        return String.format("%08x",value);
    }
    
}
