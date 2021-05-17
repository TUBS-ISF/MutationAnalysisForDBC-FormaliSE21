/**
  * This file is part of the KeY Java Card API Reference Implementation.
  *
  * Author: Wojciech Mostowski, woj@cs.ru.nl
  *   
  * For details and license see README and LICENSE files.
  *    
  * $Id: ECPrivateKeyImplEnc.java,v 1.2 2007/06/25 09:57:01 woj Exp $
  */

package javacard.security;

import javacardx.crypto.Cipher;
import javacardx.crypto.KeyEncryption;

public class ECPrivateKeyImplEnc extends ECPrivateKeyImpl implements
        KeyEncryption {

    protected ECPrivateKeyImplEnc(byte type, short size) throws CryptoException {
        super(type, size);
    }

    public Cipher getKeyCipher() {
        return super.getKeyCipher();
    }

    public void setKeyCipher(Cipher cipher) {
        super.setKeyCipher(cipher);
    }

}
