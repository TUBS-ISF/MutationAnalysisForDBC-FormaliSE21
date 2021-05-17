package JavaImplementationEnums;

/************************************************************************
 *
 * A Java implementation of Transacted Memory for smart cards.
 *
 *<p>
 * 
 * This implementation is based on the SPIN/C transacted memory described in 
 * Section 5 of P.H. Hartel, M.J. Butler, E. de Jong , and M. Longley
 * "Transacted Memory for Smart Cards".
 *
 *<p>
 *
 * Between any two write operations to EEPROM there should be a call to 
 * CardTearException.possibleCardTear() to simulate the possibilty of
 * a card tear at any moment.
 *
 * The only exception to this is in the methods DWriteCommittedAddGen
 * and DWriteCommittedMaxGen.
 *
 *<p>
 *
 * TO DO:
 *
 * - The algorithm can't cope with a CardTear halfway through
 *   DWriteCommittedAddGen and DWriteCommittedMaxGen.
 *
 * @author Erik Poll
 *
 **************************************************************************/

public class TransactedMemory {

 public static TransactedMemory theTransactedMemory = new TransactedMemory();

 /** maximum number of generations */
 final static public int MAXGEN = 3; 

 /** maximum number of generation indices */
 final static public int MAXIND = 5; 

 /** maximum number of tags */
 final static public int TSIZE = 6;  

 /** data recorded for each tag */
 public DTagData[] ddata = new DTagData[TSIZE];

 //@ invariant ddata != null && ddata.length == TSIZE;
 //@ invariant (\forall int i; 0 <= i && i < TSIZE ; ddata[i] != null);

 /** memory size in DPage units */
 final static public int MSIZE = 40; 

 /** The EEPROM memory of <code>MSIZE<code> pages */
 public DPage[] dmem = new DPage[MSIZE];

 /** FOR TESTING PURPOSES ONLY: an estimate of maximum number of pages needed,
     to detect if we might run out of EEPROM.
   */
 public int maxMemoryUsage ; 

 //@ invariant dmem != null && dmem.length == MSIZE;
 //@ invariant (\forall int i; 0 <= i && i < MSIZE ; dmem[i] != null);

/****************************************************************************/

 /** Constructs a <code> TransactedMemory</code> with <code>MSIZE</code> pages,
  *  all marked as not in use, and <code>TSIZE</code>,
  *  also marked as not in use
  */
 public TransactedMemory()
 { short i;
   for (i = 0; i < TSIZE; i++) ddata[i] = new DTagData (false,1,false);
   for (i = 0; i < MSIZE; i++) dmem[i] = new DPage (false, 
                                                    new Tag(0), 0, 
                                                    new Generation(0), 0, 
                                                    new Version(0));
 }

/* DGeneration ************************************************************/
/**
 * Discover the generation indices associated with a tag, and then
 * return the indices of the oldest and newest generation, as well as
 * the number of generations. 
 * <p>
 * If there are no generations, then the old and new generations indices 
 * are undefined. This issue was identified with SPIN, and this issue
 * also come up trying to annotate this method.
 */

public GenGenbyte DGeneration (Tag tag)  

 { // System.out.println ( "DGeneration" ) ;

   int   loc ;
   int   fst, snd;
   Generation  oldest = new Generation(0);
   Generation  newest = new Generation(0);
   boolean  cur;
   boolean[] map = new boolean[MAXIND];
   GenGenbyte gendata = new GenGenbyte();                                    

   for (fst = 0; fst <= MAXIND-1 ; fst++) {
         map[fst] = false; 
         // Not needed of course, since bool's are initialized to false anyway.
         // I leave it in to keep it similar to the C-code.
   }; 

   // register all generations in use for this tag in map:
   for (loc = 0 ; loc <= MSIZE-1 ; loc++) {
         if(dmem[loc].pageInUse && dmem[loc].tag.equals(tag)  ) {
            map[dmem[loc].generation.value] = true;
         };
   }; 

   // Note that if map[0]==true then the generations may be 
   // "wrapped" over the end of the circular buffer map.

   // Find minimum fst s.t. map[fst] != map [0] :
   cur = map[0] ;  
   for (fst = 1 ; fst <= MAXIND-1 ; fst++) {
         if (map[fst]!=cur) {
            //@ assert map[fst] != map[0];
            break;
         };
   };
   //@ assert 0 < fst && fst <= MAXIND;
   //@ assert fst == MAXIND || map[fst] != map [0];
   //@ assert (\forall int i; 0 <= i && i < fst ; map[i] == map[0] );

   //@ assert fst == MAXIND 
   //@         ==> (\forall int i; 0 <= i && i < MAXIND ; map[i] == map[0]);

   /* Ie if fst == MAXIND then either no generations are in use, or all
      generations are in use. 

      In the former case we return; in the latter case we throw an exception, 
      as things are seriously wrong (see below).
    */

   
   //@ assert !((fst == MAXIND) && map[0]); 
   if (fst == MAXIND && map[0] ) throw new Error(); 
   /// if fst == MAXIND and map[0] things are seriously wrong !!
   
   // Find minimum snd s.t. fst < snd && map[snd] == map[0] :
   cur = !cur;
   for (snd = fst+1; snd <= MAXIND-1 ; snd++) {
         if(  map[snd]!=cur  ) {
            //@ assert map[snd] == map[0];
            break;
         };
   }; 

   if ( map[0] ) {
         // The generations 0...fst-1 and snd...MAXIND-1 are in use.
         // So the generations may be "wrapped".
         /*@ assert (\forall int i;   0 <= i && i < fst ; map[i] ) &&
                    (\forall int i; snd <= i && i < MAXIND ; map[i] ) &&
                    (\forall int i; fst <= i && i < snd ; !map[i] );
           @*/
         oldest = new Generation(snd % MAXIND) ;
         newest = new Generation(fst-1);
         gendata.cnt = fst + MAXIND - snd ;
   } else if( ! map[0] ) {
         // The generations fst...snd-1 are in use.
         // The generations are not "wrapped"
         /*@ assert 
              ( fst == MAXIND && 
                (\forall int i; 0 <= i && i < MAXIND; !map[i]))
             ||
              ( fst != MAXIND && 
                (\forall int i; fst <= i && i < snd ; map[i] ) && 
                (\forall int i;   0 <= i && i < fst ; !map[i] ) &&
                (\forall int i; snd <= i && i < MAXIND ; !map[i] ));
           @*/

         if (fst == MAXIND) {                                    
             //@ assert (\forall int i; 0 <= i && i < MAXIND ; !map[i]);
             gendata.cnt = 0;                                            
             gendata.oldGen = new Generation(0);                           
             gendata.newGen = new Generation(0);                          
             return gendata;
         };
         oldest = new Generation(fst);
         newest = new Generation(snd-1);
         gendata.cnt = snd - fst;
   };
   /*@ assert  oldest.value <= newest.value ==>
               (\forall int i; 0 <= i && i < MAXIND  ;
                      (map[i] == ( oldest.value <=i  && i <= newest.value))
               );
       assert  oldest.value > newest.value ==>
               (\forall int i; 0 <= i && i < MAXIND  ;
                      (map[i] == ( i <= newest.value || oldest.value <= i))
               );
    @*/
   //@ assert  map[(oldest.value)] ;  // generation oldest.value exists
   //@ assert  map[(newest.value)] ;  // generation newest.value exists
   //@ assert  ! map[(newest.value+1) % MAXIND] ;

   // Now we return the triple <cnt,oldest,newest>
   gendata.oldGen = oldest;
   gendata.newGen = newest;
   return gendata;
}



/* DTidy ******************************************************************/

/** 
 * Tidy up the memory. This operation should be used whenever the card powers up. 
 */

   /* ensures
                ddata[0].tagInUse && SecureDRead(new Tag(0)) != null
                ==>
                SecureDRead(new Tag(0)).equals(DRead(new Tag(0))) &&
                SecureDRead(new Tag(0)).equals(\old(SecureDRead(new Tag(0))));

     ensures
                ddata[1].tagInUse && SecureDRead(new Tag(1)) != null
                ==>
                SecureDRead(new Tag(1)).equals(DRead(new Tag(1))) &&
                SecureDRead(new Tag(1)).equals(\old(SecureDRead(new Tag(1))));
     
     ensures
                ddata[2].tagInUse && SecureDRead(new Tag(2)) != null
                ==>
                SecureDRead(new Tag(2)).equals(DRead(new Tag(2))) &&
                SecureDRead(new Tag(2)).equals(\old(SecureDRead(new Tag(2))));

     ensures  
               (\forall int t ;
                    0 <= t && t < TSIZE ;
                    ddata[t].tagInUse 
                      ==>
                      ( DGDGeneration(new Tag(t)).cnt !=0
                           ==> ddata[t].committed )
                        &&
                      ( DGDGeneration(new Tag(t)).cnt ==0
                           ==> !ddata[t].committed)
               );

     */

public void DTidy()
                 throws CardTearException 

 { System.out.println ( "DTidy" ) ;
   short   tag ;
   DTagData x ;
   int   gen ;
   int    loc ;
   boolean   ok ;
   int    ver ;
   boolean[] map ;
   GenGenbyte gendata ;

   map = new boolean[MAXIND];
   
   /* Phase 1
      Detect and free the locations that are marked as being in use 
      by a tag that is itself marked as not in use. 
    */

   for (loc = 0 ; loc < MSIZE ; loc++ ) {
     if (dmem[loc].pageInUse) { 
         x = ddata[dmem[loc].tag.value];
         if ( !x.tagInUse  ){
         /* was if ( !x.tagInUse || !x.committed  )
                                 ^^^^^^^^^^^^^^^
            This would free all locations in use for a tag marked
            as not committed. However, only the locations used for the
            _last generation_ of an uncommitted tag should be removed, 
            not all locations used for an uncommitted tag.
          */
            CardTearException.possibleCardTear();
            System.out.print ( "Tidying Phase 1 - Page " ) ;
            System.out.println ( loc ) ;
            dmem[loc].resetPageInUse();
         };
     }
   };
   CardTearException.possibleCardTear();
  
   /* Phase 2
      Detect whether page 0 has been recorded for all combinations
      of tag, generation, and version. Then remove any locations for
      such a combination when no page 0 is present. */

   for (tag = (short)0 ; tag <= TSIZE-1 ; tag++ ) {
      for (gen = 0 ; gen <= MAXIND-1 ; gen++ ) {
         for (ver = DPage.VA ; ver <= DPage.VC ; ver++ ) {
            ok = false ;
            for (loc = 0 ; loc < MSIZE ; loc++ ) {
               if (dmem[loc].pageInUse && dmem[loc].tag.value==tag 
                                 && dmem[loc].generation.value==gen 
                                 && dmem[loc].version.value==ver 
                                 && dmem[loc].pageNumber==0) {
                  ok = true ;
                  break;
               };
            };
            if (!ok) {
               for (loc = 0 ; loc < MSIZE ; loc++ ) {
                  if (dmem[loc].pageInUse && dmem[loc].tag.value==tag 
                                       && dmem[loc].generation.value==gen
                                       && dmem[loc].version.value==ver) {
                     CardTearException.possibleCardTear();
                     System.out.print ( "Tidying Phase 2 - Page " ) ;
                     System.out.println ( loc ) ;
                     dmem[loc].resetPageInUse();
                  };
               };
            };
         };
      };
   };
   CardTearException.possibleCardTear();

   /* Phase 3
      Discover whether two versions of the same generation
      exist at the same time. The version numbers allow us to
      distinguish the locations of the two versions and determine
      the out-of-date version.  The locations associated with the
      latter will be removed. */


   for (tag = (short)0 ; tag <= TSIZE-1 ; tag++ ) {
      for (gen = 0 ; gen <= MAXIND-1 ; gen++ ) {
         for (ver = DPage.VA ; ver <= DPage.VC ; ver++ ) {
            map[ver] = false;
         };

         for (loc = 0 ; loc < MSIZE ; loc++ ) {
            if (dmem[loc].pageInUse && dmem[loc].tag.value==tag 
                                    && dmem[loc].generation.value==gen) {
               map[dmem[loc].version.value] = true;
            };
         };
         for (ver = DPage.VA ; ver <= DPage.VC ; ver++ ) {
            if(  map[ver] && map[Version.next(ver)]  ) {
               for (loc = 0 ; loc < MSIZE ; loc++ ) {
                  if (dmem[loc].pageInUse 
                         && dmem[loc].tag.value==tag 
                         && dmem[loc].generation.value==gen
                         && dmem[loc].version.value==ver  ) {
                     CardTearException.possibleCardTear();
                     System.out.print ( "Tidying Phase 3 - Page " ) ;
                     System.out.println ( loc ) ;
                     dmem[loc].resetPageInUse();
                  };
               };
            };
         };
      };
   };
   CardTearException.possibleCardTear();
  
   /* Phase 4
      Discover if there is one more complete generation than the
      maximum allowed. Remove the locations associated with the
      oldest generation.

      NB phase 4 has to come before phase 5!
    */

   for (tag = (short)0; tag <= TSIZE-1 ; tag++ ) {
      gendata = DGeneration(new Tag(tag));
      if (gendata.cnt > MAXGEN  ) {
         for (loc = 0 ; loc < MSIZE ; loc++ ) {
            if (dmem[loc].pageInUse && dmem[loc].tag.value==tag 
                              && dmem[loc].generation.equals(gendata.oldGen)) {
               CardTearException.possibleCardTear();
                     System.out.print ( "Tidying Phase 4 - Page " ) ;
                     System.out.println ( loc ) ;
               dmem[loc].resetPageInUse();
            };
         };
      };
   };
   CardTearException.possibleCardTear();

   /* Phase 5
      Remove any uncommited generations, ie. the last generation for a tag
      which is marked as uncommitted. After removing the last generation 
      for an uncommitted then the tag should be marked as committed, unless
      the generation removed was the only generation for this tag, in 
      which case the tag should be marked as uncommitted.
    */
   for (tag = (short)0; tag <= TSIZE-1 ; tag++ ) {
     if (! ddata[tag].committed ) {
       gendata = DGeneration(new Tag(tag));
       if (gendata.cnt>0) {
           for (loc = 0 ; loc < MSIZE ; loc++ ) {
               if (dmem[loc].pageInUse 
                   && dmem[loc].tag.value==tag 
                   && dmem[loc].generation.equals(gendata.newGen)) {
                     CardTearException.possibleCardTear();
                     System.out.print ( "Tidying Phase 5 - Page " ) ;
                     System.out.println ( loc ) ;
                     dmem[loc].resetPageInUse();
               };
           };
        //BUG hence no CardTearException.possibleCardTear();
/*   Note that there is no possibleCardTear between this removing of
     the uncommitted generation above and the committing below.
     This is because the algorithm can't cope with this.
 */
           /* The last uncommited generation for tag has been removed */
           if (gendata.cnt > 1) {ddata[tag].commit();}
                         else   { /* there is no generation for this tag */
                                  ddata[tag].decommit();}
           CardTearException.possibleCardTear();
/* NB THIS CAN GO WRONG, NAMELY IF WE GET A CARD TEAR JUST AFTER
   REMOVING THE LATEST GENERATION BUT BEFORE MARKING THE
   GENERATION AS COMMITTED ! THE NEXT DTidy() WILL THEN REMOVE
   ANOTHER GENERATION
*/
       };
     };
   };

 }


/* DWeakTidy **************************************************************/

/** 
 * FOR SPECIFICATION PRUPOSES ONLY: Tidy up the memory, but do not 
 * remove the last uncommited generations for tags, if these are complete.
 */

   /* comment out for efficiency reasons  ?? */
   /*  ensures
          (\forall int t ; 0 <= t && t < TSIZE ;
                ddata[t].tagInUse && SecureDRead(new Tag(t)) != null
                ==>
                SecureDRead(new Tag(t)).equals(DRead(new Tag(t))) &&
                SecureDRead(new Tag(t)).equals(\old(SecureDRead(new Tag(t))))
           );
     */

public void DWeakTidy()
                 throws CardTearException 

 { System.out.println ( "DTidy" ) ;
   short   tag ;
   DTagData x ;
   int   gen ;
   int    loc ;
   boolean   ok ;
   int    ver ;
   boolean[] map ;
   GenGenbyte gendata ;

   map = new boolean[MAXIND];
   
      /* Detect and free the locations that are 
            - marked as being in use by a tag that is itself marked as 
              not in use. 
       */

   for (loc = 0 ; loc < MSIZE ; loc++ ) {
     if (dmem[loc].pageInUse) { 
         x = ddata[dmem[loc].tag.value];
         if ( !x.tagInUse ){    
            CardTearException.possibleCardTear();
            System.out.print ( "Tidying Phase 1 - Page " ) ;
            System.out.println ( loc ) ;
            dmem[loc].resetPageInUse();
         };
     }
   };
   CardTearException.possibleCardTear();
  
      /* Detect whether page 0 has been recorded for all combinations
         of tag, generation, and version. Then remove any locations for
         such a combination when no page 0 is present. */

   for (tag = (short)0 ; tag <= TSIZE-1 ; tag++ ) {
      for (gen = 0 ; gen <= MAXIND-1 ; gen++ ) {
         for (ver = DPage.VA ; ver <= DPage.VC ; ver++ ) {
            ok = false ;
            for (loc = 0 ; loc < MSIZE ; loc++ ) {
               if (dmem[loc].pageInUse && dmem[loc].tag.value==tag 
                                 && dmem[loc].generation.value==gen 
                                 && dmem[loc].version.value==ver 
                                 && dmem[loc].pageNumber==0) {
                  ok = true ;
                  break;
               };
            };
            if (!ok) {
               for (loc = 0 ; loc < MSIZE ; loc++ ) {
                  if (dmem[loc].pageInUse && dmem[loc].tag.value==tag 
                                       && dmem[loc].generation.value==gen
                                       && dmem[loc].version.value==ver) {
                     CardTearException.possibleCardTear();
                     System.out.print ( "Tidying Phase 2 - Page " ) ;
                     System.out.println ( loc ) ;
                     dmem[loc].resetPageInUse();
                  };
               };
            };
         };
      };
   };
   CardTearException.possibleCardTear();

      /* Discover whether two complete versions of the same generation
         exist at the same time. The version numbers allow us to
         distinguish the locations of the two versions and determine
         the out-of-date version.  The locations associated with the
         latter will be removed. */


   for (tag = (short)0 ; tag <= TSIZE-1 ; tag++ ) {
      for (gen = 0 ; gen <= MAXIND-1 ; gen++ ) {
         for (ver = DPage.VA ; ver <= DPage.VC ; ver++ ) {
            map[ver] = false;
         };

         for (loc = 0 ; loc < MSIZE ; loc++ ) {
            if (dmem[loc].pageInUse && dmem[loc].tag.value==tag 
                                    && dmem[loc].generation.value==gen) {
               map[dmem[loc].version.value] = true;
            };
         };
         for (ver = DPage.VA ; ver <= DPage.VC ; ver++ ) {
            if(  map[ver] && map[Version.next(ver)]  ) {
               for (loc = 0 ; loc < MSIZE ; loc++ ) {
                  if (dmem[loc].pageInUse 
                         && dmem[loc].tag.value==tag 
                         && dmem[loc].generation.value==gen
                         && dmem[loc].version.value==ver  ) {
                     CardTearException.possibleCardTear();
                     System.out.print ( "Tidying Phase 3 - Page " ) ;
                     System.out.println ( loc ) ;
                     dmem[loc].resetPageInUse();
                  };
               };
            };
         };
      };
   };
   CardTearException.possibleCardTear();

      /* Discover if there is one more complete generation than the
         maximum allowed. Remove the locations associated with the
         oldest generation. */

   for (tag = (short)0; tag <= TSIZE-1 ; tag++ ) {
      gendata = DGeneration(new Tag(tag));
      if (gendata.cnt > MAXGEN  ) {
         for (loc = 0 ; loc < MSIZE ; loc++ ) {
            if (dmem[loc].pageInUse && dmem[loc].tag.value==tag 
                              && dmem[loc].generation.equals(gendata.oldGen)) {
               CardTearException.possibleCardTear();
                     System.out.print ( "Tidying Phase 4 - Page " ) ;
                     System.out.println ( loc ) ;
               dmem[loc].resetPageInUse();
            };
         };
      };
   };
   CardTearException.possibleCardTear();

   /* No Phase 5 to remove uncommited generations - this is the only 
      difference with DTidy()
    */
 }


/* isDWeakTidy ***********************************************************/

/**
 * FOR SPECIFICATION PURPOSES ONLY: Check if memory is tidy.
 *
 * <p>
 *
 * The reason for introducing this method is to debug testing scenario's -
 * an incorrect test scenario may fail to tidy the memory in certain
 * situation - which will result in subsequent errors which are not
 * the fault of the implementation of TransactedMemory.
 *
 * <p>
 *
 * This method returns false if there is junk in the memory that should
 * have been cleared by a previous call to DTidy();
 * 
 * <p>
 *
 * NB. the code of this method is very similar to that of {@link #DWeakTidy()}.
 *
 */

public /*@ pure @*/ boolean isDWeakTidy()

 { //System.out.println ( "isDTidy" ) ;
   short   tag ;
   DTagData x ;
   int   gen ;
   int    loc ;
   boolean   ok ;
   int    ver ;
   boolean[] map ;
   GenGenbyte gendata ;

   map = new boolean[MAXIND];

      /* Detect any locations that are marked as being in use
         by a tag that is itself marked as not in use.

         Note the difference with DTidy here. DTidy also removes
         uncommitted tags, but here in isDTidy we allow uncommitted tags.
       */

   for (loc = 0 ; loc < MSIZE ; loc++ ) {
     if (dmem[loc].pageInUse) { 
         x = ddata[dmem[loc].tag.value];
         if ( !x.tagInUse ) return false;
     }
   };
  
      /* Detect whether page 0 has been recorded for all combinations
         of tag, generation, and version.  */

   for (tag = (short)0 ; tag <= TSIZE-1 ; tag++ ) {
      for (gen = 0 ; gen <= MAXIND-1 ; gen++ ) {
         for (ver = DPage.VA ; ver <= DPage.VC ; ver++ ) {
            ok = false ;
            for (loc = 0 ; loc < MSIZE ; loc++ ) {
               if (dmem[loc].pageInUse && dmem[loc].tag.value==tag 
                                 && dmem[loc].generation.value==gen 
                                 && dmem[loc].version.value==ver 
                                 && dmem[loc].pageNumber==0) {
                  ok = true ;
                  break;
               };
            };
            if (!ok) {
               for (loc = 0 ; loc < MSIZE ; loc++ ) {
                  if (dmem[loc].pageInUse && dmem[loc].tag.value==tag 
                                       && dmem[loc].generation.value==gen
                                       && dmem[loc].version.value==ver) 
                     return false;
               };
            };
         };
      };
   };

      /* Discover whether two complete versions of the same generation
         exist at the same time. The version numbers allow us to
         distinguish the locations of the two versions and determine
         the out-of-date version.  */


   for (tag = (short)0 ; tag <= TSIZE-1 ; tag++ ) {
      for (gen = 0 ; gen <= MAXIND-1 ; gen++ ) {
         for (ver = DPage.VA ; ver <= DPage.VC ; ver++ ) {
            map[ver] = false;
         };

         for (loc = 0 ; loc < MSIZE ; loc++ ) {
            if (dmem[loc].pageInUse && dmem[loc].tag.value==tag 
                                    && dmem[loc].generation.value==gen) {
               map[dmem[loc].version.value] = true;
            };
         };
         for (ver = DPage.VA ; ver <= DPage.VC ; ver++ ) {
            if(  map[ver] && map[Version.next(ver)]  ) {
               for (loc = 0 ; loc < MSIZE ; loc++ ) {
                  if (dmem[loc].pageInUse 
                         && dmem[loc].tag.value==tag 
                         && dmem[loc].generation.value==gen
                         && dmem[loc].version.value==ver  ) 
                     return false;
               };
            };
         };
      };
   };

      /* Discover if there is one more complete generation than the
         maximum allowed. */

   for (tag = (short)0; tag <= TSIZE-1 ; tag++ ) {
      gendata = DGeneration(new Tag(tag));
      if (gendata.cnt > MAXGEN  ) {
         for (loc = 0 ; loc < MSIZE ; loc++ ) {
            if (dmem[loc].pageInUse && dmem[loc].tag.value==tag 
                              && dmem[loc].generation.equals(gendata.oldGen)) 
                return false;
         };
      };
   };

      /* No problems detected, so memory is tidy */
   return true;
 }

/* CarefulDNewTag *********************************************************/

/**
 * Return an unused tag and associate the given info sequence size with
 * the tag. 
 *
 * The method throws an OutOfTagsException if all tags are in use
 * or if allocating the new tag might lead to an 
 * OutOfTransactedMemoryException in the future.
 *
 * This method was useful setting up the first test scenario's,
 * which avoided the extra complication of possible
 * OutOfTransactedMemoryException
 *
 */

//@ ensures ddata[\result.value].tagInUse; 
//@ ensures  ! ddata[\result.value].committed;

public Tag CarefulDNewTag(int size)
                  throws CardTearException,
                         OutOfTagsException

 { System.out.println ( "DNewTag" ) ;
   short tag;
   for (tag = (short)0; tag <= TSIZE-1; tag++) {
     if (! ddata[tag].tagInUse) { 
       ddata[tag] = new DTagData (true,size,false);
       break;
     };
     if (tag ==  TSIZE-1) throw new OutOfTagsException();
   };
   maxMemoryUsage += size * (MAXGEN + 1) ; 
   /* Update estimate of the maximum potential memory usage with
      this new tag added.
      Clearly, at least size * MAXGEN is needed if the maximum number 
      of generations is written. Moreover:
       - The maximum number of generations may be exceeded by one if a
         DWriteCommittedMaxGen operation is interrupted, according to 
         comment in the original Promela file.
       - For the last (uncommitted) generation there may be two versions
         around.
      so we estimate that the maximum potential memory usage for
      this new tag by  size * (MAXGEN + 1). 
    */
   if (maxMemoryUsage > MSIZE)  throw new OutOfTagsException();
   return new Tag(tag);
 }

/* DNewTag ****************************************************************/

/**
 * Return an unused tag and associate the given info sequence size with
 * the tag. 
 *
 * The method throws an OutOfTagsException if all tags are in use.
 */

//@ ensures ddata[\result.value].tagInUse; 
//@ ensures  ! ddata[\result.value].committed;

public Tag DNewTag(int size)
                  throws CardTearException,
                         OutOfTagsException

 { System.out.println ( "DNewTag" ) ;
   short tag;
   for (tag = (short)0; tag <= TSIZE-1; tag++) {
     if (! ddata[tag].tagInUse) { 
       ddata[tag] = new DTagData (true,size,false);
       break;
     };
     if (tag ==  TSIZE-1) throw new OutOfTagsException();
   };
   return new Tag(tag);
 }


/* DReadGeneration ********************************************************/

/**
 * Read the information sequence of a given tag and generation.  
 *
 * TO DO: An <code>UnusedTagException</code> should be thrown if the tag is not in
 * use or the generation doesn't exists ?
 */

//@ requires ddata[tag.value].tagInUse; // tag is in use

public /*@ pure @*/ InfoSeq DReadGeneration(Tag tag, Generation gen)
                                           throws UnusedTagException

 { //System.out.println ( "DReadGeneration" ) ;
   int loc;
   InfoSeq is = new InfoSeq();

   if (ddata[tag.value].tagInUse) {
      is.seq = ddata[tag.value].size; 
      for (loc = 0; loc <= MSIZE-1; loc++) {
         if (dmem[loc].pageInUse && dmem[loc].tag.equals(tag) 
                                 && dmem[loc].generation.equals(gen) ) {
               is.data[dmem[loc].pageNumber] = dmem[loc].info;
         };
      };
   };
   /* assert (\forall int i; 
                     0 <= i && i < is.seq;
                     is.data[i] != -666);
     */
   return is;
 }

/* DRead ******************************************************************/

/**
 * Read the information sequence of the current generation that is
 * associated with the given tag. The resulting information sequence is
 * undefined if the tag is not in use. 
 */

//@ requires ddata[tag.value].tagInUse; // tag is in use

public /*@ pure @*/ InfoSeq DRead(Tag tag)
                                 throws UnusedTagException
 { // System.out.println ( "DRead" ) ;
   GenGenbyte gendata;

   gendata = DGeneration( tag );
   return DReadGeneration( tag, gendata.newGen );

 }


/* SecureDRead (tag) ********************************************************/

/** FOR SPECIFICATION PURPOSES ONLY: Alternative implementation of <code>SecureDRead</code>. 
     <p>
    For some reason this implementation is incorrect; I haven't tried
    fixing this yet.
 */

public /*@ pure @*/ InfoSeq SecureDRead2(Tag tag)
                                 throws UnusedTagException
 { TransactedMemory clone = (TransactedMemory) clone();
   boolean done = false;
   do { try { clone.DTidy();
              done = true ;
            }
        catch (CardTearException ee) {
           };
        }
   while (!done);

   return clone.DRead(tag);
 }

/** FOR SPECIFICATION PURPOSES ONLY:
 *  Read the most recent complete information sequence for a given tag;
 *  if (no complete version of) this generation exists, return null.
 *
 * <p>
 *
 * Unlike <code>DRead</code>, <code>SecureDRead</code> will give a correct 
 * answer after a card tear that caused an incomplete write.
 *
 * This method invokes <code>SecureDRead(tag,generation)</code>
 */

//@ requires ddata[tag.value].tagInUse; // tag is in use

public /*@ pure @*/ InfoSeq SecureDRead(Tag tag)
                                 throws UnusedTagException
 { //System.out.println ( "SecureDRead" ) ;
   GenGenbyte gendata;
   Generation gen;

   if (!ddata[tag.value].tagInUse) throw new UnusedTagException();

   gendata = DGeneration( tag );
   gen = gendata.newGen;
    
   /* Return newest generation, if it exists and is not incomplete */

   InfoSeq is = SecureDRead(tag, gendata.newGen);
   if (is != null ) return is;

   /* Return to read previous generation, or null if it doesn't exist */

   return SecureDRead(tag, Generation.decrind(gendata.newGen));

 }

/*  SecureDRead (tag, generation) *****************************************/

/** FOR SPECIFICATION PURPOSES ONLY:
 * Read the information sequence of generation gen for tag;
 * if (no complete version of) this generation exists, return null.
 *
 */

public /*@ pure @*/ InfoSeq SecureDRead(Tag tag, Generation gen)
                                 throws UnusedTagException
{  //System.out.println ( "SecureDReadGeneration" ) ;
   GenGenbyte gendata;

   int loc;
   InfoSeq[] is = new InfoSeq[Version.MAXVER];
   for (int i = 0 ; i < Version.MAXVER; i++) {
        is[i] = new InfoSeq();
        is[i].seq = ddata[tag.value].size; 
   }
   boolean[][] foundVersionPage = new boolean [Version.MAXVER][InfoSeq.SSIZE];

   /* read all versions of generation gen */

   for (loc = 0; loc <= MSIZE-1; loc++) {
         if (dmem[loc].pageInUse && dmem[loc].tag.equals(tag) 
                                 && dmem[loc].generation.equals(gen) ) {
               is[dmem[loc].version.value].data[dmem[loc].pageNumber] 
                                    = dmem[loc].info;
               foundVersionPage[dmem[loc].version.value]
                               [dmem[loc].pageNumber] = true;
         };
   };

  boolean[] completeVersion = new boolean [Version.MAXVER];
  for (int ver = DPage.VA ; ver <= DPage.VC ; ver++ )  
      completeVersion[ver] = true; 
  int numberOfVersionsFound = 0;

  for (int ver = DPage.VA ; ver <= DPage.VC ; ver++ )  {
     for (int page = 0; page < ddata[tag.value].size ; page++) 
          completeVersion[ver] = completeVersion[ver] &&
               foundVersionPage[ver][page];
         if (completeVersion[ver])  numberOfVersionsFound++;
    }
  /* if more than two complete versions are found, this are seriously wrong */
  //@ assert numberOfVersionsFound <= 2;

  /* if no complete version is found, we return null */
  if (numberOfVersionsFound == 0) return null;

  /* if exactly one complete version is found, return that one */

  if (numberOfVersionsFound == 1) {
   for (int ver = DPage.VA ; ver <= DPage.VC ; ver++ ) 
      if (completeVersion[ver]) return is[ver];
  }
  
  //@ assert numberOfVersionsFound > 1;

  /* if more than one complete version is found, return the more recent one */

  for (int ver = DPage.VA ; ver <= DPage.VC ; ver++ ) {
   if (  completeVersion[ver] && completeVersion[Version.next(ver)]  ) {
     return is[Version.next(ver)];
   }
  }
  //@ assert false;
  return null;
 }

/* DRelease ***************************************************************/

/** Release all the information associated with the given tag. */

/*@ ensures !(ddata[tag.value].tagInUse) ; 
  @*/

public void DRelease(Tag tag)
                    throws CardTearException

 { System.out.println ( "DRelease" ) ;
   int loc;

   if (ddata[tag.value].tagInUse) {
      CardTearException.possibleCardTear();
      ddata[tag.value].resetTagInUse();
      maxMemoryUsage -= ddata[tag.value].size * (MAXGEN + 1) ; 
      for (loc = 0; loc < MSIZE; loc++) {
          if (dmem[loc].pageInUse && dmem[loc].tag.equals(tag) ) {
               CardTearException.possibleCardTear();
 System.out.print ( "Updating page");
 System.out.print ( loc );
               dmem[loc].resetPageInUse();
          };
      };
      CardTearException.possibleCardTear();
   };
 }

/* DCommit ****************************************************************/

/** Commit the current generation for the given tag. 
  *
  * If no generation exists for the tag, the tag will not be committed.
  */

/*@ ensures  ddata[tag.value].committed ; 
 @*/

public void DCommit(Tag tag)
                   throws CardTearException

 { System.out.println ( "DCommit" ) ;
   int loc;
   if (ddata[tag.value].tagInUse) {
      for (loc = 0; loc < MSIZE; loc++) {
      if (dmem[loc].pageInUse && dmem[loc].tag.equals(tag) ) {
          CardTearException.possibleCardTear();
          ddata[tag.value].commit();
          break;
          };
      };
      CardTearException.possibleCardTear();
   };
 }

/* DWriteFirst ***********************************************************/

/** 
 * Write the to a tag immediately after the DNewTag operation. The
 * DWriteFirst operation will write the complete information sequence
 * only if sufficient space is available. 
 */

/*@ requires ddata[tag.value].tagInUse; 
    requires ! ddata[tag.value].committed ; 
    requires (\forall int i; 0 <= i && i < MSIZE ; 
                            !(dmem[i].tag.equals(tag)));
    requires SecureDRead(tag)== null; // There shouldn't be a previous version
    requires ddata[tag.value].size == is.seq; // argument of right length
    ensures  ! ddata[tag.value].committed ; 
    ensures  DRead(tag).equals(is);
    signals  (CardTearException) 
             SecureDRead(tag)== null || SecureDRead(tag).equals(is) ;
    signals  (CardTearException) !ddata[tag.value].committed;
  @*/

private void DWriteFirst(Tag tag, InfoSeq is)
                       throws CardTearException,
                   UnusedTagException,
                   OutOfTransactedMemoryException

 { System.out.println ( "Starting DWriteFirst ----------------------- " ) ;
   dump();
   System.out.println ( "Starting DWriteFirst ----------------------- " ) ;
   System.out.print ( "Tag is "); 
   System.out.print ( tag.value ); 
   System.out.print ( ", parameter is "); is.print();
   System.out.print ( "Old value is :"); DRead(tag).print();

   int loc;
   int cnt;
   int cur = 0;
   int pno;
   boolean succeeded = false;

   /* Write the information sequence to a set of free pages as
      the 'A' version of the first generation. */

   for(pno = 1; pno <= is.seq ; pno++) {

      for(loc = cur; loc < MSIZE ; loc++) {
         if ( !dmem[loc].pageInUse) { 
          CardTearException.possibleCardTear();
 System.out.print ( "Updating page");
 System.out.print ( loc );
          dmem[loc].update(true, 
                           tag, 
                           is.data[is.seq-pno], 
                           new Generation(0), 
                           is.seq-pno, 
                           new Version(DPage.VA));
         if (is.seq -pno == 0) succeeded = true;  /* all pages written */
            break;
         };
      };
      cur = loc + 1;
   };
   if (! succeeded) throw new OutOfTransactedMemoryException();
   CardTearException.possibleCardTear();
   
   System.out.println ( "DWriteFirst --------end------------ " ) ;
   dump();
   System.out.println ( "DWriteFirst --------end------------ " ) ;
 }

/* DWriteUncommitted ******************************************************/

/**
 *  Write to a tag whose current generation is uncommitted. The
 * operation will write the complete information sequence only if
 * sufficient space is available. 
 */

/*@ requires ddata[tag.value].tagInUse; 
    requires ddata[tag.value].size == is.seq; // argument of right length
    requires ! ddata[tag.value].committed ; 
    ensures DRead(tag).equals(is);
    ensures ! ddata[tag.value].committed ; 
    signals (CardTearException) 
        SecureDRead(tag)!= null ==> 
                                SecureDRead(tag).equals(is) 
                             || SecureDRead(tag).equals(\old(DRead(tag))) ;
    signals (CardTearException) 
        SecureDRead(tag) == null ==> \old(SecureDRead(tag)) == null;
    signals (CardTearException) ! ddata[tag.value].committed;
  @*/

private void DWriteUncommitted(Tag tag, InfoSeq is, Generation gen)
                             throws CardTearException,
                                    OutOfTransactedMemoryException,
                                    UnusedTagException

 { System.out.println ( "Starting DWriteUncommitted ----------------------- " ) ;
   dump();
   System.out.println ( "Starting DWriteUncommitted ----------------------- " ) ;
   System.out.print ( "Tag is "); 
   System.out.print ( tag.value ); 
   System.out.print ( ", parameter is "); is.print();
   System.out.print ( "Old value is :"); DRead(tag).print();

   int loc;
   int pno;
   Version ver = new Version(0);
   int cur;
   boolean succeeded = false;

   /* Find the version of the newest generation. */
   for (loc = 0 ; loc < MSIZE ; loc++ ) {
      if( dmem[loc].pageInUse && dmem[loc].tag.equals(tag)
                              && dmem[loc].generation.equals(gen) ) {
         ver = dmem[loc].version ;
         break;
      };
   };
        
   /* Write a new version of the newest generation. */
   cur = 0 ;
   for (pno = 1 ; pno <= is.seq ; pno++ ) {
      for (loc = cur ; loc < MSIZE ; loc++ ) {
         if (! dmem[loc].pageInUse) {
          CardTearException.possibleCardTear();
 System.out.print ( "Updating page");
 System.out.print ( loc );
          dmem[loc].update(true, 
                           tag, 
                           is.data[is.seq-pno], 
                           gen,
                           is.seq-pno, 
                           Version.next(ver));
          if (is.seq - pno == 0) succeeded = true;  /* all pages written */
          break;
         };
      };
      cur = loc + 1;
   };
   if (! succeeded) throw new OutOfTransactedMemoryException();
   CardTearException.possibleCardTear();
             
   /* Release all the information associated with the previous
      version of the newest generation. */
   for (loc = 0 ; loc < MSIZE ; loc++ ) {
      if(  dmem[loc].pageInUse && dmem[loc].tag.equals(tag)
                         && dmem[loc].generation.equals(gen)
                         && dmem[loc].version.equals(ver)  ) {
         CardTearException.possibleCardTear();
 System.out.print ( "Updating page");
 System.out.print ( loc );
         dmem[loc].resetPageInUse();
         };
   };
   CardTearException.possibleCardTear();
 }

/* DWriteCommittedAddGen **************************************************/

/**
 * Write to a tag whose current generation has been committed. We add a
 * new generation to those associated with the tag. If this results in
 * more than the maximum allowed number of generations the oldest
 * generation must be dropped.
 *
 * If we have not reached the maximum allowed number of generations we
 * needn't drop the oldest generation. 
 */

/*@ requires ddata[tag.value].tagInUse; 
    requires ddata[tag.value].size == is.seq; // argument of right length
    ensures DRead(tag).equals(is);
    ensures ! ddata[tag.value].committed ; 
    signals (CardTearException)
              SecureDRead(tag)!= null ==>
                                  SecureDRead(tag).equals(is)
                               || SecureDRead(tag).equals(\old(DRead(tag))) ;
    signals (CardTearException)
              SecureDRead(tag) == null ==> \old(SecureDRead(tag)) == null;
    signals (CardTearException) ! ddata[tag.value].committed 
                             || ddata[tag.value].committed;
  @*/

private void DWriteCommittedAddGen(Tag tag, InfoSeq is, Generation gen)
                                 throws CardTearException,
                                        OutOfTransactedMemoryException,
                                        UnusedTagException

 { System.out.println ( "Starting DWriteCommittedAddGen ----------------------- " ) ;
   dump();
   System.out.println ( "Starting DWriteCommittedAddGen ----------------------- " ) ;
   System.out.print ( "Tag is "); 
   System.out.print ( tag.value ); 
   System.out.print ( ", parameter is "); is.print();
   System.out.print ( "Old value is :"); DRead(tag).print();

   int loc ;
   int pno ;
   int cur ;
   boolean succeeded = false;

   /* Write a committed 'A' version of a new generation. */
   cur = 0 ;
   for (pno = 1 ; pno <= is.seq ; pno++ ) {
      for (loc = cur ; loc < MSIZE ; loc++ ) {
         if (!dmem[loc].pageInUse) {
          CardTearException.possibleCardTear();
 System.out.print ( "Updating page");
 System.out.print ( loc );
          dmem[loc].update(true, 
                           tag, 
                           is.data[is.seq-pno], 
                           Generation.incrind(gen), 
                           is.seq-pno, 
                           new Version (Version.VA));
          if (is.seq - pno == 0) succeeded = true;  /* all pages written */
          break;
         };
      };
      cur = loc + 1;
   };
   if (! succeeded) throw new OutOfTransactedMemoryException();
   //BUG hence no CardTearException.possibleCardTear();

/*   Note that there is no possibleCardTear between the writing of
     the last page above and the decommitting below.
     This is because the algorithm can't cope with this.
 */
            ddata[tag.value].decommit();
            CardTearException.possibleCardTear();
 }


/* DWriteCommittedMaxGen **************************************************/

/* If we have reached the maximum allowed number of generations we must
   drop the oldest generation. */

/*@ requires ddata[tag.value].tagInUse; 
    requires ddata[tag.value].size == is.seq; // argument of right length
    requires ddata[tag.value].committed ; 
    ensures DRead(tag).equals(is);
    ensures ! ddata[tag.value].committed ; 
    signals (CardTearException)
              SecureDRead(tag)!= null ==>
                                  SecureDRead(tag).equals(is)
                               || SecureDRead(tag).equals(\old(DRead(tag))) ;
    signals (CardTearException)
              SecureDRead(tag) == null ==> \old(SecureDRead(tag)) == null;
    signals (CardTearException) ddata[tag.value].committed 
                             || !ddata[tag.value].committed;
  @*/

 private void DWriteCommittedMaxGen (Tag tag, InfoSeq is, Generation gen,
                                                          Generation oldgen) 
                                   throws CardTearException,
                                          OutOfTransactedMemoryException,
                                          UnusedTagException
 { System.out.println ( "Starting DWriteCommittedMaxGen ----------------------- " ) ;
   dump();
   System.out.println ( "Starting DWriteCommittedMaxGen ----------------------- " ) ;
   System.out.print ( "Tag is "); 
   System.out.print ( tag.value ); 
   System.out.print ( ", parameter is "); is.print();
   System.out.print ( "Old value is :"); DRead(tag).print();

   GenGenbyte gendata;
   int loc ;
   int cur ;
   int pno ;
   boolean succeeded = false;

   /* The maximum number of generations has been written.
      Write a committed, 'A' version of a new generation. */

   cur = 0;
   for (pno = 1 ; pno <= is.seq ; pno++ ) {
      for (loc = cur ; loc < MSIZE ; loc++ ) {
         if (! dmem[loc].pageInUse) {
          CardTearException.possibleCardTear();
 System.out.print ( "Updating page");
 System.out.print ( loc );
          dmem[loc].update(true, 
                           tag, 
                           is.data[is.seq-pno], 
                           Generation.incrind(gen), 
                           is.seq-pno, 
                           new Version(Version.VA));
          if (is.seq - pno == 0) succeeded = true;  /* all pages written */
          break;
         };
      };
      cur = loc + 1;
   };
   if (! succeeded) throw new OutOfTransactedMemoryException();
   //BUG hence no CardTearException.possibleCardTear();

/*   Note that there is no possibleCardTear between the writing of
     the last page above and the decommitting below.
     This is because the algorithm can't cope with this.
 */
   ddata[tag.value].decommit();
   CardTearException.possibleCardTear();

   /* Release all the information associated with the oldest
      generation. */

   for (loc = 0 ; loc < MSIZE ; loc++ ) {
      if(  dmem[loc].pageInUse && dmem[loc].tag.equals(tag)
                         && dmem[loc].generation.equals(oldgen)  ) {
         CardTearException.possibleCardTear();
 System.out.print ( "Updating page");
 System.out.print ( loc );
         dmem[loc].resetPageInUse();
         };
   };
   CardTearException.possibleCardTear();
 }




/* DWrite *****************************************************************/

/** DWrite operation will update the current generation, if it is uncommited,
 * or start a new generation, if it the current generation is committed
 * or if no generations exist.
 *
 * If the tag is not is use, an UnusedTagException is thrown
 */

/*@ requires ddata[tag.value].tagInUse; 
    requires isDWeakTidy();
    requires ddata[tag.value].size == is.seq; // argument of right length
    requires SecureDRead(tag)!= null ==>
                 SecureDRead(tag).equals(DRead(tag));
    //  The requires above just checks that the memory is tidied - at least
    //  for this tag.
    //  A violation of this preconditions means our test scenario is
    //  incorrect and hasn't tidied after a card tear
    ensures DRead(tag).equals(is);
    ensures ! ddata[tag.value].committed ; 
 // More detailed postconditions are given in the DWrite* methods
 // because DWriteFirst must be treated differently.
    signals (CardTearException) ddata[tag.value].committed 
                             || !ddata[tag.value].committed;
  @*/

public void DWrite (Tag tag, InfoSeq is)
                          throws CardTearException,
                                 UnusedTagException,
                                 OutOfTransactedMemoryException

 { System.out.println ( "DWrite" ) ;
   GenGenbyte    gendata ;

   if  (!ddata[tag.value].tagInUse) {throw new UnusedTagException();};

   gendata = DGeneration(tag);

   try {
   if (gendata.cnt==0) {
          DWriteFirst(tag, is) ;
   } else if (!ddata[tag.value].committed) {
          DWriteUncommitted (tag, is, gendata.newGen);
   } else if ( gendata.cnt > 0 && gendata.cnt < MAXGEN) {
          DWriteCommittedAddGen (tag, is, gendata.newGen);
   } else if ( gendata.cnt >= MAXGEN  ) {
          DWriteCommittedMaxGen (tag, is, gendata.newGen, gendata.oldGen);
   }
   }
   catch (CardTearException e) {
       System.out.println ( "Abnormal termination: DRead is now :"); 
       DRead(tag).print();
       throw e;
   }
 }

/****************************************************************************/

 /** 
  * Mark all pages and tags as not in use.
  */

public void Dinitialise () 
 { System.out.println ( "Dinitialise" ) ;
   byte cur ;
   for(cur = 0; cur < MSIZE; cur++) {
         dmem[cur] = new DPage(false, 
                               new Tag(0), 0, 
                               new Generation(0), 0, 
                               new Version (0));
   };

   for(cur = 0; cur <= TSIZE-1-1; cur++) {
         ddata[cur] = new DTagData(false, 0, false);
   };
}

/*************************** ONLY FOR TESTING PURPOSES ***********************/


/** FOR TESTING PURPOSES ONLY: print the entire content of the memory */
public void dump ()  
 {
   short i;
   Tag tag;
   InfoSeq is;

   try {
     System.out.println ( " Contents of memory dmem ****************** " ) ;
     for (i = 0; i < MSIZE; i++) 
      { System.out.print ( " Page " ) ;
        System.out.print ( i ) ;
        System.out.print ( " " ) ;
        dmem[i].print();
      }
  
     System.out.println ( " DReading all tags in use ****************** " ) ;
     for (i = 0; i < TSIZE; i++) 
      { 
        if (ddata[i].tagInUse) 
          {  System.out.print ( "Tag " ) ;
             System.out.print ( i ) ; 
             System.out.println ( " is in use. " ) ;
             tag = new Tag(i);
             System.out.print ( " Its DReadGeneration is " ) ;
	     DGeneration(tag).print();
             if (DGeneration(tag).cnt !=0) {
                System.out.print ( " Its DRead is " ) ;
                DRead(tag).print();
                System.out.print ( " Its SecureDRead is " ) ;
                is = SecureDRead(tag);
                if (is != null) is.print(); 
                           else System.out.println("null");
             }
          }
      }
    }
    catch (UnusedTagException e) {
          System.out.print ( "UnusedTagException??? - shouldn't happen !!" );
          //@ assert false;
    }
   
 }

/** FOR TESTING PURPOSES ONLY */

public /*@ pure @*/ Object clone () {
   try {
     TransactedMemory deepClone = (TransactedMemory) super.clone();
     deepClone.dmem = new DPage[MSIZE];
     deepClone.ddata = new DTagData[TSIZE];

     for (int i = 0; i < ddata.length; i++) {
       deepClone.ddata[i] = (DTagData) ddata[i].clone();
     }
     for (int i = 0; i < dmem.length; i++) {
       deepClone.dmem[i] = (DPage) dmem[i].clone();
     }
     return deepClone;
   } catch (CloneNotSupportedException e) {
       return null; // never invoked
   }
  }

}
