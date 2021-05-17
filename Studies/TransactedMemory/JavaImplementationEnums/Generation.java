package JavaImplementationEnums;

/************************************************************************
 *
 * A value between 0 and <code>MAXIND</code> representing a generation number.
 *
 * A <code>Generation</code> is just a wrapped <code>int</code>.
 * 
 * @author Erik Poll
 */

public class Generation {

  public int value = 0;
  //@ invariant 0 <= value && value <  TransactedMemory.MAXIND;

  public Generation(int sh)
  { this.value = sh ;
  }

  public boolean equals(Generation t)
  { return (t.value == this.value);
  }
 
 /** Return next generation, modulo <code>MAXIND</code>. */

 final static Generation incrind(Generation g) 
 { return new Generation((g.value+1)% TransactedMemory.MAXIND); };

 /** Return previous generation, modulo <code>MAXIND</code>. */

 final static Generation decrind(Generation g) 
 { return new Generation((g.value+TransactedMemory.MAXIND-1)% TransactedMemory.MAXIND); };

 /** FOR TESTING PURPOSES ONLY */
 public /*@ pure @*/ Object clone () {
     try { return super.clone();
     } catch (CloneNotSupportedException e) {
       return null; // never invoked
     }
   }
}
