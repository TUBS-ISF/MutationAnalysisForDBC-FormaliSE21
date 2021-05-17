/**
  * This file is part of the KeY Java Card API Reference Implementation.
  *
  * Author: Wojciech Mostowski, woj@cs.ru.nl
  *   
  * For details and license see README and LICENSE files.
  *    
  * $Id: ECPublicKey.java,v 1.2 2007/06/25 09:57:01 woj Exp $
  */

package javacard.security;

public interface ECPublicKey extends PublicKey, ECKey {

    public void setW(byte[] buffer, short offset, short length)
            throws CryptoException;

    public short getW(byte[] buffer, short offset) throws CryptoException;

}
