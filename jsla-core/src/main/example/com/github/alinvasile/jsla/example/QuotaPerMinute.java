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
package com.github.alinvasile.jsla.example;

import java.util.concurrent.TimeUnit;

import com.github.alinvasile.jsla.core.SlaDeniedException;
import com.github.alinvasile.jsla.core.engine.Sla;
import com.github.alinvasile.jsla.core.engine.SlaValue;
import com.github.alinvasile.jsla.core.engine.UsernameGroupTransactionMonitor;

/**
 * Example implementation for a quota of 5 requests/10 seconds. Rate will be ignored for this case. 
 * 
 * @author Alin Vasile
 *
 */
public class QuotaPerMinute {

    public static void main(String[] args) throws InterruptedException {
    	UsernameGroupTransactionMonitor monitor = new UsernameGroupTransactionMonitor();
        SlaValue rate = new SlaValue(10, 1, TimeUnit.SECONDS, true);
        SlaValue quota = new SlaValue(5, 10, TimeUnit.SECONDS, false);
        
        Sla sla = new Sla(rate, quota);
        monitor.addAnonymousConstraint(sla);
        
        AnonymousUser user = new AnonymousUser();
        for(int i=0;i<5;i++){
            monitor.grant(user); // this should succeed
        }
        
        try{
            monitor.grant(user); // this should fail
            assert(false);
        } catch (SlaDeniedException e){
            e.printStackTrace();
        }
        
        try{
            monitor.grant(user); // this should fail
            assert(false);
        } catch (SlaDeniedException e){
            e.printStackTrace();
        }
        
        Thread.sleep(10 * 1000);  // sleep 10 seconds
        
        
        System.out.println("QuotaPerMinute: OK");
        
        for(int i=0;i<5;i++){
            monitor.grant(user); // this should succeed
        }
        
        try{
            monitor.grant(user); // this should fail
            assert(false);
        } catch (SlaDeniedException e){
            e.printStackTrace();
        }
        
    }
    
    
}
