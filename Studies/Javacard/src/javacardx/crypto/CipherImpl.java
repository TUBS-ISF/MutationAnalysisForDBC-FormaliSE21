/**
  * This file is part of the KeY Java Card API Reference Implementation.
  *
  * Author: Wojciech Mostowski, woj@cs.ru.nl
  *   
  * For details and license see README and LICENSE files.
  *    
  * $Id: CipherImpl.java,v 1.4 2007/07/03 09:14:19 woj Exp $
  */

package javacardx.crypto;

import javacard.security.CryptoException;
import javacard.security.Key;
import javacard.security.KeyBuilder;
import javacard.framework.JCSystem;
import de.uka.ilkd.key.javacard.KeYJCSystem;

public class CipherImpl extends Cipher {

    protected byte _algorithm = 0;

    protected Key _key = null;

    protected short _blockSize = 0;

    protected byte _mode = 0;

    protected short[] _read = null;

    protected short _maxLen = 0;

    protected short _minPad = 0;

    protected short _padLen = 0;

    CipherImpl(byte algorithm) throws CryptoException {
        if (algorithm < ALG_DES_CBC_NOPAD || algorithm > ALG_RSA_PKCS1_OAEP)
            CryptoException.throwIt(CryptoException.NO_SUCH_ALGORITHM);
        _algorithm = algorithm;
        _minPad = minPadLen();
        _padLen = _minPad;
        _read = JCSystem.makeTransientShortArray((short) 2,
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

        if (theMode != MODE_DECRYPT && theMode != MODE_ENCRYPT)
            CryptoException.throwIt(CryptoException.ILLEGAL_VALUE);
        if (!theKey.isInitialized())
            CryptoException.throwIt(CryptoException.UNINITIALIZED_KEY);

        if (algAES()) {
            if (theKey.getType() != KeyBuilder.TYPE_AES
                    && theKey.getType() != KeyBuilder.TYPE_AES_TRANSIENT_DESELECT
                    && theKey.getType() != KeyBuilder.TYPE_AES_TRANSIENT_RESET)
                CryptoException.throwIt(CryptoException.ILLEGAL_VALUE);
            _blockSize = 16;
            if (theMode == MODE_ENCRYPT)
                _maxLen = (short) (0x7fff - _minPad); // any length
            else
                _maxLen = (short) 0x7fff; // any length

        } else if (algDES()) {
            if (theKey.getType() != KeyBuilder.TYPE_DES
                    && theKey.getType() != KeyBuilder.TYPE_DES_TRANSIENT_DESELECT
                    && theKey.getType() != KeyBuilder.TYPE_DES_TRANSIENT_RESET)
                CryptoException.throwIt(CryptoException.ILLEGAL_VALUE);
            _blockSize = 8;
            if (theMode == MODE_ENCRYPT)
                _maxLen = (short) (0x7fff - _minPad); // any length
            else
                _maxLen = (short) 0x7fff; // any length

        } else if (algRSA()) {
            if (!((theKey.getType() == KeyBuilder.TYPE_RSA_PUBLIC && theMode == MODE_ENCRYPT) || ((theKey
                    .getType() == KeyBuilder.TYPE_RSA_PRIVATE || theKey
                    .getType() == KeyBuilder.TYPE_RSA_CRT_PRIVATE) && theMode == MODE_DECRYPT)))
                CryptoException.throwIt(CryptoException.ILLEGAL_VALUE);
            _blockSize = (short) (theKey.getSize() / 8);
            if (theMode == MODE_ENCRYPT)
                _maxLen = (short) (_blockSize - _minPad);
            else
                _maxLen = _blockSize;
        }

        _key = theKey;
        _mode = theMode;
        _read[1] = 0;
        _read[0] = 0;
    }

    public void init(Key theKey, byte theMode, byte[] bArray, short bOff,
            short bLen) throws CryptoException {

        // The big question here is in which order those checks should
        // be made?...

        if (theMode != MODE_DECRYPT && theMode != MODE_ENCRYPT)
            CryptoException.throwIt(CryptoException.ILLEGAL_VALUE);
        if (!theKey.isInitialized())
            CryptoException.throwIt(CryptoException.UNINITIALIZED_KEY);

        if (algAESCBC()) {
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

        } else if (algDESCBC()) {
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

        } else {
            CryptoException.throwIt(CryptoException.ILLEGAL_VALUE);

        }

        _key = theKey;
        _mode = theMode;
        _read[1] = 0;
        _read[0] = 0;
        if (theMode == MODE_ENCRYPT)
            _maxLen = (short) (0x7fff - _minPad);
        else
            _maxLen = (short) 0x7fff;
    }

    public short update(byte[] inBuff, short inOffset, short inLength,
            byte[] outBuff, short outOffset) throws CryptoException {
        // Again, what is the actual order of these checks?
        if (_key == null)
            CryptoException.throwIt(CryptoException.INVALID_INIT);
        if (!_key.isInitialized())
            CryptoException.throwIt(CryptoException.UNINITIALIZED_KEY);

        if (inLength == 0)
            return 0;
        if (inBuff == null)
            throw KeYJCSystem.npe;
        if (inLength < 0 || inOffset < 0
                || (short) (inOffset + inLength) > inBuff.length)
            throw KeYJCSystem.aioobe;

        _read[1] += inLength;
        if ((short) (_read[0] + inLength) > _maxLen
                || (short) (_read[0] + inLength) < 0)
            CryptoException.throwIt(CryptoException.ILLEGAL_VALUE);
        _read[0] += inLength;
        short nb = 0;
        if (_mode == MODE_DECRYPT)
            // In case the update takes the last block this avoids the
            // decryption of the padding bytes
            nb = (short) ((short) (_read[1] - 1) / _blockSize);
        else
            nb = (short) (_read[1] / _blockSize);

        _read[1] -= (short) (nb * _blockSize);
        if (nb > 0) {
            if (outBuff == null)
                throw KeYJCSystem.npe;
            if (outOffset < 0
                    || (short) (outOffset + (short) (nb * _blockSize)) > outBuff.length)
                throw KeYJCSystem.aioobe;
            // writes outBuff[outOffset..outOffset+result-1]
            // for update result is always a multiple of _blockSize
        }
        return (short) (nb * _blockSize);
    }

    public short doFinal(byte[] inBuff, short inOffset, short inLength,
            byte[] outBuff, short outOffset) throws CryptoException {
        if (_key == null)
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
        _read[1] += inLength;
        if (((noPadAlg() && _mode == MODE_ENCRYPT) || _mode == MODE_DECRYPT)
                && (_read[0] == 0 || (short) (_read[1] % _blockSize) != 0))
            CryptoException.throwIt(CryptoException.ILLEGAL_VALUE);

        short result = 0;
        if (_mode == MODE_ENCRYPT) {
            if ((short) (_read[1] + _minPad) == 0)
                result = _blockSize;
            else
                result = (short) (((short) ((short) ((short) ((short) (_read[1] + _minPad) - 1) / _blockSize) + 1)) * _blockSize);
        } else
            result = (short) (_read[1] - _padLen);
        _read[1] = 0;
        if (result > 0) {
            if (outBuff == null)
                throw KeYJCSystem.npe;
            if (outOffset < 0 || (short) (outOffset + result) > outBuff.length)
                throw KeYJCSystem.aioobe;
            // writes outBuff[outOffset..outOffset+result-1]
            // for doFinal and MODE_ENCRYPT result is always a multiple of
            // _blockSize
            // for doFinal and MODE_DECRYPY result here is always a
            // multiple of _blockSize, but could be anything between 0 and
            // _buffered
        }

        return result;
    }

    private boolean noPadAlg() {
        return (_algorithm == ALG_AES_BLOCK_128_ECB_NOPAD
                || _algorithm == ALG_AES_BLOCK_128_CBC_NOPAD
                || _algorithm == ALG_DES_CBC_NOPAD
                || _algorithm == ALG_DES_ECB_NOPAD || _algorithm == ALG_RSA_NOPAD);

    }

    private short minPadLen() {
        switch (_algorithm) {
        case ALG_AES_BLOCK_128_ECB_NOPAD:
        case ALG_AES_BLOCK_128_CBC_NOPAD:
        case ALG_DES_CBC_NOPAD:
        case ALG_DES_ECB_NOPAD:
        case ALG_RSA_NOPAD:
        case ALG_DES_CBC_ISO9797_M1:
        case ALG_DES_ECB_ISO9797_M1:
        case ALG_DES_CBC_PKCS5:
        case ALG_DES_ECB_PKCS5:
        case ALG_RSA_ISO9796: // this is a guess - padding with 0's ?
            return (short) 0;
        case ALG_DES_CBC_ISO9797_M2:
        case ALG_DES_ECB_ISO9797_M2:
            return (short) 1;
        case ALG_RSA_ISO14888:
            return (short) 24; // ?? SHA1 hash + 2 + 2 control bytes + fill
            // data
        case ALG_RSA_PKCS1:
            return (short) 11;
        case ALG_RSA_PKCS1_OAEP:
            return (short) 41;// 2 * SHA length + 1
        default:
            return (short) -1;
        }

    }

    private boolean algAES() {
        return (_algorithm == ALG_AES_BLOCK_128_ECB_NOPAD || _algorithm == ALG_AES_BLOCK_128_CBC_NOPAD);
    }

    private boolean algAESCBC() {
        return (_algorithm == ALG_AES_BLOCK_128_CBC_NOPAD);
    }

    private boolean algDES() {
        return (_algorithm == ALG_DES_CBC_ISO9797_M1
                || _algorithm == ALG_DES_CBC_ISO9797_M2
                || _algorithm == ALG_DES_CBC_NOPAD
                || _algorithm == ALG_DES_CBC_PKCS5
                || _algorithm == ALG_DES_ECB_ISO9797_M1
                || _algorithm == ALG_DES_ECB_ISO9797_M2
                || _algorithm == ALG_DES_ECB_NOPAD || _algorithm == ALG_DES_ECB_PKCS5);
    }

    private boolean algDESCBC() {
        return (_algorithm == ALG_DES_CBC_ISO9797_M1
                || _algorithm == ALG_DES_CBC_ISO9797_M2
                || _algorithm == ALG_DES_CBC_NOPAD || _algorithm == ALG_DES_CBC_PKCS5);
    }

    private boolean algRSA() {
        return (_algorithm == ALG_RSA_ISO14888 || _algorithm == ALG_RSA_ISO9796
                || _algorithm == ALG_RSA_NOPAD || _algorithm == ALG_RSA_PKCS1 || _algorithm == ALG_RSA_PKCS1_OAEP);

    }

}
