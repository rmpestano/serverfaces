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
package org.serverfaces.agent.server.glassfish;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.serverfaces.agent.exception.CouldNotRetrieveDataException;
import org.serverfaces.agent.server.ServerRetriever;
import org.serverfaces.common.qualifier.Log;

/**
 *
 * @author Rafael M. Pestano - Nov 15, 2012 6:54:16 PM
 */
public class GlassfishRetriever implements ServerRetriever {

    @Inject
    @Log
    Logger log;
    protected Client restClient;
    @Inject
    private Instance<String> serverAddress;
    private WebResource managementResource;

    @PostConstruct
    public void initializeClient() {
        this.restClient = Client.create();
    }

    //SERVER BASIC INFO
    
    @Override
    public String getServerAddress() {
        return serverAddress.get();
    }
    
    @Override
    public String getServerName() {
        managementResource = this.restClient.resource(getRestManagementURI());
        try {
            JSONObject result = getJSONObject("version");
            return result.getString("message");
        } catch (JSONException ex) {
            log.error("Could not retrieve serverName:" + ex.getMessage());
        }
        return null;
    }
    

    @Override
    public String getServerUpTime() {
        try {
            this.managementResource = this.restClient.resource(getRestManagementURI());
            JSONObject result = getJSONObject("uptime");
            return result.getString("message");
        } catch (JSONException ex) {
            log.error("Could not retrieve serverUptime:" + ex.getMessage());
        }
        return null;
    }

    //SERVER SESSIONS
    /**
     * URI: /monitoring/domain/server/web/session/activesessionscurrent
     *
     * @return
     */
    @Override
    public String getServerActiveSessions() {
        try {
            this.managementResource = this.restClient.resource(getRestMonitoringURI() + "web/session/");
            JSONObject result = getMonitoringJSONObject("activesessionscurrent");
            return result.getString("current");
        } catch (JSONException ex) {
            log.error("Could not retrieve serverActiveSessions:" + ex.getMessage());
        }
        return null;
    }

    //SERVER MEMORY
    /**
     * URI: /monitoring/domain/server/jvm/memory/usedheapsize-count
     *
     * @return
     */
    @Override
    public String getServerUsedMemory() {
        try {
            this.managementResource = this.restClient.resource(getRestMonitoringURI() + "jvm/memory/");
            JSONObject result = getMonitoringJSONObject("usedheapsize-count");
            return result.getString("count");
        } catch (JSONException ex) {
            log.error("Could not retrieve serverUsedMemory:" + ex.getMessage());
        }
        return null;
    }

    /**
     * URI: /monitoring/domain/server/jvm/memory/maxheapsize-count
     *
     * @return
     */
    @Override
    public String getServerAvailableMemory() {
         try {
            this.managementResource = this.restClient.resource(getRestMonitoringURI() + "jvm/memory/");
            JSONObject result = getMonitoringJSONObject("maxheapsize-count");
            return result.getString("count");
        } catch (JSONException ex) {
            log.error("Could not retrieve serverAvaiableMemory:" + ex.getMessage());
        }
        return null;
    }

    //SERVER CPU
    /**
     * URI: /monitoring/domain/server/jvm/thread-system/currentthreadcputime
     *
     * @return
     */
    @Override
    public String getServerCpuTime() {
         try {
            this.managementResource = this.restClient.resource(getRestMonitoringURI() + "jvm/thread-system/");
            JSONObject result = getMonitoringJSONObject("currentthreadcputime");
            Long resultInNano = result.getLong("count");
            //the result must be in ms as specified by ServerRetriever contract
            if(resultInNano != null){
                Long resultInMicro = new Long(resultInNano/1000000L);
                return resultInMicro.toString();
            } else{
                 return null;
            }
        } catch (JSONException ex) {
            log.error("Could not retrieve serverCpuTime:" + ex.getMessage());
        }
        return null;        
    }

    //SERVER TRANSACTIONS
    /**
     * URI: /monitoring/domain/server/transaction-service/activecount
     *
     * @return
     */
    @Override
    public String getServerActiveTransactions() {
         try {
            this.managementResource = this.restClient.resource(getRestMonitoringURI() + "transaction-service/");
            JSONObject result = getMonitoringJSONObject("activecount");
            return result.getString("count");
        } catch (JSONException ex) {
            log.error("Could not retrieve serverActiveTransactions:" + ex.getMessage());
        }
        return null;
    }

    /**
     * URI: /monitoring/domain/server/transaction-service/committedcount
     *
     * @return
     */
    @Override
    public String getServerCommitedTransactions() {
       try {
            this.managementResource = this.restClient.resource(getRestMonitoringURI() + "transaction-service/");
            JSONObject result = getMonitoringJSONObject("committedcount");
            return result.getString("count");
        } catch (JSONException ex) {
            log.error("Could not retrieve serverCommitedTransactions:" + ex.getMessage());
        }
        return null;
    }
    
    /**
     * URI: /monitoring/domain/server/transaction-service/rolledbackcount
     *
     * @return
     */
    @Override
    public String getServerRollbackTransactions() {
        try {
            this.managementResource = this.restClient.resource(getRestMonitoringURI() + "transaction-service/");
            JSONObject result = getMonitoringJSONObject("rolledbackcount");
            return result.getString("count");
        } catch (JSONException ex) {
            log.error("Could not retrieve serverRollbackTransactions:" + ex.getMessage());
        }
        return null;
    }

    //SERVER THREADS
    /**
     * URI: /monitoring/domain/server/jvm/thread-system/threadcount
     *
     * @return
     */
    @Override
    public String getServerActiveThreads() {
         try {
            this.managementResource = this.restClient.resource(getRestMonitoringURI() + "jvm/thread-system/");
            JSONObject result = getMonitoringJSONObject("threadcount");
            return result.getString("count");
        } catch (JSONException ex) {
            log.error("Could not retrieve serverActiveThreads:" + ex.getMessage());
        }
        return null;
    }

    //SERVER REQUESTS
    /**
     * URI: /monitoring/domain/server/web/request/requestcount
     *
     * @return
     */
    @Override
    public String getServerTotalRequests() {
        try {
            this.managementResource = this.restClient.resource(getRestMonitoringURI() + "web/request/");
            JSONObject result = getMonitoringJSONObject("requestcount");
            return result.getString("count");
        } catch (JSONException ex) {
            log.error("Could not retrieve serverTotalRequests:" + ex.getMessage());
        }
        return null;
    }

    //utility methods
    public String getRestManagementURI() {
        return serverAddress.get() + "/management/domain/";
    }

    public String getRestMonitoringURI() {
        return serverAddress.get() + "/monitoring/domain/server/";
    }

    JSONObject getJSONObject(String name) throws UniformInterfaceException {
        try {
            JSONObject result = this.managementResource.path(name).accept(MediaType.APPLICATION_JSON).get(JSONObject.class);
            return result;
        } catch (ClientHandlerException che) {
            throw new CouldNotRetrieveDataException("problems trying to retrieve data from glassfish rest api,monitoring module levels may be OFF");
        }
    }
    
    /**
     * glassfish monitoring API has the folowing pattern
     * objectName ->{extra-properties}->{entity}->{objectName}->property
     * 
     * 
     * @param name
     * @return JSONObject in glassfish monitoring API pattern
     * @throws UniformInterfaceException 
     */
    JSONObject getMonitoringJSONObject(String name) throws UniformInterfaceException,JSONException {
        try {
            JSONObject result = this.managementResource.path(name).accept(MediaType.APPLICATION_JSON).get(JSONObject.class);
            return result.getJSONObject("extraProperties").getJSONObject("entity").getJSONObject(name);
        } catch (ClientHandlerException che) {
            throw new CouldNotRetrieveDataException("problems trying to retrieve data from glassfish rest api,monitoring module levels may be OFF");
        } 
        
    }
    
    

    JSONArray getJSONArray(String key) {
        try {
            JSONArray result = this.managementResource.path(key).accept(MediaType.APPLICATION_JSON).get(JSONArray.class);
            return result;
        } catch (ClientHandlerException che) {
            throw new CouldNotRetrieveDataException("problems trying to retrieve data from glassfish rest api,monitoring module levels may be OFF");
        }
    }

}
