/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.serverfaces.agent.event;

import java.io.Serializable;
import org.snmp4j.agent.DefaultMOServer;
import org.snmp4j.smi.OctetString;

/**
 *
 * @author Rafael M. Pestano - Nov 26, 2012 10:33:51 AM
 */
public class InitMibEvent implements Serializable {

    private DefaultMOServer defaultMOServer;
    private OctetString defaultContext;

    public InitMibEvent() {
    }

    public InitMibEvent(DefaultMOServer defaultMOServer, OctetString defaultContext) {
        this.defaultMOServer = defaultMOServer;
        this.defaultContext = defaultContext;
    }

    public DefaultMOServer getDefaultMOServer() {
        return defaultMOServer;
    }

    public void setDefaultMOServer(DefaultMOServer defaultMOServer) {
        this.defaultMOServer = defaultMOServer;
    }

    public OctetString getDefaultContext() {
        return defaultContext;
    }

    public void setDefaultContext(OctetString defaultContext) {
        this.defaultContext = defaultContext;
    }
}
