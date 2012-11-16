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
package org.github.jsla.example;

import java.util.concurrent.TimeUnit;

import org.github.jsla.core.monitor.TransactionDeniedException;
import org.github.jsla.core.monitor.TransactionMonitor;
import org.github.jsla.core.sla.Sla;
import org.github.jsla.core.sla.SlaValue;


public class RatePerSecond {

   
    public static void main(String[] args) {
       
        TransactionMonitor monitor = new TransactionMonitor();
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
        } catch (TransactionDeniedException e){
            e.printStackTrace();
        }

    }

}