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
package org.jsla.core.monitor;

import org.jsla.core.authority.Authority;
import org.jsla.core.sla.RateControl;
import org.jsla.core.sla.Sla;

/**
 * 
 * Base implementation of a transaction monitor.
 * 
 * @author Alin Vasile
 * @since 1.0
 *
 */
public class TransactionMonitor implements TransactionMonitorService {

	protected static final String ANONYMOUS = "ANONYMOUS";

	protected RateControl usernameRateControl = new RateControl();
	protected RateControl groupRateControl = new RateControl();
	protected RateControl anonymousRateControl = new RateControl();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.github.jsla.core.monitor.TransactionMonitorService#addUsernameConstraint
	 * (java.lang.String, org.github.jsla.core.sla.Sla)
	 */
	public void addUsernameConstraint(String username, Sla sla) {
		usernameRateControl.addConstraint(username, sla);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.github.jsla.core.monitor.TransactionMonitorService#addGroupConstraint
	 * (java.lang.String, org.github.jsla.core.sla.Sla)
	 */
	public void addGroupConstraint(String group, Sla sla) {
		groupRateControl.addConstraint(group, sla);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.github.jsla.core.monitor.TransactionMonitorService#addAnonymousConstraint
	 * (org.github.jsla.core.sla.Sla)
	 */
	public void addAnonymousConstraint(Sla sla) {
		anonymousRateControl.addConstraint(ANONYMOUS, sla);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.github.jsla.core.monitor.TransactionMonitorService#grant(org.github
	 * .jsla.core.authority.Authority)
	 */
	public void grant(Authority authority) throws TransactionDeniedException {

		if (authority.isAnonymous()) {
			anonymousRateControl.grant(ANONYMOUS);
		} else {
			try {
				usernameRateControl.grant(authority.getUsername());
			} catch (NoRateDefinedException e) {
				// no rate defined for username, going with group
				groupRateControl.grant(authority.getGroup());
			}
		}

	}

}
