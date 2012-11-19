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
package org.jsla.example;

import java.util.concurrent.TimeUnit;

import org.jsla.core.SlaDeniedException;
import org.jsla.core.monitor.UsernameGroupTransactionMonitor;
import org.jsla.core.sla.Sla;
import org.jsla.core.sla.SlaValue;

/**
 * Example implementation for a rate of 5 requests/second. Quota will be ignored in this case. 
 * 
 * @author Alin Vasile
 *
 */
public class RatePerSecond {

   
    public static void main(String[] args) throws InterruptedException {
       
    	UsernameGroupTransactionMonitor monitor = new UsernameGroupTransactionMonitor();
        SlaValue rate = new SlaValue(5, 1, TimeUnit.SECONDS, false);
        SlaValue quota = new SlaValue(10, 1, TimeUnit.HOURS, true);
        
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
        
        Thread.sleep(1000); // sleep 1 second
        
        for(int i=0;i<5;i++){
            monitor.grant(user); // this should succeed
        }
        try{
            monitor.grant(user); // this should fail
            assert(false);
        } catch (SlaDeniedException e){
            e.printStackTrace();
        }
        
        Thread.sleep(5000);  // sleep 5 seconds
        
        for(int i=0;i<5;i++){
            monitor.grant(user); // this should succeed
        }
        try{
            monitor.grant(user); // this should fail
            assert(false);
        } catch (SlaDeniedException e){
            e.printStackTrace();
        }
        
        System.out.println("RatePerSecond: OK");

    }

}
