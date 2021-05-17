/**
  * This file is part of the KeY Java Card API Reference Implementation.
  *
  * Author: Wojciech Mostowski, woj@cs.ru.nl
  *   
  * For details and license see README and LICENSE files.
  *    
  * $Id: CryptoException.java,v 1.3 2007/07/09 13:19:04 woj Exp $
  */

package javacard.security;

public class CryptoException extends javacard.framework.CardRuntimeException {

    public static final short ILLEGAL_USE = (short) 5;

    public static final short ILLEGAL_VALUE = (short) 1;

    public static final short INVALID_INIT = (short) 4;

    public static final short NO_SUCH_ALGORITHM = (short) 3;

    public static final short UNINITIALIZED_KEY = (short) 2;

    private /*@spec_public@*/ static CryptoException _systemInstance;
    /*@ static invariant _systemInstance != null; @*/

    public CryptoException(short reason) {
        super(reason);
        if (_systemInstance == null)
            _systemInstance = this;
    }

    /*@ public exceptional_behavior
          requires true;
	  signals_only CryptoException;
	  signals (CryptoException e) e == _systemInstance && e._reason[0] == reason;
          assignable _systemInstance._reason[0];
      @*/
    public static void throwIt(short reason) throws CryptoException {
        _systemInstance.setReason(reason);
        throw _systemInstance;
    }

}
