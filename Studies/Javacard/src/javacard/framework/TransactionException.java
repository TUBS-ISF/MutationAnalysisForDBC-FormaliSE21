/**
  * This file is part of the KeY Java Card API Reference Implementation.
  *
  * Author: Wojciech Mostowski, woj@cs.ru.nl
  *   
  * For details and license see README and LICENSE files.
  *    
  * $Id: TransactionException.java,v 1.3 2007/07/09 13:19:04 woj Exp $
  */

package javacard.framework;

public class TransactionException extends CardRuntimeException {

    private static /*@spec_public@*/ TransactionException _systemInstance;
    /*@ static invariant _systemInstance != null; @*/

    public final static short IN_PROGRESS = (short) 1;

    public final static short NOT_IN_PROGRESS = (short) 2;

    public final static short BUFFER_FULL = (short) 3;

    public final static short INTERNAL_FAILURE = (short) 4;

    public TransactionException(short reason) {
        super(reason);
        if (_systemInstance == null)
            _systemInstance = this;
    }

    /*@ public exceptional_behavior
          requires true;
	  signals_only TransactionException;
	  signals (TransactionException e) e == _systemInstance && e._reason[0] == reason;
          assignable _systemInstance._reason[0];
      @*/
    public static void throwIt(short reason) throws TransactionException {
        _systemInstance.setReason(reason);
        throw _systemInstance;
    }

}
