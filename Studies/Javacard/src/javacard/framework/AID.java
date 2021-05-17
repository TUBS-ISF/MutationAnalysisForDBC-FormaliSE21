/**
 * This file is part of the KeY Java Card API Reference Implementation.
 *
 * Author: Wojciech Mostowski, woj@cs.ru.nl
 *   
 * For details and license see README and LICENSE files.
 *    
 * $Id: AID.java,v 1.7 2007/07/09 12:51:57 woj Exp $
 */

package javacard.framework;

import de.uka.ilkd.key.javacard.KeYJCSystem;

public class AID {

    private /*@spec_public@*/ byte[] _theAID;
    /*@ invariant
          _theAID != null &&
	  JCSystem.isTransient(_theAID) == JCSystem.NOT_A_TRANSIENT_OBJECT &&
	  _theAID.length >= 5 && _theAID.length <= 16 &&
	  ((java.lang.Object)_theAID).owner == this;
      @*/

    public AID(byte[] bArray, short offset, byte length)
            throws SystemException, SecurityException, NullPointerException,
            ArrayIndexOutOfBoundsException {
        if (length < 5 || length > 16)
            SystemException.throwIt(SystemException.ILLEGAL_VALUE);
        if (bArray == null)
            throw KeYJCSystem.npe;
        if (length < 0 || offset < 0
                || (short) (offset + length) > bArray.length)
            throw KeYJCSystem.aioobe;
        _theAID = new byte[length];
        KeYJCSystem.setJavaOwner(_theAID, this);
        Util.arrayCopy(bArray, offset, _theAID, (short) 0, length);
        KeYJCSystem.jvmSetOwner(this, KeYJCSystem.getInstance());
        KeYJCSystem.jvmSetPrivs(this, KeYJCSystem.P_PEP_OBJECT);
    }

    public final byte getBytes(byte[] dest, short offset)
            throws SecurityException, NullPointerException,
            ArrayIndexOutOfBoundsException {
        if (dest == null)
            throw KeYJCSystem.npe;
        if (KeYJCSystem.jvmGetContext(KeYJCSystem.jvmGetOwner(dest)) != KeYJCSystem
                .jvmGetContext(KeYJCSystem
                        .jvmGetOwner(KeYJCSystem.previousActiveObject))
                && KeYJCSystem.jvmGetPrivs(dest) != KeYJCSystem.P_GLOBAL_ARRAY)
            throw KeYJCSystem.se;
        if (offset < 0 || offset + _theAID.length > dest.length)
            throw KeYJCSystem.aioobe;
        Util
                .arrayCopy(_theAID, (short) 0, dest, offset,
                        (short) _theAID.length);
        return (byte) _theAID.length;
    }

    public final boolean equals(/*@nullable@*/Object anObject) throws SecurityException {
        if (KeYJCSystem.jvmGetContext(KeYJCSystem.jvmGetOwner(anObject)) != KeYJCSystem
                .jvmGetContext(KeYJCSystem
                        .jvmGetOwner(KeYJCSystem.previousActiveObject))
                && KeYJCSystem.jvmGetPrivs(anObject) != KeYJCSystem.P_GLOBAL_ARRAY)
            throw KeYJCSystem.se;
        if (anObject == null || !(anObject instanceof AID)
                || ((AID) anObject)._theAID.length != _theAID.length)
            return false;
        return (Util.arrayCompare(((AID) anObject)._theAID, (short) 0, _theAID,
                (short) 0, (short) _theAID.length) == 0);
    }

    public final boolean equals(byte[] bArray, short offset, byte length)
            throws SecurityException, ArrayIndexOutOfBoundsException {
        if (bArray == null)
            throw KeYJCSystem.npe;
        if (KeYJCSystem.jvmGetContext(KeYJCSystem.jvmGetOwner(bArray)) != KeYJCSystem
                .jvmGetContext(KeYJCSystem
                        .jvmGetOwner(KeYJCSystem.previousActiveObject))
                && KeYJCSystem.jvmGetPrivs(bArray) != KeYJCSystem.P_GLOBAL_ARRAY)
            throw KeYJCSystem.se;
        if (length < 0 || offset < 0
                || (short) (offset + length) > bArray.length)
            throw KeYJCSystem.aioobe;
        if (length != _theAID.length)
            return false;
        return Util.arrayCompare(bArray, offset, _theAID, (short) 0, length) == 0;
    }

    public final boolean partialEquals(byte[] bArray, short offset, byte length)
            throws SecurityException, ArrayIndexOutOfBoundsException {
        if (bArray == null)
            return false;
        if (KeYJCSystem.jvmGetContext(KeYJCSystem.jvmGetOwner(bArray)) != KeYJCSystem
                .jvmGetContext(KeYJCSystem
                        .jvmGetOwner(KeYJCSystem.previousActiveObject))
                && KeYJCSystem.jvmGetPrivs(bArray) != KeYJCSystem.P_GLOBAL_ARRAY)
            throw KeYJCSystem.se;
        if (length < 0 || offset < 0
                || (short) (offset + length) > bArray.length)
            throw KeYJCSystem.aioobe;
        if (length > _theAID.length)
            return false;
        return Util.arrayCompare(bArray, offset, _theAID, (short) 0, length) == 0;
    }

    public final boolean RIDEquals(AID otherAID) throws SecurityException {
        if (otherAID == null)
            return false;
        if (KeYJCSystem.jvmGetContext(KeYJCSystem.jvmGetOwner(otherAID)) != KeYJCSystem
                .jvmGetContext(KeYJCSystem
                        .jvmGetOwner(KeYJCSystem.previousActiveObject))
                && KeYJCSystem.jvmGetPrivs(otherAID) != KeYJCSystem.P_GLOBAL_ARRAY)
            throw KeYJCSystem.se;
        return Util.arrayCompare(_theAID, (short) 0, otherAID._theAID,
                (short) 0, (short) 5) == 0;

    }

    public final byte getPartialBytes(short aidOffset, byte[] dest,
            short oOffset, byte oLength) throws SecurityException,
            NullPointerException, ArrayIndexOutOfBoundsException {
        if (dest == null)
            throw KeYJCSystem.npe;
        if (KeYJCSystem.jvmGetContext(KeYJCSystem.jvmGetOwner(dest)) != KeYJCSystem
                .jvmGetContext(KeYJCSystem
                        .jvmGetOwner(KeYJCSystem.previousActiveObject))
                && KeYJCSystem.jvmGetPrivs(dest) != KeYJCSystem.P_GLOBAL_ARRAY)
            throw KeYJCSystem.se;
        if (oLength < 0 || aidOffset < 0 || aidOffset > _theAID.length
                || oOffset < 0)
            throw KeYJCSystem.aioobe;
        short i = (oLength == 0) ? (short) (_theAID.length - aidOffset)
                : oLength;
        if ((short) (aidOffset + i) > _theAID.length
                || (short) (oOffset + i) > dest.length)
            throw KeYJCSystem.aioobe;
        Util.arrayCopy(_theAID, aidOffset, dest, oOffset, i);
        return (byte) i;
    }
}
