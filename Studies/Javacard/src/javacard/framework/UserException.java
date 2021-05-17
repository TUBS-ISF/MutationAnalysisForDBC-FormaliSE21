/**
  * This file is part of the KeY Java Card API Reference Implementation.
  *
  * Author: Wojciech Mostowski, woj@cs.ru.nl
  *   
  * For details and license see README and LICENSE files.
  *    
  * $Id: UserException.java,v 1.3 2007/07/09 13:19:04 woj Exp $
  */

package javacard.framework;

public class UserException extends CardException {

    private static /*@spec_public@*/ UserException _systemInstance;
    /*@ static invariant _systemInstance != null; @*/

    public UserException(short reason) {
        super(reason);
        if (_systemInstance == null)
            _systemInstance = this;
    }

    /*@ public exceptional_behavior
          requires true;
	  signals_only UserException;
	  signals (UserException e) e == _systemInstance && e._reason[0] == reason;
          assignable _systemInstance._reason[0];
      @*/
    public static void throwIt(short reason) throws UserException {
        _systemInstance.setReason(reason);
        throw _systemInstance;
    }

}
