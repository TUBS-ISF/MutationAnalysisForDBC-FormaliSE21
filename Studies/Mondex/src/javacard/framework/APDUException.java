/************************************************************************
 * The Mondex Case Study - The KeY Approach
 * javacard api reference implementation
 * author: Dr. Wojciech Mostowski (W.Mostowski@cs.ru.nl)
 * (adapted by Dr. Isabel Tonin - tonin@ira.uka.de)
 * Universität Karlsruhe - Institut für Theoretische Informatik
 * http://key-project.org/
 */
package javacard.framework;

public class APDUException extends CardRuntimeException  {

   /*@ static invariant \is_initialized(APDUException); @*/
   /*@ static invariant _systemInstance != null; @*/
   private /*@spec_public@*/ static APDUException _systemInstance;

   public static final short ILLEGAL_USE = (short)1;
   public static final short BUFFER_BOUNDS = (short)2;
   public static final short BAD_LENGTH = (short)3;
   public static final short IO_ERROR = (short)4;
   public static final short NO_T0_GETRESPONSE = (short)170;
   public static final short T1_IFD_ABORT = (short)171;
   public static final short NO_T0_REISSUE = (short)172;

   /*@ exceptional_behavior
        requires true;
	signals (APDUException ae) ae == _systemInstance && ae._reason[0] == reason ;
	assignable _systemInstance._reason[0] ;
   @*/
   public static void throwIt(short reason) throws APDUException {
      _systemInstance.setReason(reason);
      throw _systemInstance;
   }

} 
