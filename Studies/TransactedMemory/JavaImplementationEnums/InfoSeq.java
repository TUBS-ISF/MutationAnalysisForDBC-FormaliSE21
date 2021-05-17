package JavaImplementationEnums;


/************************************************************************
 *
 * An information sequence, here list of int's, associated with 
 * (some generation of) a tag.
 *
 * @author Erik Poll
 *
 */

public class InfoSeq {

  /** maximum length of any information seqence */
  final static public int SSIZE = 4; 

  /** length of the information seqence */
  public int seq;

  //@ invariant 0 <=  seq && seq <= data.length;

  /** The sequence of information */
  public int[] data;

  //@ invariant data != null && data.length >= seq;

  /* data[0] ... data[seq-1] is the data stored in an Info sequence */

  /** Constructs an <code>InfoSeq</code> with the maximum length <code>SSIZE</code>,
   *  initialised to [-666, ... , -666].
   *
   *  We initialise the contents of the <code>data</code> array to -666
   *  to be able to detect unitiliased data.
   */

  public InfoSeq () {
   data = new int[SSIZE];
   for (int j = 0; j < SSIZE ; j++) data[j] = -666;
  }
 
  /** Constructs an <code>InfoSeq</code> with the length <code>seq</code>,
   *  initialised to [-666, ... , -666].
   *
   *  We initialise the contents of the <code>data</code> array to -666 
   *  to be able to detect unitiliased data.
   */
  public InfoSeq (int seq) {
   this.seq = seq;
   data = new int[seq];
   for (int j = 0; j < seq ; j++) { 
               data[j] = -666;
               System.out.println("-666");
   }
  }

  //@ requires o != null; 
  public boolean equals (InfoSeq o) {
    boolean b = (this.seq == o.seq);
    for (int i=0; i<seq; i++) {
       b = b && (data[i] == o.data[i]);
       };
    return b;
    }

  /** FOR TESTING PURPOSES ONLY */
  public void print() {
    System.out.print("seq = ") ;
    System.out.print(seq);
    System.out.print ( ", data = [" ) ;
    for (int i = 0 ; i < seq ; i++)
      { System.out.print ( data[i]) ;
        if (i < seq -1) { System.out.print ( ":") ;}
      }
    System.out.println("]");
  }
}
