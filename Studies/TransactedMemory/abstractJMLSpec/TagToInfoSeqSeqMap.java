package abstractJMLSpec;

import org.jmlspecs.models.*;


public /*@ pure @*/ class TagToInfoSeqSeqMap extends JMLValueToValueMap{
 
 /*@ invariant
   @  (\forall Tag t; this.domain().has(t)
   @        ==>
   @        \typeof(t) == \type(Tag) &&
   @        \typeof(this.apply(t)) == \type(InfoSeqSeq)  
   @  );
   @*/

   public InfoSeqSeq apply(Tag t) throws JMLNoSuchElementException
   { return (InfoSeqSeq) super.apply(t); 
   }
    
   public TagToInfoSeqSeqMap extend(Tag dv, InfoSeqSeq rv)
   { return (TagToInfoSeqSeqMap) super.extend(dv,rv);
   }

   public TagToInfoSeqSeqMap remove(Tag rv)
   { return (TagToInfoSeqSeqMap) super.removeDomainElement(rv);
   }

}
