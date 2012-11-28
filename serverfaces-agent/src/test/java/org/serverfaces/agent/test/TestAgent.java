/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.serverfaces.agent.test;

import java.io.IOException;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import org.apache.log4j.Logger;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.serverfaces.agent.SNMPAgent;
import org.serverfaces.agent.monitoriment.Monitor;
import org.serverfaces.agent.server.ServerRetriever;
import org.serverfaces.agent.server.glassfish.GlassfishRetriever;
import org.serverfaces.agent.server.jboss.JBossASRetriever;
import org.serverfaces.agent.server.tomcat.TomcatRetriever;
import org.serverfaces.agent.server.tomee.TomEERetriever;
import org.serverfaces.agent.server.weblogic.WebLogicRetriever;
import org.serverfaces.agent.util.SimpleSNMPManager;
import org.serverfaces.common.qualifier.Log;
import org.snmp4j.smi.OID;

/**
 *
 * @author Rafael M. Pestano - Nov 16, 2012 3:38:34 PM
 *
 * NOTE that to the tests run successful in Glassfish you MUST start the
 * (real)server before(no need to deploy the Serverfaces Agent) cause the
 * embedded glassfish(raised by arquillian) doesn't have the REST monitoring API
 * which is where the agent get its data such as serverUptime and so on
 */
@RunWith(Arquillian.class)
public class TestAgent {

    public static final String NO_SUCH_OBJECT = "noSuchObject";
    
    @Inject
    Monitor monitor;
    
    @Inject
    SimpleSNMPManager snmpManager;
    
    @Inject @Log
    Logger log;
    
    //OIDs
    @Inject
    Instance<OID> serverName;//same as -> new OID(".1.3.6.1.2.1.1.1.0")
    @Inject
    Instance<OID> serverUptime;//same as -> new OID(".1.3.6.1.2.1.1.2.0")
    @Inject
    Instance<OID> serverActiveSessions;//same as -> new OID(".1.3.6.1.2.1.1.3.0")
    @Inject
    Instance<OID> serverUsedMemory;//same as -> new OID(".1.3.6.1.2.1.1.4.0")
    @Inject
    Instance<OID> serverAvailableMemory;//same as -> new OID(".1.3.6.1.2.1.1.5.0")
    @Inject
    Instance<OID> serverCpuTime;//same as -> new OID(".1.3.6.1.2.1.1.6.0")
    @Inject
    Instance<OID> serverActiveTransactions;//same as -> new OID(".1.3.6.1.2.1.1.7.0")
    @Inject
    Instance<OID> serverCommitedTransactions;//same as -> new OID(".1.3.6.1.2.1.1.8.0")
    @Inject
    Instance<OID> serverRollbackTransactions;//same as -> new OID(".1.3.6.1.2.1.1.9.0")
    @Inject
    Instance<OID> serverActiveThreads;//same as -> new OID(".1.3.6.1.2.1.1.10.0")
    @Inject
    Instance<OID> serverTotalRequests;//same as -> new OID(".1.3.6.1.2.1.1.11.0")
    @Inject
    Instance<OID> serverLog;    //same as -> new OID(".1.3.6.1.2.1.1.12.0")

    @Deployment
    public static Archive<?> createTestArchive() {
        MavenDependencyResolver resolver = DependencyResolvers
                .use(MavenDependencyResolver.class)
                .loadMetadataFromPom("pom.xml");

        WebArchive war = ShrinkWrap.create(WebArchive.class, "test.war")
                .addClasses(ServerRetriever.class, Monitor.class, SNMPAgent.class,
                GlassfishRetriever.class, JBossASRetriever.class, TomEERetriever.class,
                TomcatRetriever.class, WebLogicRetriever.class, SNMPAgent.class)
                .addPackage("org.serverfaces.agent.util").addPackage("org.serverfaces.agent.mib")
                .addPackage("org.serverfaces.common").addPackage("org.serverfaces.common.qualifier")
                .addAsLibraries(resolver.artifact("org.snmp4j:snmp4j-agent:2.0.6").resolveAsFiles())
                .addAsWebInfResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"))
                .addAsResource("agent.properties").addAsResource("mib.properties");
        // verify that the JAR files ended up in the WAR
//        System.out.println(war.toString(true));
        return war;
    }

    @Test
    public void getServerNameResultNotNullAndExistingObject() throws IOException {
        String serverNameValue = snmpManager.getAsString(this.serverName.get());
        this.validateSNMPGet(serverNameValue);
        log.info("Server name: "+serverNameValue);
    }

    @Test
    public void getServerUpTimeResultNotNullAndExistingObject() throws IOException {
        String serverUptimeValue = snmpManager.getAsString(this.serverUptime.get());
        this.validateSNMPGet(serverUptimeValue);
        log.info("Server uptime: "+serverUptimeValue);
    }

    @Test
    public void getServerActiveSessionsResultNotNullAndExistingObject() throws IOException {
        Integer serverActiveSessionsValue = snmpManager.getAsInt(this.serverActiveSessions.get());
        this.validateSNMPGet(serverActiveSessionsValue);
        log.info("Server active sessions: "+serverActiveSessionsValue);
    }

    @Test
    public void getServerUsedMemoryResultNotNullAndExistingObject() throws IOException {
        Integer serverUsedMemoryValue = snmpManager.getAsInt(this.serverUsedMemory.get());
        this.validateSNMPGet(serverUsedMemoryValue);
        log.info("Server used memory(bytes): "+serverUsedMemoryValue);
    }

    @Test
    public void getServerAvailableMemoryResultNotNullAndExistingObject() throws IOException {
        Integer serverAvailableMemoryValue = snmpManager.getAsInt(this.serverAvailableMemory.get());
        this.validateSNMPGet(serverAvailableMemoryValue);
        log.info("Server available memory(bytes): "+serverAvailableMemoryValue);
    }

    @Test
    public void getServerCpuTimeResultNotNullAndExistingObject() throws IOException {
        Integer serverCpuTimeValue = snmpManager.getAsInt(this.serverCpuTime.get());
        this.validateSNMPGet(serverCpuTimeValue);
        log.info("Server cpu time(ms): "+serverCpuTimeValue);
    }
    
    @Test
    public void getServerActiveTransactionsResultNotNullAndExistingObject() throws IOException {
        Integer serverActiveTransactionsValue = snmpManager.getAsInt(this.serverActiveTransactions.get());
        this.validateSNMPGet(serverActiveTransactionsValue);
        log.info("Server active transactions: "+serverActiveTransactionsValue);
    }
    
    @Test
    public void getServerCommitedTransactionsResultNotNullAndExistingObject() throws IOException {
        Integer serverCommitedTransactionsValue = snmpManager.getAsInt(this.serverCommitedTransactions.get());
        this.validateSNMPGet(serverCommitedTransactionsValue);
        log.info("Server commited transactions: "+serverCommitedTransactionsValue);
    }
    
    @Test
    public void getServerRollbackTransactionsResultNotNullAndExistingObject() throws IOException {
        Integer serverRollbackTransactionsValue = snmpManager.getAsInt(this.serverRollbackTransactions.get());
        this.validateSNMPGet(serverRollbackTransactionsValue);
        log.info("Server rollback transactions: "+serverRollbackTransactionsValue);
    }
    
    @Test
    public void getServerActiveThreadsResultNotNullAndExistingObject() throws IOException {
        Integer serverActiveThreadsValue = snmpManager.getAsInt(this.serverActiveThreads.get());
        this.validateSNMPGet(serverActiveThreadsValue);
        log.info("Server active threads: "+serverActiveThreadsValue);
    }
    
    @Test
    public void getServerTotalRequestsResultNotNullAndExistingObject() throws IOException {
        Long serverTotalRequestsValue = snmpManager.getAsLong(this.serverTotalRequests.get());
        this.validateSNMPGet(serverTotalRequestsValue);
        log.info("Server total requests: "+serverTotalRequestsValue);
    }
    
    @Test
    public void getServerLogResultNotNullAndExistingObject() throws IOException {
        String serverLogValue = snmpManager.getAsString(this.serverLog.get());
        this.validateSNMPGet(serverLogValue);
        log.info("Server Log: "+serverLogValue);
    }

    /**
     *
     * common assertions of a SNMP get
     *
     * @param getResult
     */
    private void validateSNMPGet(Object getResult) {
        /**
         * result null usually occours when manager couldn't reach agent
         */
        Assert.assertNotNull(getResult);
        /**
         * no such result usually occours when manager request an OID that
         * doesnt exist in the mib(its not managed by the agent)
         */
        Assert.assertNotSame(NO_SUCH_OBJECT, getResult);

    }
}
