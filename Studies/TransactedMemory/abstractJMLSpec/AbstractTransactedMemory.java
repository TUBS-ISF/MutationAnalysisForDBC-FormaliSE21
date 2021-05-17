package abstractJMLSpec;

import org.jmlspecs.models.*;

public class AbstractTransactedMemory {

 public int MSIZE; 
 public int MAXGEN;
 public JMLValueSet TAGS;  

 public JMLValueSet committed; //tags comitadas
 public TagToInfoSeqSeqMap assocs; // partial function Tag -> InfoSeqSeq
 public JMLValueToValueMap size;   // partial function Tag -> JMLInteger
 public int usage; 

 /*@ invariant  
   @  (assocs.domain()).equals(size.domain()) 
   @  &&
   @  committed.isSubset(assocs.domain()) 
   @  &&
   @  (\forall Tag t; committed.has(t) 
   @                       ==> 
   @                     assocs.apply(t).int_length()!=0 )
   @  &&
   @  (\forall Tag t; (size.domain()).has(t)
   @        ==>
   @        \typeof(t) == \type(Tag) &&
   @        \typeof(size.apply(t))== \type(JMLInteger))
   @  &&
   @  (\forall Tag t; assocs.domain().has(t) 
   @        ==> 
   @        \typeof(t) == \type(Tag) &&
   @        \typeof(assocs.apply(t)) == \type(InfoSeqSeq) &&
   @        assocs.apply(t).int_length() <= MAXGEN &&
   @        ( \forall int i;
   @              0 <= i && i < assocs.apply(t).int_length()
   @              ==>
   @                \typeof(assocs.apply(t).itemAt(i))
   @                == \type(InfoSeq)
   @                &&
   @                ((InfoSeq)
   @                    assocs.apply(t).itemAt(i)
   @                ).int_length() 
   @                == ((JMLInteger)size.apply(t)).intValue()
   @        ) )
   @  &&
   @  usage == (\sum Tag t; assocs.domain().has(t) ;
   @                assocs.apply(t).int_length() *
   @                ((JMLInteger)size.apply(t)).intValue()
   @           )
   @  &&
   @  0 <= usage && usage <= MSIZE;
   @*/

 /*@ constraint // size(t) never changes
   @  (\forall Tag t; assocs.domain().has(t) && (\old(assocs).domain()).has(t)
   @                  ==>
   @                  (size.apply(t)).equals(\old(size).apply(t))
   @  );
   @*/
 

 /* *******************************************************************************/
 public AbstractTransactedMemory(int msize, int maxgen, JMLValueSet tags)
 /*@ public normal_behavior
   @  requires   msize > 0 && maxgen > 0;
   @  modifiable MSIZE, MAXGEN, TAGS, assocs, usage, size, committed;
   @  ensures    assocs.isEmpty();
   @  ensures_redundantly size.isEmpty() && committed.isEmpty() && usage == 0;
   @*/

 { MSIZE = msize;
   MAXGEN = maxgen;
   TAGS = tags;
   assocs = new TagToInfoSeqSeqMap() ;
   size = new JMLValueToValueMap() ;
   committed = new JMLValueSet() ;
 }
 
 /* ******************************************************************************/
 public Tag ANewTag (int n)
 /*@ public normal_behavior
   @  requires   n > 0;
   @  modifiable assocs, usage, size;
   @  ensures    !(\old(assocs).domain().has(\result)) &&
   @             assocs.equals(\old(assocs).extend(\result, new InfoSeqSeq())) &&
   @             size.equals(  \old(size).extend(\result, new JMLInteger(n))) &&
   @             !(\old(assocs.domain()).has(\result));
   @*/

 { Tag newTag;
   do { newTag = (Tag) TAGS.choose(); }
   while (! assocs.domain().has(newTag));  
    // Not very efficient, of course :-)
    // This repetition is the main (only?) reason why  this spec is not executable !!

   assocs = assocs.extend(newTag, new InfoSeqSeq());
   size = size.extend(newTag, new JMLInteger(n));
   return newTag;
 }
 
 /* ******************************************************************************/
 public /*@ pure @*/ InfoSeq AReadGeneration(Tag t, int g)
                throws JMLSequenceException
 /*@ public normal_behavior  
   @ requires   assocs.domain().has(t)  &&
   @            ! assocs.apply(t).isEmpty() &&
   @            0 <= g && g < assocs.apply(t).int_length();
   @ modifiable \nothing;
   @ ensures    \result.equals(assocs.apply(t).itemAt(g));
   @*/

 { return (InfoSeq)assocs.apply(t).itemAt(g);
 }

 /* ******************************************************************************/
 public /*@ pure @*/ int CurrentGeneration()
 /*@ public normal_behavior 
   @ modifiable \nothing;
   @ ensures    \result == 0;
   @*/

 { return 0;
 }

 /* ******************************************************************************/
 public /*@ pure @*/ InfoSeq ARead(Tag t)
                throws JMLSequenceException
 /*@ public normal_behavior  
   @ requires   assocs.domain().has(t)  &&
   @            ! assocs.apply(t).isEmpty();
   @ modifiable \nothing;
   @ ensures    \result.equals(assocs.apply(t).itemAt(0));
   @ ensures    \result.equals(AReadGeneration(t,CurrentGeneration()));
   @*/

 { return AReadGeneration(t,CurrentGeneration());
 }

 /* ******************************************************************************/
 public void ARelease(Tag t)
 /*@ public normal_behavior 
   @ requires   assocs.domain().has(t); 
   @ modifiable assocs, usage, size, committed;
   @ ensures    assocs.equals(    \old(assocs).remove(t))        &&
   @              size.equals(      \old(size).removeDomainElement(t))        &&
   @         committed.equals( \old(committed).remove(t));
   @*/

 { usage = usage - assocs.apply(t).int_length() *
                    ((JMLInteger)size.apply(t)).intValue();
   assocs = assocs.remove(t);
   size = size.removeDomainElement(t);
   committed = committed.remove(t);
 }

 /* ******************************************************************************/
 public void ACommit(Tag t)
 /*@ public normal_behavior 
   @ requires   assocs.domain().has(t) && 
   @            ! assocs.apply(t).isEmpty() ;
   @ modifiable committed;
   @ ensures    committed.equals( \old(committed).insert(t));
   @*/

 { committed = committed.insert(t);
 }
 
 /* ******************************************************************************/
 public void AWriteFirst(Tag t, InfoSeq info)
 /*@ public normal_behavior 
   @ requires   assocs.domain().has(t) && 
   @            info.int_length() == ((JMLInteger)size.apply(t)).intValue() &&
   @            assocs.apply(t).isEmpty() &&
   @            usage + info.int_length() <= MSIZE;
   @ modifiable assocs, usage;
   @ ensures    assocs.equals( 
   @                 \old(assocs).extend(t, new InfoSeqSeq(info)));
   @ ensures_redundantly usage == \old(usage) + info.int_length() ;
   @*/

 { assocs = assocs.extend(t, new InfoSeqSeq(info));
   usage = usage + info.int_length() ;
 }
 
 /* ******************************************************************************/
 public void AWriteUncommitted(Tag t, InfoSeq info)
                throws JMLSequenceException
 /*@ public normal_behavior 
   @ requires   assocs.domain().has(t) &&
   @            info.int_length() == ((JMLInteger)size.apply(t)).intValue() &&
   @            ! assocs.apply(t).isEmpty() &&
   @            ! committed.has(t);
   @ modifiable assocs;
   @ ensures    assocs.equals
   @              (\old(assocs).extend(t,
   @                   assocs.apply(t).replaceItemAt(0,info)));
   @ ensures_redundantly usage == \old(usage);
   @*/

 { InfoSeqSeq old = assocs.apply(t);
   assocs = assocs.extend(t, (InfoSeqSeq)old.replaceItemAt(0,info));
 }

 /* ******************************************************************************/
 public void AWriteCommitted(Tag t, InfoSeq info)
                throws JMLSequenceException
 /*@ public normal_behavior 
   @ requires   assocs.domain().has(t) &&
   @            info.int_length() == ((JMLInteger)size.apply(t)).intValue() &&
   @            ! assocs.apply(t).isEmpty() &&
   @            committed.has(t) &&
   @            usage + info.int_length() <= MSIZE;
   @ modifiable assocs, usage, committed;
   @ ensures    committed.equals(\old(committed).remove(t)) &&
   @            assocs.equals
   @              (\old(assocs).extend(t,
   @                    ((assocs.apply(t)
   @                     ).insertFront(info)
   @                    ).prefix(MAXGEN)));
   @ ensures_redundantly usage == \old(usage) + info.int_length() ;
   @*/

 { InfoSeqSeq old = assocs.apply(t);
   old = old.insertFront(info);
   old = (InfoSeqSeq)old.prefix(MAXGEN);
   assocs = assocs.extend(t, old);
   committed = committed.remove(t);
   usage = usage + info.int_length() ;
 }

 /* ******************************************************************************/
 public void AWrite(Tag t, InfoSeq info)
             throws JMLSequenceException
  /*  The specs of the three preceding methods, ie. 
   *  AWriteFirst, AWriteUncommitted, AWriteCommitted
   */
 { if ( assocs.apply(t).isEmpty() 
         && usage + info.int_length() <= MSIZE 
      ) AWriteFirst(t,info);
   if ( ! assocs.apply(t).isEmpty() 
         && ! committed.has(t) 
      ) AWriteUncommitted(t,info);
   if ( ! assocs.apply(t).isEmpty() 
        && committed.has(t) 
        && usage + info.int_length() <= MSIZE
      ) AWriteCommitted(t,info);
 }
 

}
