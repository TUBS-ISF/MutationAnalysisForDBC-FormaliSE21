/**
  * This file is part of the KeY Java Card API Reference Implementation.
  *
  * Author: Wojciech Mostowski, woj@cs.ru.nl
  *   
  * For details and license see README and LICENSE files.
  *    
  * $Id: ECPrivateKeyImpl.java,v 1.2 2007/06/25 09:57:01 woj Exp $
  */

package javacard.security;

public class ECPrivateKeyImpl extends ECKeyImpl implements ECPrivateKey {

    ECPrivateKeyImpl(byte type, short size) throws CryptoException {
        super(type, size, (short) ((short) ((short) (size / 8) + 1) * 8));
    }

    public void setS(byte[] buffer, short offset, short length)
            throws CryptoException {
        setKeyMaterial(buffer, offset, length, _size1,
                (short) ((short) (4 * _size1) + _size2), (short) 5);
    }

    public short getS(byte[] buffer, short offset) throws CryptoException {
        return getKeyMaterial(buffer, offset, _size1,
                (short) ((short) (4 * _size1) + _size2), (short) 5);

    }

}
