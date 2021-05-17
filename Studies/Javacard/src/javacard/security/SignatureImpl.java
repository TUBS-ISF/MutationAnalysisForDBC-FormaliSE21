/**
  * This file is part of the KeY Java Card API Reference Implementation.
  *
  * Author: Wojciech Mostowski, woj@cs.ru.nl
  *   
  * For details and license see README and LICENSE files.
  *    
  * $Id: SignatureImpl.java,v 1.4 2007/07/03 09:14:19 woj Exp $
  */

package javacard.security;

import javacard.framework.JCSystem;
import de.uka.ilkd.key.javacard.KeYJCSystem;

public class SignatureImpl extends Signature {

    protected byte _algorithm = 0;

    protected Key _key = null;

    protected short _blockSize = 0;

    protected byte _mode = 0;

    protected short[] _read = null;

    protected short _maxLen = 0;

    protected short _minPad = 0;

    protected short _sigLen = 0;

    SignatureImpl(byte algorithm) throws CryptoException {
        if (algorithm < ALG_DES_MAC4_NOPAD
                || algorithm > ALG_RSA_RIPEMD160_PKCS1_PSS)
            CryptoException.throwIt(CryptoException.NO_SUCH_ALGORITHM);
        _algorithm = algorithm;
        _minPad = minPadLen();
        _read = JCSystem.makeTransientShortArray((short) 1,
                JCSystem.CLEAR_ON_RESET);
	KeYJCSystem.setJavaOwner(_read, this);
    }

    public byte getAlgorithm() {
        return _algorithm;
    }

    public void init(Key theKey, byte theMode) throws CryptoException {

        // CBC use zero vector

        // The big question here is in which order those checks should
        // be made?...

        if (theMode != MODE_SIGN && theMode != MODE_VERIFY)
            CryptoException.throwIt(CryptoException.ILLEGAL_VALUE);
        if (!theKey.isInitialized())
            CryptoException.throwIt(CryptoException.UNINITIALIZED_KEY);

        if (algAES()) {
            if (theKey.getType() != KeyBuilder.TYPE_AES
                    && theKey.getType() != KeyBuilder.TYPE_AES_TRANSIENT_DESELECT
                    && theKey.getType() != KeyBuilder.TYPE_AES_TRANSIENT_RESET)
                CryptoException.throwIt(CryptoException.ILLEGAL_VALUE);
            _blockSize = 16;
            _sigLen = 16;
            _maxLen = (short) 0x7fff;
        } else if (algDES()) {
            if (theKey.getType() != KeyBuilder.TYPE_DES
                    && theKey.getType() != KeyBuilder.TYPE_DES_TRANSIENT_DESELECT
                    && theKey.getType() != KeyBuilder.TYPE_DES_TRANSIENT_RESET)
                CryptoException.throwIt(CryptoException.ILLEGAL_VALUE);
            _blockSize = 8;
            _maxLen = (short) (0x7fff - _minPad); // any length

            if (algDESAlg3()) {
                if (theKey.getSize() != KeyBuilder.LENGTH_DES3_2KEY)
                    CryptoException.throwIt(CryptoException.ILLEGAL_VALUE);

            } else {
                if (theKey.getSize() != KeyBuilder.LENGTH_DES
                        && theKey.getSize() != KeyBuilder.LENGTH_DES3_3KEY)
                    CryptoException.throwIt(CryptoException.ILLEGAL_VALUE);
            }
            if (algDESMac4()) {
                _sigLen = 4;
            } else {
                _sigLen = 8;
            }
        } else if (algDSA()) {
            if (!((theKey.getType() == KeyBuilder.TYPE_DSA_PUBLIC && theMode == MODE_VERIFY) || (theKey
                    .getType() == KeyBuilder.TYPE_DSA_PRIVATE && theMode == MODE_SIGN)))
                CryptoException.throwIt(CryptoException.ILLEGAL_VALUE);
            _blockSize = 0;
            _sigLen = 8;
            _maxLen = (short) 0x7fff;
        } else if (algRSA()) {
            if (!((theKey.getType() == KeyBuilder.TYPE_RSA_PUBLIC && theMode == MODE_VERIFY) || ((theKey
                    .getType() == KeyBuilder.TYPE_RSA_PRIVATE || theKey
                    .getType() == KeyBuilder.TYPE_RSA_CRT_PRIVATE) && theMode == MODE_SIGN)))
                CryptoException.throwIt(CryptoException.ILLEGAL_VALUE);
            _blockSize = 0;
            _sigLen = (short) (theKey.getSize() / 8);
            _maxLen = (short) 0x7fff;
        }
        _key = theKey;
        _mode = theMode;
        _read[0] = 0;
    }

    public void init(Key theKey, byte theMode, byte[] bArray, short bOff,
            short bLen) throws CryptoException {

        if (theMode != MODE_SIGN && theMode != MODE_VERIFY)
            CryptoException.throwIt(CryptoException.ILLEGAL_VALUE);
        if (!theKey.isInitialized())
            CryptoException.throwIt(CryptoException.UNINITIALIZED_KEY);

        if (algAES()) {
            if (theKey.getType() != KeyBuilder.TYPE_AES
                    && theKey.getType() != KeyBuilder.TYPE_AES_TRANSIENT_DESELECT
                    && theKey.getType() != KeyBuilder.TYPE_AES_TRANSIENT_RESET)
                CryptoException.throwIt(CryptoException.ILLEGAL_VALUE);
            if (bLen != 16)
                CryptoException.throwIt(CryptoException.ILLEGAL_VALUE);
            if (bArray == null)
                throw KeYJCSystem.npe;
            if (bOff < 0 || (short) (bOff + bLen) > bArray.length)
                throw KeYJCSystem.aioobe;
            _blockSize = 16;
            _maxLen = 0x7fff;
            _sigLen = 16;
        } else if (algDES()) {
            if (theKey.getType() != KeyBuilder.TYPE_DES
                    && theKey.getType() != KeyBuilder.TYPE_DES_TRANSIENT_DESELECT
                    && theKey.getType() != KeyBuilder.TYPE_DES_TRANSIENT_RESET)
                CryptoException.throwIt(CryptoException.ILLEGAL_VALUE);
            if (bLen != 8)
                CryptoException.throwIt(CryptoException.ILLEGAL_VALUE);
            if (bArray == null)
                throw KeYJCSystem.npe;
            if (bOff < 0 || (short) (bOff + bLen) > bArray.length)
                throw KeYJCSystem.aioobe;
            _blockSize = 8;
            _maxLen = (short) (0x7fff - _minPad);

            if (algDESAlg3()) {
                if (theKey.getSize() != KeyBuilder.LENGTH_DES3_2KEY)
                    CryptoException.throwIt(CryptoException.ILLEGAL_VALUE);
            } else {
                if (theKey.getSize() != KeyBuilder.LENGTH_DES
                        && theKey.getSize() != KeyBuilder.LENGTH_DES3_3KEY)
                    CryptoException.throwIt(CryptoException.ILLEGAL_VALUE);
            }
            if (algDESMac4()) {
                _sigLen = 4;
            } else {
                _sigLen = 8;
            }
        } else {
            CryptoException.throwIt(CryptoException.ILLEGAL_VALUE);

        }
        _key = theKey;
        _mode = theMode;
        _read[0] = 0;
    }

    public short getLength() throws CryptoException {
        if (_key == null)
            CryptoException.throwIt(CryptoException.INVALID_INIT);
        if (!_key.isInitialized())
            CryptoException.throwIt(CryptoException.UNINITIALIZED_KEY);
        return _sigLen;
    }

    public void update(byte[] inBuff, short inOffset, short inLength)
            throws CryptoException {
        // Again, what is the actual order of these checks?
        if (_key == null)
            CryptoException.throwIt(CryptoException.INVALID_INIT);
        if (!_key.isInitialized())
            CryptoException.throwIt(CryptoException.UNINITIALIZED_KEY);

        if (inLength == 0)
            return;
        if (inBuff == null)
            throw KeYJCSystem.npe;
        if (inLength < 0 || inOffset < 0
                || (short) (inOffset + inLength) > inBuff.length)
            throw KeYJCSystem.aioobe;

        if ((short) (_read[0] + inLength) > _maxLen
                || (short) (_read[0] + inLength) < 0)
            CryptoException.throwIt(CryptoException.ILLEGAL_VALUE);
        _read[0] += inLength;
    }

    public short sign(byte[] inBuff, short inOffset, short inLength,
            byte[] sigBuff, short sigOffset) throws CryptoException {
        if (_key == null || _mode != MODE_SIGN)
            CryptoException.throwIt(CryptoException.INVALID_INIT);
        if (!_key.isInitialized())
            CryptoException.throwIt(CryptoException.UNINITIALIZED_KEY);

        if (inLength > 0 && inBuff == null)
            throw KeYJCSystem.npe;
        if (inLength < 0
                || (inLength > 0 && (inOffset < 0 || (short) (inOffset + inLength) > inBuff.length)))
            throw KeYJCSystem.aioobe;
        if ((short) (_read[0] + inLength) > _maxLen
                || (short) (_read[0] + inLength) < 0)
            CryptoException.throwIt(CryptoException.ILLEGAL_VALUE);
        _read[0] += inLength;

        boolean nopad = (_algorithm == ALG_AES_MAC_128_NOPAD
                || _algorithm == ALG_DES_MAC4_NOPAD || _algorithm == ALG_DES_MAC8_NOPAD);

        if (nopad
                && (_read[0] == 0 || (_blockSize != 0 && (short) (_read[0] % _blockSize) != 0)))
            CryptoException.throwIt(CryptoException.ILLEGAL_VALUE);

        if (sigBuff == null)
            throw KeYJCSystem.npe;
        if (sigOffset < 0 || (short) (sigOffset + _sigLen) > sigBuff.length)
            throw KeYJCSystem.aioobe;
        _read[0] = 0;
        return _sigLen;
    }

    public boolean verify(byte[] inBuff, short inOffset, short inLength,
            byte[] sigBuff, short sigOffset, short sigLength)
            throws CryptoException {
        if (_key == null || _mode != MODE_VERIFY)
            CryptoException.throwIt(CryptoException.INVALID_INIT);
        if (!_key.isInitialized())
            CryptoException.throwIt(CryptoException.UNINITIALIZED_KEY);

        if (inLength > 0 && inBuff == null)
            throw KeYJCSystem.npe;
        if (inLength < 0
                || (inLength > 0 && (inOffset < 0 || (short) (inOffset + inLength) > inBuff.length)))
            throw KeYJCSystem.aioobe;
        if ((short) (_read[0] + inLength) > _maxLen
                || (short) (_read[0] + inLength) < 0)
            CryptoException.throwIt(CryptoException.ILLEGAL_VALUE);
        _read[0] += inLength;

        if (noPadAlg()
                && (_read[0] == 0 || (_blockSize != 0 && (short) (_read[0] % _blockSize) != 0)))
            CryptoException.throwIt(CryptoException.ILLEGAL_VALUE);

        if (sigLength != _sigLen) {
            _read[0] = 0;
            return false;
        }

        if (sigBuff == null)
            throw KeYJCSystem.npe;
        if (sigOffset < 0 || (short) (sigOffset + sigLength) > sigBuff.length)
            throw KeYJCSystem.aioobe;
        _read[0] = 0;
        return (sigBuff[0] == 0xDA); // or anything - makes the result more
        // or less random

    }

    private short minPadLen() {

        if (_algorithm == ALG_DES_MAC4_ISO9797_1_M2_ALG3
                || _algorithm == ALG_DES_MAC4_ISO9797_M1
                || _algorithm == ALG_DES_MAC4_ISO9797_M2
                || _algorithm == ALG_DES_MAC8_ISO9797_1_M2_ALG3
                || _algorithm == ALG_DES_MAC8_ISO9797_M1
                || _algorithm == ALG_DES_MAC8_ISO9797_M2

        )
            return (short) 1;
        return (short) 0;

    }

    private boolean noPadAlg() {
        return (_algorithm == ALG_AES_MAC_128_NOPAD
                || _algorithm == ALG_DES_MAC4_NOPAD || _algorithm == ALG_DES_MAC8_NOPAD);
    }

    private boolean algAES() {
        return (_algorithm == ALG_AES_MAC_128_NOPAD);
    }

    private boolean algDES() {
        return (_algorithm == ALG_DES_MAC4_ISO9797_1_M2_ALG3
                || _algorithm == ALG_DES_MAC4_ISO9797_M1
                || _algorithm == ALG_DES_MAC4_ISO9797_M2
                || _algorithm == ALG_DES_MAC4_NOPAD
                || _algorithm == ALG_DES_MAC4_PKCS5
                || _algorithm == ALG_DES_MAC8_ISO9797_1_M2_ALG3
                || _algorithm == ALG_DES_MAC8_ISO9797_M1
                || _algorithm == ALG_DES_MAC8_ISO9797_M2
                || _algorithm == ALG_DES_MAC8_NOPAD || _algorithm == ALG_DES_MAC8_PKCS5);
    }

    private boolean algDESAlg3() {
        return (_algorithm == ALG_DES_MAC4_ISO9797_1_M2_ALG3 || _algorithm == ALG_DES_MAC8_ISO9797_1_M2_ALG3);
    }

    private boolean algDESMac4() {
        return (_algorithm == ALG_DES_MAC4_ISO9797_1_M2_ALG3
                || _algorithm == ALG_DES_MAC4_ISO9797_M1
                || _algorithm == ALG_DES_MAC4_ISO9797_M2
                || _algorithm == ALG_DES_MAC4_NOPAD || _algorithm == ALG_DES_MAC4_PKCS5);
    }

    private boolean algDSA() {
        return (_algorithm == ALG_DSA_SHA || _algorithm == ALG_ECDSA_SHA);
    }

    private boolean algRSA() {
        return (_algorithm == ALG_RSA_MD5_PKCS1
                || _algorithm == ALG_RSA_MD5_PKCS1_PSS
                || _algorithm == ALG_RSA_MD5_RFC2409
                || _algorithm == ALG_RSA_RIPEMD160_ISO9796
                || _algorithm == ALG_RSA_RIPEMD160_PKCS1
                || _algorithm == ALG_RSA_RIPEMD160_PKCS1_PSS
                || _algorithm == ALG_RSA_SHA_ISO9796
                || _algorithm == ALG_RSA_SHA_PKCS1
                || _algorithm == ALG_RSA_SHA_PKCS1_PSS || _algorithm == ALG_RSA_SHA_RFC2409);
    }

}
