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
package com.github.alinvasile.jsla.core.engine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.alinvasile.jsla.core.AccessMonitor;
import com.github.alinvasile.jsla.core.Authority;
import com.github.alinvasile.jsla.core.NoRateDefinedException;
import com.github.alinvasile.jsla.core.SlaDeniedException;

/**
 * 
 * User/group name based enforcement of a SLA monitor. Allows unknown users to
 * use a special rate control, for anonymous access.
 * 
 * @author Alin Vasile
 * @since 1.0
 * 
 */
public class UsernameGroupTransactionMonitor implements AccessMonitor {

    protected static final String ANONYMOUS = "ANONYMOUS";

    protected Constraint usernameRateControl = new RateQuotaConstraint();
    protected Constraint groupRateControl = new RateQuotaConstraint();
    protected Constraint anonymousRateControl = new RateQuotaConstraint();

    private static final Logger logger = LoggerFactory.getLogger(UsernameGroupTransactionMonitor.class);

    /**
     * Adds a username SLA.
     * 
     * @param username
     *            the user name to add the SLA for.
     * @param sla
     *            the SLA to add.
     */
    public void addUsernameConstraint(String username, Sla sla) {
        usernameRateControl.addConstraint(username, sla);
    }

    /**
     * Adds a group SLA.
     * 
     * @param group
     *            the group name to add the SLA for.
     * @param sla
     *            the SLA to add.
     */
    public void addGroupConstraint(String group, Sla sla) {
        groupRateControl.addConstraint(group, sla);
    }

    /**
     * Adds an anonymous SLA. This SLA will be used for anonymous users.
     * 
     * @param sla
     *            the SLA to add.
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
    public void grant(Authority authority) throws SlaDeniedException {

        if (logger.isDebugEnabled()) {
            logger.debug("Grant " + authority);
        }

        if (authority.isAnonymous()) {
            if (logger.isInfoEnabled()) {
                logger.info("User is anonymous, checking access");
            }
            anonymousRateControl.grant(ANONYMOUS);
        } else {
            try {
                if (logger.isInfoEnabled()) {
                    logger.info("Checking username access: " + authority.getUsername());
                }
                usernameRateControl.grant(authority.getUsername());
            } catch (NoRateDefinedException e) {
                // no rate defined for username, going with group

                if (authority.getGroup() != null) {
                    if (logger.isInfoEnabled()) {
                        logger.info("Checking group access: " + authority.getGroup());
                    }

                    groupRateControl.grant(authority.getGroup());
                } else {
                    if (logger.isInfoEnabled()) {
                        logger.info("Authority doesn't belong to a group, skipping group access");
                    }
                }
            }
        }

    }

}
