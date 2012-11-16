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
package org.github.jsla.core.monitor;

import org.github.jsla.core.authority.Authority;
import org.github.jsla.core.sla.RateControl;
import org.github.jsla.core.sla.Sla;

public class TransactionMonitor {
    
    private static final String ANONYMOUS = "ANONYMOUS";

    private RateControl usernameRateControl = new RateControl();
    private RateControl groupRateControl = new RateControl();
    private RateControl anonymousRateControl = new RateControl();
    
    public void addUsernameConstraint(String username, Sla sla) {
        usernameRateControl.addConstraint(username, sla);
    }
    
    public void addGroupConstraint(String group, Sla sla) {
        groupRateControl.addConstraint(group, sla);
    }
    
    public void addAnonymousConstraint(Sla sla) {
        anonymousRateControl.addConstraint(ANONYMOUS, sla);
    }

    public void grant(Authority authority) throws NoRateDefinedException, TransactionDeniedException {
        
        if(authority.isAnonymous()){
            anonymousRateControl.grant(ANONYMOUS);
        } else {
            try{
                usernameRateControl.grant(authority.getUsername());
            } catch(NoRateDefinedException e){
                // no rate defined for username, going with group
                groupRateControl.grant(authority.getGroup());
            }
        }

    }

}
