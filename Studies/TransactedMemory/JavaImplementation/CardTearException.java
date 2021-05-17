package JavaImplementation;

public class CardTearException extends Exception {
 
/************************************************************************/
// non-deterministically throw a CardTearException

 // private static java.util.Random generator = new java.util.Random(11111);

 // throw a CardTearException with probability of 1/100
 public static void possibleCardTear()
                     throws CardTearException
  {  // if (generator.nextFloat() < 0.01 ) { 
              System.out.println ( "CARD TEAR !!!" ) ;
              throw new CardTearException(); 
     // }                       

  }

}
