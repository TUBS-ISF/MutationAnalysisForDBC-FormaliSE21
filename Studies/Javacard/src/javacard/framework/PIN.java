/**
 * This file is part of the KeY Java Card API Reference Implementation.
 *
 * Author: Wojciech Mostowski, woj@cs.ru.nl
 *   
 * For details and license see README and LICENSE files.
 *    
 * $Id: PIN.java,v 1.3 2007/07/03 10:07:47 woj Exp $
 */

package javacard.framework;

public interface PIN {

    public boolean isValidated();

    public byte getTriesRemaining();

    public boolean check(byte[] pin, short offset, byte length)
            throws NullPointerException, ArrayIndexOutOfBoundsException;

    public void reset();
}
