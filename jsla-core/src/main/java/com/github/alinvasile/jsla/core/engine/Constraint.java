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

import com.github.alinvasile.jsla.core.NoRateDefinedException;
import com.github.alinvasile.jsla.core.SlaDeniedException;

public interface Constraint {

	/**
	 * Adds a SLA constraint for the given user property.
	 * 
	 * @param authority
	 *            the user property, such as user or group name.
	 * @param sla
	 *            the SLA to add.
	 */
	void addConstraint(String authority, Sla sla);

	
	/**
	 * Grant access based on the given user property.
	 * 
	 * @param authority
	 *            the user property, such as user or group name.
	 * @throws NoRateDefinedException
	 *             when no SLA is defined for the given user property.
	 * @throws SlaDeniedException
	 *             when SLA is breached for the given user property, be it rate
	 *             or quota.
	 */
	void grant(String authority) throws NoRateDefinedException,
			SlaDeniedException;

}