/**
  * This file is part of the KeY Java Card API Reference Implementation.
  *
  * Author: Wojciech Mostowski, woj@cs.ru.nl
  *   
  * For details and license see README and LICENSE files.
  *    
  * $Id: BasicService.java,v 1.2 2007/06/25 09:57:01 woj Exp $
  */

package javacard.framework.service;

import javacard.framework.APDU;
import javacard.framework.ISO7816;
import javacard.framework.Util;

public class BasicService implements Service {

    public BasicService() {
    }

    public boolean processDataIn(APDU apdu) {
        return false;
    }

    public boolean processCommand(APDU apdu) {
        return false;
    }

    public boolean processDataOut(APDU apdu) {
        return false;
    }

    public short receiveInData(APDU apdu) throws ServiceException {
        if (apdu.getCurrentState() != APDU.STATE_INITIAL
                && apdu.getCurrentState() != APDU.STATE_FULL_INCOMING)
            ServiceException.throwIt(ServiceException.CANNOT_ACCESS_IN_COMMAND);
        if (apdu.getCurrentState() == APDU.STATE_FULL_INCOMING)
            return (short) 0;
        short bytesRead = apdu.setIncomingAndReceive();
        if (apdu.getCurrentState() != APDU.STATE_FULL_INCOMING)
            ServiceException.throwIt(ServiceException.COMMAND_DATA_TOO_LONG);
        return bytesRead;
    }

    public void setProcessed(APDU apdu) throws ServiceException {
        if (apdu.getCurrentState() < APDU.STATE_INITIAL)
            ServiceException
                    .throwIt(ServiceException.CANNOT_ACCESS_OUT_COMMAND);
        if (apdu.getCurrentState() < APDU.STATE_OUTGOING)
            apdu.setOutgoing();
    }

    public boolean isProcessed(APDU apdu) {
        if (apdu.getCurrentState() < APDU.STATE_INITIAL
                || apdu.getCurrentState() >= APDU.STATE_OUTGOING)
            return true;
        return false;
    }

    public void setOutputLength(APDU apdu, short length)
            throws ServiceException {
        if (length < 0 || length > 256)
            ServiceException.throwIt(ServiceException.ILLEGAL_PARAM);
        apdu.getBuffer()[ISO7816.OFFSET_LC] = (byte) length;
    }

    public short getOutputLength(APDU apdu) throws ServiceException {
        if (apdu.getCurrentState() < APDU.STATE_OUTGOING)
            ServiceException
                    .throwIt(ServiceException.CANNOT_ACCESS_OUT_COMMAND);
        return (short) (apdu.getBuffer()[ISO7816.OFFSET_LC] & (byte) 0xff);
    }

    public void setStatusWord(APDU apdu, short sw) {
        Util.setShort(apdu.getBuffer(), ISO7816.OFFSET_P1, sw);
    }

    public short getStatusWord(APDU apdu) throws ServiceException {
        if (apdu.getCurrentState() < APDU.STATE_OUTGOING)
            ServiceException
                    .throwIt(ServiceException.CANNOT_ACCESS_OUT_COMMAND);
        return Util.getShort(apdu.getBuffer(), ISO7816.OFFSET_P1);
    }

    public boolean fail(APDU apdu, short sw) throws ServiceException {
        if (apdu.getCurrentState() < APDU.STATE_INITIAL)
            ServiceException
                    .throwIt(ServiceException.CANNOT_ACCESS_OUT_COMMAND);
        setProcessed(apdu);
        setOutputLength(apdu, (short) 0);
        setStatusWord(apdu, sw);
        return true;
    }

    public boolean succeed(APDU apdu) throws ServiceException {
        if (apdu.getCurrentState() < APDU.STATE_INITIAL)
            ServiceException
                    .throwIt(ServiceException.CANNOT_ACCESS_OUT_COMMAND);
        setProcessed(apdu);
        setStatusWord(apdu, ISO7816.SW_NO_ERROR);
        return true;
    }

    public boolean succeedWithStatusWord(APDU apdu, short sw)
            throws ServiceException {
        if (apdu.getCurrentState() < APDU.STATE_INITIAL)
            ServiceException
                    .throwIt(ServiceException.CANNOT_ACCESS_OUT_COMMAND);
        setProcessed(apdu);
        setStatusWord(apdu, sw);
        return true;
    }

    public byte getCLA(APDU apdu) {
        return apdu.getBuffer()[ISO7816.OFFSET_CLA];
    }

    public byte getINS(APDU apdu) {
        return apdu.getBuffer()[ISO7816.OFFSET_INS];
    }

    public byte getP1(APDU apdu) throws ServiceException {
        if (apdu.getCurrentState() != APDU.STATE_INITIAL
                && apdu.getCurrentState() != APDU.STATE_FULL_INCOMING)
            ServiceException.throwIt(ServiceException.CANNOT_ACCESS_IN_COMMAND);
        return apdu.getBuffer()[ISO7816.OFFSET_P1];
    }

    public byte getP2(APDU apdu) throws ServiceException {
        if (apdu.getCurrentState() != APDU.STATE_INITIAL
                && apdu.getCurrentState() != APDU.STATE_FULL_INCOMING)
            ServiceException.throwIt(ServiceException.CANNOT_ACCESS_IN_COMMAND);
        return apdu.getBuffer()[ISO7816.OFFSET_P2];
    }

    public boolean selectingApplet() {
        try {
            return (getINS(APDU.getCurrentAPDU()) == ISO7816.INS_SELECT);
        } catch (SecurityException se) {
            return false;
        }
    }

}
