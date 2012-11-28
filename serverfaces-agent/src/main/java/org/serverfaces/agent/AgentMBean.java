/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.serverfaces.agent;

import java.io.IOException;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Instance;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.serverfaces.agent.exception.CouldNotRetrieveDataException;
import org.serverfaces.agent.mib.MibManager;
import org.snmp4j.agent.mo.MOScalar;
import org.snmp4j.smi.OID;

/**
 *
 * @author Rafael M. Pestano - Nov 24, 2012 5:27:58 PM
 */
@Named
@SessionScoped
public class AgentMBean implements Serializable {

    @Inject
    MibManager mibManager;
    @Inject
    SNMPAgent agent;
    @Inject
    Instance<OID> serverAddress;
    @Inject
    Instance<OID> serverName;
    private String uiServerName;
    private String uiServerAddress;
    @Inject
    private String agentAddress;

    public String getAgentAddress() {
        return agentAddress;
    }

    public void setAgentAddress(String agentAddress) {
        this.agentAddress = agentAddress;
    }

    public void changeAgentAddress(String newAddress) throws IOException {
        agent.setAgentAddress(agentAddress);
        agent.stop();
        agent.start();
    }

    public String getUiServerName() {
        if (uiServerName == null) {
            MOScalar moScalar = mibManager.findScalar(serverName.get());
            if(moScalar != null){
                  uiServerName = moScalar.getValue().toString();
            }
        }
        return uiServerName;
    }

    public String getUiServerAddress() {
        if (uiServerAddress == null) {
            MOScalar moScalar = mibManager.findScalar(serverAddress.get());
            if(moScalar != null){
                uiServerAddress = moScalar.getValue().toString();
            }
        }
        return uiServerAddress;
    }
    
    public boolean isAgentRunning(){
        return (agent != null && agent.isRunning());
    }
    
    public String startStop() throws IOException{
        if(isAgentRunning()){
            mibManager.terminateMIB();
            agent.stop();
        }
        else{
            agent.start();
            try{
                mibManager.initMIB(agent.getServer(),agent.getDefaultContext());
            }catch(CouldNotRetrieveDataException ex){
                mibManager.terminateMIB();
                agent.stop();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Could not start agent",ex.getMessage()));
            }
            
        }
        return null;
    }
}
