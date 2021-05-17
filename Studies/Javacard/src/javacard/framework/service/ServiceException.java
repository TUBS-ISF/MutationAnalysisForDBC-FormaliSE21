/**
  * This file is part of the KeY Java Card API Reference Implementation.
  *
  * Author: Wojciech Mostowski, woj@cs.ru.nl
  *   
  * For details and license see README and LICENSE files.
  *    
  * $Id: ServiceException.java,v 1.4 2007/07/09 13:27:06 woj Exp $
  */

package javacard.framework.service;

public class ServiceException extends javacard.framework.CardRuntimeException {

    public static final short CANNOT_ACCESS_IN_COMMAND = (short) 4;

    public static final short CANNOT_ACCESS_OUT_COMMAND = (short) 5;

    public static final short COMMAND_DATA_TOO_LONG = (short) 3;

    public static final short COMMAND_IS_FINISHED = (short) 6;

    public static final short DISPATCH_TABLE_FULL = (short) 2;

    public static final short ILLEGAL_PARAM = (short) 1;

    public static final short REMOTE_OBJECT_NOT_EXPORTED = (short) 7;

    private /*@spec_public@*/ static ServiceException _systemInstance;
    /*@ static invariant _systemInstance != null; @*/

    public ServiceException(short reason) {
        super(reason);
        if (_systemInstance == null)
            _systemInstance = this;
    }

    /*@ public exceptional_behavior
          requires true;
	  signals_only ServiceException;
	  signals (ServiceException e) e == _systemInstance && e._reason[0] == reason;
          assignable _systemInstance._reason[0];
      @*/
    public static void throwIt(short reason) throws ServiceException {
        _systemInstance.setReason(reason);
        throw _systemInstance;
    }

}
