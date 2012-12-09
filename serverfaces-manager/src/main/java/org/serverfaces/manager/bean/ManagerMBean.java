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
import org.serverfaces.common.manager.SNMPManager;
import org.serverfaces.common.model.Server;
import org.serverfaces.common.qualifier.Log;
import org.serverfaces.manager.util.MessagesController;
import org.snmp4j.smi.OID;

/**
 *
 * @author Rafael M. Pestano - Nov 18, 2012 7:23:53 PM
 */

@Named
@SessionScoped
public class ManagerMBean implements Serializable{
    
    private String agentAddress;
    private Server server;
    
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
    Instance<OID> serverTotalErrors;
    @Inject
    Instance<OID> serverMaxResponseTime;
    @Inject
    Instance<OID> serverAvgResponseTime;
    @Inject
    Instance<OID> serverLog;
   
    
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
             //for local addresses just use '/'+'port' eg: /16612 = localhost/16112
            if(agentAddress != null && agentAddress.startsWith("/")){
                agentAddress = "localhost".concat(agentAddress);
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
            this.doServerMontoring(s);
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

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }
    

    private boolean verifyAgentConection() {
          try{
              sNMPManager.setAgentAddress(agentAddress);
              String result = sNMPManager.getAsString(serverName.get());
              if(result == null || "".equals(result)){
                  return false;
              }
              return true;
          }catch(Exception ex){
              return false;
          }
         
                 
    }

    public void doMonitoring() throws IOException{
        for (Server server : servers) {
            doServerMontoring(server);
        }
    }
    
    public void doServerMontoring(Server server) throws IOException {
          sNMPManager.setAgentAddress(server.getAgentAddress());
          server.setName(sNMPManager.getAsString(serverName.get()));
          server.setActiveSessions(sNMPManager.getAsInt(serverActiveSessions.get()));
          server.setActiveThreads(sNMPManager.getAsInt(serverActiveThreads.get()));
          server.setActiveTransactions(sNMPManager.getAsInt(serverActiveTransactions.get()));
          server.setAvailableMemory(sNMPManager.getAsInt(serverAvailableMemory.get()));
          server.setUsedMemory(sNMPManager.getAsInt(serverUsedMemory.get()));
          server.setCommitedTransactions(sNMPManager.getAsInt(serverCommitedTransactions.get()));
          server.setCpuTime(sNMPManager.getAsInt(serverCpuTime.get()));
          server.setRollbackTransactions(sNMPManager.getAsInt(serverRollbackTransactions.get()));
          server.setTotalRequests(sNMPManager.getAsLong(serverTotalRequests.get()));
          server.setTotalErrors(sNMPManager.getAsLong(serverTotalErrors.get()));
          server.setMaxResponseTime(sNMPManager.getAsInt(serverMaxResponseTime.get()));
          server.setAvgResponseTime(sNMPManager.getAsInt(serverAvgResponseTime.get()));
          server.setUptime(sNMPManager.getAsString(serverUptime.get()));
          server.setApplications(sNMPManager.getServerApplications());
     }
    
    public void doApplicationMonitoring(Server s) throws IOException{
        server = s;
    }
    
    public void doRemoveServer(Server s){
        if(server != null && server.equals(s)){
            server = null;
        }
        messages.addInfo("Server "+s.getName() +" removed successfully");
        this.getServers().remove(s);
        
    }
    
}
