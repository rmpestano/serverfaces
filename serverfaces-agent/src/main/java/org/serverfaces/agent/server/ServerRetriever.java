/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.serverfaces.agent.server;

/**
 *
 * @author Rafael M. Pestano
 */
public interface ServerRetriever {

    /**
     *
     * @return name of the server being monitored
     */
    String getServerName();
    
     /**
     *
     * @return address(url) where server is running
     */
    String getServerAddress();

    /**
     *
     * @return time since server has be started
     */
    String getServerUpTime();

    /**
     *
     * @return number of active sessions
     */
    String getServerActiveSessions();

    /**
     *
     * @return amount of used memory in bytes.
     */
    String getServerUsedMemory();

    /**
     *
     * @return amount of available memory in bytes.
     */
    String getServerAvailableMemory();

    /**
     *
     * @return returns the total CPU time for the current server thread in
     * microseconds(ms)
     */
    String getServerCpuTime();

    /**
     *
     * @return number of transactions that are currently active.
     */
    String getServerActiveTransactions();

    /**
     *
     * @return number of transactions that have been committed.
     */
    String getServerCommitedTransactions();

    /**
     *
     * @return number of transactions that have been rolled back.
     */
    String getServerRollbackTransactions();

    /**
     *
     * @return current number of live threads including both daemon and
     * non-daemon threads.
     */
    String getServerActiveThreads();

    /**
     *
     * @return cumulative number of requests processed so far
     */
    String getServerTotalRequests();
}