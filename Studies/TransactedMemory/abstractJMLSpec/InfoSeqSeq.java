package abstractJMLSpec;

import org.jmlspecs.models.*;


public /*@ pure @*/ class InfoSeqSeq extends JMLValueSequence 
 {
  
 /*@ invariant
   @        ( \forall int i;
   @              0 <= i && i < this.int_length()
   @              ==>
   @                \typeof(this.itemAt(i)) == \type(InfoSeq)
   @        );
   @*/


  
  public InfoSeqSeq() {
    super();
  }  

  public InfoSeqSeq(InfoSeq e) {
    super(e);
  }  

  public InfoSeqSeq insertFront(InfoSeq item){
   return (InfoSeqSeq) super.insertFront((JMLType) item);
  }

 };
 

