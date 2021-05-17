/**
  * This file is part of the KeY Java Card API Reference Implementation.
  *
  * Author: Wojciech Mostowski, woj@cs.ru.nl
  *   
  * For details and license see README and LICENSE files.
  *    
  * $Id: DSAPublicKeyImpl.java,v 1.2 2007/06/25 09:57:01 woj Exp $
  */

package javacard.security;

public class DSAPublicKeyImpl extends DSAKeyImpl implements DSAPublicKey {

    DSAPublicKeyImpl(byte type, short size) throws CryptoException {
        super(type, size, (short) (size / 8));
    }

    public void setY(byte[] buffer, short offset, short length)
            throws CryptoException {
        setKeyMaterial(buffer, offset, length, (short) (_size / 8),
                (short) ((short) (_size / 4) + 20), (short) 3);

    }

    public short getY(byte[] buffer, short offset) throws CryptoException {
        return getKeyMaterial(buffer, offset, (short) (_size / 8),
                (short) ((short) (_size / 4) + 20), (short) 3);
    }

}
