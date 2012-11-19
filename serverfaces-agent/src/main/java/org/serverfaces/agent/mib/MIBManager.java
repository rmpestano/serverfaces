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
package org.serverfaces.agent.mib;

import org.serverfaces.agent.SNMPAgent;
import java.io.Serializable;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import org.apache.log4j.Logger;
import org.serverfaces.agent.server.ServerRetriever;
import org.serverfaces.agent.util.MOCreator;
import org.serverfaces.common.qualifier.Log;
import org.snmp4j.agent.mo.MOScalar;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.Variable;

/**
 *
 * @author Rafael M. Pestano - Nov 02, 2012 07:17:18 AM
 *
 * The MIB manager is the guy who asks the current server retriever for valuable
 * information to then register/update this information as managed objects in
 * agent mib
 */
@Dependent
public class MIBManager implements Serializable {

    @Inject
    @Log
    Logger log;
    @Inject
    private SNMPAgent agent;
    @Inject
    Instance<OID> serverName;
    @Inject
    Instance<OID> serverUptime;
    @Inject
    Instance<OID> serverActiveSessions;
    @Inject
    Instance<OID> serverUsedMemory;
    @Inject
    Instance<OID> serverAvailableMemory;
    @Inject
    Instance<OID> serverCpuTime;
    @Inject
    Instance<OID> serverActiveTransactions;
    @Inject
    Instance<OID> serverCommitedTransactions;
    @Inject
    Instance<OID> serverRollbackTransactions;
    @Inject
    Instance<OID> serverActiveThreads;
    @Inject
    Instance<OID> serverTotalRequests;
    @Inject
    Instance<OID> serverAddress;
    /**
     * A server retriever is the guy who retrieves information from a server,
     * the default retriever is GlassfishRetriever you can switch the retriever
     * via CDI Alternatives see:
     * http://docs.oracle.com/javaee/6/tutorial/doc/gjsdf.html
     */
    @Inject
    private ServerRetriever serverRetriever;

    /**
     * register known OIDs in the management information base
     */
    public void initMIB() {
        log.debug("Registering Mib objects...");
        agent.registerManagedObject(MOCreator.createReadWrite(serverName.get(),
                serverRetriever.getServerName()));
        agent.registerManagedObject(MOCreator.createReadWrite(serverAddress.get(),
                serverRetriever.getServerAddress()));
        agent.registerManagedObject(MOCreator.createReadWrite(serverUptime.get(),
                serverRetriever.getServerUpTime()));
        agent.registerManagedObject(MOCreator.createReadWrite(serverActiveSessions.get(),
                serverRetriever.getServerActiveSessions()));
        agent.registerManagedObject(MOCreator.createReadWrite(serverUsedMemory.get(),
                serverRetriever.getServerUsedMemory()));
        agent.registerManagedObject(MOCreator.createReadWrite(serverAvailableMemory.get(),
                serverRetriever.getServerAvailableMemory()));
        agent.registerManagedObject(MOCreator.createReadWrite(serverCpuTime.get(),
                serverRetriever.getServerCpuTime()));
        agent.registerManagedObject(MOCreator.createReadWrite(serverActiveTransactions.get(),
                serverRetriever.getServerActiveTransactions()));
        agent.registerManagedObject(MOCreator.createReadWrite(serverCommitedTransactions.get(),
                serverRetriever.getServerCommitedTransactions()));
        agent.registerManagedObject(MOCreator.createReadWrite(serverRollbackTransactions.get(),
                serverRetriever.getServerRollbackTransactions()));
        agent.registerManagedObject(MOCreator.createReadWrite(serverActiveThreads.get(),
                serverRetriever.getServerActiveThreads()));
        agent.registerManagedObject(MOCreator.createReadWrite(serverTotalRequests.get(),
                serverRetriever.getServerTotalRequests()));
        log.debug("MIB objects registered successfully");
    }

    /**
     * updates dynamic values in management information base
     */
    public void updateMIB() {
        this.setScalar(serverUptime.get(), new OctetString(serverRetriever.getServerUpTime()));
        this.setScalar(serverActiveSessions.get(), new OctetString(serverRetriever.getServerActiveSessions()));
        this.setScalar(serverUsedMemory.get(), new OctetString(serverRetriever.getServerUsedMemory()));
        this.setScalar(serverAvailableMemory.get(), new OctetString(serverRetriever.getServerAvailableMemory()));
        this.setScalar(serverCpuTime.get(), new OctetString(serverRetriever.getServerCpuTime()));
        this.setScalar(serverActiveTransactions.get(), new OctetString(serverRetriever.getServerActiveTransactions()));
        this.setScalar(serverCommitedTransactions.get(), new OctetString(serverRetriever.getServerCommitedTransactions()));
        this.setScalar(serverRollbackTransactions.get(), new OctetString(serverRetriever.getServerRollbackTransactions()));
        this.setScalar(serverActiveThreads.get(), new OctetString(serverRetriever.getServerActiveThreads()));
        this.setScalar(serverTotalRequests.get(), new OctetString(serverRetriever.getServerTotalRequests()));
    }

    public MOScalar findScalar(OID oid) {
        return (MOScalar) agent.getServer().getManagedObject(oid, agent.getDefaultContext());
    }

    public void setScalar(OID oid, Variable value) {
        findScalar(oid).setValue(value);
    }
}
