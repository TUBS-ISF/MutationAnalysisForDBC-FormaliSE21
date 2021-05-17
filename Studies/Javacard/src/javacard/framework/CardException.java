/**
  * This file is part of the KeY Java Card API Reference Implementation.
  *
  * Author: Wojciech Mostowski, woj@cs.ru.nl
  *   
  * For details and license see README and LICENSE files.
  *    
  * $Id: CardException.java,v 1.6 2007/07/09 13:19:04 woj Exp $
  */

package javacard.framework;

import de.uka.ilkd.key.javacard.KeYJCSystem;

public class CardException extends java.lang.Exception {

    private static /*@spec_public@*/ CardException _systemInstance;
    /*@ static invariant _systemInstance != null; @*/

    protected /*@spec_public@*/ short[] _reason;
    /*@ invariant
          _reason != null &&
	  JCSystem.isTransient(_reason) == JCSystem.CLEAR_ON_RESET &&
	  _reason.length == 1 &&
	  ((java.lang.Object)_reason).owner == this;	  
      @*/

    public CardException(short reason) {
        _reason = JCSystem.makeTransientShortArray((short) 1,
                JCSystem.CLEAR_ON_RESET);
	KeYJCSystem.setJavaOwner(_reason, this);
        _reason[0] = reason;
        if (_systemInstance == null)
            _systemInstance = this;
    }

    /*@ public normal_behavior
          requires true;
	  ensures \result == _reason[0];
          assignable \nothing;
      @*/
    public short getReason() {
        return _reason[0];
    }

    /*@ public normal_behavior
          requires true;
	  ensures _reason[0] == reason;
          assignable _reason[0];
      @*/
    public void setReason(short reason) {
        _reason[0] = reason;
    }

    /*@ public exceptional_behavior
          requires true;
	  signals_only CardException;
	  signals (CardException e) e == _systemInstance && e._reason[0] == reason;
          assignable _systemInstance._reason[0];
      @*/
    public static void throwIt(short reason) throws CardException {
        _systemInstance.setReason(reason);
        throw _systemInstance;
    }

}
