/**
  * This file is part of the KeY Java Card API Reference Implementation.
  *
  * Author: Wojciech Mostowski, woj@cs.ru.nl
  *   
  * For details and license see README and LICENSE files.
  *    
  * $Id: Checksum.java,v 1.2 2007/06/25 09:57:01 woj Exp $
  */

package javacard.security;

public abstract class Checksum {

    public static final byte ALG_ISO3309_CRC16 = (byte) 1;

    public static final byte ALG_ISO3309_CRC32 = (byte) 2;

    protected Checksum() {
    }

    public static final Checksum getInstance(byte algorithm,
            boolean externalAccess) throws CryptoException {
        if (externalAccess)
            return new ChecksumImplExt(algorithm);
        else
            return new ChecksumImpl(algorithm);
    }

    public abstract void init(byte[] bArray, short bOff, short bLen)
            throws CryptoException;

    public abstract byte getAlgorithm();

    public abstract short doFinal(byte[] inBuff, short inOffset,
            short inLength, byte[] outBuff, short outOffset);

    public abstract void update(byte[] inBuff, short inOffset, short inLength);

}
