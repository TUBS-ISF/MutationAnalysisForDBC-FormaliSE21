/**
 * This file is part of the KeY Java Card API Reference Implementation.
 *
 * Author: Wojciech Mostowski, woj@cs.ru.nl
 *   
 * For details and license see README and LICENSE files.
 *    
 * $Id: JCSystem.java,v 1.5 2007/07/03 10:07:47 woj Exp $
 */

package javacard.framework;

import de.uka.ilkd.key.javacard.KeYJCSystem;

/**
 * Some of the methods in this class should not have any spec. This is because
 * it is better to execute them symbolically (inline the implementation) in a
 * KeY proof and let dedicated taclets for jvm* methods take care of the rest.
 * Such no spec methods are marked with "no spec"
 */
public final class JCSystem {

    public static final byte NOT_A_TRANSIENT_OBJECT = (byte) 0;

    public static final byte CLEAR_ON_RESET = (byte) 1;

    public static final byte CLEAR_ON_DESELECT = (byte) 2;

    public static final byte MEMORY_TYPE_PERSISTENT = (byte) 0;

    public static final byte MEMORY_TYPE_TRANSIENT_RESET = (byte) 1;

    public static final byte MEMORY_TYPE_TRANSIENT_DESELECT = (byte) 2;

    private static final short API_VERSION = (short) 0x0202;

    private static byte _transactionDepth;

    // no spec
    public static /*@pure@*/ byte isTransient(Object theObj) {
        if (theObj == null)
            return NOT_A_TRANSIENT_OBJECT;
        return KeYJCSystem.jvmIsTransient(theObj);
    }

    // no spec
    public static boolean[] makeTransientBooleanArray(short length, byte event)
            throws SystemException, NegativeArraySizeException {
        if (event != CLEAR_ON_RESET && event != CLEAR_ON_DESELECT)
            SystemException.throwIt(SystemException.ILLEGAL_VALUE);
        if (event == CLEAR_ON_DESELECT
                && KeYJCSystem
                        .jvmGetContext(KeYJCSystem.selectedApplets[KeYJCSystem.currentChannel]) != KeYJCSystem
                        .jvmGetContext(KeYJCSystem
                                .jvmGetOwner(KeYJCSystem.currentActiveObject)))
            SystemException.throwIt(SystemException.ILLEGAL_TRANSIENT);
        if (length < 0)
            throw KeYJCSystem.nase;
        boolean[] result = KeYJCSystem.jvmMakeTransientBooleanArray(length,
                event);
        KeYJCSystem.setJavaOwner(result, null);
        return result;
    }

    // no spec
    public static byte[] makeTransientByteArray(short length, byte event)
            throws SystemException, NegativeArraySizeException {
        if (event != CLEAR_ON_RESET && event != CLEAR_ON_DESELECT)
            SystemException.throwIt(SystemException.ILLEGAL_VALUE);
        if (event == CLEAR_ON_DESELECT
                && KeYJCSystem
                        .jvmGetContext(KeYJCSystem.selectedApplets[KeYJCSystem.currentChannel]) != KeYJCSystem
                        .jvmGetContext(KeYJCSystem
                                .jvmGetOwner(KeYJCSystem.currentActiveObject)))
            SystemException.throwIt(SystemException.ILLEGAL_TRANSIENT);
        if (length < 0)
            throw KeYJCSystem.nase;
        byte[] result = KeYJCSystem.jvmMakeTransientByteArray(length, event);
        KeYJCSystem.setJavaOwner(result, null);
        return result;
    }

    // no spec
    public static short[] makeTransientShortArray(short length, byte event)
            throws SystemException, NegativeArraySizeException {
        if (event != CLEAR_ON_RESET && event != CLEAR_ON_DESELECT)
            SystemException.throwIt(SystemException.ILLEGAL_VALUE);
        if (event == CLEAR_ON_DESELECT
                && KeYJCSystem
                        .jvmGetContext(KeYJCSystem.selectedApplets[KeYJCSystem.currentChannel]) != KeYJCSystem
                        .jvmGetContext(KeYJCSystem
                                .jvmGetOwner(KeYJCSystem.currentActiveObject)))
            SystemException.throwIt(SystemException.ILLEGAL_TRANSIENT);
        if (length < 0)
            throw KeYJCSystem.nase;
        short[] result = KeYJCSystem.jvmMakeTransientShortArray(length, event);
        KeYJCSystem.setJavaOwner(result, null);
        return result;
    }

    // no spec
    public static Object[] makeTransientObjectArray(short length, byte event)
            throws SystemException, NegativeArraySizeException {
        if (event != CLEAR_ON_RESET && event != CLEAR_ON_DESELECT)
            SystemException.throwIt(SystemException.ILLEGAL_VALUE);
        if (event == CLEAR_ON_DESELECT
                && KeYJCSystem
                        .jvmGetContext(KeYJCSystem.selectedApplets[KeYJCSystem.currentChannel]) != KeYJCSystem
                        .jvmGetContext(KeYJCSystem
                                .jvmGetOwner(KeYJCSystem.currentActiveObject)))
            SystemException.throwIt(SystemException.ILLEGAL_TRANSIENT);
        if (length < 0)
            throw KeYJCSystem.nase;
        Object[] result = KeYJCSystem
                .jvmMakeTransientObjectArray(length, event);
        KeYJCSystem.setJavaOwner(result, null);
        return result;
    }

    public static short getVersion() {
        return API_VERSION;
    }

    // no spec
    public static void beginTransaction() throws TransactionException {
        if (_transactionDepth != 0)
            TransactionException.throwIt(TransactionException.IN_PROGRESS);
        _transactionDepth++;
        KeYJCSystem.jvmBeginTransaction();
    }

    // no spec
    public static void abortTransaction() throws TransactionException {
        if (_transactionDepth == 0)
            TransactionException.throwIt(TransactionException.NOT_IN_PROGRESS);
        KeYJCSystem.jvmAbortTransaction();
        _transactionDepth--;
    }

    // no spec
    public static void commitTransaction() throws TransactionException {
        if (_transactionDepth == 0)
            TransactionException.throwIt(TransactionException.NOT_IN_PROGRESS);
        KeYJCSystem.jvmCommitTransaction();
        _transactionDepth--;
    }

    public static byte getTransactionDepth() {
        return _transactionDepth;
    }

    public static boolean isObjectDeletionSupported() {
        return KeYJCSystem._jvmObjectDeletionSupported;
    }

    public static void requestObjectDeletion() throws SystemException {
        if (!KeYJCSystem._jvmObjectDeletionSupported)
            SystemException.throwIt(SystemException.ILLEGAL_USE);
    }

    public static AID getAID() {
        Object currOwner = KeYJCSystem
                .jvmGetOwner(KeYJCSystem.currentActiveObject);
        if (!(currOwner instanceof Applet))
            return null;
        short i = KeYJCSystem.getInstance().findApplet((Applet) currOwner);
        if (i == -1)
            return null; // This should not happen normally
        return KeYJCSystem.getInstance().getAID(i);
    }

    public static AID lookupAID(byte[] buffer, short offset, byte length) {
        short i = KeYJCSystem.getInstance().findAID(buffer, offset, length);
        if (i == -1)
            return null;
        if (KeYJCSystem.getInstance().getApplet(i) == null)
            return null;
        return KeYJCSystem.getInstance().getAID(i);
    }

    public static byte getAssignedChannel() {
        return KeYJCSystem.currentChannel;
    }

    public static AID getPreviousContextAID() {
        Object tmp = KeYJCSystem.jvmGetOwner(KeYJCSystem.previousContextObject);
        if (tmp == null)
            return null;
        if (!(tmp instanceof Applet))
            return null;
        short i = KeYJCSystem.getInstance().findApplet((Applet) tmp);
        if (i == -1)
            return null; // This should not happen normally
        return KeYJCSystem.getInstance().getAID(i);
    }

    public static short getAvailableMemory(byte memoryType)
            throws SystemException {
        if (memoryType != MEMORY_TYPE_PERSISTENT
                && memoryType != MEMORY_TYPE_TRANSIENT_RESET
                && memoryType != MEMORY_TYPE_TRANSIENT_DESELECT)
            SystemException.throwIt(SystemException.ILLEGAL_VALUE);
        if (memoryType == MEMORY_TYPE_PERSISTENT)
            return KeYJCSystem._jvmFreeMemoryPersistent;
        else
            return KeYJCSystem._jvmFreeMemoryTransient;
    }

    public static short getUnusedCommitCapacity() {
        return KeYJCSystem._jvmFreeCommitCapacity;
    }

    public static short getMaxCommitCapacity() {
        return KeYJCSystem._jvmMaxCommitCapacity;
    }

    public static boolean isAppletActive(AID theApplet) {
        return KeYJCSystem.isAppletActive(theApplet);
    }

    public static Shareable getAppletShareableInterfaceObject(AID serverAID,
            byte parameter)
    // throws SecurityException
    {
        return KeYJCSystem.getInstance().getShareable(getAID(), serverAID,
                parameter);
    }

}
