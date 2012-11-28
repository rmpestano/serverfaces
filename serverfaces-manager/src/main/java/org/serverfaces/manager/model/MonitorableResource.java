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
 * Represents a manageable resource in the context of a web server(mainly
 * applications and server itself)
 */
@Embeddable
public class MonitorableResource implements Serializable {

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
    private Long totalRequests;
    private String log;

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

    public Integer getUsedMemory() {
        return usedMemory;
    }

    public void setUsedMemory(Integer usedMemory) {
        this.usedMemory = usedMemory;
    }

    public Integer getAvailableMemory() {
        return availableMemory;
    }

    public void setAvailableMemory(Integer availableMemory) {
        this.availableMemory = availableMemory;
    }

    public Integer getCpuTime() {
        return cpuTime;
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
}
