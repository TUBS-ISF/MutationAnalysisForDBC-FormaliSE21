/**
  * This file is part of the KeY Java Card API Reference Implementation.
  *
  * Author: Wojciech Mostowski, woj@cs.ru.nl
  *   
  * For details and license see README and LICENSE files.
  *    
  * $Id: PINException.java,v 1.3 2007/07/09 13:19:04 woj Exp $
  */

package javacard.framework;

public class PINException extends CardRuntimeException {

    private static /*@spec_public@*/ PINException _systemInstance;
    /*@ static invariant _systemInstance != null; @*/

    public static final short ILLEGAL_VALUE = (short) 1;

    public PINException(short reason) {
        super(reason);
        if (_systemInstance == null)
            _systemInstance = this;
    }

    /*@ public exceptional_behavior
          requires true;
	  signals_only PINException;
	  signals (PINException e) e == _systemInstance && e._reason[0] == reason;
          assignable _systemInstance._reason[0];
      @*/
    public static void throwIt(short reason) throws PINException {
        _systemInstance.setReason(reason);
        throw _systemInstance;
    }

}
