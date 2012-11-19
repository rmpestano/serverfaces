/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.serverfaces.manager.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.log4j.Logger;
import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;
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
    
    private DashboardModel model;
    private final int LEFT_COLUMN_INDEX = 0;
    private final int RIGHT_COLUMN_INDEX = 1;
    private final int NUM_ALLOWED_SERVERS = 10;
    private String agentAddress;
    @Inject @Log
    private transient Logger log;
    
    @Inject
    Instance<OID> serverName;
    
    @Inject
    private SNMPManager sNMPManager;
    
    @Inject
    private MessagesController messages;
    
    private List<Server> servers;
    
    
    @PostConstruct
    public void initialize(){
        model = new DefaultDashboardModel();
        model.addColumn(new DefaultDashboardColumn());
        model.addColumn(new DefaultDashboardColumn());
        servers = new ArrayList<Server>();
    }

    public DashboardModel getModel() {
        return model;
    }

    public void setModel(DashboardModel model) {
        this.model = model;
    }
    
    public void addServer(){
        
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
        
        //add widget to the column with less servers
        DashboardColumn column = null;
        if(model.getColumn(LEFT_COLUMN_INDEX).getWidgetCount() <= model.getColumn(RIGHT_COLUMN_INDEX).getWidgetCount()){
            column = model.getColumn(LEFT_COLUMN_INDEX);
        }
        else{
            column = model.getColumn(RIGHT_COLUMN_INDEX);
        }
        column.addWidget("panel"+getNumMonitoredServers());
        Server s = new Server(agentAddress);
        s.getInfo().setName("Glassfish");
        servers.add(s);
        messages.addInfo("Server added successfully!");
    }
    
    public String getWidgetByIndex(int index){
        
        return getColumnByIndex(index).getWidget(index); 
    }
    
    private void removeColumnWidget(String widget, int column){
        model.getColumn(column).removeWidget(widget);
    }
    
    private void addColumnWidget(String widget, int column){
        model.getColumn(column).addWidget(widget);
    }
    
    private DashboardColumn getColumnByIndex(int index){
        if(index %2 == 0){
            return model.getColumn(LEFT_COLUMN_INDEX);
        }else{
            return model.getColumn(RIGHT_COLUMN_INDEX);
        }
    }
    
    
    public int getNumMonitoredServers(){
        return servers.size();
    }
    
    /**
     * 
     * @param agentAddress
     * @return <code>true</code> if agendAddress is a widget of one of the widget columns 
     *         <code>false</code> if agentAddress isn't found in any column 
     */
    private boolean serverAlreadyMonitored(String agentAddress){
        return (model.getColumn(RIGHT_COLUMN_INDEX).getWidgets().contains(agentAddress) || model.getColumn(RIGHT_COLUMN_INDEX).getWidgets().contains(agentAddress));
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
    
}
