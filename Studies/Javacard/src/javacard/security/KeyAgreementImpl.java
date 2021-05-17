/**
  * This file is part of the KeY Java Card API Reference Implementation.
  *
  * Author: Wojciech Mostowski, woj@cs.ru.nl
  *   
  * For details and license see README and LICENSE files.
  *    
  * $Id: KeyAgreementImpl.java,v 1.2 2007/06/25 09:57:01 woj Exp $
  */

package javacard.security;

import de.uka.ilkd.key.javacard.KeYJCSystem;

public class KeyAgreementImpl extends KeyAgreement {

    protected byte _algorithm = 0;

    protected PrivateKey _privateKey = null;

    KeyAgreementImpl(byte algorithm) throws CryptoException {
        if (algorithm != ALG_EC_SVDP_DH && algorithm != ALG_EC_SVDP_DHC)
            CryptoException.throwIt(CryptoException.NO_SUCH_ALGORITHM);
        _algorithm = algorithm;
    }

    public byte getAlgorithm() {
        return _algorithm;
    }

    public void init(PrivateKey privateKey) throws CryptoException {

        if (privateKey.getType() != KeyBuilder.TYPE_EC_F2M_PRIVATE
                && privateKey.getType() != KeyBuilder.TYPE_EC_FP_PRIVATE)
            CryptoException.throwIt(CryptoException.ILLEGAL_VALUE);

        if (!privateKey.isInitialized()
                || (_algorithm == ALG_EC_SVDP_DHC && !((ECPrivateKeyImpl) privateKey)._kSet))
            CryptoException.throwIt(CryptoException.UNINITIALIZED_KEY);

        _privateKey = privateKey;
    }

    public short generateSecret(byte[] publicData, short publicOffset,
            short publicLength, byte[] secretData, short secretOffset) {

        if (_privateKey == null)
            CryptoException.throwIt(CryptoException.INVALID_INIT);

        if (publicData == null)
            throw KeYJCSystem.npe;

        if (publicLength != ((ECKeyImpl) _privateKey)._size2)
            CryptoException.throwIt(CryptoException.ILLEGAL_VALUE);

        if (publicOffset < 0
                || (short) (publicOffset + publicLength) > publicData.length)
            throw KeYJCSystem.aioobe;
        if (secretData == null)
            throw KeYJCSystem.npe;

        short result = (short) (_privateKey.getSize() / 8);
        if (secretOffset < 0 || secretOffset + result > secretData.length)
            throw KeYJCSystem.aioobe;
        return result;
    }

}
