package JavaImplementationEnums;
 
/************************************************************************
 *
 * An exception that model a card tear, ie. a sudden loss of power
 *
 * @author Erik Poll
 */

public class CardTearException extends Exception {
 
/************************************************************************/

 private static java.util.Random generator = new java.util.Random(11111);

/**  non-deterministically throw a CardTearException */

 public static void possibleCardTear()
                     throws CardTearException
  {  if (generator.nextFloat() < 0.2 ) { 
              System.out.println ( "CARD TEAR !!!" ) ;
              TestFramework.theTransactedMemory.dump(); 
              System.out.println ( "CARD TEAR !!!" ) ;
              throw new CardTearException(); 
      }                       

  }

}
