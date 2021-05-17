/**
  * This file is part of the KeY Java Card API Reference Implementation.
  *
  * Author: Wojciech Mostowski, woj@cs.ru.nl
  *   
  * For details and license see README and LICENSE files.
  *    
  * $Id: RSAPrivateCrtKeyImpl.java,v 1.2 2007/06/25 09:57:01 woj Exp $
  */

package javacard.security;

public class RSAPrivateCrtKeyImpl extends KeyImpl implements RSAPrivateCrtKey {

    RSAPrivateCrtKeyImpl(byte type, short size) throws CryptoException {
        super(type, size, (short) ((short) (size / 16) * 5), (short) 5);
    }

    public void setP(byte[] buffer, short offset, short length)
            throws CryptoException {
        setKeyMaterial(buffer, offset, length, (short) (_size / 16), (short) 0,
                (short) 0);

    }

    public short getP(byte[] buffer, short offset) throws CryptoException {
        return getKeyMaterial(buffer, offset, (short) (_size / 16), (short) 0,
                (short) 0);

    }

    public void setQ(byte[] buffer, short offset, short length)
            throws CryptoException {
        setKeyMaterial(buffer, offset, length, (short) (_size / 16),
                (short) (_size / 16), (short) 1);
    }

    public short getQ(byte[] buffer, short offset) throws CryptoException {
        return getKeyMaterial(buffer, offset, (short) (_size / 16),
                (short) (_size / 16), (short) 1);
    }

    public void setDP1(byte[] buffer, short offset, short length)
            throws CryptoException {
        setKeyMaterial(buffer, offset, length, (short) (_size / 16),
                (short) (2 * (short) (_size / 16)), (short) 2);

    }

    public short getDP1(byte[] buffer, short offset) throws CryptoException {
        return getKeyMaterial(buffer, offset, (short) (_size / 16),
                (short) (2 * (short) (_size / 16)), (short) 2);
    }

    public void setDQ1(byte[] buffer, short offset, short length)
            throws CryptoException {
        setKeyMaterial(buffer, offset, length, (short) (_size / 16),
                (short) (3 * (short) (_size / 16)), (short) 3);
    }

    public short getDQ1(byte[] buffer, short offset) throws CryptoException {
        return getKeyMaterial(buffer, offset, (short) (_size / 16),
                (short) (3 * (short) (_size / 16)), (short) 3);
    }

    public void setPQ(byte[] buffer, short offset, short length)
            throws CryptoException {
        setKeyMaterial(buffer, offset, length, (short) (_size / 16),
                (short) (4 * (short) (_size / 16)), (short) 4);

    }

    public short getPQ(byte[] buffer, short offset) throws CryptoException {
        return getKeyMaterial(buffer, offset, (short) (_size / 16),
                (short) (4 * (short) (_size / 16)), (short) 4);
    }

}
