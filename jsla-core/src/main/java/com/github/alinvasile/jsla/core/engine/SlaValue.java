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

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * A SLA value represents an association between a maximum allowed of
 * transactions in a given time interval. Several examples are
 * "10 requests per second", "5 requests per minute" and so on.
 * </p>
 * 
 * 
 * @author Alin Vasile
 * 
 * @since 1.0
 * 
 */

public class SlaValue {

    /** constant value representing an unlimited SLA value */
    public static final SlaValue UNLIMITED = new SlaValue(-1, -1, TimeUnit.SECONDS, true);

    /** the number of allowed transactions in the given time interval */
    private final long amount;

    /**
     * the quantity value for the time interval, such as one second or five
     * minutes
     */
    private final long referenceValue;

    /** the unit of time interval, such as DAYS or HOURS */
    private final TimeUnit referenceUnit;

    /** an indicator whether this SLA value can be exceeded or not. */
    private final boolean canBeExceeded;

    /**
     * Creates a non-exceedable SLA value with the given amount of allowed
     * transactions in one time unit.
     * 
     * @param amount
     *            the number of allowed transactions in one second.
     * @param referenceUnit
     *            the time unit this interval is expressed.
     */
    public SlaValue(long amount, TimeUnit referenceUnit) {
        this(amount, 1, referenceUnit, false);
    }

    /**
     * Constructs a non-exceedable SLA value with the given arguments.
     * 
     * @param amount
     *            the number of allowed transactions in one second.
     * @param referenceValue
     *            the interval this value is defined for.
     * @param referenceUnit
     *            the time unit this interval is expressed.
     */
    public SlaValue(long amount, long referenceValue, TimeUnit referenceUnit) {
        this(amount, referenceValue, referenceUnit, false);
    }

    /**
     * Constructs a SLA value with the given arguments.
     * 
     * @param amount
     *            amount the number of allowed transactions in one second.
     * @param referenceValue
     *            referenceValue the interval this value is defined for.
     * @param referenceUnit
     *            referenceUnit the time unit this interval is expressed.
     * @param canBeExceeded
     *            boolean flag, marks this SLA value as exceedable or not.
     */
    public SlaValue(long amount, long referenceValue, TimeUnit referenceUnit, boolean canBeExceeded) {
        super();
        this.amount = amount;
        this.referenceValue = referenceValue;
        this.referenceUnit = referenceUnit;
        this.canBeExceeded = canBeExceeded;
    }

    public long getAmount() {
        return amount;
    }

    public long getReferenceValue() {
        return referenceValue;
    }

    public TimeUnit getReferenceUnit() {
        return referenceUnit;
    }

    public boolean isCanBeExceeded() {
        return canBeExceeded;
    }

    @Override
    public String toString() {
        return "SlaValue [amount=" + amount + ", referenceValue=" + referenceValue + ", referenceUnit=" + referenceUnit
                + ", canBeExceeded=" + canBeExceeded + "]";
    }

}
