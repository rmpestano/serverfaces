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
package org.serverfaces.agent.monitoriment;

import org.serverfaces.agent.mib.MIBManager;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import org.apache.log4j.Logger;
import org.serverfaces.agent.SNMPAgent;
import org.serverfaces.agent.exception.CouldNotRetrieveDataException;
import org.serverfaces.agent.util.Constants;
import org.serverfaces.common.qualifier.Log;
import org.snmp4j.smi.OctetString;

/**
 *
 * @author Rafael M. Pestano - Nov 01, 2012 17:11:16 PM
 */
@Singleton
@Startup
public class Monitor {

    @Inject @Log
    Logger log;
    
    @Inject 
    private SNMPAgent agent;

    @Inject
    private MIBManager mibManager;
    
    boolean started;
    
    
    @PostConstruct
    public void startup() {
        try {
            log.debug("SNMP agent is starting...");
            
            agent.start();
            // Since BaseAgent registers some MIBs by default we need to unregister
            // one before we register our own sysDescr.
            agent.unregisterManagedObject(agent.getSnmpv2MIB());
            agent.setDefaultContext(new OctetString("public"));
            mibManager.initMIB();
            log.debug("SNMP agent started successfully.");
            started = true;
        } catch(CouldNotRetrieveDataException retrieveEX){
            //if agent cannot retrieve data from the server it doest make sense do be monitoring
            log.error("Agent is going to be stopped due to the following error:"+retrieveEX.getMessage());
            this.terminate();
        }catch(java.net.BindException be){
              //address already in use
            log.error("Could not start SNMP Agent due to the following error:" + be.getMessage());
        } catch (Exception ex) {
            log.error("Could not start SNMP Agent due to the following error:" + ex.getMessage());
            this.terminate();
            ex.printStackTrace(); 
        }



    }

    @PreDestroy
    public void terminate() {
        log.debug("Stopping SNMP agent...");
        if(agent != null && agent.getSession() != null){
          agent.stop();   
        }
        agent = null;
        log.debug("SNMP Agent stopped successfully.");
    }

    @Schedule(hour = "*", minute = "*", second = Constants.MONITORING_INTERVAL)
    public void doMonitoring() {
        if(!started){
            throw new RuntimeException("Agent is not started, monitoriment will be cancelled.");
        }
        log.debug("Starting SNMP monitoriment...");
        try{
            mibManager.updateMIB();
        }catch(CouldNotRetrieveDataException ex){
            log.error("Agent is going to be stopped due to the following error:"+ex.getMessage());
            this.terminate();
        }
        
        log.debug("Finished SNMP monitoriment, mib is now updated...");
    }
}
