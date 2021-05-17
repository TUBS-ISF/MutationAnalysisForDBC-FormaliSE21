/**
  * This file is part of the KeY Java Card API Reference Implementation.
  *
  * Author: Wojciech Mostowski, woj@cs.ru.nl
  *   
  * For details and license see README and LICENSE files.
  *    
  * $Id: KeyAgreement.java,v 1.2 2007/06/25 09:57:01 woj Exp $
  */

package javacard.security;

public abstract class KeyAgreement {

    public static final byte ALG_EC_SVDP_DH = (byte) 1;

    public static final byte ALG_EC_SVDP_DHC = (byte) 2;

    protected KeyAgreement() {
    }

    public static final KeyAgreement getInstance(byte algorithm,
            boolean externalAccess) throws CryptoException {
        if (externalAccess)
            return new KeyAgreementImplExt(algorithm);
        else
            return new KeyAgreementImpl(algorithm);
    }

    public abstract void init(PrivateKey privKey) throws CryptoException;

    public abstract byte getAlgorithm();

    public abstract short generateSecret(byte[] publicData, short publicOffset,
            short publicLength, byte[] secret, short secretOffset)
            throws CryptoException;
}
