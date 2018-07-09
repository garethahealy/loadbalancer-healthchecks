/*
 * #%L
 * GarethHealy :: LoadBalancer HealthChecks :: Fabric8 Gateway AMQP
 * %%
 * Copyright (C) 2013 - 2018 Gareth Healy
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

import java.util.concurrent.TimeUnit;

import org.apache.camel.ExchangePattern;
import org.apache.camel.test.blueprint.CamelBlueprintTestSupport;
import org.junit.Test;

public class IsAliveFalseTest extends CamelBlueprintTestSupport {

    @Override
    protected String getBlueprintDescriptor() {
        return "OSGI-INF/blueprint/camel-context.xml";
    }

    @Test(expected = Exception.class)
    public void amqpIsDown() throws InterruptedException {
        //Wait for the first quartz timer to tick
        TimeUnit.SECONDS.sleep(5);

        template.sendBody("jetty://http://localhost:9200/amqp-healthcheck", ExchangePattern.InOut, new String(""));
    }
}
