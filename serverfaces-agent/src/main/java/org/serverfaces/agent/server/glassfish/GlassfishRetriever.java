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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
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
import org.serverfaces.common.model.Application;
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
            throw new CouldNotRetrieveDataException("Could not retrieve serverName:" + ex.getMessage());
        }
    }

    @Override
    public String getServerUpTime() {
        try {
            this.managementResource = this.restClient.resource(getRestManagementURI());
            JSONObject result = getJSONObject("uptime");
            return result.getString("message");
        } catch (JSONException ex) {
            throw new CouldNotRetrieveDataException("Could not retrieve serverUptime:" + ex.getMessage());
        }
    }

    //SERVER SESSIONS
    /**
     * URI: /monitoring/domain/server/web/session/activesessionscurrent
     *
     * @return
     */
    @Override
    public Integer getServerActiveSessions() {
        try {
            this.managementResource = this.restClient.resource(getRestMonitoringURI() + "web/session/");
            JSONObject result = getMonitoringJSONObject("activesessionscurrent");
            Integer value = result.getInt("current");
            return value == null || value < 0 ? 0: value ;
        } catch (JSONException ex) {
            throw new CouldNotRetrieveDataException("Could not retrieve serverActiveSessions:" + ex.getMessage());
        }
    }

    //SERVER MEMORY
    /**
     * URI: /monitoring/domain/server/jvm/memory/usedheapsize-count
     *
     * @return
     */
    @Override
    public Integer getServerUsedMemory() {
        try {
            this.managementResource = this.restClient.resource(getRestMonitoringURI() + "jvm/memory/");
            JSONObject result = getMonitoringJSONObject("usedheapsize-count");
            Integer value = result.getInt("count");
            return value != null ? value : 0;
        } catch (JSONException ex) {
            throw new CouldNotRetrieveDataException("Could not retrieve serverUsedMemory:" + ex.getMessage());
        }
    }

    /**
     * URI: /monitoring/domain/server/jvm/memory/maxheapsize-count
     *
     * @return
     */
    @Override
    public Integer getServerAvailableMemory() {
        try {
            this.managementResource = this.restClient.resource(getRestMonitoringURI() + "jvm/memory/");
            JSONObject result = getMonitoringJSONObject("maxheapsize-count");
            Integer value = result.getInt("count");
            return value != null ? value : 0;
        } catch (JSONException ex) {
            throw new CouldNotRetrieveDataException("Could not retrieve serverAvaiableMemory:" + ex.getMessage());
        }
    }

    //SERVER CPU
    /**
     * URI: /monitoring/domain/server/jvm/thread-system/currentthreadcputime
     *
     * @return
     */
    @Override
    public Integer getServerCpuTime() {
        try {
            this.managementResource = this.restClient.resource(getRestMonitoringURI() + "jvm/thread-system/");
            JSONObject result = getMonitoringJSONObject("currentthreadcputime");
            Long resultInNano = result.getLong("count");
            //the result must be in ms as specified by ServerRetriever contract
            if (resultInNano != null) {
                Long resultInMicro = new Long(resultInNano / 1000000L);
                return resultInMicro.intValue();
            } else {
                return 0;
            }
        } catch (JSONException ex) {
            throw new CouldNotRetrieveDataException("Could not retrieve serverCpuTime:" + ex.getMessage());
        }
    }

    //SERVER TRANSACTIONS
    /**
     * URI: /monitoring/domain/server/transaction-service/activecount
     *
     * @return
     */
    @Override
    public Integer getServerActiveTransactions() {
        try {
            this.managementResource = this.restClient.resource(getRestMonitoringURI() + "transaction-service/");
            JSONObject result = getMonitoringJSONObject("activecount");
            Integer value = result.getInt("count");
            return value != null ? value : 0;
        } catch (JSONException ex) {
            throw new CouldNotRetrieveDataException("Could not retrieve serverActiveTransactions:" + ex.getMessage());
        }
    }

    /**
     * URI: /monitoring/domain/server/transaction-service/committedcount
     *
     * @return
     */
    @Override
    public Integer getServerCommitedTransactions() {
        try {
            this.managementResource = this.restClient.resource(getRestMonitoringURI() + "transaction-service/");
            JSONObject result = getMonitoringJSONObject("committedcount");
            Integer value = result.getInt("count");
            return value != null ? value : 0;
        } catch (JSONException ex) {
            throw new CouldNotRetrieveDataException("Could not retrieve serverCommitedTransactions:" + ex.getMessage());
        }
    }

    /**
     * URI: /monitoring/domain/server/transaction-service/rolledbackcount
     *
     * @return
     */
    @Override
    public Integer getServerRollbackTransactions() {
        try {
            this.managementResource = this.restClient.resource(getRestMonitoringURI() + "transaction-service/");
            JSONObject result = getMonitoringJSONObject("rolledbackcount");
            Integer value = result.getInt("count");
            return value != null ? value : 0;
        } catch (JSONException ex) {
            throw new CouldNotRetrieveDataException("Could not retrieve serverRollbackTransactions:" + ex.getMessage());
        }
    }

    //SERVER THREADS
    /**
     * URI: /monitoring/domain/server/jvm/thread-system/threadcount
     *
     * @return
     */
    @Override
    public Integer getServerActiveThreads() {
        try {
            this.managementResource = this.restClient.resource(getRestMonitoringURI() + "jvm/thread-system/");
            JSONObject result = getMonitoringJSONObject("threadcount");
            Integer value = result.getInt("count");
            return value != null ? value : 0;
        } catch (JSONException ex) {
            throw new CouldNotRetrieveDataException("Could not retrieve serverActiveThreads:" + ex.getMessage());
        }
    }

    //SERVER REQUESTS
    /**
     * URI: /monitoring/domain/server/web/request/requestcount
     *
     * @return
     */
    @Override
    public Long getServerTotalRequests() {
        try {
            this.managementResource = this.restClient.resource(getRestMonitoringURI() + "web/request/");
            JSONObject result = getMonitoringJSONObject("requestcount");
            Long value = result.getLong("count");
            return value != null ? value : 0;
        } catch (JSONException ex) {
            throw new CouldNotRetrieveDataException("Could not retrieve serverTotalRequests:" + ex.getMessage());
        }
    }
    
    /**
     * URI: /monitoring/domain/server/web/request/errorcount
     *
     * @return
     */
    @Override
    public Long getServerTotalErrors(){
         try {
            this.managementResource = this.restClient.resource(getRestMonitoringURI() + "web/request/");
            JSONObject result = getMonitoringJSONObject("errorcount");
            Long value = result.getLong("count");
            return value != null ? value : 0;
        } catch (JSONException ex) {
            throw new CouldNotRetrieveDataException("Could not retrieve serverErrors:" + ex.getMessage());
        }
    }
    
    /**
     * URI: /monitoring/domain/server/web/request/processingtime
     *
     * @return
     */
    @Override
    public Integer getServerAvgResponseTime(){
         try {
            this.managementResource = this.restClient.resource(getRestMonitoringURI() + "web/request/");
            JSONObject result = getMonitoringJSONObject("processingtime");
            Integer value = result.getInt("count");
            return value != null ? value : 0;
        } catch (JSONException ex) {
            throw new CouldNotRetrieveDataException("Could not retrieve serverAvgRequestTime:" + ex.getMessage());
        }
    } 
    
     /**
     * URI: /monitoring/domain/server/web/request/maxtime
     *
     * @return
     */
    @Override
    public Integer getServerMaxResponseTime(){
         try {
            this.managementResource = this.restClient.resource(getRestMonitoringURI() + "web/request/");
            JSONObject result = getMonitoringJSONObject("maxtime");
            Integer value = result.getInt("count");
            return value != null ? value : 0;
        } catch (JSONException ex) {
            throw new CouldNotRetrieveDataException("Could not retrieve serverMaxRequestTime:" + ex.getMessage());
        }
    } 
    
    @Override
    public List<Application> getServerApplications(){
        try {
            List<Application> currentApplications = new ArrayList<Application>();
            this.managementResource = this.restClient.resource(getRestMonitoringURI());
            JSONObject appEntries = getJSONObject("applications/").getJSONObject("extraProperties").getJSONObject("childResources");
            Iterator<String> i = appEntries.keys();
             for ( Iterator<String> entries =  appEntries.keys(); entries.hasNext(); ) {
                 try{
                     String name = entries.next();
                     this.managementResource =  this.restClient.resource(getRestMonitoringURI() +"applications/"+name+"/server/");
                     Application app = new Application();
                     app.setName(name);
                     app.setActiveSessions(getMonitoringJSONObject("activesessionscurrent").getInt("current"));
                     app.setTotalRequests(getMonitoringJSONObject("requestcount").getLong("count"));
                     app.setTotalErrors(getMonitoringJSONObject("errorcount").getLong("count"));
                     app.setMaxResponseTime(getMonitoringJSONObject("maxtime").getInt("count"));
                     app.setAvgResponseTime(getMonitoringJSONObject("processingtime").getInt("count"));
                     currentApplications.add(app);
                 }catch(Exception ex){
                     //do nothing, only get applications with 'server' related info
                 }
                 
             }
             return currentApplications;
        } catch (JSONException ex) {
             throw new CouldNotRetrieveDataException("Could not retrieve serverApplications:" + ex.getMessage());
        }
    }

    @Override
    public String getServerLog() {
        return null;//TODO read server.log file
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
        } catch (CouldNotRetrieveDataException cnrex) {
            throw new CouldNotRetrieveDataException("problems trying to retrieve data from server:" + cnrex.getMessage() + " .Monitoring module levels may be OFF.");
        } catch (Exception ex) {
            throw new CouldNotRetrieveDataException(ex.getMessage());
        }
    }

    /**
     * glassfish monitoring API has the folowing pattern objectName
     * ->{extra-properties}->{entity}->{objectName}->property
     *
     *
     * @param name
     * @return JSONObject in glassfish monitoring API pattern
     * @throws UniformInterfaceException
     */
    JSONObject getMonitoringJSONObject(String name) throws JSONException {
        try {
            JSONObject result = this.managementResource.path(name).accept(MediaType.APPLICATION_JSON).get(JSONObject.class);
            return result.getJSONObject("extraProperties").getJSONObject("entity").getJSONObject(name);
        } catch (UniformInterfaceException ue) {
            throw new CouldNotRetrieveDataException("Problems trying to access Glassfish REST API, make sure monitoring levels are ON");
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
