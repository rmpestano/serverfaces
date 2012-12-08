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
package org.serverfaces.agent.mo;

import org.snmp4j.agent.mo.MOAccessImpl;
import org.snmp4j.agent.mo.MOScalar;
import org.snmp4j.smi.Counter64;
import org.snmp4j.smi.Gauge32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.Variable;

/**
 *
 * @author Rafael M. Pestano
 */
public class MOScalarFactory {

    public static MOScalar createReadOnly(OID oid, Object value) {
        return new MOScalar(oid,
                MOAccessImpl.ACCESS_READ_ONLY,
                getVariable(value));
    }

    public static MOScalar createReadWrite(OID oid, Object value) {
        System.out.println("Criando OID:"+oid);
        System.out.println("Valor:"+value);
        return new MOScalar(oid,
                MOAccessImpl.ACCESS_READ_WRITE,
                getVariable(value));
    }

    private static Variable getVariable(Object value) {
        if (value == null) {
            return new OctetString();
        }
        if (value instanceof String) {
            return new OctetString((String) value);
        }
        if (value instanceof Integer) {
            return new Gauge32(((Integer) value).longValue());
        }
        if (value instanceof Long) {
            return new Counter64(((Long) value));
        }
        
        throw new IllegalArgumentException("Unmanaged Type: " + value.getClass());
    }
}