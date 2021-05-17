/**
  * This file is part of the KeY Java Card API Reference Implementation.
  *
  * Author: Wojciech Mostowski, woj@cs.ru.nl
  *   
  * For details and license see README and LICENSE files.
  *    
  * $Id: RandomDataImpl.java,v 1.2 2007/06/25 09:57:01 woj Exp $
  */

package javacard.security;

import de.uka.ilkd.key.javacard.KeYJCSystem;

public class RandomDataImpl extends RandomData {

    protected byte _algorithm = 0;

    RandomDataImpl(byte algorithm) throws CryptoException {
        if (algorithm != ALG_PSEUDO_RANDOM && algorithm != ALG_SECURE_RANDOM)
            CryptoException.throwIt(CryptoException.NO_SUCH_ALGORITHM);
        _algorithm = algorithm;
    }

    public byte getAlgorithm() {
        return _algorithm;
    }

    public void setSeed(byte[] buffer, short offset, short length) {

        if (buffer == null)
            throw KeYJCSystem.npe;
        if (offset < 0 || length < 0
                || (short) (offset + length) > buffer.length)
            throw KeYJCSystem.aioobe;
    }

    public void generateData(byte[] buffer, short offset, short length)
            throws CryptoException {

        if (length < 0)
            CryptoException.throwIt(CryptoException.ILLEGAL_VALUE);
        if (buffer == null)
            throw KeYJCSystem.npe;
        if (offset < 0 || (short) (offset + length) > buffer.length)
            throw KeYJCSystem.aioobe;
    }

}
