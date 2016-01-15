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

import java.util.concurrent.TimeUnit;

import org.apache.camel.ExchangePattern;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.test.blueprint.CamelBlueprintTestSupport;
import org.junit.Assert;
import org.junit.Test;

public class IsAliveTrueTest extends CamelBlueprintTestSupport {

    @Override
    protected String getBlueprintDescriptor() {
        return "OSGI-INF/blueprint/camel-context.xml";
    }

    @Test
    public void httpIsUp() throws Exception {
        RouteDefinition jettyServer = createFakeJettyServer();
        context.addRouteDefinition(jettyServer);

        for (int i = 0; i < 5; i++) {
            Object body = template.sendBody("jetty://http://localhost:9001/http-healthcheck", ExchangePattern.InOut, new String(""));

            Assert.assertNotNull(body);
            Assert.assertTrue(Boolean.parseBoolean(context.getTypeConverter().convertTo(String.class, body)));

            TimeUnit.SECONDS.sleep(5);
        }
    }

    protected RouteDefinition createFakeJettyServer() throws Exception {
        RouteDefinition jetty = new RouteDefinition();
        jetty.from("jetty:http://0.0.0.0:9000/")
            .setBody().constant("{\"/hawtio-swagger/\":[\"http://10.20.1.21:8183/hawtio-swagger\"]}");

        return jetty;
    }
}
