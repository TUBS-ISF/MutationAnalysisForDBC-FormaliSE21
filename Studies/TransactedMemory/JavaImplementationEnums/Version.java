package JavaImplementationEnums;

/**
 * A version number, which is either 
 * <code>VA</code>, <code>VB</code>, or <code>VC</code>.
 *
 * A <code>Version</code> is just a wrapped <code>int</code>.
 *
 * @author Erik Poll
 */

public class Version {
 
 final public static int VA = 0;
 final public static int VB = 1;
 final public static int VC = 2;
 
 final static public int MAXVER = 3; /* maximum number of versions */
 
  public int value = 0;
  //@ invariant  value == VA || value == VB || value == VC;

  public Version(int sh)
  { this.value = sh ;
  }

  public boolean equals(Version v)
  { return (v.value == this.value);
  }
 
  /** Return next version */

  final static public Version next(Version v) 
  { return new Version( (v.value+1) % MAXVER );
  }

  /** Idem, but for unwrapped int */
  final static public int next(int v) 
  { return ( (v+1) % MAXVER );
  }

 /** FOR TESTING PURPOSES ONLY */
  public Object clone () {
    try { return super.clone();
    } catch (CloneNotSupportedException e) {
       return null; // never invoked
    }
  }

}
