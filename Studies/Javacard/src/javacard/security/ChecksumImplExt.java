/**
  * This file is part of the KeY Java Card API Reference Implementation.
  *
  * Author: Wojciech Mostowski, woj@cs.ru.nl
  *   
  * For details and license see README and LICENSE files.
  *    
  * $Id: ChecksumImplExt.java,v 1.2 2007/06/25 09:57:01 woj Exp $
  */

package javacard.security;

import javacard.framework.Shareable;

public class ChecksumImplExt extends ChecksumImpl implements Shareable {
    ChecksumImplExt(byte algorithm) throws CryptoException {
        super(algorithm);
    }
}
