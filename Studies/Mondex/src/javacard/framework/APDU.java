/************************************************************************
 * The Mondex Case Study - The KeY Approach
 * javacard api reference implementation
 * author: Dr. Wojciech Mostowski (W.Mostowski@cs.ru.nl)
 * (adapted by Dr. Isabel Tonin - tonin@ira.uka.de)
 * Universität Karlsruhe - Institut für Theoretische Informatik
 * http://key-project.org/
 */

package javacard.framework;

import javacard.framework.APDU;

public final class APDU { 
	/*@ invariant _Lc >= 0 && _Lc < 256 ; @*/
   	private /*@spec_public@*/ short _Lc;

    /*@ invariant _Lr >= 0 && _Lr <= 256 ; @*/
   	private /*@spec_public@*/ short _Lr;

    /*@ invariant _Le >= 0 && _Le <= 256 ; @*/
   	private /*@spec_public@*/ short _Le;

        /** Most cards have an APDU buffer of size 261, the spec
	  * says it has to be at least 37
	  */
    /*@ static invariant BUFFER_LENGTH >= 37 ; @*/
    private /*@ spec_public @*/ final static short BUFFER_LENGTH = (short)261;

    /*@ invariant _buffer != null; @*/
    /*@ invariant _buffer.length == BUFFER_LENGTH; @*/
    private /*@ spec_public @*/ byte[] _buffer;

    private /*@ spec_public @*/ byte _apduState;

    private static byte _tprotocol;

	public static final byte PROTOCOL_T0 =   (byte)0;
	public static final byte PROTOCOL_T1 =   (byte)1;
	public static final byte STATE_INITIAL =               (byte)0;
	public static final byte STATE_PARTIAL_INCOMING =      (byte)1;
	public static final byte STATE_FULL_INCOMING =         (byte)2;
	public static final byte STATE_OUTGOING =              (byte)3;
	public static final byte STATE_OUTGOING_LENGTH_KNOWN = (byte)4;
	public static final byte STATE_PARTIAL_OUTGOING =      (byte)5;
	public static final byte STATE_FULL_OUTGOING =         (byte)6;

    public /*@pure@*/ byte[] getBuffer() {
	return _buffer;
    }

    public short setOutgoing() throws APDUException {
	if(_apduState >= STATE_OUTGOING) {
	   APDUException.throwIt(APDUException.ILLEGAL_USE);
	}
	_apduState = STATE_OUTGOING;
       if(_tprotocol == PROTOCOL_T0)
	 return (short)256;
       else 
         return _Le;
    }

    public void setOutgoingLength(short len) throws APDUException {
       if(_apduState != STATE_OUTGOING) {
	  APDUException.throwIt(APDUException.ILLEGAL_USE);
       }
       if(len < 0 | len > 256) {
	  APDUException.throwIt(APDUException.BAD_LENGTH);
       }
       _Lr = len;
       _apduState = STATE_OUTGOING_LENGTH_KNOWN;
    }

    public short setIncomingAndReceive() throws APDUException {
       if(_apduState != STATE_INITIAL) {
	  APDUException.throwIt(APDUException.ILLEGAL_USE);
       }
       if(_Lc <= (short)(BUFFER_LENGTH - ISO7816.OFFSET_LC - 1)) {
         _apduState = STATE_FULL_INCOMING;
         return _Lc;
       }
       _apduState = STATE_PARTIAL_INCOMING;
       _Lc = (short)(_Lc - (short)(BUFFER_LENGTH - ISO7816.OFFSET_LC - 1));
       return (short)(BUFFER_LENGTH - ISO7816.OFFSET_LC - 1);
    }


    public void sendBytes(short bOff, short len) throws APDUException {
      if(_apduState < STATE_OUTGOING_LENGTH_KNOWN | _apduState >= STATE_FULL_OUTGOING) {
	  APDUException.throwIt(APDUException.ILLEGAL_USE);
       }
       if(len < 0 | bOff < 0 | (short)(bOff + len) > _buffer.length) {
	  APDUException.throwIt(APDUException.BUFFER_BOUNDS);
       }
       _apduState = STATE_FULL_OUTGOING;
    }

    public void setOutgoingAndSend(short bOff, short len)
	throws APDUException {
	setOutgoing();
	setOutgoingLength(len);
	sendBytes(bOff, len);
    }

}
