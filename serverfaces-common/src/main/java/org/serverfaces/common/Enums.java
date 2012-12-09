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
package org.serverfaces.common;

/**
 *
 * @author Rafael M. Pestano
 * 
 * enum values are keys in the properties files
 */
public enum Enums {

    ENUMERATIONS;

    public static enum AGENT {

        /**
         * address where snmp agent will be listen
         * in the form of {protocol}:{ip-address}/{port}
         * where protocol = udp/tcp,
         * eg: udp:127.0.0.1/16112
         * 
         */
        ADDRESS("agent.address");
        public final String value;

        AGENT(String value) {
            this.value = value;
        }

    }
    
     public static enum SERVER {

        
        ADDRESS("server.address");
        public final String value;

        SERVER(String value) {
            this.value = value;
        }

    }
    
    public static enum MIB {

        SERVER_NAME("server.name"),
        SERVER_ADDRESS("server.address"),
        SERVER_UPTIME("server.uptime"),
        SERVER_ACTIVE_SESSIONS("server.activeSessions"),
        SERVER_USED_MEMORY("server.usedMemory"),
        SERVER_AVAILABLE_MEMORY("server.availableMemory"),
        SERVER_CPU_TIME("server.cpuTime"),
        SERVER_ACTIVE_TRANSACTIONS("server.activeTransactions"),
        SERVER_COMMITED_TRANSACTIONS("server.commitedTransactions"),
        SERVER_ROLLBACK_TRANSACTIONS("server.rollbackTransactions"),
        SERVER_ACTIVE_THREADS("server.activeThreads"),
        SERVER_TOTAL_REQUESTS("server.totalRequests"),
        SERVER_LOG("server.log"),
        SERVER_TOTAL_ERRORS("server.totalErrors"),
        SERVER_MAX_RESPONSE("server.maxResponseTime"),
        SERVER_AVG_RESPONSE("server.avgResponseTime"),
        SERVER_APPLICATIONS("server.applications"),
        SERVER_COMMAND("server.command");
        
        
        public final String value;

        MIB(String value) {
            this.value = value;
        }

    }
}
