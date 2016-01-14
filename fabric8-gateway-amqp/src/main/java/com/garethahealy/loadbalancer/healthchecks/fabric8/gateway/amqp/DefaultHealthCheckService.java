/*
 * #%L
 * GarethHealy :: LoadBalancer HealthChecks :: Fabric8 Gateway AMQP
 * %%
 * Copyright (C) 2013 - 2016 Gareth Healy
 * %%
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
 * #L%
 */
package com.garethahealy.loadbalancer.healthchecks.fabric8.gateway.amqp;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultHealthCheckService implements HealthCheckService {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultHealthCheckService.class);

    private AtomicBoolean isDead = new AtomicBoolean(false);
    private AtomicInteger messagesSent = new AtomicInteger(0);
    private AtomicInteger messagesReceived = new AtomicInteger(0);

    public Boolean isAlive() {
        Boolean isAlive;
        if (isDead.get()) {
            isAlive = false;

            LOG.debug("IsDead == true, thus IsAlive == false");
        } else {
            Integer difference = messagesSent.get() - messagesReceived.get();
            isAlive = difference <= 1;

            LOG.debug("SentCount minus ReceivedCount has a difference of {}; thus IsAlive == {}", difference, isAlive);
        }

        return isAlive;
    }

    public void die() {
        LOG.debug("Exception occurred; isDead == true");

        isDead.set(true);
    }

    public void resurrect() {
        LOG.debug("Resurrecting; isDead == false");

        isDead.set(false);
    }


    public void incrementSentCount() {
        messagesSent.incrementAndGet();
    }

    public void incrementReceivedCount() {
        messagesReceived.incrementAndGet();
    }
}
