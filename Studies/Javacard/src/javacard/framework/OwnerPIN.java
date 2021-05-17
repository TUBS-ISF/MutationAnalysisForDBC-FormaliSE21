/**
 * This file is part of the KeY Java Card API Reference Implementation.
 *
 * Author: Wojciech Mostowski, woj@cs.ru.nl
 *   
 * For details and license see README and LICENSE files.
 *    
 * $Id: OwnerPIN.java,v 1.6 2007/07/09 12:51:57 woj Exp $
 */

package javacard.framework;

import de.uka.ilkd.key.javacard.KeYJCSystem;

public class OwnerPIN implements PIN {

    private /*@spec_public@*/ byte _maxPINSize;
    /*@ invariant _maxPINSize > 0; @*/

    private /*@spec_public@*/ byte _maxTries;
    /*@ invariant _maxTries > 0; @*/

    private /*@spec_public@*/ byte[] _temps;
    /*@ invariant 
          _temps != null &&
	  _temps.length == 1 &&
	  JCSystem.isTransient(_temps) == JCSystem.CLEAR_ON_RESET &&
	  ((java.lang.Object)_temps).owner == this;
      @*/

    private /*@spec_public@*/ boolean[] _isValidated;
    /*@ invariant 
          _isValidated != null &&
	  _isValidated.length == 1 &&
	  JCSystem.isTransient(_isValidated) == JCSystem.NOT_A_TRANSIENT_OBJECT &&
	  ((java.lang.Object)_isValidated).owner == this;
      @*/

    private /*@spec_public@*/ byte[] _pin;
    /*@ invariant 
          _pin != null &&
	  _pin.length == (short)(_maxPINSize + 1) &&
	  JCSystem.isTransient(_pin) == JCSystem.NOT_A_TRANSIENT_OBJECT &&
	  ((java.lang.Object)_pin).owner == this;
      @*/
    /*@ invariant _pin[0] >= 0 && _pin[0] <= _maxTries; @*/

    protected boolean getValidatedFlag() {
        return _isValidated[0];
    }

    protected void setValidatedFlag(boolean value) {
        _isValidated[0] = value;
    }

    public OwnerPIN(byte maxTries, byte maxPINSize) throws PINException {
        if (maxPINSize < 1)
            PINException.throwIt(PINException.ILLEGAL_VALUE);
        _pin = new byte[maxPINSize + 1];
        KeYJCSystem.setJavaOwner(_pin, this);
        _isValidated = JCSystem.makeTransientBooleanArray((short) 1,
                JCSystem.CLEAR_ON_RESET);
        KeYJCSystem.setJavaOwner(_isValidated, this);
        _temps = JCSystem.makeTransientByteArray((short) 1,
                JCSystem.CLEAR_ON_RESET);
        KeYJCSystem.setJavaOwner(_temps, this);
        _pin[0] = maxTries;
        _maxTries = maxTries;
        _maxPINSize = maxPINSize;
        _isValidated[0] = false;
    }

    public void update(byte[] pin, short offset, byte length)
            throws PINException {
        if (length > _maxPINSize)
            PINException.throwIt(PINException.ILLEGAL_VALUE);
        Util.arrayCopy(pin, offset, _pin, (short) 1, length);
        _pin[0] = _maxTries;
        setValidatedFlag(false);
    }

    public void resetAndUnblock() {
        setValidatedFlag(false);
        _pin[0] = _maxTries;
    }

    public boolean isValidated() {
        return getValidatedFlag();
    }

    public byte getTriesRemaining() {
        return _pin[0];
    }

    public boolean check(byte[] pin, short offset, byte length)
            throws NullPointerException, ArrayIndexOutOfBoundsException {
        setValidatedFlag(false);
        if (getTriesRemaining() == 0)
            return false;
        _temps[0] = (byte) (_pin[0] - 1);
        Util.arrayCopyNonAtomic(_temps, (short) 0, _pin, (short) 0, (short) 1);
        if (length != _maxPINSize)
            return false;
        if (Util.arrayCompare(_pin, (short) 1, pin, offset, length) == 0) {
            setValidatedFlag(true);
            _pin[0] = _maxTries;
            return true;
        }
        return false;
    }

    public void reset() {
        if (isValidated())
            resetAndUnblock();
    }
}
