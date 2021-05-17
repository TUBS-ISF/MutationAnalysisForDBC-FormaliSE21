/**
  * This file is part of the KeY Java Card API Reference Implementation.
  *
  * Author: Wojciech Mostowski, woj@cs.ru.nl
  *   
  * For details and license see README and LICENSE files.
  *    
  * $Id: CipherImplExt.java,v 1.2 2007/06/25 09:57:01 woj Exp $
  */

package javacardx.crypto;

import javacard.framework.Shareable;
import javacard.security.CryptoException;

public class CipherImplExt extends CipherImpl implements Shareable {
    CipherImplExt(byte algorithm) throws CryptoException {
        super(algorithm);
    }
}
