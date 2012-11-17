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
import org.github.jsla.core.sla.Sla;

/**
 * Builts SLAs and matches authorities against them.
 * 
 * @author Alin Vasile
 * @since 1.0
 */
public interface TransactionMonitorService {

	/**
	 * Adds a username SLA.
	 * 
	 * @param username
	 *            the user name to add the SLA for.
	 * @param sla
	 *            the SLA to add.
	 */
	void addUsernameConstraint(String username, Sla sla);

	/**
	 * Adds a group SLA.
	 * 
	 * @param group
	 *            the group name to add the SLA for.
	 * @param sla
	 *            the SLA to add.
	 */
	void addGroupConstraint(String group, Sla sla);

	/**
	 * Adds an anonymous SLA. This SLA will be used for anonymous users.
	 * 
	 * @param sla
	 *            the SLA to add.
	 */
	void addAnonymousConstraint(Sla sla);

	/**
	 * Grants or denies access for the given authority.
	 * 
	 * @param authority
	 *            the authority to grant access.
	 * @throws NoRateDefinedException
	 *             when no SLA is defined for this authority.
	 * @throws TransactionDeniedException
	 *             when the authority is denied access due to SLA breach.
	 */
	void grant(Authority authority) throws NoRateDefinedException,
			TransactionDeniedException;

}