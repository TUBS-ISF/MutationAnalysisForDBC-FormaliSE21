/**
  * This file is part of the KeY Java Card API Reference Implementation.
  *
  * Author: Wojciech Mostowski, woj@cs.ru.nl
  *   
  * For details and license see README and LICENSE files.
  *    
  * $Id: RSAPrivateCrtKey.java,v 1.2 2007/06/25 09:57:01 woj Exp $
  */

package javacard.security;

public interface RSAPrivateCrtKey extends PrivateKey {

    public void setP(byte[] buffer, short offset, short length)
            throws CryptoException;

    public void setQ(byte[] buffer, short offset, short length)
            throws CryptoException;

    public void setDP1(byte[] buffer, short offset, short length)
            throws CryptoException;

    public void setDQ1(byte[] buffer, short offset, short length)
            throws CryptoException;

    public void setPQ(byte[] buffer, short offset, short length)
            throws CryptoException;

    public short getP(byte[] buffer, short offset) throws CryptoException;

    public short getQ(byte[] buffer, short offset) throws CryptoException;

    public short getDP1(byte[] buffer, short offset) throws CryptoException;

    public short getDQ1(byte[] buffer, short offset) throws CryptoException;

    public short getPQ(byte[] buffer, short offset) throws CryptoException;

}
