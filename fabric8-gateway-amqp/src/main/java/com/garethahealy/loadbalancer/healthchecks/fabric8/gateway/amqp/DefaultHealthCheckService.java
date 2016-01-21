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

import javax.management.NotCompliantMBeanException;
import javax.management.StandardMBean;

import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultHealthCheckService extends StandardMBean implements HealthCheckService {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultHealthCheckService.class);

    private AtomicBoolean isDead = new AtomicBoolean(false);
    private AtomicInteger messagesSent = new AtomicInteger(0);
    private AtomicInteger messagesReceived = new AtomicInteger(0);

    protected DefaultHealthCheckService(Class<?> mbeanInterface) throws NotCompliantMBeanException {
        super(mbeanInterface);
    }

    public DefaultHealthCheckService() throws NotCompliantMBeanException {
        this(HealthCheckService.class);
    }

    public Boolean isAlive(Exchange exchange) {
        Boolean isAlive = isAlive();
        Integer code = isAlive ? 200 : 500;

        exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, code);
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, "text/plain; charset=utf-8");
        exchange.getIn().setHeader("Cache-Control", "no-cache, no-store, must-revalidate, max-age=0");
        exchange.getIn().setHeader("Pragma", "no-cache");
        exchange.getIn().setHeader("Expires", "0");

        return isAlive;
    }

    public Boolean isAlive() {
        Boolean isAlive;
        if (isDead.get()) {
            isAlive = false;

            LOG.warn("Gateway/AMQP is in invalid state.");
        } else {
            Integer difference = messagesSent.get() - messagesReceived.get();
            isAlive = difference <= 1;

            LOG.debug("SentCount minus ReceivedCount has a difference of {}; thus IsAlive == {}", difference, isAlive);
        }

        return isAlive;
    }

    public void die() {
        LOG.warn("Exception occurred sending AMQP payload; isDead == true");

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
