/**
  * This file is part of the KeY Java Card API Reference Implementation.
  *
  * Author: Wojciech Mostowski, woj@cs.ru.nl
  *   
  * For details and license see README and LICENSE files.
  *    
  * $Id: RSAKeyImpl.java,v 1.2 2007/06/25 09:57:01 woj Exp $
  */

package javacard.security;

public abstract class RSAKeyImpl extends KeyImpl {

    protected short _expSize;

    protected RSAKeyImpl(byte type, short size, short expSize)
            throws CryptoException {
        super(type, size, (short) ((short) (size / 8) + expSize), (short) 2);
        _expSize = expSize;
    }

    public void setModulus(byte[] buffer, short offset, short length)
            throws CryptoException {
        setKeyMaterial(buffer, offset, length, (short) (_size / 8), (short) 0,
                (short) 0);
    }

    public void setExponent(byte[] buffer, short offset, short length)
            throws CryptoException {
        setKeyMaterial(buffer, offset, length, _expSize, (short) (_size / 8),
                (short) 1);
    }

    public short getModulus(byte[] buffer, short offset) throws CryptoException {
        return getKeyMaterial(buffer, offset, (short) (_size / 8), (short) 0,
                (short) 0);

    }

    public short getExponent(byte[] buffer, short offset)
            throws CryptoException {
        return getKeyMaterial(buffer, offset, _expSize, (short) (_size / 8),
                (short) 1);

    }

}
