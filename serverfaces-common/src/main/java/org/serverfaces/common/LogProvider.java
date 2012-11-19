/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.serverfaces.common;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import org.apache.log4j.Logger;
import org.serverfaces.common.qualifier.Log;

/**
 *
 * @author Rafael M. Pestano - Nov 16, 2012 9:16:46 PM
 */
public class LogProvider {
    
    @Produces @Log
    public  Logger produce(InjectionPoint ip){
        return  Logger.getLogger(ip.getMember().getDeclaringClass());
    }

}
