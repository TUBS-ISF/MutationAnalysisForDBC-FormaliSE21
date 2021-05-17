/************************************************************************
 * The Mondex Case Study - The KeY Approach
 * javacard api reference implementation
 * author: Dr. Wojciech Mostowski (W.Mostowski@cs.ru.nl)
 * (adapted by Dr. Isabel Tonin - tonin@ira.uka.de)
 * Universität Karlsruhe - Institut für Theoretische Informatik
 * http://key-project.org/
 */

package de.uka.ilkd.key.javacard;

/** This class contains methods specific to KeY model of the Java Card
  * API (2.1.1 & 2.2.1). These methods do not have any specs as they
  * should be always symbolically exected in the proof with dedicated
  * taclets. No contracts should be used for these methods (especially
  * the jvm*Transaction methods).
  */
public class KeYJCSystem {

    public static native byte jvmIsTransient(Object theObj);

    public static native boolean[] jvmMakeTransientBooleanArray(
      short length, byte event) ;

    public static native byte[] jvmMakeTransientByteArray(
      short length, byte event) ;

    public static native short[] jvmMakeTransientShortArray(
      short length, byte event) ;

    public static native Object[] jvmMakeTransientObjectArray(
      short length, byte event) ;
    
    public final static native void jvmBeginTransaction();

    public static native void jvmAbortTransaction();

    public static native void jvmCommitTransaction();

    public static native void jvmSuspendTransaction();

    public static native void jvmResumeTransaction();

    public static native void jvmArrayCopy(
      byte[] src, short srcOff, byte[] dest, short destOff, short  length);
    
    public static native void jvmArrayCopyNonAtomic(
      byte[] src, short srcOff, byte[] dest, short destOff, short  length);

    public static native void jvmArrayFillNonAtomic(
      byte[] bArray, short bOff, short bLen, byte  bValue);

    public static native byte jvmArrayCompare(
      byte[] src, short srcOff, byte[] dest, short destOff, short  length);

    public static native short jvmMakeShort(byte b1, byte b2);

    public static native short jvmSetShort(
      byte[] bArray, short  bOff, short  sValue);

    /** Assume there is always free memory. This may change in future
      * versions of KeY (Java Card memory consumption modeling).
      */
    public static final short _jvmFreeMemoryPersistent = (short)32767;
    public static final short _jvmFreeMemoryTransient  = (short)32767;
    public static final short _jvmMaxCommitCapacity    = (short)32767;
    public static final short _jvmFreeCommitCapacity   = (short)32767;

    /** Assume object deletion is supported */
    public static final boolean _jvmObjectDeletionSupported = true;

    /** Unspecified boolean, sometimes useful for ref. implementation. */
    public static boolean _unspecifiedBoolean;

}
