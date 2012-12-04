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

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import org.apache.log4j.Logger;
import org.serverfaces.agent.SNMPAgent;
import org.serverfaces.agent.event.InitMibEvent;
import org.serverfaces.agent.event.UpdateMibEvent;
import org.serverfaces.agent.exception.CouldNotRetrieveDataException;
import org.serverfaces.agent.util.Constants;
import org.serverfaces.common.qualifier.Log;

/**
 *
 * @author Rafael M. Pestano - Nov 01, 2012 17:11:16 PM
 */
@Singleton
@Startup
@Lock(LockType.READ)
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class Monitor {

    @Inject
    @Log
    Logger log;
    @Inject
    SNMPAgent agent;
    @Inject
    Event<UpdateMibEvent> updateMib;
    @Inject
    Event<InitMibEvent> initMib;
    
    @PostConstruct
    public void startup() {
        try {
            log.debug("SNMP agent is starting...");
            agent.start();
            initMib.fire(new InitMibEvent(agent.getServer(),agent.getDefaultContext()));
            log.debug("SNMP agent started successfully.");
        } catch (CouldNotRetrieveDataException retrieveEX) {
            log.error("Agent is going to be stopped due to the following error:" + retrieveEX.getMessage() + ".\nYou can try to resolve the problem and start agent later via web interface");
            this.terminate();
        } catch (Exception ex) {
            log.error("Could not start SNMP Agent due to the following error:" + ex.getMessage());
            this.terminate();
            ex.printStackTrace();
        }



    }

    @PreDestroy
    public void terminate() {
        log.debug("Stopping SNMP agent...");
        agent.stop();
        log.debug("SNMP Agent stopped successfully.");
    }

    @Schedule(hour = "*", minute = "*", second = Constants.MONITORING_INTERVAL)
    public void doMonitoring() {
        if (!agent.isRunning()) {
            log.debug("Nothing to monitor, Agent is not started.");
            return;
        }
        log.debug("Starting SNMP monitoriment...");
        try {
            updateMib.fire(new UpdateMibEvent());
            log.debug("Finished SNMP monitoriment, mib is now updated...");
        } catch (CouldNotRetrieveDataException ex) {
            log.error("Agent is going to be stopped due to the following error:" + ex.getMessage() + ". You can start it again through web interface.");
            this.terminate();
        }catch(Exception ex){
            log.error("Unexpected exception during monitoriment: "+ex.getMessage() + ". Agent will be stoped now, please start agent manualy through web interface");
            this.terminate();
            if(log.isDebugEnabled()){
                ex.printStackTrace();
            }
        }

    }
}
