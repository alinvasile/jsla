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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.alinvasile.jsla.core.Authority;
import com.github.alinvasile.jsla.core.NoRateDefinedException;
import com.github.alinvasile.jsla.core.RateExceededException;
import com.github.alinvasile.jsla.core.engine.Sla;
import com.github.alinvasile.jsla.core.engine.SlaValue;
import com.github.alinvasile.jsla.core.engine.UsernameGroupTransactionMonitor;

/**
 * Test class for user rates.
 * 
 * @author Alin Vasile
 *
 */
public class TestUsernameGroupRate {

	private Authority authority;
	
	private Authority anonymous;
	
	private UsernameGroupTransactionMonitor service;
	
	@Before
	public void setUp(){
		authority = mock(Authority.class);
		when(authority.getUsername()).thenReturn("alinvasile");
		when(authority.getGroup()).thenReturn("gold");
		when(authority.isAnonymous()).thenReturn(false);
		
		anonymous = mock(Authority.class);
		when(anonymous.isAnonymous()).thenReturn(true);
		
		service = new UsernameGroupTransactionMonitor();
	}
	
	@After
	public void tearDown(){
		service = null;
	}
	
	@Test(expected=NoRateDefinedException.class)
	public void testNoUserSlaRate(){
		Sla sla = Sla.createSlaRateWithQuotaUnlimited(new SlaValue(1, 1, TimeUnit.SECONDS)); 
		
		service.addUsernameConstraint("nobody", sla);
		service.addGroupConstraint("nobody", sla);
		
		service.grant(authority);
	}
	
	@Test()
	public void testUsernameSlaRate(){
		Sla sla = Sla.createSlaRateWithQuotaUnlimited(new SlaValue(1, 1, TimeUnit.SECONDS)); 
		
		service.addUsernameConstraint(authority.getUsername(), sla);
		service.addGroupConstraint("nobody", sla);
		
		service.grant(authority);
	}
	
	@Test(expected=RateExceededException.class)
	public void testUsernameSlaRateExceeded(){
		Sla sla = Sla.createSlaRateWithQuotaUnlimited(new SlaValue(1, 1, TimeUnit.SECONDS)); 
		
		service.addUsernameConstraint(authority.getUsername(), sla);
		
		service.grant(authority);
		service.grant(authority);
	}
	
	@Test()
	public void testGroupSlaRate(){
		Sla sla = Sla.createSlaRateWithQuotaUnlimited(new SlaValue(1, 1, TimeUnit.SECONDS)); 
		
		service.addGroupConstraint(authority.getGroup(), sla);
		
		service.grant(authority);
	}
	
	@Test(expected=RateExceededException.class)
	public void testGroupSlaRateExceeded(){
		Sla sla = Sla.createSlaRateWithQuotaUnlimited(new SlaValue(1, 1, TimeUnit.SECONDS)); 
		
		service.addGroupConstraint(authority.getGroup(), sla);
		
		service.grant(authority);
		service.grant(authority);
	}
	
	@Test()
	public void testAnonymousSlaRate(){
		Sla sla = Sla.createSlaRateWithQuotaUnlimited(new SlaValue(1, 1, TimeUnit.SECONDS)); 
		
		service.addAnonymousConstraint(sla);
		
		service.grant(anonymous);
	}
	
	@Test(expected=RateExceededException.class)
	public void testAnonymousSlaRateExceeded(){
		Sla sla = Sla.createSlaRateWithQuotaUnlimited(new SlaValue(1, 1, TimeUnit.SECONDS)); 
		
		service.addAnonymousConstraint(sla);
		
		service.grant(anonymous);
		service.grant(anonymous);
	}
	
	
}
