/**
  * This file is part of the KeY Java Card API Reference Implementation.
  *
  * Author: Wojciech Mostowski, woj@cs.ru.nl
  *   
  * For details and license see README and LICENSE files.
  *    
  * $Id: KeyImpl.java,v 1.4 2007/07/03 09:14:19 woj Exp $
  */

package javacard.security;

import javacard.framework.Util;
import javacard.framework.JCSystem;
import javacardx.crypto.Cipher;
import de.uka.ilkd.key.javacard.KeYJCSystem;

abstract public class KeyImpl implements Key {

    protected byte _type;

    protected short _size;

    protected boolean[] _initialized;

    protected Cipher _cipher;

    protected byte[] _key;

    protected KeyImpl(byte type, short size, short materialSize, short initSize)
            throws CryptoException {

        if (!KeyBuilder.isValidKeyTypeSize(type, size)) {
            CryptoException.throwIt(CryptoException.ILLEGAL_VALUE);
        }

        _size = size;
        _type = type;
        _cipher = null;

        if (KeyBuilder.isPersistentKeyType(type)) {
            _initialized = new boolean[initSize];
            _key = new byte[materialSize];
        } else if (KeyBuilder.isTransientDeselectKeyType(type)) {
            _initialized = JCSystem.makeTransientBooleanArray(initSize,
                    JCSystem.CLEAR_ON_DESELECT);
            _key = JCSystem.makeTransientByteArray(materialSize,
                    JCSystem.CLEAR_ON_DESELECT);
        } else if (KeyBuilder.isTransientResetKeyType(type)) {
            _initialized = JCSystem.makeTransientBooleanArray(initSize,
                    JCSystem.CLEAR_ON_RESET);
            _key = JCSystem.makeTransientByteArray(materialSize,
                    JCSystem.CLEAR_ON_RESET);
        }
	KeYJCSystem.setJavaOwner(_initialized, this);
	KeYJCSystem.setJavaOwner(_key, this);
    }

    public byte getType() {
        return _type;
    }

    public short getSize() {
        return _size;
    }

    public boolean isInitialized() {
        boolean result = true;
        short i = 0;
	/*@
	   loop_invariant i >= 0 && i <= _initialized.length &&
	     result == (\forall short j; j >= 0 && j < i; _initialized[j] == true);
	   decreases _initialized.length - i;
	  @*/
        while (i < _initialized.length) {
            result = (result && _initialized[i]);
            i++;
        }
        return result;
    }

    protected Cipher getKeyCipher() {
        return _cipher;
    }

    protected void setKeyCipher(Cipher cipher) {
        _cipher = cipher;
    }

    public void clearKey() {
        Util.arrayFillNonAtomic(_key, (short) 0, (short) _key.length, (byte) 0);
        short i = 0;
	/*@
	   loop_invariant i >= 0 && i <= _initialized.length &&
	     (\forall short j; j >= 0 && j < i; _initialized[j] == false);
	   decreases _initialized.length - i;
	  @*/
        while (i < _initialized.length) {
            _initialized[i] = false;
            i++;
        }
    }

    void initialize() {
        short i = 0;
	/*@
	   loop_invariant i>=0 && i<= _initialized.length &&
	     (\forall short j; j >= 0 && j < i; _initialized[j] == true);
	   decreases _initialized.length - i;
	  @*/
        while (i < _initialized.length) {
            _initialized[i] = true;
            i++;
        }
    }

    protected void setKeyMaterial(byte[] buffer, short offset, short length,
            short kSize, short kOff, short iOff) throws CryptoException {
        short l = length;
        if (_cipher == null) {
            if (l == 0)
                l = kSize;
            if (l != kSize)
                CryptoException.throwIt(CryptoException.ILLEGAL_VALUE);
            Util.arrayCopy(buffer, offset, _key, kOff, l);
        } else {
            try {
                if (l == 0)
                    l = (short) (buffer.length - offset);
                short res = _cipher.doFinal(buffer, offset, l, _key, kOff);
                if (res != kSize)
                    CryptoException.throwIt(CryptoException.ILLEGAL_VALUE);
            } catch (Exception e) {
                CryptoException.throwIt(CryptoException.ILLEGAL_VALUE);
            }
        }
        _initialized[iOff] = true;
    }

    protected byte getKeyMaterial(byte[] buffer, short offset, short kSize,
            short kOff, short iOff) throws CryptoException {
        if (!_initialized[iOff])
            CryptoException.throwIt(CryptoException.UNINITIALIZED_KEY);
        Util.arrayCopy(_key, kOff, buffer, offset, kSize);
        return (byte) kSize;
    }

}
