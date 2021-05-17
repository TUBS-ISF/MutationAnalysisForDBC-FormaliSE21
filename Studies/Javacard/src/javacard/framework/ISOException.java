/**
  * This file is part of the KeY Java Card API Reference Implementation.
  *
  * Author: Wojciech Mostowski, woj@cs.ru.nl
  *   
  * For details and license see README and LICENSE files.
  *    
  * $Id: ISOException.java,v 1.3 2007/07/09 13:19:04 woj Exp $
  */

package javacard.framework;

public class ISOException extends CardRuntimeException {

    private static /*@spec_public@*/ ISOException _systemInstance;
    /*@ static invariant _systemInstance != null; @*/

    public ISOException(short reason) {
        super(reason);
        if (_systemInstance == null)
            _systemInstance = this;
    }

    /*@ public exceptional_behavior
          requires true;
	  signals_only ISOException;
	  signals (ISOException e) e == _systemInstance && e._reason[0] == reason;
          assignable _systemInstance._reason[0];
      @*/
    public static void throwIt(short reason) throws ISOException {
        _systemInstance.setReason(reason);
        throw _systemInstance;
    }

}
