package org.serverfaces.common;

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
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import org.apache.log4j.Logger;
import org.serverfaces.common.qualifier.Log;
import org.serverfaces.common.qualifier.PropertyFile;
import org.snmp4j.smi.OID;

/**
 *
 * create instances of OIDs based on their key names in
 * mib.properties
 *
 * Usage:
 *
 * @Inject Instance<OID> serverName; will inject the serverName OID configured
 * in the mib.properties server.name = .1.3.6.1.2.1.1.1.0
 *
 * Note that the name of the instance variable must match the name of the
 * properties key in camel case convention so for eg: server.name =
 * @Inject Instance<OID> serverName server.uptime =
 * @Inject Instance<OID> serverUptime and so on.
 *
 * in practice you can look directly to the keys of the provider 
 * to find out the name of the instance variables to inject.
 * 
 * @author Rafael M. Pestano
 */
@Singleton
@Startup
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
@Lock(LockType.READ)
public class OIDProvider {

    private Map<String, OID> provider;
    
    @Inject
    @PropertyFile(file = "/mib.properties")
    private Properties mib;
    @Inject @Log
    private Logger log;

    @PostConstruct
    public void initialize() {
        this.provider = new HashMap<String, OID>();
        this.provider.put("serverName", new OID(mib.getProperty(Enums.MIB.SERVER_NAME.value)));
        this.provider.put("serverAddress", new OID(mib.getProperty(Enums.MIB.SERVER_ADDRESS.value)));
        this.provider.put("serverUptime", new OID(mib.getProperty(Enums.MIB.SERVER_UPTIME.value)));
        this.provider.put("serverActiveSessions", new OID(mib.getProperty(Enums.MIB.SERVER_ACTIVE_SESSIONS.value)));
        this.provider.put("serverUsedMemory", new OID(mib.getProperty(Enums.MIB.SERVER_USED_MEMORY.value)));
        this.provider.put("serverAvailableMemory", new OID(mib.getProperty(Enums.MIB.SERVER_AVAILABLE_MEMORY.value)));
        this.provider.put("serverCpuTime", new OID(mib.getProperty(Enums.MIB.SERVER_CPU_TIME.value)));
        this.provider.put("serverActiveTransactions", new OID(mib.getProperty(Enums.MIB.SERVER_ACTIVE_TRANSACTIONS.value)));
        this.provider.put("serverCommitedTransactions", new OID(mib.getProperty(Enums.MIB.SERVER_COMMITED_TRANSACTIONS.value)));
        this.provider.put("serverRollbackTransactions", new OID(mib.getProperty(Enums.MIB.SERVER_ROLLBACK_TRANSACTIONS.value)));
        this.provider.put("serverActiveThreads", new OID(mib.getProperty(Enums.MIB.SERVER_ACTIVE_THREADS.value)));
        this.provider.put("serverTotalRequests", new OID(mib.getProperty(Enums.MIB.SERVER_TOTAL_REQUESTS.value)));
        this.provider.put("serverLog", new OID(mib.getProperty(Enums.MIB.SERVER_LOG.value)));
        log.debug("OID Provider Initialized!");
    }

    @Produces
    public OID getOID(InjectionPoint ip) {
        return provider.get(ip.getMember().getName());
    }

    public Map<String, OID> getProvider() {
        return provider;
    }
}
