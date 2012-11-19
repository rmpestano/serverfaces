/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.serverfaces.agent.exception;

import java.io.Serializable;
import javax.ejb.ApplicationException;

/**
 *
 * @author Rafael M. Pestano - Nov 16, 2012 9:55:05 PM
 */
@ApplicationException
public class CouldNotRetrieveDataException extends RuntimeException implements Serializable{

  public CouldNotRetrieveDataException(Throwable cause) {
        super(cause);
    }
  
   public CouldNotRetrieveDataException(String summary) {
        super(summary);
   }
  
} 
