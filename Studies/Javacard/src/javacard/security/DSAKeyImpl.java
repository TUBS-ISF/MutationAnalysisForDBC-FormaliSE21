/**
  * This file is part of the KeY Java Card API Reference Implementation.
  *
  * Author: Wojciech Mostowski, woj@cs.ru.nl
  *   
  * For details and license see README and LICENSE files.
  *    
  * $Id: DSAKeyImpl.java,v 1.2 2007/06/25 09:57:01 woj Exp $
  */

package javacard.security;

public abstract class DSAKeyImpl extends KeyImpl implements DSAKey {

    protected DSAKeyImpl(byte type, short size, short xySize)
            throws CryptoException {
        super(type, size, (short) ((short) ((short) (size / 4) + 20) + xySize),
                (short) 4);
    }

    public void setG(byte[] buffer, short offset, short length)
            throws CryptoException {
        setKeyMaterial(buffer, offset, length, (short) (_size / 8), (short) 0,
                (short) 0);
    }

    public short getG(byte[] buffer, short offset) throws CryptoException {
        return getKeyMaterial(buffer, offset, (short) (_size / 8), (short) 0,
                (short) 0);
    }

    public void setP(byte[] buffer, short offset, short length)
            throws CryptoException {
        setKeyMaterial(buffer, offset, length, (short) (_size / 8),
                (short) (_size / 8), (short) 1);
    }

    public short getP(byte[] buffer, short offset) throws CryptoException {
        return getKeyMaterial(buffer, offset, (short) (_size / 8),
                (short) (_size / 8), (short) 1);
    }

    public void setQ(byte[] buffer, short offset, short length)
            throws CryptoException {
        setKeyMaterial(buffer, offset, length, (short) 20, (short) (_size / 4),
                (short) 2);
    }

    public short getQ(byte[] buffer, short offset) throws CryptoException {
        return getKeyMaterial(buffer, offset, (short) 20, (short) (_size / 4),
                (short) 2);
    }

}
