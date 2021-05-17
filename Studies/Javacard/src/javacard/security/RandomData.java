/**
  * This file is part of the KeY Java Card API Reference Implementation.
  *
  * Author: Wojciech Mostowski, woj@cs.ru.nl
  *   
  * For details and license see README and LICENSE files.
  *    
  * $Id: RandomData.java,v 1.2 2007/06/25 09:57:01 woj Exp $
  */

package javacard.security;

public abstract class RandomData {

    public static final byte ALG_PSEUDO_RANDOM = (byte) 1;

    public static final byte ALG_SECURE_RANDOM = (byte) 2;

    protected RandomData() {
    }

    public static final RandomData getInstance(byte algorithm)
            throws CryptoException {
        return new RandomDataImpl(algorithm);
    }

    public abstract void generateData(byte[] buffer, short offset, short length)
            throws CryptoException;

    public abstract void setSeed(byte[] buffer, short offset, short length);

}
