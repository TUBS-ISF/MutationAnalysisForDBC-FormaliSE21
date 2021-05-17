/**
  * This file is part of the KeY Java Card API Reference Implementation.
  *
  * Author: Wojciech Mostowski, woj@cs.ru.nl
  *   
  * For details and license see README and LICENSE files.
  *    
  * $Id: Applet.java,v 1.2 2007/06/25 09:57:01 woj Exp $
  */

package javacard.framework;

import de.uka.ilkd.key.javacard.KeYJCSystem;

abstract public class Applet {

    protected Applet() {
    }

    public static void install(byte[] bArray, short bOffset, byte bLength)
            throws ISOException {
        ISOException.throwIt(ISO7816.SW_FUNC_NOT_SUPPORTED);
    }

    public abstract void process(APDU apdu) throws ISOException;

    public boolean select() {
        return true;
    }

    public void deselect() {
    }

    public Shareable getShareableInterfaceObject(AID clientAID, byte parameter) {
        return null;
    }

    protected final void register(byte[] bArray, short bOffset, byte bLength)
            throws SystemException {
        if (bLength < 5 || bLength > 16)
            SystemException.throwIt(SystemException.ILLEGAL_VALUE);
        if (JCSystem.lookupAID(bArray, bOffset, bLength) != null)
            SystemException.throwIt(SystemException.ILLEGAL_AID);
        KeYJCSystem s = KeYJCSystem.getInstance();
        final short i = s.getInstallSlot();
        if (i == -1) // already registered
            SystemException.throwIt(SystemException.ILLEGAL_AID);
        s.setAID(bArray, bOffset, bLength, i);
        s.setApplet(this, i);
    }

    protected final void register() throws SystemException {
        KeYJCSystem s = KeYJCSystem.getInstance();
        final short i = s.getInstallSlot();
        if (i == -1)
            SystemException.throwIt(SystemException.ILLEGAL_AID);
        s.setApplet(this, i);
    }

    protected final boolean selectingApplet() {
        try {
            byte[] buf = APDU.getCurrentAPDUBuffer();
            return (buf[ISO7816.OFFSET_INS] == ISO7816.INS_SELECT);
        } catch (SecurityException se) {
            return false;
        }
    }
}
