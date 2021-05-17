/**
  * This file is part of the KeY Java Card API Reference Implementation.
  *
  * Author: Wojciech Mostowski, woj@cs.ru.nl
  *   
  * For details and license see README and LICENSE files.
  *    
  * $Id: RSAPublicKeyImplEnc.java,v 1.4 2007/08/14 10:30:35 woj Exp $
  */

package javacard.security;

import javacardx.crypto.Cipher;
import javacardx.crypto.KeyEncryption;

public class RSAPublicKeyImplEnc extends RSAPublicKeyImpl implements KeyEncryption {

    RSAPublicKeyImplEnc(byte type, short size) throws CryptoException {
        super(type, size);
    }

    public Cipher getKeyCipher() {
        return super.getKeyCipher();
    }

    public void setKeyCipher(Cipher cipher) {
        super.setKeyCipher(cipher);
    }

}
