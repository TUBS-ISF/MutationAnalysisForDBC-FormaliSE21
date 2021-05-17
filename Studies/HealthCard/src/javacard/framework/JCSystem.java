/************************************************************************
 * The Mondex Case Study - The KeY Approach
 * javacard api reference implementation
 * author: Dr. Wojciech Mostowski (W.Mostowski@cs.ru.nl)
 * (adapted by Dr. Isabel Tonin - tonin@ira.uka.de)
 * Universität Karlsruhe - Institut für Theoretische Informatik
 * http://key-project.org/
 */

package javacard.framework;

/** Some of the methods in this class do not have any spec. This
  * is because it is better to execute them symbolically (inline the
  * implementation) in a KeY proof and let dedicated taclets for jvm*
  * methods take care of the rest. Such no spec methods are marked
  * with "no spec"
  */
public final class JCSystem {

    public static final byte NOT_A_TRANSIENT_OBJECT = (byte)0;
    public static final byte CLEAR_ON_RESET = (byte)1;
    public static final byte CLEAR_ON_DESELECT = (byte)2;

    public static final byte MEMORY_TYPE_PERSISTENT = (byte)0;
    public static final byte MEMORY_TYPE_TRANSIENT_RESET = (byte)1;
    public static final byte MEMORY_TYPE_TRANSIENT_DESELECT = (byte)2;

    private static final short API_VERSION = (short)0x0202;

    /** Most of the calls (all?) are only done from within a
      * selected/active applet, thus the following invariant:
      */
    // no spec (but pure, so that it can be used in specs)
    public static /*@pure@*/ byte isTransient(Object theObj){
        if(theObj == null) return NOT_A_TRANSIENT_OBJECT;
	return KeYJCSystem.jvmIsTransient(theObj);
    }

    // no spec
    public static short[] makeTransientShortArray(short length, byte event)
      throws SystemException {
        if(event != CLEAR_ON_RESET && event != CLEAR_ON_DESELECT)
          SystemException.throwIt(SystemException.ILLEGAL_VALUE);
	return KeYJCSystem.jvmMakeTransientShortArray(length, event);
    }

} 
