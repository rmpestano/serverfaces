/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.serverfaces.agent.util;

import java.io.IOException;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

/**
 *
 * @author Rafael M. Pestano - Nov 16, 2012 5:12:20 PM
 *
 * A simple snmp manager to test agent
 */
public class SimpleSNMPManager {

    Snmp snmp = null;
    @Inject 
    Instance<String> agentAddress;
    
    private TransportMapping transport;

    
    public TransportMapping getTransport() throws IOException{
        if(transport == null){
                transport =  new DefaultUdpTransportMapping();
        }
       return transport;     
    }
    
    @PostConstruct
    public void initManager() throws IOException{
        this.start();
    }

    /**
     * Start the Snmp session. If you forget the listen() method you will not
     * get any answers because the communication is asynchronous and the
     * listen() method listens for answers.
     *
     * @throws IOException
     */
    public void start() throws IOException {
        snmp = new Snmp(getTransport());
        // Do not forget this line!
        getTransport().listen();
    }
    
    public void stop() throws IOException{
        getTransport().close();
        snmp = null;
    }

    /**
     * Method which takes a single OID and returns the response from the agent
     * as a String.
     *
     * @param oid
     * @return
     * @throws IOException
     */
    public String getAsString(OID oid) throws IOException {
        ResponseEvent event = get(new OID[]{oid});
        PDU pdu = event.getResponse();
        if(pdu != null){
            return pdu.get(0).getVariable().toString();
        }
        return null;
    }
    
     public Long getAsLong(OID oid) throws IOException {
        ResponseEvent event = get(new OID[]{oid});
        PDU pdu = event.getResponse();
        if(pdu != null){
            Variable var = pdu.get(0).getVariable();
            return (var != null ? var.toLong(): null);
        }
        return null;
    }
    
     public Integer getAsInt(OID oid) throws IOException {
        ResponseEvent event = get(new OID[]{oid});
        PDU pdu = event.getResponse();
        if(pdu != null){
            Variable var = pdu.get(0).getVariable();
            return (var != null ? var.toInt(): null);
        }
        return null;
    }

    /**
     * This method is capable of handling multiple OIDs
     *
     * @param oids
     * @return
     * @throws IOException
     */
    public ResponseEvent get(OID oids[]) throws IOException {
        PDU pdu = new PDU();
        for (OID oid : oids) {
            pdu.add(new VariableBinding(oid));
        }
        pdu.setType(PDU.GET);
        ResponseEvent event = snmp.send(pdu, getTarget(), null);
        // manager sends a PDU from adress new UdpAddress()
        // to target which is the address where agent is listening
        if (event != null) {
            return event;
        }
        throw new RuntimeException("GET timed out");
    }

    /**
     * This method returns a Target, which contains information about where the
     * data should be fetched and how.
     *
     * @return
     */
    private Target getTarget() {
        Address targetAddress = GenericAddress.parse(agentAddress.get());
        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString("public"));
        target.setAddress(targetAddress);
        target.setRetries(2);
        target.setTimeout(1500);
        target.setVersion(SnmpConstants.version2c);
        return target;
    }
}
