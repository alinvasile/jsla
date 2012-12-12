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

/**
 * 
 * <p>
 * An SLA is composed by:
 * <ul>
 * <li>a transaction <b>rate</b>, which is the number of allowed transactions in
 * a given time interval. Can be seen as a limitation in frequency.</li>
 * <li>a <b>quota</b>, which is the number of total transactions allowed in a
 * time interval. Usually this time interval is bigger than the one expressed in
 * the transaction rate. Can be seen as a limitation if quantity.</li>
 * </ul>
 * </p>
 * 
 * <p>
 * Example of an SLA is a transaction rate of 5 transactions/second plus a quota
 * of 100 requests/day.
 * </p>
 * 
 * @author Alin Vasile
 * 
 * @since 1.0
 * 
 */
public class Sla {

	/** the SLA rate (frequency) */
	private final SlaValue rate;

	/** the SLA quota (quantity) */
	private final SlaValue quota;

	/**
	 * Constructs an SLA with the given rate and quota.
	 * 
	 * @param rate
	 *            the SLA rate
	 * @param quota
	 *            the SLA quota
	 */
	public Sla(SlaValue rate, SlaValue quota) {
		if (rate == null) {
			throw new IllegalArgumentException("rate cannot be null");
		}

		if (quota == null) {
			throw new IllegalArgumentException("quota cannot be null");
		}

		this.rate = rate;
		this.quota = quota;
	}

	public SlaValue getRate() {
		return rate;
	}

	public SlaValue getQuota() {
		return quota;
	}

	/**
	 * Constructs a SLA value with the given rate and an unlimited quota.
	 * 
	 * @param rate
	 *            the rate defined for this SLA.
	 * @return the built SLA value.
	 */
	public static Sla createSlaRateWithQuotaUnlimited(SlaValue rate) {
		if (rate == null) {
			throw new IllegalArgumentException("rate cannot be null");
		}

		return new Sla(rate, SlaValue.UNLIMITED);
	}

	/**
	 * Constructs a SLA value with the given quota and an unlimited rate.
	 * 
	 * @param quota
	 *            the quota defined for this SLA.
	 * @return the built SLA value.
	 */
	public static Sla createSlaQuotaWithRateUnlimited(SlaValue quota) {
		if (quota == null) {
			throw new IllegalArgumentException("quota cannot be null");
		}

		return new Sla(SlaValue.UNLIMITED, quota);
	}

	@Override
	public String toString() {
		return "Sla [rate=" + rate + ", quota=" + quota + "]";
	}
	
	

}
