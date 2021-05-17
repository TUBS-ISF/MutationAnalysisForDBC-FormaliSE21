/**
  * This file is part of the KeY Java Card API Reference Implementation.
  *
  * Author: Wojciech Mostowski, woj@cs.ru.nl
  *   
  * For details and license see README and LICENSE files.
  *    
  * $Id: KeYJCSystem.java,v 1.7 2007/07/04 09:30:24 woj Exp $
  */

package de.uka.ilkd.key.javacard;

/**
 * This class contains methods specific to KeY model of the Java Card API (2.1.1 &
 * 2.2.1). Some methods should not have any specs as they should be always
 * symbolically exected in the proof with dedicated taclets. No contracts should
 * be used for these methods (especially the jvm*Transaction methods).
 */
public class KeYJCSystem {

    private static KeYJCSystem _instance = new KeYJCSystem();

    public static native byte jvmIsTransient(Object theObj);

    public static native boolean[] jvmMakeTransientBooleanArray(short length,
            byte event);

    public static native byte[] jvmMakeTransientByteArray(short length,
            byte event);

    public static native short[] jvmMakeTransientShortArray(short length,
            byte event);

    public static native Object[] jvmMakeTransientObjectArray(short length,
            byte event);

    public static native void jvmBeginTransaction();

    public static native void jvmAbortTransaction();

    public static native void jvmCommitTransaction();

    public static native void jvmSuspendTransaction();

    public static native void jvmResumeTransaction();

    public static native void jvmArrayCopy(byte[] src, short srcOff,
            byte[] dest, short destOff, short length);

    public static native void jvmArrayCopyNonAtomic(byte[] src, short srcOff,
            byte[] dest, short destOff, short length);

    public static native void jvmArrayFillNonAtomic(byte[] bArray, short bOff,
            short bLen, byte bValue);

    public static native byte jvmArrayCompare(byte[] src, short srcOff,
            byte[] dest, short destOff, short length);

    public static native short jvmMakeShort(byte b1, byte b2);

    public static native short jvmSetShort(byte[] bArray, short bOff,
            short sValue);

    /**
     * Assume there is always free memory. This may change in future versions of
     * KeY (Java Card memory consumption modeling).
     */
    public static final short _jvmFreeMemoryPersistent = (short) 32767;

    public static final short _jvmFreeMemoryTransient = (short) 32767;

    public static final short _jvmMaxCommitCapacity = (short) 32767;

    public static final short _jvmFreeCommitCapacity = (short) 32767;

    /** Assume object deletion is supported */
    public static final boolean _jvmObjectDeletionSupported = true;

    public static final short P_TEP_OBJECT = 1;

    public static final short P_PEP_OBJECT = 2;

    public static final short P_GLOBAL_ARRAY = 3;

    public static final short P_IMPLICIT_SHAREABLE = 4;

    public static KeYJCSystem getInstance() {
        return _instance;
    }

    private KeYJCSystem() {
        currentActiveObject = this;
	previousActiveObject = null;
	previousContextObject = null;
	jvmSetOwner(this, this);
        jvmSetContext(this, (short) 0);
        jvmSetPrivs(this, P_TEP_OBJECT);
	appletTable = new javacard.framework.Applet[MAX_APPLETS];
	setJavaOwner(appletTable, this);
	aidTable = new javacard.framework.AID[MAX_APPLETS];
	setJavaOwner(aidTable, this);
	selectedApplets  = new Object[MAX_CHANNELS];
	currentChannel = 0;
        npe = new NullPointerException();
	jvmSetPrivs(npe, P_TEP_OBJECT);
        aioobe = new ArrayIndexOutOfBoundsException();
        jvmSetPrivs(aioobe, P_TEP_OBJECT);
        nase = new NegativeArraySizeException();
        jvmSetPrivs(nase, P_TEP_OBJECT);
        se = new SecurityException();
        jvmSetPrivs(se, P_TEP_OBJECT);
        Exception e = null;
        e = new javacard.framework.CardException((short) -1);
        jvmSetPrivs(e, P_TEP_OBJECT);
        e = new javacard.framework.CardRuntimeException((short) -1);
        jvmSetPrivs(e, P_TEP_OBJECT);
        e = new javacard.framework.ISOException((short) -1);
        jvmSetPrivs(e, P_TEP_OBJECT);
        e = new javacard.framework.APDUException((short) -1);
        jvmSetPrivs(e, P_TEP_OBJECT);
        e = new javacard.framework.UserException((short) -1);
        jvmSetPrivs(e, P_TEP_OBJECT);
        e = new javacard.framework.SystemException((short) -1);
        jvmSetPrivs(e, P_TEP_OBJECT);
        e = new javacard.framework.TransactionException((short) -1);
        jvmSetPrivs(e, P_TEP_OBJECT);
        e = new javacard.framework.PINException((short) -1);
        jvmSetPrivs(e, P_TEP_OBJECT);
        e = new javacard.framework.service.ServiceException((short) -1);
        jvmSetPrivs(e, P_TEP_OBJECT);
        e = new javacard.security.CryptoException((short) -1);
        jvmSetPrivs(e, P_TEP_OBJECT);
    }

    public static NullPointerException npe;

    public static ArrayIndexOutOfBoundsException aioobe;

    public static NegativeArraySizeException nase;

    public static SecurityException se;

    // sets obj.owner := owner
    public static void setJavaOwner(Object obj, Object owner) {
      //@ set obj.owner = owner;
    }

    // sets obj.<jcOwner> := owner
    // public static native void jvmSetOwner(Object obj, Object owner);
    public static void jvmSetOwner(Object obj, Object owner) {
    }

    // retruns obj.<jcOwner>
    // public static native Object jvmGetOwner(Object obj);
    public static Object jvmGetOwner(Object obj) {
        return obj;
    }

    // sets obj.<context> := context
    // public static native void jvmSetContext(Object obj, short context);
    public static void jvmSetContext(Object obj, short context) {
    }

    // returns obj.<context>
    // public static native short jvmGetContext(Object obj);
    public static short jvmGetContext(Object obj) {
        return 0;
    }

    // sets obj.<jcrePrivs> := priv
    // public static native void jvmSetPrivs(Object obj, short priv);
    public static void jvmSetPrivs(Object obj, short priv) {
    }

    // returns obj.<jcrePrivs>
    // public static native short jvmGetPrivs(Object obj);
    public static short jvmGetPrivs(Object obj) {
        return 0;
    }

    public static Object currentActiveObject;

    public static Object previousActiveObject;

    public static Object previousContextObject;

    public static final byte MAX_CHANNELS = (byte) 4;

    public static Object[] selectedApplets;

    public static byte currentChannel;

    private static final short MAX_APPLETS = 16;

    private javacard.framework.Applet[] appletTable;

    private javacard.framework.AID[] aidTable;

    public short findAID(javacard.framework.AID aid) {
        short i = 0;
        /*@
          loop_invariant i>=0 && i<=MAX_APPLETS;
          decreases MAX_APPLETS - i;
         @*/
        while (i < MAX_APPLETS) {
            if (aidTable[i] != null && aidTable[i].equals(aid))
                return i;
            i++;
        }
        return -1;
    }

    public short findAID(byte[] buffer, short offset, byte length) {
        short i = 0;
        /*@
          loop_invariant i>=0 && i<=MAX_APPLETS;
          decreases MAX_APPLETS - i;
         @*/
        while (i < MAX_APPLETS) {
            if (aidTable[i] != null
                    && aidTable[i].equals(buffer, offset, length))
                return i;
            i++;
        }
        return -1;
    }

    public short findApplet(javacard.framework.Applet applet) {
        short i = 0;
        /*@
          loop_invariant i>=0 && i<=MAX_APPLETS;
          decreases MAX_APPLETS - i;
         @*/
        while (i < MAX_APPLETS) {
            if (appletTable[i] == applet)
                return i;
            i++;
        }
        return -1;
    }

    public void setAID(javacard.framework.AID aid, short index) {
        aidTable[index] = aid;
    }

    public void setAID(byte[] bArray, short bOffset, byte bLen, short index) {
        aidTable[index] = new javacard.framework.AID(bArray, bOffset, bLen);
    }

    public void setApplet(javacard.framework.Applet applet, short index) {
        appletTable[index] = applet;
    }

    public javacard.framework.AID getAID(short index) {
        return aidTable[index];
    }

    public javacard.framework.Applet getApplet(short index) {
        return appletTable[index];
    }

    public short installSlot;

    public short getInstallSlot() {
        if (appletTable[installSlot] != null)
            return -1;
        return installSlot;
    }

    public static boolean isAppletActive(javacard.framework.AID aid) {
        if (aid == null)
            return false;
        final short index = getInstance().findAID(aid);
        if (index == -1)
            return false;
        final javacard.framework.Applet a = getInstance().getApplet(index);
        if (a == null)
            return false;
        short i = 0;
        /*@
           loop_invariant i>=0 && i<=MAX_CHANNELS;
           decreases MAX_CHANNELS - i;
         @*/
        while (i < MAX_CHANNELS) {
            if (selectedApplets[i] == a)
                return true;
            i++;
        }
        return false;
    }

    public javacard.framework.Shareable getShareable(
            javacard.framework.AID clientAID, javacard.framework.AID serverAID,
            byte param)
    // throws SecurityException
    {
        if (serverAID == null)
            return null;
        short i = findAID(serverAID);
        if (i == -1)
            return null;
        javacard.framework.Applet tmp = getApplet(i);
        if (tmp == null)
            return null;
        // The new Java Card 2.2.2 also requires this?!:
        /*
          if(!(tmp instanceof Multiselectable) && isAppletActive(serverAID))
          throw se;
         */
        Object res = tmp.getShareableInterfaceObject(clientAID, param);
        if (res instanceof javacard.framework.Shareable)
            return (javacard.framework.Shareable) res;
        return null;

    }

}
