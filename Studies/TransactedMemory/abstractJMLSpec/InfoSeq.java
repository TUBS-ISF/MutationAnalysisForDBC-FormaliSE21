package abstractJMLSpec;

import org.jmlspecs.models.*;


public class InfoSeq extends JMLValueSequence 
 {
  
 /*@ invariant
   @        ( \forall int i;
   @              0 <= i && i < this.int_length()
   @              ==>
   @                \typeof(this.itemAt(i)) == \type(Info)
   @        );
   @*/

  
  public InfoSeq() {
    super();
  }  

  public InfoSeq(Info e) {
    super(e);
  }  

 };
 

