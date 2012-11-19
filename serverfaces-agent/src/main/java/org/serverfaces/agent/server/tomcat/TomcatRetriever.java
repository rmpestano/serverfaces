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
    public String getServerActiveSessions() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getServerUsedMemory() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getServerAvailableMemory() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getServerCpuTime() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getServerTotalRequests() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getServerActiveTransactions() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getServerCommitedTransactions() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getServerRollbackTransactions() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getServerActiveThreads() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
}
