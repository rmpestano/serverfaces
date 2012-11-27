/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.serverfaces.agent.event;

import java.io.Serializable;
import org.snmp4j.agent.MOGroup;

/**
 *
 * @author Rafael M. Pestano - Nov 26, 2012 10:33:51 AM
 */
public class UnregisterMOsEvent implements Serializable {

    private MOGroup moGroup;
     


    public UnregisterMOsEvent(MOGroup moGroup) {
        this.moGroup = moGroup;
    }
    

    public MOGroup getMoGroup() {
        return moGroup;
    }

    public void setMoGroup(MOGroup moGroup) {
        this.moGroup = moGroup;
    }

     
}
