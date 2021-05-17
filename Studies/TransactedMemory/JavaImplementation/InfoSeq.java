package JavaImplementation;

public class InfoSeq {

  final static public int SSIZE = 4;

  public int seq;

  public int[] data = new int[SSIZE];

  //@ invariant data != null && data.length == SSIZE;
 
}
