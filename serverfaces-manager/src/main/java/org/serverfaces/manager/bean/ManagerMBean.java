/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.serverfaces.manager.bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.log4j.Logger;
import org.serverfaces.common.qualifier.Log;
import org.serverfaces.manager.SNMPManager;
import org.serverfaces.manager.model.Server;
import org.serverfaces.manager.util.MessagesController;
import org.snmp4j.smi.OID;

/**
 *
 * @author Rafael M. Pestano - Nov 18, 2012 7:23:53 PM
 */

@Named
@SessionScoped
public class ManagerMBean implements Serializable{
    
    private final int NUM_ALLOWED_SERVERS = 10;
    private String agentAddress;
    @Inject @Log
    private transient Logger log;
    
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
    private SNMPManager sNMPManager;
    
    @Inject
    private MessagesController messages;
    
    private List<Server> servers;
    
    
    @PostConstruct
    public void initialize(){
        servers = new ArrayList<Server>();
    }

    public void addServer(){
        try {
            //TODO remove this restriction after adding dynaForm in server.xhml
            if(getNumMonitoredServers() == NUM_ALLOWED_SERVERS){
                messages.addError("You cannot manage more then 10 servers!");
                return;
            }
            if(this.serverAlreadyMonitored(agentAddress)){
                messages.addError("Could not add server because its already being monitored!");
                return;
            }
            
            if(!this.verifyAgentConection()){
                messages.addError("Could not establish connection with agent:"+agentAddress);
                return;
            }
            Server s = new Server(agentAddress);
            servers.add(s);
            this.doServerMoniring(s);
            messages.addInfo("Server added successfully!");
        } catch (IOException ex) {
            //TODO handle exception
        }
    }
    
    
    public int getNumMonitoredServers(){
        return servers.size();
    }
    
    /**
     * @param agentAddress
     */
    private boolean serverAlreadyMonitored(String agentAddress){
        for (Server server : servers) {
            if(server.getAgentAddress().equalsIgnoreCase(agentAddress)){
                return true;
            }
        }
        return false;
    }

    public List<Server> getServers() {
        return servers;
    }

    public void setServers(List<Server> servers) {
        this.servers = servers;
    }

    public String getAgentAddress() {
        return agentAddress;
    }

    public void setAgentAddress(String agentAddress) {
        this.agentAddress = agentAddress;
    }

    private boolean verifyAgentConection() {
          try{
              sNMPManager.setAgentAddress(agentAddress);
              sNMPManager.getAsString(serverName.get());
              return true;
          }catch(Exception ex){
              return false;
          }
         
                 
    }

    public void doServerMoniring(Server server) throws IOException {
          sNMPManager.setAgentAddress(server.getAgentAddress());
          server.getInfo().setName(sNMPManager.getAsString(serverName.get()));
          server.getInfo().setActiveSessions(sNMPManager.getAsString(serverActiveSessions.get()));
          server.getInfo().setActiveThreads(sNMPManager.getAsString(serverActiveThreads.get()));
          server.getInfo().setActiveTransactions(sNMPManager.getAsString(serverActiveTransactions.get()));
          server.getInfo().setAvailableMemory(sNMPManager.getAsString(serverAvailableMemory.get()));
          server.getInfo().setUsedMemory(sNMPManager.getAsString(serverUsedMemory.get()));
          server.getInfo().setCommitedTransactions(sNMPManager.getAsString(serverCommitedTransactions.get()));
          server.getInfo().setCpuTime(sNMPManager.getAsString(serverCpuTime.get()));
          server.getInfo().setRollbackTransactions(sNMPManager.getAsString(serverRollbackTransactions.get()));
          server.getInfo().setTotalRequests(sNMPManager.getAsString(serverTotalRequests.get()));
          server.getInfo().setUptime(sNMPManager.getAsString(serverUptime.get()));
     }
    
}
