/**
  * This file is part of the KeY Java Card API Reference Implementation.
  *
  * Author: Wojciech Mostowski, woj@cs.ru.nl
  *   
  * For details and license see README and LICENSE files.
  *    
  * $Id: SystemException.java,v 1.3 2007/07/09 13:19:04 woj Exp $
  */

package javacard.framework;

public class SystemException extends CardRuntimeException {

    private static /*@spec_public@*/ SystemException _systemInstance;
    /*@ static invariant _systemInstance != null; @*/

    public static final short ILLEGAL_VALUE = (short) 1;

    public static final short NO_TRANSIENT_SPACE = (short) 2;

    public static final short ILLEGAL_TRANSIENT = (short) 3;

    public static final short ILLEGAL_AID = (short) 4;

    public static final short NO_RESOURCE = (short) 5;

    public static final short ILLEGAL_USE = (short) 6;

    public SystemException(short reason) {
        super(reason);
        if (_systemInstance == null)
            _systemInstance = this;
    }

    /*@ public exceptional_behavior
          requires true;
	  signals_only SystemException;
	  signals (SystemException e) e == _systemInstance && e._reason[0] == reason;
          assignable _systemInstance._reason[0];
      @*/
    public static void throwIt(short reason) throws SystemException {
        _systemInstance.setReason(reason);
        throw _systemInstance;
    }
}
