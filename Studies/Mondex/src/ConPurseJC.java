///************************************************************************
// * The Mondex Case Study - The KeY Approach
// * Purse main class
// * author: Dr. Isabel Tonin (tonin@ira.uka.de)
// * Universität Karlsruhe - Institut für Theoretische Informatik
// * http://key-project.org/
// */
//
//import javacard.framework.APDU;
//import javacard.framework.APDUException;
//import javacard.framework.Applet;
//import javacard.framework.ISO7816;
//import javacard.framework.ISOException;
//import javacard.framework.Util;
//import javacard.framework.TransactionException;
//import javacard.framework.JCSystem;
//
//public class ConPurseJC extends Applet
//{
//
///**********************************************************
// * Class Invariants
// */
//
///*@ invariant 
//  @ (exLog != null) && (exLog.length > 0) &&
//  @ (exLog.length < (APDU.BUFFER_LENGTH / 10)) &&
//  @ (logIdx >= 0) && (logIdx <= exLog.length) &&
//  @ (balance >= 0) && (balance <= ShortMaxValue) &&
//  @ (nextSeq >= 0) && (nextSeq <= ShortMaxValue) &&
//  @ (status >= 0) && (status <= 5) &&
//  @ (transaction != null) && (transaction.value > 0) &&
//  @ ((status == Epr) ==> (transaction.value <= balance)) &&
//  @ ((status == Epv) ==> (transaction.value <= (ShortMaxValue - balance))) &&
//  @ (\forall byte i; i>=0 && i<exLog.length; exLog[i] != null);
//  @*/
//
///*@ constraint
//  @ ((\old(balance) != balance) ==>
//  @    ((balance - \old(balance)) == bookedValue()));
//  @*/
//
///*@ constraint
//  @ ((\old(logIdx) != logIdx) ==> (status == Idle));
//  @*/
//
///**********************************************************
// * Method for checking the consistency of the invariants
// */
//
// /*@ requires true;
//   @ ensures false;
//   @*/
// /*@ spec_public @*/ void checkConsistency () {}
//
///**********************************************************
// * Method for showing the Security Properties
// */
//
///*@ requires
//  @ (status != Idle) ==>
//  @  (\exists ConPurseJC x; x!= null && x.transaction == transaction && x.name != name;
//  @  ((status == Endf) ==> (x.status == Endt)) &&
//  @  ((status == Endt) ==> ((x.status == Epa) || (x.status == Endf))) &&
//  @  ((status == Epa) ==> ((x.status == Epv) || (x.status == Endt))) &&
//  @  ((status == Epv) ==> ((x.status == Idle) || (x.status == Epr) ||
//  @                        (x.status == Epa))) &&
//  @  ((status == Epr) ==> ((x.status == Idle) || (x.status == Epv))));
//  @ assignable \nothing;
//  @ ensures
//  @ ((status == Idle) ||
//  @   (\exists ConPurseJC x; x!= null && x.transaction == transaction && x.name != name;
//  @   ((bookedValue() > 0) ==> (x.bookedValue() < 0)) &&
//  @   ((x.bookedValue() > 0) ==> (bookedValue() < 0)) &&
//  @   (bookedValue() + x.bookedValue() +
//  @   ((((status == Epa) || (status == Epv)) &&
//  @     ((x.status == Epa) || (x.status == Epv))) ? transaction.value : 0)
//  @    == 0)));
//  @*/
//  public void showProperties(){}
//
///*@ pure @*/ private /*@ spec_public @*/ short bookedValue()
//   {
//       if ((status == Epa) || (status == Endf))
//	   return (short)-transaction.value;
//       else if (status == Endt)
//	   return transaction.value;
//       else return 0;
//   }
//
///**********************************************************
// * The Application
// **********************************************************/
//
///**********************************************************
// * Constants declaration
// */
//
//// CLA byte in the command APDU header
//  private final static byte Mondex_CLA = (byte)0xB0;
//// INS byte in the command APDU header
//
//// Z spec value transfer operations
//  private final static byte StartFrom = 1;
//  private final static byte StartTo = 2;
//  private final static byte Req = 3;
//  private final static byte Val = 4;
//  private final static byte Ack = 5;
//
//// Z spec exception logging operations
//  private final static byte ReadExLog = 8;
//  private final static byte ClearExLog = 9;
//
//// Z spec Wallet status
//  private final static byte Idle = 0; //originally Expecting Any To/From
//  private final static byte Epr = 1; //Expecting Request
//  private final static byte Epv = 2; //Expecting Value
//  private final static byte Epa = 3; //Expecting Acnowledge
//
//// Extra status (replaces final eaTo and eaFrom from the Z spec)
//  private final static byte Endf = 4; //Transaction From ended successfully
//  private final static byte Endt = 5; //Transaction To ended successfully
//
///**********************************************************
// * SW1 and SW2 for Response APDU Command (as defined in ISO7816-4)
// * (not specified in the Z specification)
// */
//
//// Process Completed
//  private final static short SW_RETURN_VALUE = 0x6100;
//  private final static short SW_IGNORED = 0x6200;
//
//// Process Interrupted - execution error
//  private final static short SW_TRANSACTION_ERROR = 0x6400;
//
//// Process Interrupted - checking error
//  private final static short SW_INVALID_CPD = 0x6700;
//  private final static short SW_INVALID_VALUE = 0x6710;
//  private final static short SW_VALUE_OVERFLOW = 0x6720;
//  private final static short SW_INSUFFICIENT_FUNDS = 0x6730;
//  private final static short SW_LOG_FULL = 0x6740;
//  private final static short SW_INVALID_CLEAR_CODE = 0x6750;
//  private final static short SW_APDU_ERROR = 0x6760;
//
///**********************************************************
// * Implementation Specific Constants declaration
// */
//
//// maximum value that a variable can hold
//  private final static byte  ByteMaxValue = 127;
//  private final static short ShortMaxValue = 32767;
//
//// models any short value (necessary for implementing the image method)
//  private static short aShort;
//
///**********************************************************
// * Fields declaration (according to the Z specification)
// */
//
//  private /*@ spec_public @*/ short balance;
//  private /*@ spec_public @*/ PayDetails [] exLog;
//  private /*@ spec_public @*/ short name;
//  private /*@ spec_public @*/ short nextSeq;
//  private /*@ spec_public @*/ byte status;
//  private /*@ spec_public @*/ byte logIdx;
//	  
//// actual transaction details
//  private /*@ spec_public @*/ PayDetails transaction;
//
///**********************************************************
// * Methods inherited from Applet
// * (required by the JCRE - Javacard Runtime Environment)
// */
//
//// Constructor: an instance of class ConPurseJC is instantiated by its
//// install method.  The applet registers itself with the JCRE by calling
//// the register method, which is defined in class Applet.
//  private ConPurseJC ()
//  {
//      name = 0;
//      nextSeq = 0;
//      balance = 0;
//      status = Idle;
//// exLog length must be smaller or equal than the 1/10 APDU buffer length
//      exLog = new PayDetails[25];
//      transaction = new PayDetails();
//      register();
//  }
//	  
//// This method is invoked by the JCRE to create an applet instance and to
//// register the instance with the JCRE.  The installation parameters are
//// supplied in the byte array parameter, and must be in a format defined
//// by the applet. They are used to initialize the applet instance.
//// For this case study there is no initialization parameter.
//  public static void install(byte[] bArray)
//  {
//      new ConPurseJC();
//  }
//	  
//// This method is called by the JCRE to indicate that this applet has been
//// selected.  It performs the initialization required to process the
//// subsequent APDU messages.  The applet can decline to be selected, for
//// instance, if the pin is blocked.  In this case study, it does not
//// perform any initialization and always accept the selection.
//// For the case study the abort operation is performed in order put the
//// card in a Idle state ready to start a new transaction, logging the old
//// transaction if necessary.
//  public boolean select()
//  {
//      abort_if_necessary();
//      return true;
//  }
//	  
//// This method is called by the JCRE to inform the applet that it should
//// perform any clean-up and bookkeeping tasks before the applet is
//// deselected.
//// For the case study not clean-up operation was defined.
//  public void deselect()
//  {
//  }
//	  
///*@ public behavior
//  @ requires apdu != null;
//  @ assignable logIdx, balance, status, nextSeq, transaction.fromName,
//  @    transaction.toName, transaction.fromSeq, transaction.toSeq,
//  @    transaction.value,apdu._buffer[0..((logIdx*10) - 1)];
//  @ ensures
//  @ ((\old(logIdx) != logIdx) ==>
//  @   ((logIdx == 0) && (status == Idle) && (\old(status) == Idle))) &&
//  @ ((\old(status) == status) ==>
//  @   (\old(balance) == balance) && (\old(nextSeq) == nextSeq)) &&
//  @
//  @ ((\old(status) != status) ==> (
//  @  (\old(apdu._buffer[ISO7816.OFFSET_INS]) == apdu._buffer[ISO7816.OFFSET_INS]) &&
//  @  
//  @  ((\old(status) == Idle) ==>
//  @   ((((status == Epr)&&(apdu._buffer[ISO7816.OFFSET_INS] == StartFrom)) ||
//  @     ((status == Epv)&&(apdu._buffer[ISO7816.OFFSET_INS] == StartTo))) &&
//  @     (\old(balance) == balance))) &&
//  @  ((\old(status) == Epr) ==> ((status == Epa) && 
//  @   (apdu._buffer[ISO7816.OFFSET_INS] == Req) && (\old(balance) > balance))) &&
//  @  ((\old(status) == Epv) ==> ((status == Endt) && 
//  @   (apdu._buffer[ISO7816.OFFSET_INS] == Val) && (\old(balance) < balance))) &&
//  @  ((\old(status) == Epa) ==> ((status == Endf) &&
//  @   (apdu._buffer[ISO7816.OFFSET_INS] == Ack) && (\old(balance) == balance))) &&
//  @
//  @  (status != Idle) &&
//  @  ((status == Epr) ==> (\old(status) == Idle)) &&
//  @  ((status == Epv) ==> (\old(status) == Idle)) &&
//  @  ((status == Epa) ==> (\old(status) == Epr)) &&
//  @  ((status == Endf) ==> (\old(status) == Epa)) &&
//  @  ((status == Endt) ==> (\old(status) == Epv)) &&
//  @
//  @  ((\old(balance) > balance) ==> ((status == Epa) &&
//  @        ((balance - \old(balance)) == -transaction.value))) &&
//  @  ((\old(balance) < balance) ==> ((status == Endt) &&
//  @        ((balance - \old(balance)) == transaction.value))) &&
//  @  ((\old(balance) == balance) ==> ((status == Idle) || (status == Epr) ||
//  @        (status == Epv) || (status == Endf))) &&
//  @
//  @  (((status == Epr) || (status == Epv)) ==>
//  @   ((nextSeq == (\old(nextSeq) + 1)) ||
//  @   ((nextSeq == 0)  && (\old(nextSeq) >= ShortMaxValue)))) &&
//  @  (!((status == Epr) || (status == Epv)) ==>
//  @   ((\old(nextSeq) == nextSeq) &&
//  @    (\old(transaction.fromName) == transaction.fromName) &&
//  @    (\old(transaction.toName) == transaction.toName) &&
//  @    (\old(transaction.fromSeq) == transaction.fromSeq) &&
//  @    (\old(transaction.toSeq) == transaction.toSeq) &&
//  @    (\old(transaction.value) == transaction.value)))));
//  @ signals_only ISOException;
//  @ signals (ISOException e) (\old(balance) == balance) && 
//  @  (\old(status) == status) && (\old(logIdx) == logIdx) &&
//  @  (\old(nextSeq) == nextSeq);
//  @*/
//  public void process(APDU apdu)
//  {
//// After the applet is successfully selected, the JCRE dispatches incoming
//// APDUs to the process method.   At this point, only the first 5 bytes
//// [CLA, INS, P1, P2, LC] are available in the APDU buffer.
//      byte[] buffer = apdu.getBuffer();
//// The JCRE also passes the SELECT APDU commands to the applet
//// (which is ignored)
//      if ((buffer[ISO7816.OFFSET_CLA] == 0) &&
//          (buffer[ISO7816.OFFSET_INS] == (short)(0xA4)) )
//	  ISOException.throwIt(SW_IGNORED);
//// Check CLA
//      if (buffer[ISO7816.OFFSET_CLA] != Mondex_CLA)
//	 ISOException.throwIt (ISO7816.SW_CLA_NOT_SUPPORTED);
//// Ignores message not sent to this purse
//      if (Util.getShort(buffer, ISO7816.OFFSET_P1) != name)
//	 ISOException.throwIt(SW_IGNORED);
//// Calls the method indicated by the INS byte
//      switch (buffer[ISO7816.OFFSET_INS])
//      {
//      case StartFrom: start_from_operation(apdu); break;
//      case StartTo: start_to_operation(apdu); break;
//      case Req: req_operation(apdu); break;
//      case Val: val_operation(apdu); break;
//      case Ack: ack_operation(apdu); break;
//      case ReadExLog: read_ex_log_operation(apdu); break;
//      case ClearExLog: clear_ex_log_operation(apdu); break;
//      default: ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
//      }
//  }
//
///**********************************************************
// * Application Specific Methods
// */
//
///*@ requires true;
//  @ assignable exLog, logIdx, status;
//  @ ensures
//  @ (status == Idle) &&
//  @ (((\old(status) == Epv) || (\old(status) == Epa)) ==>
//  @  ((\old(logIdx) < exLog.length) && (logIdx == (\old(logIdx) + 1)) &&
//  @   (exLog[\old(logIdx)] == transaction))) &&
//  @ (((\old(status) != Epv) && (\old(status) != Epa)) ==>
//  @  ((logIdx == \old(logIdx)) && (\old(exLog[logIdx]) == exLog[logIdx])));
//  @ signals_only ISOException;
//  @ signals (ISOException e)
//  @ (\old(logIdx) == logIdx) && (\old(status) == status);
//  @*/
//  private /*@ spec_public @*/ void abort_if_necessary() throws ISOException
//  {
//      if (!((status == Epv) || (status == Epa)))
//      status = Idle;
//      else if (logIdx >= exLog.length)
//	        ISOException.throwIt(SW_LOG_FULL);
//	   else
//	   {
//	      try
//	      {
//		  exLog[logIdx] = transaction;
//		  JCSystem.beginTransaction();
//		  logIdx++;
//                  status = Idle;
//		  JCSystem.commitTransaction();
//	      }
//	      catch (TransactionException e)
//	      {
//		  ISOException.throwIt(SW_TRANSACTION_ERROR);
//	      }
//	  }
//  }
//
///*@ requires apdu != null;
//  @ assignable status, transaction.fromName, transaction.toName,
//  @   transaction.fromSeq, transaction.toSeq, transaction.value, nextSeq;
//  @ ensures
//  @ (\old(status) == Idle) && (status == Epr) && (logIdx < exLog.length) &&
//  @ ((nextSeq == \old(nextSeq) + 1) ||
//  @  ((nextSeq == 0)  && (\old(nextSeq) >= ShortMaxValue))) &&
//  @ (transaction.fromSeq == \old(nextSeq)) &&
//  @ (transaction.fromName == name) && (transaction.toName != name) &&
//  @ (transaction.toName > 0) && (transaction.toSeq >= 0);
//  @ signals_only ISOException;
//  @ signals (ISOException e)
//  @ (\old(status) == status) && (\old(nextSeq) == nextSeq);
//  @*/
//  private /*@ spec_public @*/ void start_from_operation(APDU apdu) throws ISOException
//    {
//	if (logIdx >= exLog.length) ISOException.throwIt(SW_LOG_FULL);
//	if (status == Idle) 
//        {
//	    readCounterPartDetails(apdu, StartFrom);
//	    try
//	    {
//		JCSystem.beginTransaction();
//		if (nextSeq < ShortMaxValue)
//		    nextSeq = (short) (nextSeq + 1);
//		else nextSeq = 0;
//		status = Epr;
//		JCSystem.commitTransaction();
//	    }
//	    catch (TransactionException e)
//	    {
//		ISOException.throwIt(SW_TRANSACTION_ERROR);
//	    }
//	}
//	else ISOException.throwIt(SW_IGNORED);
//    }
//	  
///*@ requires apdu != null;
//  @ assignable status, transaction.fromName, transaction.toName,
//  @   transaction.fromSeq, transaction.toSeq, transaction.value, nextSeq;
//  @ ensures
//  @ (\old(status) == Idle) && (status == Epv) && (logIdx < exLog.length) &&
//  @ ((nextSeq == \old(nextSeq) + 1) ||
//  @  ((nextSeq == 0)  && \old(nextSeq) >= ShortMaxValue)) &&
//  @ (transaction.toSeq == \old(nextSeq)) &&
//  @ (transaction.toName == name) && (transaction.fromName != name) &&
//  @ (transaction.fromName > 0) && (transaction.fromSeq >= 0);
//  @ signals_only ISOException;
//  @ signals (ISOException e)
//  @ (\old(status) == status) && (\old(nextSeq) == nextSeq);
//  @*/
//  private /*@ spec_public @*/ void start_to_operation(APDU apdu) throws ISOException
//  {
//      if (logIdx >= exLog.length) ISOException.throwIt(SW_LOG_FULL);
//      if (status == Idle)
//      {
//	  readCounterPartDetails(apdu, StartTo);
//	  try
//          {
//	      JCSystem.beginTransaction();
//	      if (nextSeq < ShortMaxValue)
//		  nextSeq = (short) (nextSeq + 1);
//	      else nextSeq = 0;
//	      status = Epv;
//	      JCSystem.commitTransaction();
//	  }
//	  catch (TransactionException e)
//          {
//	      ISOException.throwIt(SW_TRANSACTION_ERROR);
//	  }
//      }
//      else ISOException.throwIt(SW_IGNORED);
//  }
//
///*@ requires apdu != null;
//  @ assignable balance, status;
//  @ ensures
//  @ (balance == \old(balance)-transaction.value) &&
//  @ (\old(status) == Epr) && (status == Epa);
//  @ signals_only ISOException;
//  @ signals (ISOException e)
//  @ ((balance == \old(balance)) && (status == \old(status)));
//  @*/
//  private /*@ spec_public @*/ void req_operation(APDU apdu) throws ISOException
//  {
//      if (status == Epr)
//      {
//	  checkSameTransaction(apdu);
//	  try
//          {
//	      JCSystem.beginTransaction();
//	      balance = (short)(balance - transaction.value);
//	      status = Epa;
//	      JCSystem.commitTransaction();
//	  }
//	  catch (TransactionException e)
//          {
//	      ISOException.throwIt(SW_TRANSACTION_ERROR);
//	  }
//      }
//      else ISOException.throwIt(SW_IGNORED);
//  }
//
///*@ requires apdu != null;
//  @ assignable balance, status;
//  @ ensures
//  @ ((balance == \old(balance)+transaction.value) &&
//  @ (\old(status) == Epv) && (status == Endt));
//  @ signals_only ISOException;
//  @ signals (ISOException e)
//  @ ((balance == \old(balance)) && (status == \old(status)));
//  @*/
//  private /*@ spec_public @*/ void val_operation(APDU apdu) throws ISOException
//  {
//      if (status == Epv) {
//	  checkSameTransaction(apdu);
//	  try
//	  {
//	      JCSystem.beginTransaction();
//	      balance = (short)(balance + transaction.value);
//	      status = Endt;
//	      JCSystem.commitTransaction();
//	  }
//	  catch (TransactionException e)
//	  {
//	      ISOException.throwIt(SW_TRANSACTION_ERROR);
//	  }
//      }
//      else ISOException.throwIt(SW_IGNORED);
//  }
//
///*@ requires apdu != null;
//  @ assignable status;
//  @ ensures (\old(status) == Epa) && (status == Endf);
//  @ signals_only ISOException;
//  @ signals (ISOException e) (status == \old(status));
//  @*/
//  private /*@ spec_public @*/ void ack_operation(APDU apdu) throws ISOException
//  {
//      if (status == Epa)
//      {
//	  checkSameTransaction(apdu);
//	  status = Endf;
//      }
//      else ISOException.throwIt(SW_IGNORED);
//  }
//
//    //		/*@ loop_invariant i <= logIdx && i >= 0;
//
///*@ requires apdu != null;
//  @ assignable apdu._buffer[0..((logIdx*10) - 1)];
//  @ ensures true;
//  @ signals_only ISOException;
//  @ signals (ISOException e) true;
//  @*/
//  private /*@ spec_public @*/ void read_ex_log_operation(APDU apdu) throws ISOException
//  {
//      byte[] buffer = apdu.getBuffer();
//      if (buffer[ISO7816.OFFSET_LC] == 0) 
//	  ISOException.throwIt((short)(SW_RETURN_VALUE + logIdx));
//      if (buffer[ISO7816.OFFSET_LC] != (logIdx * 10)) 
//	  ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
//      byte i = 0;
//	  /*@ loop_invariant (logIdx >= i) && (i >= 0);
//      @ decreases (logIdx - i);
//      @*/
//      while (i < logIdx)
//      {
//	  Util.setShort(buffer, (short)(i*10), exLog[i].fromName);
//	  Util.setShort(buffer, (short)(i*10+2), exLog[i].toName);
//	  Util.setShort(buffer, (short)(i*10+4), exLog[i].value);
//	  Util.setShort(buffer, (short)(i*10+6), exLog[i].fromSeq);
//	  Util.setShort(buffer, (short)(i*10+8), exLog[i].toSeq);
//	  i++;
//      }
//      try
//      {
//	  apdu.setOutgoingAndSend((short)0, (short)exLog.length);
//      }
//      catch (APDUException e)
//      {
//	  ISOException.throwIt(SW_APDU_ERROR);
//      }
//  }
//
///*@ requires apdu != null;
//  @ assignable logIdx;
//  @ ensures (logIdx == 0) && (status == Idle);
//  @ signals_only ISOException;
//  @ signals (ISOException e) (logIdx == \old(logIdx));
//  @*/
//   private /*@ spec_public @*/ void clear_ex_log_operation(APDU apdu) throws ISOException
//   {
//       if (status == Idle)
//       {
//	   try
//           {
//	       if (apdu.setIncomingAndReceive() != 2)
//		   ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
//	       byte[] buffer = apdu.getBuffer();
//	       if (image() != Util.getShort(buffer, (short) 5))
//		   ISOException.throwIt(SW_INVALID_CLEAR_CODE);
//	       logIdx = 0;
//	   }
//	   catch (APDUException e)
//           {
//	       ISOException.throwIt(SW_APDU_ERROR);
//	   }
//       }
//       else ISOException.throwIt(SW_IGNORED);
//   }
//	  
///*@  public normal_behavior
//  @  requires true;
//  @  assignable \nothing;
//  @  ensures true;
//  @*/
//  private /*@ spec_public @*/ short image()
//  {
//      return aShort;
//  }
//	  
//  private /*@ spec_public @*/  void readCounterPartDetails(APDU apdu, byte ins)
//      throws ISOException
//  {
//      try
//      {
//          
//	  if (apdu.setIncomingAndReceive() != 6)
//	      ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
//	  byte[] buffer = apdu.getBuffer();
//	  short cpdName = Util.getShort(buffer, (short) 5);
//	  short cpdSeq = Util.getShort(buffer, (short) 7);
//	  short value = Util.getShort(buffer, (short) 9);
//	  if ((cpdName == name) || !(cpdName > 0) || (cpdSeq < 0))
//	      ISOException.throwIt(SW_INVALID_CPD);
//	  if (value <= 0) ISOException.throwIt(SW_INVALID_VALUE);
//	  if (ins == StartFrom)
//	      if (value > balance)
//		  ISOException.throwIt(SW_INSUFFICIENT_FUNDS);
//	      else
//              {
//		  transaction.fromName = name;
//		  transaction.toName = cpdName;
//		  transaction.fromSeq = nextSeq;
//		  transaction.toSeq = cpdSeq;
//		  transaction.value = value;
//	      }
//	  if (ins == StartTo)
//	      if (value > (short) (ShortMaxValue - balance))
//		  ISOException.throwIt(SW_VALUE_OVERFLOW);
//	      else
//              {
//		  transaction.fromName = cpdName;
//		  transaction.toName = name;
//		  transaction.fromSeq = cpdSeq;
//		  transaction.toSeq = nextSeq;
//		  transaction.value = value;
//	      }
//      }
//      catch (APDUException e)
//      {
//	  ISOException.throwIt(SW_APDU_ERROR);
//      }
//  }
//
//  private void checkSameTransaction(APDU apdu) throws ISOException
//  {
//      if ((status != Epr) && (status != Epa) && (status != Epv))
//	  ISOException.throwIt(SW_IGNORED);
//      try
//      {
//	  if (apdu.setIncomingAndReceive() != 6)
//	      ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
//      }
//      catch (APDUException e)
//      {
//	  ISOException.throwIt(SW_APDU_ERROR);
//      }
//      byte[] buffer = apdu.getBuffer();
//      short cpdName = Util.getShort(buffer,(short)5);
//      short cpdSeq = Util.getShort(buffer,(short)7);
//      short value = Util.getShort(buffer,(short)9);
//      if ((value != transaction.value) ||
//	  (((status == Epr) || (status == Epa)) &&
//	   ((transaction.toName != cpdName) ||
//	    (transaction.toSeq != cpdSeq))) ||
//	  ((status == Epv) && ((transaction.fromName != cpdName) ||
//			       (transaction.fromSeq != cpdSeq))))
//	  ISOException.throwIt(SW_IGNORED);
//  }
//}
