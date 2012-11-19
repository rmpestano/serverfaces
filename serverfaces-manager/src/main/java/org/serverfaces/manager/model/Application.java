/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.serverfaces.manager.model;

import java.io.Serializable;
import javax.persistence.Embedded;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author Rafael M. Pestano - Nov 18, 2012 9:20:52 PM
 */
public class Application implements Serializable {

    @Id
    private long id;
    @Embedded
    private MonitorableResource info;
    @ManyToOne
    private Server server;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public MonitorableResource getInfo() {
        return info;
    }

    public void setInfo(MonitorableResource appInfo) {
        this.info = appInfo;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final Application other = (Application) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
    
    
}
