package org.jsla.web.jee5;

import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContext;

import org.jsla.core.engine.Sla;
import org.jsla.core.engine.SlaValue;
import org.jsla.core.engine.UsernameGroupTransactionMonitor;
import org.jsla.web.AccessEngineFactory;
import org.jsla.web.context.AccessHierarchy;
import org.jsla.web.context.DefaultAccessHierarchy;

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
