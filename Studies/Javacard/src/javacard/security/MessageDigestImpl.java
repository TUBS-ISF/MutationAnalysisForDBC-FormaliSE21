/**
  * This file is part of the KeY Java Card API Reference Implementation.
  *
  * Author: Wojciech Mostowski, woj@cs.ru.nl
  *   
  * For details and license see README and LICENSE files.
  *    
  * $Id: MessageDigestImpl.java,v 1.3 2007/07/09 12:51:57 woj Exp $
  */

package javacard.security;

import de.uka.ilkd.key.javacard.KeYJCSystem;

public class MessageDigestImpl extends MessageDigest {

    protected byte _algorithm = 0;
    /*@ invariant 
         _algorithm == ALG_MD5 || _algorithm == ALG_SHA || _algorithm == ALG_RIPEMD160;
      @*/

    protected byte _len = 0;
    /*@ invariant
         (_algorithm == ALG_MD5 ==> _len == 16) &&
	 (_algorithm == ALG_SHA ==> _len == 20) &&
	 (_algorithm == ALG_RIPEMD160 ==> _len == 20);
      @*/

    MessageDigestImpl(byte algorithm) throws CryptoException {
        if (algorithm != ALG_MD5 && algorithm != ALG_SHA
                && algorithm != ALG_RIPEMD160)
            CryptoException.throwIt(CryptoException.NO_SUCH_ALGORITHM);
        _algorithm = algorithm;
        if (algorithm == ALG_MD5)
            _len = 16;
        if (algorithm == ALG_SHA || algorithm == ALG_RIPEMD160)
            _len = 20;
    }

    public byte getAlgorithm() {
        return _algorithm;
    }

    public byte getLength() {
        return _len;
    }

    public void reset() {
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
