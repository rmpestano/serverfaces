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
package org.serverfaces.agent.server;

import java.util.List;
import org.serverfaces.common.model.Application;

/**
 *
 * @author Rafael M. Pestano
 */
public interface ServerRetriever {

    /**
     *
     * @return name of the server being monitored
     */
    String getServerName();
    
     /**
     *
     * @return address(url) where server is running
     */
    String getServerAddress();

    /**
     *
     * @return time since server has be started
     */
    String getServerUpTime();

    /**
     *
     * @return number of active sessions
     */
    Integer getServerActiveSessions();

    /**
     *
     * @return amount of used memory in bytes.
     */
    Integer getServerUsedMemory();

    /**
     *
     * @return amount of available memory in bytes.
     */
    Integer getServerAvailableMemory();

    /**
     *
     * @return returns the total CPU time for the current server thread in
     * microseconds(ms)
     */
    Integer getServerCpuTime();

    /**
     *
     * @return number of transactions that are currently active.
     */
    Integer getServerActiveTransactions();

    /**
     *
     * @return number of transactions that have been committed.
     */
    Integer getServerCommitedTransactions();

    /**
     *
     * @return number of transactions that have been rolled back.
     */
    Integer getServerRollbackTransactions();

    /**
     *
     * @return current number of live threads including both daemon and
     * non-daemon threads.
     */
    Integer getServerActiveThreads();

    /**
     *
     * @return cumulative number of requests processed so far
     */
    Long getServerTotalRequests();
    
   
    String getServerLog();
    
    /**
     * 
     * @return Cumulative value of the error count with error count representing 
     * the number of cases where the response code was greater than or equal to 400
     */
    Long getServerTotalErrors();
    
    /**
     * 
     * @return Longest response time(millisecond) for a request not a cumulative value but 
     * the largest response time from among the response times
     */
    Integer getServerMaxResponseTime();
    
    /**
     * 
     * @return Average response time(millisecond)
     */
    Integer getServerAvgResponseTime();
    
    
    List<Application> getServerApplications();
}