/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.serverfaces.manager.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author Rafael M. Pestano - Nov 18, 2012 8:54:33 PM
 */
public class Server implements Serializable {

    @Id
    private String agentAddress;
    private String address;
    @Embedded
    private MonitorableResource info = new MonitorableResource();
    @OneToMany
    private List<Application> applications;

    public Server() {
    }

    public Server(String agentAddress) {
        this.agentAddress = agentAddress;
    }
    
    
    public String getAgentAddress() {
        return agentAddress;
    }

    public void setAgentAddress(String agentAddress) {
        this.agentAddress = agentAddress;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public MonitorableResource getInfo() {
        return info;
    }

    public void setInfo(MonitorableResource serverInfo) {
        this.info = serverInfo;
    }

    public List<Application> getApplications() {
        return applications;
    }

    public void setApplications(List<Application> applications) {
        this.applications = applications;
    }

    

    //each server has one and only one agent
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (this.agentAddress != null ? this.agentAddress.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Server other = (Server) obj;
        if ((this.agentAddress == null) ? (other.agentAddress != null) : !this.agentAddress.equals(other.agentAddress)) {
            return false;
        }
        return true;
    }
    
    
}
