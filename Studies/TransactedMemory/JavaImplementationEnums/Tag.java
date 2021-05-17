package JavaImplementationEnums;

/**
 * A tag, ie. an address of some <code>InfoSeq</code> stored in EEPROM.
 *
 * A <code>Tag</code> is just a wrapped <code>int</code>.
 *
 * @author Erik Poll
 */

public class Tag {

  public int value = 0;
  //@ invariant 0 <= value && value < TransactedMemory.TSIZE;

  public Tag(int sh)
  { this.value = sh ;
  }

  public boolean equals(Tag t)
  { return (t.value == this.value);
  }

  /** FOR TESTING PURPOSES ONLY */

  public /*@ pure @*/ Object clone () {
    try { return super.clone();
    } catch (CloneNotSupportedException e) {
       return null; // never invoked
    }
    
  }

}
