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

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class ContextCountTest {

    @Test
    public void canInit() {
        ContextCount count = new ContextCount("[/hawtio-swagger/|1]");
        count.init();

        Map<String, Integer> contexts = count.getContexts();

        Assert.assertNotNull(contexts);
        Assert.assertEquals(1, contexts.size());
        Assert.assertTrue(contexts.containsKey("/hawtio-swagger/"));
        Assert.assertEquals(1, (int)contexts.get("/hawtio-swagger/"));
    }
}
