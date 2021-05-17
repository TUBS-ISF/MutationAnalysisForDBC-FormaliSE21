/**
 * This file is part of the KeY Java Card API Reference Implementation.
 *
 * Author: Wojciech Mostowski, woj@cs.ru.nl
 *   
 * For details and license see README and LICENSE files.
 *    
 * $Id: Util.java,v 1.3 2007/07/03 10:07:47 woj Exp $
 */

package javacard.framework;

import de.uka.ilkd.key.javacard.KeYJCSystem;

/**
 * For some of the methods in this class it maybe sometimes better not to use
 * their contracts. This is because it is better to execute them symbolically
 * (inline the implementation) in a KeY proof and let dedicated taclets for jvm*
 * methods take care of the rest. Such no spec methods are marked with "no spec"
 */
public class Util {

    // no spec
    public static short arrayCopy(byte[] src, short srcOff, byte[] dest,
            short destOff, short length) throws TransactionException,
            NullPointerException, ArrayIndexOutOfBoundsException {
        if (src == null || dest == null)
            throw KeYJCSystem.npe;
        if (length < 0 || srcOff < 0 || destOff < 0
                || (short) (srcOff + length) > src.length
                || (short) (destOff + length) > dest.length)
            throw KeYJCSystem.aioobe;
        final boolean doTransaction = JCSystem.isTransient(dest) == JCSystem.NOT_A_TRANSIENT_OBJECT
                && JCSystem.getTransactionDepth() == (byte) 0;
        if (doTransaction)
            KeYJCSystem.jvmBeginTransaction();
        if (src == dest) {
            byte[] _tempArray = new byte[length];
            KeYJCSystem
                    .jvmArrayCopy(src, srcOff, _tempArray, (short) 0, length);
            KeYJCSystem.jvmArrayCopy(_tempArray, (short) 0, dest, destOff,
                    length);
        } else {
            KeYJCSystem.jvmArrayCopy(src, srcOff, dest, destOff, length);
        }
        if (doTransaction)
            KeYJCSystem.jvmCommitTransaction();
        return (short) (destOff + length);
    }

    // no spec
    public static short arrayCopyNonAtomic(byte[] src, short srcOff,
            byte[] dest, short destOff, short length)
            throws NullPointerException, ArrayIndexOutOfBoundsException {

        if (src == null || dest == null)
            throw KeYJCSystem.npe;
        if (length < 0 || srcOff < 0 || destOff < 0
                || srcOff + length > src.length
                || destOff + length > dest.length)
            throw KeYJCSystem.aioobe;
        if (src == dest) {
            byte[] _tempArray = new byte[length];
            KeYJCSystem
                    .jvmArrayCopy(src, srcOff, _tempArray, (short) 0, length);
            KeYJCSystem.jvmArrayCopyNonAtomic(_tempArray, (short) 0, dest,
                    destOff, length);
        } else {
            KeYJCSystem.jvmArrayCopyNonAtomic(src, srcOff, dest, destOff,
                    length);
        }
        return (short) (destOff + length);
    }

    // no spec
    public static short arrayFillNonAtomic(byte[] bArray, short bOff,
            short bLen, byte bValue) throws NullPointerException,
            ArrayIndexOutOfBoundsException {
        if (bArray == null)
            throw KeYJCSystem.npe;
        if (bLen < 0 || bOff < 0 || (short) (bOff + bLen) > bArray.length)
            throw KeYJCSystem.aioobe;
        KeYJCSystem.jvmArrayFillNonAtomic(bArray, bOff, bLen, bValue);
        return (short) (bOff + bLen);
    }

    public static byte arrayCompare(byte[] src, short srcOff, byte[] dest,
            short destOff, short length) throws NullPointerException,
            ArrayIndexOutOfBoundsException {
        if (src == null || dest == null)
            throw KeYJCSystem.npe;
        if (length < 0 || srcOff < 0 || destOff < 0
                || (short) (srcOff + length) > src.length
                || (short) (destOff + length) > dest.length)
            throw KeYJCSystem.aioobe;
        return KeYJCSystem.jvmArrayCompare(src, srcOff, dest, destOff, length);
    }

    // no spec
    public static short makeShort(byte b1, byte b2) {
        return KeYJCSystem.jvmMakeShort(b1, b2);
    }

    // no spec
    public static short getShort(byte[] bArray, short bOff)
            throws NullPointerException, ArrayIndexOutOfBoundsException {
        if (bArray == null)
            throw KeYJCSystem.npe;
        if (bOff < 0 || (short) (bOff + 1) >= bArray.length)
            throw KeYJCSystem.aioobe;
        return KeYJCSystem.jvmMakeShort(bArray[bOff], bArray[bOff + 1]);
    }

    // no spec
    public static short setShort(byte[] bArray, short bOff, short sValue)
            throws TransactionException, NullPointerException,
            ArrayIndexOutOfBoundsException {
        if (bArray == null)
            throw KeYJCSystem.npe;
        if (bOff < 0 || (short) (bOff + 1) >= bArray.length)
            throw KeYJCSystem.aioobe;
        final boolean doTransaction = JCSystem.isTransient(bArray) == JCSystem.NOT_A_TRANSIENT_OBJECT
                && JCSystem.getTransactionDepth() == (byte) 0;
        if (doTransaction)
            KeYJCSystem.jvmBeginTransaction();
        KeYJCSystem.jvmSetShort(bArray, bOff, sValue);
        if (doTransaction)
            KeYJCSystem.jvmCommitTransaction();
        return (short) (bOff + 2);
    }

}
