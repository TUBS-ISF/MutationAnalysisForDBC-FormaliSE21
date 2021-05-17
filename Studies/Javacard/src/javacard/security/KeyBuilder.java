/**
  * This file is part of the KeY Java Card API Reference Implementation.
  *
  * Author: Wojciech Mostowski, woj@cs.ru.nl
  *   
  * For details and license see README and LICENSE files.
  *    
  * $Id: KeyBuilder.java,v 1.2 2007/06/25 09:57:01 woj Exp $
  */

package javacard.security;

public class KeyBuilder {

    public static final short LENGTH_AES_128 = (short) 128;

    public static final short LENGTH_AES_192 = (short) 192;

    public static final short LENGTH_AES_256 = (short) 256;

    public static final short LENGTH_DES = (short) 64;

    public static final short LENGTH_DES3_2KEY = (short) 128;

    public static final short LENGTH_DES3_3KEY = (short) 192;

    public static final short LENGTH_DSA_1024 = (short) 1024;

    public static final short LENGTH_DSA_512 = (short) 512;

    public static final short LENGTH_DSA_768 = (short) 768;

    public static final short LENGTH_EC_F2M_113 = (short) 113;

    public static final short LENGTH_EC_F2M_131 = (short) 131;

    public static final short LENGTH_EC_F2M_163 = (short) 163;

    public static final short LENGTH_EC_F2M_193 = (short) 193;

    public static final short LENGTH_EC_FP_112 = (short) 112;

    public static final short LENGTH_EC_FP_128 = (short) 128;

    public static final short LENGTH_EC_FP_160 = (short) 160;

    public static final short LENGTH_EC_FP_192 = (short) 192;

    public static final short LENGTH_RSA_1024 = (short) 1024;

    public static final short LENGTH_RSA_1280 = (short) 1280;

    public static final short LENGTH_RSA_1536 = (short) 1536;

    public static final short LENGTH_RSA_1984 = (short) 1984;

    public static final short LENGTH_RSA_2048 = (short) 2048;

    public static final short LENGTH_RSA_512 = (short) 512;

    public static final short LENGTH_RSA_736 = (short) 736;

    public static final short LENGTH_RSA_768 = (short) 768;

    public static final short LENGTH_RSA_896 = (short) 896;

    public static final byte TYPE_AES = (byte) 15;

    public static final byte TYPE_AES_TRANSIENT_DESELECT = (byte) 14;

    public static final byte TYPE_AES_TRANSIENT_RESET = (byte) 13;

    public static final byte TYPE_DES = (byte) 3;

    public static final byte TYPE_DES_TRANSIENT_DESELECT = (byte) 2;

    public static final byte TYPE_DES_TRANSIENT_RESET = (byte) 1;

    public static final byte TYPE_DSA_PRIVATE = (byte) 8;

    public static final byte TYPE_DSA_PUBLIC = (byte) 7;

    public static final byte TYPE_EC_F2M_PRIVATE = (byte) 10;

    public static final byte TYPE_EC_F2M_PUBLIC = (byte) 9;

    public static final byte TYPE_EC_FP_PRIVATE = (byte) 12;

    public static final byte TYPE_EC_FP_PUBLIC = (byte) 11;

    public static final byte TYPE_RSA_CRT_PRIVATE = (byte) 6;

    public static final byte TYPE_RSA_PRIVATE = (byte) 5;

    public static final byte TYPE_RSA_PUBLIC = (byte) 4;

    public static Key buildKey(byte keyType, short keyLength,
            boolean keyEncryption) throws CryptoException {

        if (isDES(keyType, keyLength)) {
            if (keyEncryption)
                return new DESKeyImplEnc(keyType, keyLength);
            else
                return new DESKeyImpl(keyType, keyLength);
        } else if (isAES(keyType, keyLength)) {
            if (keyEncryption)
                return new AESKeyImplEnc(keyType, keyLength);
            else
                return new AESKeyImpl(keyType, keyLength);
        } else if (isRSAPublic(keyType, keyLength)) {
            if (keyEncryption)
                return new RSAPublicKeyImplEnc(keyType, keyLength);
            else
                return new RSAPublicKeyImpl(keyType, keyLength);
        } else if (isRSAPrivate(keyType, keyLength)) {
            if (keyEncryption)
                return new RSAPrivateKeyImplEnc(keyType, keyLength);
            else
                return new RSAPrivateKeyImpl(keyType, keyLength);
        } else if (isRSACRTPrivate(keyType, keyLength)) {
            if (keyEncryption)
                return new RSAPrivateCrtKeyImplEnc(keyType, keyLength);
            else
                return new RSAPrivateCrtKeyImpl(keyType, keyLength);
        } else if (isECPrivate(keyType, keyLength)) {
            if (keyEncryption)
                return new ECPrivateKeyImplEnc(keyType, keyLength);
            else
                return new ECPrivateKeyImpl(keyType, keyLength);
        } else if (isECPublic(keyType, keyLength)) {
            if (keyEncryption)
                return new ECPublicKeyImplEnc(keyType, keyLength);
            else
                return new ECPublicKeyImpl(keyType, keyLength);
        } else if (isDSAPublic(keyType, keyLength)) {
            if (keyEncryption)
                return new DSAPublicKeyImplEnc(keyType, keyLength);
            else
                return new DSAPublicKeyImpl(keyType, keyLength);
        } else if (isDSAPrivate(keyType, keyLength)) {
            if (keyEncryption)
                return new DSAPrivateKeyImplEnc(keyType, keyLength);
            else
                return new DSAPrivateKeyImpl(keyType, keyLength);
        } else {
            CryptoException.throwIt(CryptoException.NO_SUCH_ALGORITHM);
        }
        return null;
    }

    static boolean isDES(byte keyType, short keyLength) {
        return ((keyType == TYPE_DES || keyType == TYPE_DES_TRANSIENT_DESELECT || keyType == TYPE_DES_TRANSIENT_RESET) && (keyLength == LENGTH_DES
                || keyLength == LENGTH_DES3_2KEY || keyLength == LENGTH_DES3_3KEY));
    }

    static boolean isAES(byte keyType, short keyLength) {
        return ((keyType == TYPE_AES || keyType == TYPE_AES_TRANSIENT_DESELECT || keyType == TYPE_AES_TRANSIENT_RESET) && (keyLength == LENGTH_AES_128
                || keyLength == LENGTH_AES_192 || keyLength == LENGTH_AES_256));
    }

    static boolean isRSALen(short keyLength) {
        return (keyLength == LENGTH_RSA_512 || keyLength == LENGTH_RSA_736
                || keyLength == LENGTH_RSA_768 || keyLength == LENGTH_RSA_896
                || keyLength == LENGTH_RSA_1024 || keyLength == LENGTH_RSA_1280
                || keyLength == LENGTH_RSA_1536 || keyLength == LENGTH_RSA_1984 || keyLength == LENGTH_RSA_2048);
    }

    static boolean isRSAPublic(byte keyType, short keyLength) {
        return (keyType == TYPE_RSA_PUBLIC && isRSALen(keyLength));
    }

    static boolean isRSAPrivate(byte keyType, short keyLength) {
        return (keyType == TYPE_RSA_PRIVATE && isRSALen(keyLength));
    }

    static boolean isRSACRTPrivate(byte keyType, short keyLength) {
        return (keyType == TYPE_RSA_CRT_PRIVATE && isRSALen(keyLength));
    }

    static boolean isDSAPublic(byte keyType, short keyLength) {
        return (keyType == TYPE_DSA_PUBLIC && isDSALen(keyLength));
    }

    static boolean isDSAPrivate(byte keyType, short keyLength) {
        return (keyType == TYPE_DSA_PRIVATE && isDSALen(keyLength));
    }

    static boolean isDSALen(short keyLength) {
        return (keyLength == LENGTH_DSA_512 || keyLength == LENGTH_DSA_768 || keyLength == LENGTH_DSA_1024);
    }

    static boolean isECf2mLen(short keyLength) {
        return (keyLength == LENGTH_EC_F2M_113
                || keyLength == LENGTH_EC_F2M_131
                || keyLength == LENGTH_EC_F2M_163 || keyLength == LENGTH_EC_F2M_193);
    }

    static boolean isECfpLen(short keyLength) {
        return (keyLength == LENGTH_EC_FP_112 || keyLength == LENGTH_EC_FP_128
                || keyLength == LENGTH_EC_FP_160 || keyLength == LENGTH_EC_FP_192);
    }

    static boolean isECPublic(byte keyType, short keyLength) {
        return ((keyType == TYPE_EC_F2M_PUBLIC && isECf2mLen(keyLength)) || (keyType == TYPE_EC_FP_PUBLIC && isECfpLen(keyLength)));
    }

    static boolean isECPrivate(byte keyType, short keyLength) {
        return ((keyType == TYPE_EC_F2M_PRIVATE && isECf2mLen(keyLength)) || (keyType == TYPE_EC_FP_PRIVATE && isECfpLen(keyLength)));
    }

    static boolean isValidKeyTypeSize(byte keyType, short keyLength) {
        boolean isAES = isAES(keyType, keyLength);
        boolean isDES = isDES(keyType, keyLength);
        boolean isDSA = (isDSAPrivate(keyType, keyLength) || isDSAPublic(
                keyType, keyLength));
        boolean isRSA = (isRSAPrivate(keyType, keyLength)
                || isRSACRTPrivate(keyType, keyLength) || isRSAPublic(keyType,
                keyLength));
        boolean isECF2M = (isECf2mLen(keyLength) && (keyType == TYPE_EC_F2M_PRIVATE || keyType == TYPE_EC_F2M_PUBLIC));
        boolean isECFP = (isECfpLen(keyLength) && (keyType == TYPE_EC_FP_PRIVATE || keyType == TYPE_EC_FP_PUBLIC));

        return (isAES || isDES || isRSA || isECFP || isECF2M || isDSA);

    }

    static boolean isPersistentKeyType(byte keyType) {
        return (keyType == TYPE_AES || keyType == TYPE_DES
                || keyType == TYPE_RSA_PUBLIC || keyType == TYPE_RSA_PRIVATE
                || keyType == TYPE_RSA_CRT_PRIVATE
                || keyType == TYPE_EC_F2M_PUBLIC
                || keyType == TYPE_EC_F2M_PRIVATE
                || keyType == TYPE_EC_FP_PUBLIC
                || keyType == TYPE_EC_FP_PRIVATE || keyType == TYPE_DSA_PUBLIC || keyType == TYPE_DSA_PRIVATE);

    }

    static boolean isTransientDeselectKeyType(byte keyType) {
        return (keyType == TYPE_AES_TRANSIENT_DESELECT || keyType == TYPE_DES_TRANSIENT_DESELECT);

    }

    static boolean isTransientResetKeyType(byte keyType) {
        return (keyType == TYPE_AES_TRANSIENT_RESET || keyType == TYPE_DES_TRANSIENT_RESET);

    }

}
