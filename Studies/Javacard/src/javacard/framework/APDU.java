/**
  * This file is part of the KeY Java Card API Reference Implementation.
  *
  * Author: Wojciech Mostowski, woj@cs.ru.nl
  *   
  * For details and license see README and LICENSE files.
  *    
  * $Id: APDU.java,v 1.2 2007/06/25 09:57:01 woj Exp $
  */

package javacard.framework;

import de.uka.ilkd.key.javacard.KeYJCSystem;

public final class APDU {

    private short _Lc;

    private short _Lr;

    private short _Le;

    /**
     * Most cards have an APDU buffer of size 261, the spec says it has to be at
     * least 37
     */
    private final static short BUFFER_LENGTH = (short) 261;

    private byte[] _buffer;

    private byte _apduState;

    private static APDU _instance;

    private static byte _tprotocol;

    private static short _t1inBlockSize;

    private static short _t1outBlockSize;

    private static byte _t1NAD;

    public static final byte PROTOCOL_MEDIA_CONTACTLESS_TYPE_A = (byte) 0xff;

    public static final byte PROTOCOL_MEDIA_CONTACTLESS_TYPE_B = (byte) 0xef;

    public static final byte PROTOCOL_MEDIA_DEFAULT = (byte) 0;

    public static final byte PROTOCOL_MEDIA_MASK = (byte) 0x8f;

    public static final byte PROTOCOL_MEDIA_USB = (byte) 0xdf;

    public static final byte PROTOCOL_T0 = (byte) 0;

    public static final byte PROTOCOL_T1 = (byte) 1;

    public static final byte PROTOCOL_TYPE_MASK = (byte) 0x0f;

    public static final byte STATE_ERROR_NO_T0_GETRESPONSE = (byte) 0x80;

    public static final byte STATE_ERROR_T1_IFD_ABORT = (byte) 0x81;

    public static final byte STATE_ERROR_IO = (byte) 0x82;

    public static final byte STATE_ERROR_NO_T0_REISSUE = (byte) 0x83;

    public static final byte STATE_INITIAL = (byte) 0;

    public static final byte STATE_PARTIAL_INCOMING = (byte) 1;

    public static final byte STATE_FULL_INCOMING = (byte) 2;

    public static final byte STATE_OUTGOING = (byte) 3;

    public static final byte STATE_OUTGOING_LENGTH_KNOWN = (byte) 4;

    public static final byte STATE_PARTIAL_OUTGOING = (byte) 5;

    public static final byte STATE_FULL_OUTGOING = (byte) 6;

    public byte[] getBuffer() {
        return _buffer;
    }

    public static short getInBlockSize() {
        if (_tprotocol == PROTOCOL_T0)
            return (short) 1;
        else
            return _t1inBlockSize;
    }

    public static short getOutBlockSize() {
        if (_tprotocol == PROTOCOL_T0)
            return (short) 258;
        else
            return _t1outBlockSize;
    }

    public static byte getProtocol() {
        return _tprotocol;
    }

    public byte getNAD() {
        if (_tprotocol == PROTOCOL_T0)
            return (byte) 0;
        else
            return _t1NAD;
    }

    public byte getCurrentState() {
        return _apduState;
    }

    public short setOutgoing() throws APDUException {
        if (_apduState >= STATE_OUTGOING) {
            APDUException.throwIt(APDUException.ILLEGAL_USE);
        }
        _apduState = STATE_OUTGOING;
        if (_tprotocol == PROTOCOL_T0)
            return (short) 256;
        else
            return _Le;
    }

    public short setOutgoingNoChaining() throws APDUException {
        return setOutgoing();
    }

    public void setOutgoingLength(short len) throws APDUException {
        if (_apduState != STATE_OUTGOING) {
            APDUException.throwIt(APDUException.ILLEGAL_USE);
        }
        if (len < 0 | len > 256) {
            APDUException.throwIt(APDUException.BAD_LENGTH);
        }
        _Lr = len;
        _apduState = STATE_OUTGOING_LENGTH_KNOWN;
    }

    public short setIncomingAndReceive() throws APDUException {
        if (_apduState != STATE_INITIAL) {
            APDUException.throwIt(APDUException.ILLEGAL_USE);
        }
        if (_Lc <= (short) (BUFFER_LENGTH - ISO7816.OFFSET_LC - 1)) {
            _apduState = STATE_FULL_INCOMING;
            return _Lc;
        }
        _apduState = STATE_PARTIAL_INCOMING;
        _Lc = (short) (_Lc - (short) (BUFFER_LENGTH - ISO7816.OFFSET_LC - 1));
        return (short) (BUFFER_LENGTH - ISO7816.OFFSET_LC - 1);
    }

    public short receiveBytes(short bOff) throws APDUException {
        if (_apduState != STATE_PARTIAL_INCOMING
                && _apduState != STATE_FULL_INCOMING) {
            APDUException.throwIt(APDUException.ILLEGAL_USE);
        }
        if (_apduState == STATE_FULL_INCOMING)
            return (short) 0;
        if (_Lc <= (short) (BUFFER_LENGTH - bOff)) {
            _apduState = STATE_FULL_INCOMING;
            return _Lc;
        }
        _Lc = (short) (_Lc - (short) (BUFFER_LENGTH - bOff));
        if (_Lc == 0)
            _apduState = STATE_FULL_INCOMING;
        return (short) (BUFFER_LENGTH - bOff);
    }

    public static APDU getCurrentAPDU() throws SecurityException {
        if (KeYJCSystem.selectedApplets[KeYJCSystem.currentChannel] == null
                || KeYJCSystem
                        .jvmGetContext(KeYJCSystem.selectedApplets[KeYJCSystem.currentChannel]) != KeYJCSystem
                        .jvmGetContext(KeYJCSystem.currentActiveObject))
            throw KeYJCSystem.se;
        return _instance;
    }

    public static byte[] getCurrentAPDUBuffer() throws SecurityException {
        return getCurrentAPDU().getBuffer();
    }

    public static byte getCLAChannel() {
        return de.uka.ilkd.key.javacard.KeYJCSystem.currentChannel;
    }

    public static void waitExtension() {
    }

    public void sendBytes(short bOff, short len) throws APDUException {
        if (_apduState < STATE_OUTGOING_LENGTH_KNOWN
                | _apduState >= STATE_FULL_OUTGOING | len > _Lr) {
            APDUException.throwIt(APDUException.ILLEGAL_USE);
        }
        if (len < 0 | bOff < 0 | (short) (bOff + len) > _buffer.length) {
            APDUException.throwIt(APDUException.BUFFER_BOUNDS);
        }
        _apduState = STATE_FULL_OUTGOING;
    }

    public void setOutgoingAndSend(short bOff, short len) throws APDUException {
        setOutgoing();
        setOutgoingLength(len);
        sendBytes(bOff, len);
    }

    public void sendBytesLong(byte[] outData, short bOff, short len)
            throws APDUException, SecurityException {
        if (_apduState < STATE_OUTGOING_LENGTH_KNOWN
                | _apduState >= STATE_FULL_OUTGOING) {
            APDUException.throwIt(APDUException.ILLEGAL_USE);
        }
        if (len < 0 | bOff < 0 | (short) (bOff + len) > outData.length) {
            APDUException.throwIt(APDUException.BUFFER_BOUNDS);
        }
        _apduState = STATE_FULL_OUTGOING;
    }

}
