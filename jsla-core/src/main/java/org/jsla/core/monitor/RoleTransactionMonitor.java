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

import org.jsla.core.RateControl;
import org.jsla.core.SlaDeniedException;
import org.jsla.core.authority.Authority;

public class RoleTransactionMonitor implements AccessMonitor {

	protected RateControl roleRateControl = new RateControl();
	protected RateControl anonymousRateControl = new RateControl();
	
	public void grant(Authority authority) throws SlaDeniedException {
		// TODO Auto-generated method stub
		
	}

}
