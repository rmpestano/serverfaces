/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.serverfaces.agent.server.tomcat;

import javax.enterprise.inject.Alternative;
import org.serverfaces.agent.server.ServerRetriever;

/**
 *
 * @author Rafael M. Pestano - Nov 15, 2012 8:23:22 PM
 */
@Alternative
public class TomcatRetriever implements ServerRetriever{

    @Override
    public String getServerName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getServerAddress() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getServerUpTime() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Integer getServerActiveSessions() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Integer getServerUsedMemory() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Integer getServerAvailableMemory() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Integer getServerCpuTime() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Integer getServerActiveTransactions() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Integer getServerCommitedTransactions() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Integer getServerRollbackTransactions() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Integer getServerActiveThreads() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Long getServerTotalRequests() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getServerLog() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void getServerApplications() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Long getServerErrors() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Integer getServerMaxResponseTime() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Integer getServerAvgResponseTime() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
    
}
