/**
 * This file is part of the KeY Java Card API Reference Implementation.
 *
 * Author: Wojciech Mostowski, woj@cs.ru.nl
 *   
 * For details and license see README and LICENSE files.
 *    
 * $Id: AESKey.java,v 1.3 2007/07/03 09:50:57 woj Exp $
 */

package javacard.security;

public interface AESKey extends SecretKey {

    public void setKey(byte[] keyData, short kOff) throws CryptoException,
            NullPointerException, ArrayIndexOutOfBoundsException;

    public byte getKey(byte[] keyData, short kOff) throws CryptoException;

}
