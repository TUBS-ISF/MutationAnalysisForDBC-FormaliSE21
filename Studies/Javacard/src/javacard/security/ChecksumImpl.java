/**
  * This file is part of the KeY Java Card API Reference Implementation.
  *
  * Author: Wojciech Mostowski, woj@cs.ru.nl
  *   
  * For details and license see README and LICENSE files.
  *    
  * $Id: ChecksumImpl.java,v 1.3 2007/07/09 12:51:57 woj Exp $
  */

package javacard.security;

import de.uka.ilkd.key.javacard.KeYJCSystem;

public class ChecksumImpl extends Checksum {

    protected byte _algorithm = 0;
    /*@ invariant
          _algorithm == ALG_ISO3309_CRC16 || _algorithm == ALG_ISO3309_CRC32;
      @*/

    protected short _len = 0;
    /*@ invariant
          (_algorithm == ALG_ISO3309_CRC16 ==> _len == 2) &&
	  (_algorithm == ALG_ISO3309_CRC32 ==> _len == 4);
      @*/

    ChecksumImpl(byte algorithm) throws CryptoException {
        if (algorithm != ALG_ISO3309_CRC16 && algorithm != ALG_ISO3309_CRC32)
            CryptoException.throwIt(CryptoException.NO_SUCH_ALGORITHM);
        _algorithm = algorithm;
        if (algorithm == ALG_ISO3309_CRC16)
            _len = 2;
        else
            _len = 4;
    }

    public byte getAlgorithm() {
        return _algorithm;
    }

    public void init(byte[] bArray, short bOff, short bLen)
            throws CryptoException {

        if (bLen != _len)
            CryptoException.throwIt(CryptoException.ILLEGAL_VALUE);
        if (bArray == null)
            throw KeYJCSystem.npe;
        if (bOff < 0 || (short) (bOff + bLen) > bArray.length)
            throw KeYJCSystem.aioobe;
    }

    public void update(byte[] inBuff, short inOffset, short inLength) {

        if (inLength == 0)
            return;
        if (inBuff == null)
            throw KeYJCSystem.npe;
        if (inLength < 0 || inOffset < 0
                || (short) (inOffset + inLength) > inBuff.length)
            throw KeYJCSystem.aioobe;
    }

    public short doFinal(byte[] inBuff, short inOffset, short inLength,
            byte[] outBuff, short outOffset) {

        if (inLength > 0 && inBuff == null)
            throw KeYJCSystem.npe;
        if (inLength < 0
                || (inLength > 0 && (inOffset < 0 || (short) (inOffset + inLength) > inBuff.length)))
            throw KeYJCSystem.aioobe;
        if (outBuff == null)
            throw KeYJCSystem.npe;
        if (outOffset < 0 || outOffset + _len > outBuff.length)
            throw KeYJCSystem.aioobe;
        return _len;
    }

}
