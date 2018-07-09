/*
 * #%L
 * GarethHealy :: LoadBalancer HealthChecks :: Fabric8 Gateway HTTP
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
package com.garethahealy.loadbalancer.healthchecks.fabric8.gateway.http;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.NotCompliantMBeanException;

import org.junit.Assert;
import org.junit.Test;

public class DefaultHealthCheckServiceTest {

    @Test
    public void isAlive() throws NotCompliantMBeanException {
        Map<String, List<String>> gatewayJson = new HashMap<String, List<String>>();
        gatewayJson.put("/hawtio-swagger/", Collections.unmodifiableList(Arrays.asList("http://10.20.1.21:8183/hawtio-swagger")));
        gatewayJson.put("/my-service/", Collections.unmodifiableList(Arrays.asList("http://10.20.1.21:8184/my-service")));

        ContextCount count = new ContextCount("[/hawtio-swagger/|1]");
        count.init();

        HealthCheckService service = new DefaultHealthCheckService(count);
        service.validateJson(gatewayJson);

        Assert.assertTrue(service.isAlive());
    }

    @Test
    public void isDeadDueToCountMissmatch() throws NotCompliantMBeanException {
        Map<String, List<String>> gatewayJson = new HashMap<String, List<String>>();
        gatewayJson.put("/hawtio-swagger/", Collections.unmodifiableList(Arrays.asList("http://10.20.1.21:8183/hawtio-swagger")));
        gatewayJson.put("/my-service/", Collections.unmodifiableList(Arrays.asList("http://10.20.1.21:8184/my-service")));

        ContextCount count = new ContextCount("[/hawtio-swagger/|2]");
        count.init();

        HealthCheckService service = new DefaultHealthCheckService(count);
        service.validateJson(gatewayJson);

        Assert.assertFalse(service.isAlive());
    }

    @Test
    public void isDeadDueToMissingContext() throws NotCompliantMBeanException {
        Map<String, List<String>> gatewayJson = new HashMap<String, List<String>>();
        gatewayJson.put("/my-service/", Collections.unmodifiableList(Arrays.asList("http://10.20.1.21:8184/my-service")));

        ContextCount count = new ContextCount("[/hawtio-swagger/|2]");
        count.init();

        HealthCheckService service = new DefaultHealthCheckService(count);
        service.validateJson(gatewayJson);

        Assert.assertFalse(service.isAlive());
    }
}
