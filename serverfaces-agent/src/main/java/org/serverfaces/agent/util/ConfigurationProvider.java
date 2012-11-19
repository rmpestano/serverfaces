package org.serverfaces.agent.util;

/*
 * Copyright 2012 Rafael M. Pestano.  
 * 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
import javax.annotation.PostConstruct;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.serverfaces.common.Enums.AGENT;
import org.serverfaces.common.Enums.SERVER;
import org.serverfaces.common.qualifier.PropertyFile;

/**
 *
 * Basic CDI configurator that produces common configurations used in the
 * project, usage:
 *
 * @Inject Instance<String/Integer> propertyName
 *
 * where propertyName must match the camel case conventions of a key in the
 * agent.properties file so for eg:
 *
 * agent.address = udp:127.0.0.1/16112 should be injected as
 *
 * @Inject Instance<String> agentAddress;
 *
 *
 * @author Rafael M. Pestano
 */
@Singleton
public class ConfigurationProvider {

    private Map<String, String> provider;
    @Inject
    @PropertyFile(file = "/agent.properties")
    private Properties agentProperties;

    @PostConstruct
    public void init() {
        this.provider = new HashMap<String, String>();
        this.provider.put("agentAddress", agentProperties.getProperty(AGENT.ADDRESS.value));
        this.provider.put("serverAddress", agentProperties.getProperty(SERVER.ADDRESS.value));
    }

    @Produces
    public int getInteger(InjectionPoint ip) {
        return Integer.parseInt(getString(ip));
    }

    @Produces
    public String getString(InjectionPoint ip) {
        return this.provider.get(ip.getMember().getName());
    }

    public String getValue(String key) {
        return this.provider.get(key);
    }

    public Map<String, String> getProvider() {
        return provider;
    }
}
