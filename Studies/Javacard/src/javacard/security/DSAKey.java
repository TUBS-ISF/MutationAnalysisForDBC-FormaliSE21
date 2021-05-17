/**
  * This file is part of the KeY Java Card API Reference Implementation.
  *
  * Author: Wojciech Mostowski, woj@cs.ru.nl
  *   
  * For details and license see README and LICENSE files.
  *    
  * $Id: DSAKey.java,v 1.2 2007/06/25 09:57:01 woj Exp $
  */

package javacard.security;

public interface DSAKey {

    public void setP(byte[] buffer, short offset, short length)
            throws CryptoException;

    public void setQ(byte[] buffer, short offset, short length)
            throws CryptoException;

    public void setG(byte[] buffer, short offset, short length)
            throws CryptoException;

    public short getP(byte[] buffer, short offset) throws CryptoException;

    public short getQ(byte[] buffer, short offset) throws CryptoException;

    public short getG(byte[] buffer, short offset) throws CryptoException;

}
