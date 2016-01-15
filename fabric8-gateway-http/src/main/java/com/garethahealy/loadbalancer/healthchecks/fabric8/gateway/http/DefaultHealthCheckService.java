/*
 * #%L
 * GarethHealy :: LoadBalancer HealthChecks :: Fabric8 Gateway HTTP
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
package com.garethahealy.loadbalancer.healthchecks.fabric8.gateway.http;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultHealthCheckService implements HealthCheckService {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultHealthCheckService.class);

    private AtomicBoolean isDead = new AtomicBoolean(false);
    private ContextCount contextCount;

    public DefaultHealthCheckService(ContextCount contextCount) {
        this.contextCount = contextCount;
    }

    public Boolean isAlive(Exchange exchange) {
        Boolean isAlive = isAlive();
        Integer code = isAlive ? 200 : 500;

        LOG.debug("IsAlive == {}", isAlive);

        exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, code);
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, "text/plain; charset=utf-8");
        exchange.getIn().setHeader("Cache-Control", "no-cache, no-store, must-revalidate, max-age=0");
        exchange.getIn().setHeader("Pragma", "no-cache");
        exchange.getIn().setHeader("Expires", "0");

        return isAlive;
    }

    public Boolean isAlive() {
        return !isDead.get();
    }

    public void validateJson(Map json) {
        if (json == null || json.size() <= 0) {
            LOG.debug("JSON map is null or empty");

            isDead.set(true);
            return;
        }

        for (Map.Entry<String, Integer> entry : contextCount.getContexts().entrySet()) {
            LOG.debug("Checking context {} for expected count of {}", entry.getKey(), entry.getValue());

            if (json.containsKey(entry.getKey())) {
                LOG.debug("Found context {}", entry.getKey());

                List<String> proxys = (List)json.get(entry.getKey());
                if (proxys.size() == entry.getValue()) {
                    LOG.debug("{} has matching count for expected/found of {}. Gateway is valid.", entry.getKey(), entry.getValue());
                } else {
                    LOG.warn("Gateway context found {} does match expected count {}. Gateway is invalid.", proxys.size(), entry.getValue());

                    isDead.set(true);

                    break;
                }
            } else {
                LOG.warn("Gateway does not contain context {}. Gateway is invalid.", entry.getKey());

                isDead.set(true);

                break;
            }
        }
    }
}
