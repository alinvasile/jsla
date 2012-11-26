package org.jsla.web.jee5;

import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContext;

import org.jsla.core.AccessMonitor;
import org.jsla.core.engine.Sla;
import org.jsla.core.engine.SlaValue;
import org.jsla.core.engine.UsernameGroupTransactionMonitor;
import org.jsla.web.MonitorFactory;

public class UsernameGroupMonitorFactory implements MonitorFactory {
    
    public AccessMonitor buildAccessMonitor(ServletContext context){
        UsernameGroupTransactionMonitor monitor = new UsernameGroupTransactionMonitor();
        
        Sla anonymousSla = Sla.createSlaRateWithQuotaUnlimited(new SlaValue(10,TimeUnit.SECONDS));
        monitor.addAnonymousConstraint(anonymousSla);
        
        Sla goldGroupSla =  Sla.createSlaRateWithQuotaUnlimited(new SlaValue(20,TimeUnit.SECONDS));
        monitor.addGroupConstraint("gold", goldGroupSla);
        
        Sla silverGroupSla =  Sla.createSlaRateWithQuotaUnlimited(new SlaValue(15,TimeUnit.SECONDS));
        monitor.addGroupConstraint("silver", silverGroupSla);
        
        Sla alinUserSla = Sla.createSlaRateWithQuotaUnlimited(new SlaValue(25,TimeUnit.SECONDS));
        monitor.addUsernameConstraint("alinvasile", alinUserSla);
        
        return monitor;
    }

}
