/**
 * This file is part of the KeY Java Card API Reference Implementation.
 *
 * Author: Wojciech Mostowski, woj@cs.ru.nl
 *   
 * For details and license see README and LICENSE files.
 *    
 * $Id: RMIService.java,v 1.4 2007/07/09 12:51:57 woj Exp $
 */

package javacard.framework.service;

import java.rmi.Remote;

import de.uka.ilkd.key.javacard.KeYJCSystem;
import javacard.framework.APDU;
import javacard.framework.ISO7816;

public class RMIService extends BasicService implements RemoteService {

    public static final byte DEFAULT_RMI_INVOKE_INSTRUCTION = (byte) 0x38;

    private /*@spec_public@*/ byte _invokeInstructionByte = DEFAULT_RMI_INVOKE_INSTRUCTION;

    private /*@spec_public@*/ Remote _remoteObject;
    /*@ invariant 
          _remoteObject != null;    
      @*/

    public RMIService(Remote initialObject) throws NullPointerException {
        if (initialObject == null)
            throw KeYJCSystem.npe;
        _remoteObject = initialObject;
    }

    public void setInvokeInstructionByte(byte ins) {
        _invokeInstructionByte = ins;
    }

    public boolean processCommand(APDU apdu) {
        /*
         * if (???) ServiceException
         * .throwIt(ServiceException.REMOTE_OBJECT_NOT_EXPORTED);
         */
        byte ins = getINS(apdu);
        if (ins == _invokeInstructionByte || ins == ISO7816.INS_SELECT)
            return true;
        return false;
    }

}
