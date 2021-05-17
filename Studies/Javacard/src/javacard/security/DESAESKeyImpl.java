/**
 * This file is part of the KeY Java Card API Reference Implementation.
 *
 * Author: Wojciech Mostowski, woj@cs.ru.nl
 *   
 * For details and license see README and LICENSE files.
 *    
 * $Id: DESAESKeyImpl.java,v 1.3 2007/07/03 09:50:57 woj Exp $
 */

package javacard.security;

public abstract class DESAESKeyImpl extends KeyImpl {

    protected DESAESKeyImpl(byte type, short size) throws CryptoException {
        super(type, size, (short) (size / 8), (short) 1);
    }

    public void setKey(byte[] keyData, short kOff) throws CryptoException,
            NullPointerException, ArrayIndexOutOfBoundsException {
        setKeyMaterial(keyData, kOff, (short) 0, (short) (_size / 8),
                (short) 0, (short) 0);
    }

    public byte getKey(byte[] keyData, short kOff) throws CryptoException {
        return getKeyMaterial(keyData, kOff, (short) (_size / 8), (short) 0,
                (short) 0);
    }

}
