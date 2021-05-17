/************************************************************************
 * The Mondex Case Study - The KeY Approach
 * javacard api reference implementation
 * author: Dr. Wojciech Mostowski (W.Mostowski@cs.ru.nl)
 * (adapted by Dr. Isabel Tonin - tonin@ira.uka.de)
 * Universität Karlsruhe - Institut für Theoretische Informatik
 * http://key-project.org/
 */

package javacard.framework;

import de.uka.ilkd.key.javacard.*;

/** Some of the methods in this class do not have any spec. This
  * is because it is better to execute them symbolically (inline the
  * implementation) in a KeY proof and let dedicated taclets for jvm*
  * methods take care of the rest. Such no spec methods are marked
  * with "no spec"
  */
public class Util  { 

    // Could be specified, but KeY does not handle byte and short
    // args. in query methods very well, see bug #638
    //
    // no spec
    public static short getShort(byte[] bArray, short bOff) {
	if(bArray == null) throw new NullPointerException();
	if(bOff < 0 || (short)(bOff + 1) >= bArray.length)
	  throw new ArrayIndexOutOfBoundsException();
	return KeYJCSystem.jvmMakeShort(
	  bArray[bOff], bArray[bOff+1]);
    }

    // no spec
    public static short setShort(byte[] bArray, short  bOff, short  sValue) 
      throws TransactionException {
	if(bArray == null) throw new NullPointerException();
	if(bOff < 0 || (short)(bOff + 1) >= bArray.length)
	  throw new ArrayIndexOutOfBoundsException();
	final boolean doTransaction = JCSystem.isTransient(bArray) == JCSystem.NOT_A_TRANSIENT_OBJECT
	                        && JCSystem.getTransactionDepth() == (byte)0;
	if(doTransaction) KeYJCSystem.jvmBeginTransaction();
        KeYJCSystem.jvmSetShort(bArray, bOff, sValue);
	if(doTransaction) KeYJCSystem.jvmCommitTransaction();
        return (short)(bOff + 2);
    }

}
