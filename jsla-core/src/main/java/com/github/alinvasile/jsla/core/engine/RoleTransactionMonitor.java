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

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.alinvasile.jsla.core.AccessMonitor;
import com.github.alinvasile.jsla.core.Authority;
import com.github.alinvasile.jsla.core.SlaDeniedException;

/**
 * 
 * Role based enforcement of a SLA monitor. Users without a role will use a
 * special rate control, for no-role or anonymous access.
 * 
 * @author Alin Vasile
 * @since 1.0
 * 
 */
public class RoleTransactionMonitor implements AccessMonitor {

    private static final Logger logger = LoggerFactory.getLogger(RoleTransactionMonitor.class);

    /**
     * Constant that can be used for resources that don't require a role.
     */
    protected static final String NO_ROLE = "NO_ROLE";

    static final int PRIORITY_LOW = 0;

    /**
     * Contains a Map of constraints ordered by priority.
     */
    protected Map<Entry, Constraint> priorityMap = Collections.synchronizedMap(new TreeMap<Entry, Constraint>());

    /**
     * Rate control for anonymous access.
     */
    protected Constraint anonymousRateControl = new RateQuotaConstraint();

    /*
     * 
     * (non-Javadoc)
     * 
     * @see AccessMonitor#grant(Authority) )
     */
    public void grant(Authority authority) throws SlaDeniedException {
        if (logger.isDebugEnabled()) {
            logger.debug("Grant " + authority);
        }

        if (authority.getRoles() == null || authority.getRoles().length == 0) {
            if (logger.isInfoEnabled()) {
                logger.info("User doesn't have roles, checking no_role access");
            }
            anonymousRateControl.grant(NO_ROLE);
        } else {
            if (logger.isInfoEnabled()) {
                logger.info("Checking role access: " + authority.getUsername());
            }

            List<String> roleList = Arrays.asList(authority.getRoles());

            if (logger.isDebugEnabled()) {
                logger.debug("User has roles: " + roleList);
            }

            Set<Entry> set = priorityMap.keySet();
            Iterator<Entry> iterator = set.iterator();
            while (iterator.hasNext()) {
                Entry next = iterator.next();

                if (logger.isDebugEnabled()) {
                    logger.debug("Checking entry: " + next);
                }

                if (roleList.contains(next.roleName)) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("User has role: " + next.roleName);
                    }

                    Constraint rateControl = priorityMap.get(next);
                    rateControl.grant(authority.getUsername());
                }
            }
        }

    }

    /**
     * Adds a role name SLA. If multiple SLAs have the same priority, they will
     * be verified in the order of addition.
     * 
     * @param role
     *            the role name to add the SLA for.
     * @param sla
     *            the SLA to add.
     * @param priority
     *            the priority assigned to this SLA.
     */
    public void addRoleConstraint(String role, Sla sla, int priority) {
        if (priority < 0) {
            throw new IllegalArgumentException("priority must be greater than 0!");
        }
        RateQuotaConstraint rateControl = new RateQuotaConstraint();
        rateControl.addConstraint(role, sla);
        priorityMap.put(new Entry(role, priority), rateControl);

    }

    /**
     * Adds a role name SLA with the lowest priority.
     * 
     * @param role
     *            the role name to add the SLA for.
     * @param sla
     *            the SLA to add.
     */
    public void addRoleConstraint(String role, Sla sla) {
        addRoleConstraint(role, sla, PRIORITY_LOW);
    }

    /**
     * Adds an anonymous SLA. This SLA will be used for anonymous users or users
     * without any roles.
     * 
     * @param sla
     *            the SLA to add.
     */
    public void addAnonymousConstraint(Sla sla) {
        anonymousRateControl.addConstraint(NO_ROLE, sla);
    }

    protected static class Entry implements Comparable<Entry> {
        String roleName;
        int priority;

        Entry(String roleName, int priority) {
            this.roleName = roleName;
            this.priority = priority;
        }

        public int compareTo(Entry arg0) {
            // reverse order, from highest priority to lowest
            return arg0.priority - this.priority;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + priority;
            result = prime * result + ((roleName == null) ? 0 : roleName.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Entry other = (Entry) obj;
            if (priority != other.priority)
                return false;
            if (roleName == null) {
                if (other.roleName != null)
                    return false;
            } else if (!roleName.equals(other.roleName))
                return false;
            return true;
        }

    }

}
