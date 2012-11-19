/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.serverfaces.manager.model;

import java.io.Serializable;
import javax.persistence.Embeddable;

/**
 *
 * @author Rafael M. Pestano - Nov 18, 2012 9:12:58 PM
 * 
 * Represents a manageable resource in the context of a web server(mainly applications and server itself)
 */
@Embeddable
public class MonitorableResource implements Serializable{
    
    private String name;
    private String uptime;
    private String activeSessions;
    private String availableMemory;
    private String cpuTime;
    private String activeTransactions;
    private String commitedTransactions;
    private String rollbackTransactions;
    private String activeThreads;
    private String totalRequests;

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

    public String getActiveSessions() {
        return activeSessions;
    }

    public void setActiveSessions(String activeSessions) {
        this.activeSessions = activeSessions;
    }

    public String getAvailableMemory() {
        return availableMemory;
    }

    public void setAvailableMemory(String availableMemory) {
        this.availableMemory = availableMemory;
    }

    public String getCpuTime() {
        return cpuTime;
    }

    public void setCpuTime(String cpuTime) {
        this.cpuTime = cpuTime;
    }

    public String getActiveTransactions() {
        return activeTransactions;
    }

    public void setActiveTransactions(String activeTransactions) {
        this.activeTransactions = activeTransactions;
    }

    public String getCommitedTransactions() {
        return commitedTransactions;
    }

    public void setCommitedTransactions(String commitedTransactions) {
        this.commitedTransactions = commitedTransactions;
    }

    public String getRollbackTransactions() {
        return rollbackTransactions;
    }

    public void setRollbackTransactions(String rollbackTransactions) {
        this.rollbackTransactions = rollbackTransactions;
    }

    public String getActiveThreads() {
        return activeThreads;
    }

    public void setActiveThreads(String activeThreads) {
        this.activeThreads = activeThreads;
    }

    public String getTotalRequests() {
        return totalRequests;
    }

    public void setTotalRequests(String totalRequests) {
        this.totalRequests = totalRequests;
    }
    

}
