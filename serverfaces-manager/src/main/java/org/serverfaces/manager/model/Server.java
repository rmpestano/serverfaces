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
    private String name;
    private String uptime;
    private Integer activeSessions;
    private Integer usedMemory;
    private Integer availableMemory;
    private Integer cpuTime;
    private Integer activeTransactions;
    private Integer commitedTransactions;
    private Integer rollbackTransactions;
    private Integer activeThreads;
    private Long totalErrors;
    private Integer maxResponseTime;
    private Integer avgResponseTime;
    private Long totalRequests;
    private String log;
    
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUptime() {
        return uptime;
    }

    public void setUptime(String uptime) {
        this.uptime = uptime;
    }

    public Integer getActiveSessions() {
        return activeSessions;
    }

    public void setActiveSessions(Integer activeSessions) {
        this.activeSessions = activeSessions;
    }

    /**
     * 
     * @return used memory in MBs
     */
    public Integer getUsedMemory() {
        return usedMemory/1000;
    }

    public void setUsedMemory(Integer usedMemory) {
        this.usedMemory = usedMemory;
    }

    /**
     * 
     * @return available memory in MBs
     */
    public Integer getAvailableMemory() {
        return availableMemory/1000;
    }

    public void setAvailableMemory(Integer availableMemory) {
        this.availableMemory = availableMemory;
    }

    /**
     * 
     * @return CPU time in MHz
     * remember that CPUtime comes in miliseconds
     */
    public Integer getCpuTime() {
        return cpuTime/100;
    }

    public void setCpuTime(Integer cpuTime) {
        this.cpuTime = cpuTime;
    }

    public Integer getActiveTransactions() {
        return activeTransactions;
    }

    public void setActiveTransactions(Integer activeTransactions) {
        this.activeTransactions = activeTransactions;
    }

    public Integer getCommitedTransactions() {
        return commitedTransactions;
    }

    public void setCommitedTransactions(Integer commitedTransactions) {
        this.commitedTransactions = commitedTransactions;
    }

    public Integer getRollbackTransactions() {
        return rollbackTransactions;
    }

    public void setRollbackTransactions(Integer rollbackTransactions) {
        this.rollbackTransactions = rollbackTransactions;
    }

    public Integer getActiveThreads() {
        return activeThreads;
    }

    public void setActiveThreads(Integer activeThreads) {
        this.activeThreads = activeThreads;
    }

    public Long getTotalErrors() {
        return totalErrors;
    }

    public void setTotalErrors(Long totalErrors) {
        this.totalErrors = totalErrors;
    }


    public Integer getMaxResponseTime() {
        return maxResponseTime;
    }

    public void setMaxResponseTime(Integer maxResponseTime) {
        this.maxResponseTime = maxResponseTime;
    }

    public Integer getAvgResponseTime() {
        return avgResponseTime;
    }

    public void setAvgResponseTime(Integer avgResponseTime) {
        this.avgResponseTime = avgResponseTime;
    }

    public Long getTotalRequests() {
        return totalRequests;
    }

    public void setTotalRequests(Long totalRequests) {
        this.totalRequests = totalRequests;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
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
