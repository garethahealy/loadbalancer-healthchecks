/*
 * #%L
 * GarethHealy :: LoadBalancer HealthChecks :: Fabric8 Gateway AMQP
 * %%
 * Copyright (C) 2013 - 2017 Gareth Healy
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

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.xml.sax.SAXException;

import org.apache.activemq.broker.BrokerService;
import org.apache.camel.ExchangePattern;
import org.apache.camel.test.blueprint.CamelBlueprintTestSupport;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class IsAliveTrueTest extends CamelBlueprintTestSupport {

    private static BrokerService broker;

    @Override
    protected String getBlueprintDescriptor() {
        return "OSGI-INF/blueprint/camel-context.xml";
    }

    @BeforeClass
    public static void beforeClass() throws Exception {
        broker = new BrokerService();
        broker.setPersistent(false);
        broker.addConnector("amqp://0.0.0.0:5672");
        broker.start();
    }

    @AfterClass
    public static void afterClass() throws Exception {
        broker.stop();
    }

    @Test
    public void amqpIsUp() throws InterruptedException, IOException, SAXException {
        for (int i = 0; i < 5; i++) {
            Object body = template.sendBody("jetty://http://localhost:9200/amqp-healthcheck", ExchangePattern.InOut, new String(""));

            Assert.assertNotNull(body);
            Assert.assertTrue(Boolean.parseBoolean(context.getTypeConverter().convertTo(String.class, body)));

            TimeUnit.SECONDS.sleep(5);
        }
    }
}
