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
package com.github.alinvasile.jsla.ws;

import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContext;

import com.github.alinvasile.jsla.core.engine.Sla;
import com.github.alinvasile.jsla.core.engine.SlaValue;
import com.github.alinvasile.jsla.core.engine.UsernameGroupTransactionMonitor;
import com.github.alinvasile.jsla.web.AccessEngineFactory;
import com.github.alinvasile.jsla.web.context.AccessHierarchy;
import com.github.alinvasile.jsla.web.context.DefaultAccessHierarchy;

public class UsernameGroupMonitorFactory implements AccessEngineFactory {

    public AccessHierarchy buildAccessMonitor(ServletContext context) {
        
        AccessHierarchy engine = new DefaultAccessHierarchy();
        
        addRootAcessMonitor(engine);
        
        addReportsAcessMonitor(engine);
        
        addStatisticsAcessMonitor(engine);
        
        return engine;
    }
    
    private void addRootAcessMonitor(AccessHierarchy engine){
        UsernameGroupTransactionMonitor monitor = new UsernameGroupTransactionMonitor();
        
        Sla anonymousSla = Sla.createSlaRateWithQuotaUnlimited(new SlaValue(10,TimeUnit.SECONDS));
        monitor.addAnonymousConstraint(anonymousSla);
        
        Sla goldGroupSla =  Sla.createSlaRateWithQuotaUnlimited(new SlaValue(20,TimeUnit.SECONDS));
        monitor.addGroupConstraint("gold", goldGroupSla);
        
        Sla silverGroupSla =  Sla.createSlaRateWithQuotaUnlimited(new SlaValue(15,TimeUnit.SECONDS));
        monitor.addGroupConstraint("silver", silverGroupSla);
       
        engine.add("/*", monitor);
    }
    
    private void addReportsAcessMonitor(AccessHierarchy engine){
        UsernameGroupTransactionMonitor monitor = new UsernameGroupTransactionMonitor();
        
        Sla goldGroupSla =  Sla.createSlaRateWithQuotaUnlimited(new SlaValue(40,TimeUnit.SECONDS));
        monitor.addGroupConstraint("gold", goldGroupSla);
        
        Sla silverGroupSla =  Sla.createSlaRateWithQuotaUnlimited(new SlaValue(20,TimeUnit.SECONDS));
        monitor.addGroupConstraint("silver", silverGroupSla);
        
        Sla alinUserSla = Sla.createSlaRateWithQuotaUnlimited(new SlaValue(25,TimeUnit.SECONDS));
        monitor.addUsernameConstraint("alinvasile", alinUserSla);
        
        Sla ionutUserSla = Sla.createSlaRateWithQuotaUnlimited(new SlaValue(30,TimeUnit.SECONDS));
        monitor.addUsernameConstraint("ionutb", ionutUserSla);
        
        engine.add("/reports/*", monitor);
    }
    
    private void addStatisticsAcessMonitor(AccessHierarchy engine){
        UsernameGroupTransactionMonitor monitor = new UsernameGroupTransactionMonitor();
        
        Sla alinUserSla = Sla.createSlaRateWithQuotaUnlimited(new SlaValue(25,TimeUnit.SECONDS));
        monitor.addUsernameConstraint("alinvasile", alinUserSla);
        
        engine.add("/statistics/*", monitor);
    }

}
