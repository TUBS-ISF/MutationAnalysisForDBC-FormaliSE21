/**
  * This file is part of the KeY Java Card API Reference Implementation.
  *
  * Author: Wojciech Mostowski, woj@cs.ru.nl
  *   
  * For details and license see README and LICENSE files.
  *    
  * $Id: APDUException.java,v 1.3 2007/07/09 13:19:04 woj Exp $
  */

package javacard.framework;

public class APDUException extends CardRuntimeException {

    private static /*@spec_public@*/ APDUException _systemInstance;
    /*@ static invariant _systemInstance != null; @*/

    public static final short ILLEGAL_USE = (short) 1;

    public static final short BUFFER_BOUNDS = (short) 2;

    public static final short BAD_LENGTH = (short) 3;

    public static final short IO_ERROR = (short) 4;

    public static final short NO_T0_GETRESPONSE = (short) 170;

    public static final short T1_IFD_ABORT = (short) 171;

    public static final short NO_T0_REISSUE = (short) 172;

    public APDUException(short reason) {
        super(reason);
        if (_systemInstance == null)
            _systemInstance = this;
    }

    /*@ public exceptional_behavior
          requires true;
	  signals_only APDUException;
	  signals (APDUException e) e == _systemInstance && e._reason[0] == reason;
          assignable _systemInstance._reason[0];
      @*/
    public static void throwIt(short reason) throws APDUException {
        _systemInstance.setReason(reason);
        throw _systemInstance;
    }

}
