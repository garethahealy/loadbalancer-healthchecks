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

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class ContextCount {

    private Map<String, Integer> contexts;
    private String contextCounts;

    public ContextCount(String contextCounts) {
        this.contextCounts = contextCounts;
    }

    public void init() {
        contexts = new HashMap<String, Integer>();

        String[] items = StringUtils.split(contextCounts, ',');
        for (String item : items) {
            String[] contextCount = StringUtils.split(item, "|");
            String context = contextCount[0].substring(1, contextCount[0].length());
            Integer count = Integer.parseInt(contextCount[1].substring(0, contextCount[1].length() - 1));

            contexts.put(context, count);
        }
    }

    public Map<String, Integer> getContexts() {
        return contexts;
    }
}
