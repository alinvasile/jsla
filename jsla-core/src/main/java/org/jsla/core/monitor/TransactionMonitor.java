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

import org.jsla.core.NoRateDefinedException;
import org.jsla.core.RateControl;
import org.jsla.core.TransactionDeniedException;
import org.jsla.core.authority.Authority;
import org.jsla.core.sla.Sla;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	
	private static final Logger logger = LoggerFactory.getLogger(TransactionMonitor.class);

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

		if(logger.isDebugEnabled()){
			logger.debug("Grant " + authority);
		}
		
		if (authority.isAnonymous()) {
			if(logger.isInfoEnabled()){
				logger.info("User is anonymous, checking access");
			}
			anonymousRateControl.grant(ANONYMOUS);
		} else {
			try {
				if(logger.isInfoEnabled()){
					logger.info("Checking username access: " + authority.getUsername());
				}
				usernameRateControl.grant(authority.getUsername());
			} catch (NoRateDefinedException e) {
				// no rate defined for username, going with group
				if(logger.isInfoEnabled()){
					logger.info("Checking group access: " + authority.getGroup());
				}
				groupRateControl.grant(authority.getGroup());
			}
		}

	}

}
