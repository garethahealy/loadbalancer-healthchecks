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
/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.garethahealy.loadbalancer.healthchecks.fabric8.gateway.amqp;

import java.io.InputStream;

import com.meterware.httpunit.HttpUnitOptions;
import com.meterware.servletunit.ServletRunner;
import com.meterware.servletunit.ServletUnitClient;

import org.apache.camel.test.blueprint.CamelBlueprintTestSupport;
import org.junit.After;
import org.junit.Before;

public class ServletCamelRouterTestSupport extends CamelBlueprintTestSupport {
    public static final String CONTEXT = "/amqp-healthcheck";
    public static final String CONTEXT_URL = "http://localhost";
    protected ServletRunner sr;
    protected boolean startCamelContext = true;

    @Before
    public void setUp() throws Exception {
        InputStream is = this.getClass().getResourceAsStream(getConfiguration());
        assertNotNull("The configuration input stream should not be null", is);
        sr = new ServletRunner(is, CONTEXT);

        HttpUnitOptions.setExceptionsThrownOnErrorStatus(true);
        if (startCamelContext) {
            super.setUp();
        }
    }

    @After
    public void tearDown() throws Exception {
        if (startCamelContext) {
            super.tearDown();
        }
        sr.shutDown();
    }

    /**
     * @return The web.xml to use for testing.
     */
    protected String getConfiguration() {
        return "/com/garethahealy/loadbalancer/healthchecks/fabric8/gateway/amqp/web.xml";
    }

    protected ServletUnitClient newClient() {
        return sr.newClient();
    }

}
