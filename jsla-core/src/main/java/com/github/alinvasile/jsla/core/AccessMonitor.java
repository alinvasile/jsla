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
package com.github.alinvasile.jsla.core;


/**
 * Matches authorities against SLAs.
 * 
 * @author Alin Vasile
 * @since 1.0
 */
public interface AccessMonitor {

	
	/**
	 * Grants or denies access for the given authority.
	 * 
	 * @param authority
	 *            the authority to grant access.
	 * @throws SlaDeniedException
	 *             when the authority is denied access due to SLA breach.
	 */
	void grant(Authority authority) throws SlaDeniedException;

}