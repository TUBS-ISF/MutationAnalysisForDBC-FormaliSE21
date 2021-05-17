/**
  * This file is part of the KeY Java Card API Reference Implementation.
  *
  * Author: Wojciech Mostowski, woj@cs.ru.nl
  *   
  * For details and license see README and LICENSE files.
  *    
  * $Id: Cipher.java,v 1.2 2007/06/25 09:57:01 woj Exp $
  */

package javacardx.crypto;

import javacard.security.CryptoException;
import javacard.security.Key;

public abstract class Cipher {

    public static final byte ALG_AES_BLOCK_128_CBC_NOPAD = (byte) 13;

    public static final byte ALG_AES_BLOCK_128_ECB_NOPAD = (byte) 14;

    public static final byte ALG_DES_CBC_ISO9797_M1 = (byte) 2;

    public static final byte ALG_DES_CBC_ISO9797_M2 = (byte) 3;

    public static final byte ALG_DES_CBC_NOPAD = (byte) 1;

    public static final byte ALG_DES_CBC_PKCS5 = (byte) 4;

    public static final byte ALG_DES_ECB_ISO9797_M1 = (byte) 6;

    public static final byte ALG_DES_ECB_ISO9797_M2 = (byte) 7;

    public static final byte ALG_DES_ECB_NOPAD = (byte) 5;

    public static final byte ALG_DES_ECB_PKCS5 = (byte) 8;

    public static final byte ALG_RSA_ISO14888 = (byte) 9;

    public static final byte ALG_RSA_ISO9796 = (byte) 11;

    public static final byte ALG_RSA_NOPAD = (byte) 12;

    public static final byte ALG_RSA_PKCS1 = (byte) 10;

    public static final byte ALG_RSA_PKCS1_OAEP = (byte) 15;

    public static final byte MODE_DECRYPT = (byte) 1;

    public static final byte MODE_ENCRYPT = (byte) 2;

    protected Cipher() {
    }

    public static final Cipher getInstance(byte algorithm,
            boolean externalAccess) throws CryptoException {
        if (externalAccess)
            return new CipherImplExt(algorithm);
        else
            return new CipherImpl(algorithm);
    }

    public abstract void init(Key theKey, byte theMode) throws CryptoException;

    public abstract void init(Key theKey, byte theMode, byte[] bArray,
            short bOff, short bLen) throws CryptoException;

    public abstract byte getAlgorithm();

    public abstract short doFinal(byte[] inBuff, short inOffset,
            short inLength, byte[] outBuff, short outOffset)
            throws CryptoException;

    public abstract short update(byte[] inBuff, short inOffset, short inLength,
            byte[] outBuff, short outOffset) throws CryptoException;

}
