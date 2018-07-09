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

import javax.management.NotCompliantMBeanException;

import org.junit.Assert;
import org.junit.Test;

public class DefaultHealthCheckServiceTest {

    @Test
    public void checkIsAliveTrue() throws NotCompliantMBeanException {
        HealthCheckService service = new DefaultHealthCheckService();
        service.incrementSentCount();
        service.incrementReceivedCount();

        Assert.assertTrue(service.isAlive());
    }

    @Test
    public void checkIsDead() throws NotCompliantMBeanException {
        HealthCheckService service = new DefaultHealthCheckService();
        service.die();

        Assert.assertFalse(service.isAlive());
    }

    @Test
    public void checkCanResurrect() throws NotCompliantMBeanException {
        HealthCheckService service = new DefaultHealthCheckService();
        service.die();

        Assert.assertFalse(service.isAlive());

        service.resurrect();

        Assert.assertTrue(service.isAlive());
    }
}
