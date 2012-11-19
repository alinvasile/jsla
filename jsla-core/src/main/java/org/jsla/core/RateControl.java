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
package org.jsla.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.isomorphism.util.TokenBucket;
import org.isomorphism.util.TokenBuckets;
import org.jsla.core.sla.Sla;
import org.jsla.core.sla.SlaValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Maps a user property (username, group name) to a SLA. Internally it uses the
 * Token Bucket algorithm for rate and quota control.
 * 
 * @author Alin Vasile
 * @since 1.0
 * 
 */
public class RateControl {

	/** A mapping between the user property and the defined SLAs. */
	private Map<String, Sla> slaConstraints = new ConcurrentHashMap<String, Sla>();

	/** A mapping between the user property and the rate Token Bucket. */
	private Map<String, TokenBucket> rateConstraints = new ConcurrentHashMap<String, TokenBucket>();

	/** A mapping between the user property and the quota Token Bucket. */
	private Map<String, TokenBucket> quotaConstraints = new ConcurrentHashMap<String, TokenBucket>();
	
	private static final Logger logger = LoggerFactory.getLogger(RateControl.class);

	/**
	 * Adds a SLA constraint for the given user property.
	 * 
	 * @param authority
	 *            the user property, such as user or group name.
	 * @param sla
	 *            the SLA to add.
	 */
	public void addConstraint(String authority, Sla sla) {
		
		if(logger.isDebugEnabled()){
			logger.debug("Adding constraint " + sla + " for " + authority );
		}
		
		SlaValue rate = sla.getRate();
		SlaValue quota = sla.getQuota();

		/* skip rates that are exceedable */
		if (!rate.isCanBeExceeded()) {
			if(logger.isDebugEnabled()){
				logger.debug("rate not exceedable, adding tocken bucket");
			}
			
			TokenBucket rateBucket = TokenBuckets.newFixedIntervalRefill(
					rate.getAmount(), rate.getAmount(),
					rate.getReferenceValue(), rate.getReferenceUnit());
			rateConstraints.put(authority, rateBucket);
		}

		/* skip quotas that are exceedable */
		if (!quota.isCanBeExceeded()) {
			if(logger.isDebugEnabled()){
				logger.debug("quota not exceedable, adding tocken bucket");
			}
			
			TokenBucket quotaBucket = TokenBuckets.newFixedIntervalRefill(
					quota.getAmount(), quota.getAmount(),
					quota.getReferenceValue(), quota.getReferenceUnit());
			quotaConstraints.put(authority, quotaBucket);
		}

		slaConstraints.put(authority, sla);
		
		if(logger.isDebugEnabled()){
			logger.debug("Added constraint for " + authority + ", constraints map is now " + slaConstraints );
		}
	}

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
	public void grant(String authority) throws NoRateDefinedException,
			SlaDeniedException {
		if(logger.isDebugEnabled()){
			logger.debug("Check access for " + authority );
		}
		
		Sla sla = slaConstraints.get(authority);
		
		if(logger.isDebugEnabled()){
			logger.debug("SLA for " + authority + " is " + sla);
		}

		if (sla == null) {
			throw new NoRateDefinedException("No SLA defined for " + authority);
		}

		if (!sla.getQuota().isCanBeExceeded()) {

			TokenBucket quotaBucket = quotaConstraints.get(authority);

			if(logger.isDebugEnabled()){
				logger.debug("Quota bucket for " + authority + " is " + quotaBucket);
			}
			
			if (quotaBucket == null) {
				throw new NoRateDefinedException("No quota SLA defined for "
						+ authority);
			}

			boolean quotaAllowed = quotaBucket.tryConsume();
			
			if(logger.isInfoEnabled()){
				logger.info("Quota allowed for " + authority + ": " + quotaAllowed);
			}
			
			if (!quotaAllowed) {
				// deny access
				throw new QuotaExceededException("Quota exceeded for "
						+ authority);
			}
		}

		if (!sla.getRate().isCanBeExceeded()) {
			TokenBucket rateBucket = rateConstraints.get(authority);
			
			if(logger.isDebugEnabled()){
				logger.debug("Rate bucket for authority " + authority + " is " + rateBucket);
			}

			if (rateBucket == null) {
				throw new NoRateDefinedException("No rate SLA defined for "
						+ authority);
			}

			boolean rateAllowed = rateBucket.tryConsume();
			
			if(logger.isInfoEnabled()){
				logger.info("Rate allowed for " + authority + ": " + rateAllowed);
			}
			
			if (!rateAllowed) {
				// deny access
				throw new RateExceededException("Rate exceeded for "
						+ authority);
			}
		}

	}

}
