/**
  * This file is part of the KeY Java Card API Reference Implementation.
  *
  * Author: Wojciech Mostowski, woj@cs.ru.nl
  *   
  * For details and license see README and LICENSE files.
  *    
  * $Id: ECPublicKeyImpl.java,v 1.2 2007/06/25 09:57:01 woj Exp $
  */

package javacard.security;

public class ECPublicKeyImpl extends ECKeyImpl implements ECPublicKey {

    ECPublicKeyImpl(byte type, short size) throws CryptoException {
        super(type, size,
                (short) ((short) ((short) ((short) (size / 8) + 1) * 16) + 1)

        );
    }

    public void setW(byte[] buffer, short offset, short length)
            throws CryptoException {
        setKeyMaterial(buffer, offset, length, _size2,
                (short) ((short) (4 * _size1) + _size2), (short) 5);
    }

    public short getW(byte[] buffer, short offset) throws CryptoException {
        return getKeyMaterial(buffer, offset, _size2,
                (short) ((short) (4 * _size1) + _size2), (short) 5);
    }

}
