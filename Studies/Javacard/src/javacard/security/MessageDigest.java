/**
  * This file is part of the KeY Java Card API Reference Implementation.
  *
  * Author: Wojciech Mostowski, woj@cs.ru.nl
  *   
  * For details and license see README and LICENSE files.
  *    
  * $Id: MessageDigest.java,v 1.2 2007/06/25 09:57:01 woj Exp $
  */

package javacard.security;

public abstract class MessageDigest {

    public static final byte ALG_MD5 = (byte) 2;

    public static final byte ALG_RIPEMD160 = (byte) 3;

    public static final byte ALG_SHA = (byte) 1;

    protected MessageDigest() {
    }

    public static final MessageDigest getInstance(byte algorithm,
            boolean externalAccess) throws CryptoException {
        if (externalAccess)
            return new MessageDigestImplExt(algorithm);
        else
            return new MessageDigestImpl(algorithm);

    }

    public abstract byte getAlgorithm();

    public abstract byte getLength();

    public abstract short doFinal(byte[] inBuff, short inOffset,
            short inLength, byte[] outBuff, short outOffset);

    public abstract void update(byte[] inBuff, short inOffset, short inLength);

    public abstract void reset();

}
