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
package org.github.jsla.core.sla;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.github.jsla.core.monitor.NoRateDefinedException;
import org.github.jsla.core.monitor.TransactionDeniedException;
import org.isomorphism.util.TokenBucket;
import org.isomorphism.util.TokenBuckets;

public class RateControl {

    private Map<String, Sla> slaConstraints = new ConcurrentHashMap<String, Sla>();
    
    private Map<String, TokenBucket> rateConstraints = new ConcurrentHashMap<String, TokenBucket>();
    
    private Map<String, TokenBucket> quotaConstraints = new ConcurrentHashMap<String, TokenBucket>();
    
    public void addConstraint(String authority, Sla sla) {
        SlaValue rate = sla.getRate();
        SlaValue quota = sla.getQuota();

        TokenBucket rateBucket = TokenBuckets.newFixedIntervalRefill(rate.getAmount(), rate.getAmount(),
                rate.getReferenceValue(), rate.getReferenceUnit());
        TokenBucket quotaBucket = TokenBuckets.newFixedIntervalRefill(quota.getAmount(), quota.getAmount(),
                quota.getReferenceValue(), quota.getReferenceUnit());

        rateConstraints.put(authority, rateBucket);
        quotaConstraints.put(authority, quotaBucket);
        slaConstraints.put(authority, sla);
    }
    
    public void grant(String authority) throws NoRateDefinedException, TransactionDeniedException {
        TokenBucket quotaBucket = quotaConstraints.get(authority);

        if (quotaBucket == null) {
            throw new NoRateDefinedException("No quota SLA defined for " + authority);
        }

        Sla sla = slaConstraints.get(authority);

        boolean quotaAllowed = quotaBucket.tryConsume();
        if (!quotaAllowed && !sla.getQuota().isCanBeExceeded()) {
            // deny access
            throw new TransactionDeniedException("Quota exceeded for " + authority);
        }

        TokenBucket rateBucket = rateConstraints.get(authority);

        if (rateBucket == null) {
            throw new NoRateDefinedException("No rate SLA defined for " + authority);
        }

        boolean rateAllowed = rateBucket.tryConsume();
        if (!rateAllowed && !sla.getRate().isCanBeExceeded()) {
            // deny access
            throw new TransactionDeniedException("Rate exceeded for " + authority);
        }

    }
    
}
