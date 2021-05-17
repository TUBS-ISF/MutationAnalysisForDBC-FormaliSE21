/**
  * This file is part of the KeY Java Card API Reference Implementation.
  *
  * Author: Wojciech Mostowski, woj@cs.ru.nl
  *   
  * For details and license see README and LICENSE files.
  *    
  * $Id: DSAPrivateKeyImpl.java,v 1.2 2007/06/25 09:57:01 woj Exp $
  */

package javacard.security;
import javacardx.crypto.KeyEncryption;

public class DSAPrivateKeyImpl extends DSAKeyImpl implements DSAPrivateKey {

    DSAPrivateKeyImpl(byte type, short size) throws CryptoException {
        super(type, size, (short) 20);
    }

    public void setX(byte[] buffer, short offset, short length)
            throws CryptoException {
        setKeyMaterial(buffer, offset, length, (short) 20,
                (short) ((short) (_size / 4) + 20), (short) 3);

    }

    public short getX(byte[] buffer, short offset) throws CryptoException {
        return getKeyMaterial(buffer, offset, (short) 20,
                (short) ((short) (_size / 4) + 20), (short) 3);

    }

}
