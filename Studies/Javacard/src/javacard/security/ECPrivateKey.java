/**
  * This file is part of the KeY Java Card API Reference Implementation.
  *
  * Author: Wojciech Mostowski, woj@cs.ru.nl
  *   
  * For details and license see README and LICENSE files.
  *    
  * $Id: ECPrivateKey.java,v 1.2 2007/06/25 09:57:01 woj Exp $
  */

package javacard.security;

public interface ECPrivateKey extends PrivateKey, ECKey {

    public void setS(byte[] buffer, short offset, short length)
            throws CryptoException;

    public short getS(byte[] buffer, short offset) throws CryptoException;

}
