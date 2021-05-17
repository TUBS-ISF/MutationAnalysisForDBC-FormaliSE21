/**
  * This file is part of the KeY Java Card API Reference Implementation.
  *
  * Author: Wojciech Mostowski, woj@cs.ru.nl
  *   
  * For details and license see README and LICENSE files.
  *    
  * $Id: KeyPair.java,v 1.3 2007/08/21 14:09:06 woj Exp $
  */

package javacard.security;

public final class KeyPair {

    public static final byte ALG_DSA = (byte) 3;

    public static final byte ALG_EC_F2M = (byte) 4;

    public static final byte ALG_EC_FP = (byte) 5;

    public static final byte ALG_RSA = (byte) 1;

    public static final byte ALG_RSA_CRT = (byte) 2;

    private PrivateKey _privateKey = null;

    private PublicKey _publicKey = null;

    public KeyPair(byte algorithm, short keyLength) throws CryptoException {
        if (!validAlgorithm(algorithm, keyLength))
            CryptoException.throwIt(CryptoException.NO_SUCH_ALGORITHM);
        /*
         * byte privType = getPrivType(algorithm); byte pubType =
         * getPubType(algorithm);
         * 
         * _privateKey = (PrivateKey)KeyBuilder.buildKey(privType, keyLength,
         * false); _publicKey = (PublicKey)KeyBuilder.buildKey(pubType,
         * keyLength, false);
         */
        if (rsaAlg(algorithm)) {
            _publicKey = new RSAPublicKeyImpl(KeyBuilder.TYPE_RSA_PUBLIC,
                    keyLength);
            if (algorithm == ALG_RSA) {
                _privateKey = new RSAPrivateKeyImpl(
                        KeyBuilder.TYPE_RSA_PRIVATE, keyLength);
            } else {
                _privateKey = new RSAPrivateCrtKeyImpl(
                        KeyBuilder.TYPE_RSA_CRT_PRIVATE, keyLength);
            }
        } else if (algorithm == ALG_DSA) {
            _publicKey = new DSAPublicKeyImpl(KeyBuilder.TYPE_DSA_PUBLIC,
                    keyLength);
            _privateKey = new DSAPrivateKeyImpl(KeyBuilder.TYPE_DSA_PRIVATE,
                    keyLength);
        } else if (ecAlg(algorithm)) {
            byte pubType = getECPubType(algorithm);
            byte privType = getECPrivType(algorithm);
            _publicKey = new ECPublicKeyImpl(pubType, keyLength);
            _privateKey = new ECPrivateKeyImpl(privType, keyLength);
        }

    }

    public KeyPair(PublicKey publicKey, PrivateKey privateKey)
            throws CryptoException {
        byte privType = privateKey.getType();
        byte pubType = publicKey.getType();
        if (!validPair(pubType, privType))
            CryptoException.throwIt(CryptoException.ILLEGAL_VALUE);
        if (privateKey.getSize() != publicKey.getSize())
            CryptoException.throwIt(CryptoException.ILLEGAL_VALUE);
        _privateKey = privateKey;
        _publicKey = publicKey;
    }

    public final void genKeyPair() throws CryptoException {
        ((KeyImpl) _publicKey).initialize();
        ((KeyImpl) _privateKey).initialize();
    }

    public PublicKey getPublic() {
        return _publicKey;
    }

    public PrivateKey getPrivate() {
        return _privateKey;
    }

    private static boolean typesRSA(byte pubType, byte privType) {
        return (pubType == KeyBuilder.TYPE_RSA_PUBLIC && privType == KeyBuilder.TYPE_RSA_PRIVATE);
    }

    private static boolean typesDSA(byte pubType, byte privType) {
        return (pubType == KeyBuilder.TYPE_DSA_PUBLIC && privType == KeyBuilder.TYPE_DSA_PRIVATE);
    }

    private static boolean typesRSACRT(byte pubType, byte privType) {
        return (pubType == KeyBuilder.TYPE_RSA_PUBLIC && privType == KeyBuilder.TYPE_RSA_CRT_PRIVATE);
    }

    private static boolean typesECF2M(byte pubType, byte privType) {
        return (pubType == KeyBuilder.TYPE_EC_F2M_PUBLIC && privType == KeyBuilder.TYPE_EC_F2M_PRIVATE);
    }

    private static boolean typesECFP(byte pubType, byte privType) {
        return (pubType == KeyBuilder.TYPE_EC_FP_PUBLIC && privType == KeyBuilder.TYPE_EC_FP_PRIVATE);
    }

    private static boolean validAlgorithm(byte algorithm, short keySize) {
        if (algorithm == ALG_DSA && KeyBuilder.isDSALen(keySize)
                || algorithm == ALG_EC_F2M && KeyBuilder.isECf2mLen(keySize)
                || algorithm == ALG_EC_FP && KeyBuilder.isECfpLen(keySize)
                || algorithm == ALG_RSA && KeyBuilder.isRSALen(keySize)
                || algorithm == ALG_RSA_CRT && KeyBuilder.isRSALen(keySize))
            return true;
        return false;
    }

    private static boolean rsaAlg(byte algorithm) {
        return (algorithm == ALG_RSA || algorithm == ALG_RSA_CRT);
    }

    private static boolean ecAlg(byte algorithm) {
        return (algorithm == ALG_EC_F2M || algorithm == ALG_EC_FP);
    }

    private static byte getECPubType(byte algorithm) {
        if (algorithm == ALG_EC_F2M) {
            return KeyBuilder.TYPE_EC_F2M_PUBLIC;
        } else {
            return KeyBuilder.TYPE_EC_FP_PUBLIC;
        }
    }

    private static byte getECPrivType(byte algorithm) {
        if (algorithm == ALG_EC_F2M) {
            return KeyBuilder.TYPE_EC_F2M_PRIVATE;
        } else {
            return KeyBuilder.TYPE_EC_FP_PRIVATE;
        }
    }

    private static boolean validPair(byte pubType, byte privType) {
        if (typesRSA(pubType, privType) || typesRSACRT(pubType, privType)
                || typesDSA(pubType, privType) || typesECF2M(pubType, privType)
                || typesECFP(pubType, privType))
            return true;
        return false;
    }

    /*
     * private static byte getPubType(byte algorithm) { if(algorithm == ALG_DSA) {
     * return KeyBuilder.TYPE_DSA_PUBLIC; }else if(algorithm == ALG_EC_F2M) {
     * return KeyBuilder.TYPE_EC_F2M_PUBLIC; }else if(algorithm == ALG_EC_FP) {
     * return KeyBuilder.TYPE_EC_FP_PUBLIC; }else if(algorithm == ALG_RSA ||
     * algorithm == ALG_RSA_CRT) { return KeyBuilder.TYPE_RSA_PUBLIC; } return
     * -1; }
     * 
     * private static byte getPrivType(byte algorithm) { if(algorithm ==
     * ALG_DSA) { return KeyBuilder.TYPE_DSA_PRIVATE; }else if(algorithm ==
     * ALG_EC_F2M) { return KeyBuilder.TYPE_EC_F2M_PRIVATE; }else if(algorithm ==
     * ALG_EC_FP) { return KeyBuilder.TYPE_EC_FP_PRIVATE; }else if(algorithm ==
     * ALG_RSA) { return KeyBuilder.TYPE_RSA_PRIVATE; }else if(algorithm ==
     * ALG_RSA_CRT) { return KeyBuilder.TYPE_RSA_CRT_PRIVATE; } return -1; }
     */

}
