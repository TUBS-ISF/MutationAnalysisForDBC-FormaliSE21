package JavaImplementation;

/* ***********************************************************************

A Java/JML translation of the SPIN/C transacted memory described in 
Section 5 of P.H. Hartel, M.J. Butler, E. de Jong , and M. Longley
"Transacted Memory for Smart Card".

Erik Poll

**************************************************************************/

// Tag == short 
// Size == int 
// Info == short 

//import edu.iastate.cs.jml.models.*;

public class TransactedMemory {

 final static public int MAXGEN = 3; /* maximum number of generations */

 final static public int MAXIND = 5; /* maximum number of generation indices */

 final static int incrind(int i) { return ((i+1)% MAXIND); };
 final static int decrind(int i) { return ((i+MAXIND-1)% MAXIND); };

 final static public int MAXVER = 3; /* maximum number of versions */
 final static int next(int v) { return ((v+1)% MAXVER); };

 final static public int TSIZE = 2;  /* size of the tag space */
 public DTagData[] ddata = new DTagData[TSIZE];

 //@ invariant ddata != null && ddata.length == TSIZE;
 //@ invariant (\forall int i; 0 <= i && i < TSIZE ==> ddata[i] != null);

 final static public int MSIZE = 10; /* memory size in DPage units */
 public DPage[] dmem = new DPage[MSIZE];

 //@ invariant dmem != null && dmem.length == MSIZE;
 //@ invariant (\forall int i; 0 <= i && i < MSIZE ==> dmem[i] != null);

 final static public int ISIZE = 4;

/****************************************************************************/
 public TransactedMemory()
 { short i;
   for (i = 0; i < TSIZE; i++) ddata[i] = new DTagData (false,0,false);
   for (i = 0; i < MSIZE; i++) dmem[i] = new DPage (false, 0, 0, 0, 0, 0);
 }

/****************************************************************************/
/* Discover the generation indices associated with a tag, and then
   return the indices of the oldest and newest generation, as well as
   the number of generations. If there are no generations, then the old
   and new generations induces are undefined. This issue was identified
   with SPIN. */

public GenGenbyte DGeneration (short tag)  
       throws // CardTearException, ?? probably no need for this ??
              UnusedTagException
/*@ requires 0 <= tag && tag < TSIZE;
     ensures \result != null;
  */
	     
              
 { System.out.println ( "DGeneration" ) ;

   int   loc ;
   int   fst, snd, old, neww ;
   boolean  cur;
   boolean[] map = new boolean[MAXIND];
   int  cnt ;
   GenGenbyte gendata ;

   old = 0; neww = 0;

   for (fst = 0; fst <= MAXIND-1 ; fst++) {
         map[fst] = false; 
         // not needed of course, since bool's are initialized to false anyway
   }; 
   for (loc = 0 ; loc <= MSIZE-1 ; loc++) {
         if(dmem[loc].pageInUse && dmem[loc].tag==tag  ) {
            map[dmem[loc].generation] = true;
         };
   };
   cur = map[0] ;
   for (fst = 1 ; fst <= MAXIND-1 ; fst++) {
         if (map[fst]!=cur) {
            break;
         };
   };
   cur =! cur;
   for (snd = fst+1; snd <= MAXIND-1 ; snd++) {
         if(  map[snd]!=cur  ) {
            break;
         };
   };
   if (  map[0]  ) {
         old = snd % MAXIND ;
         neww = fst-1;
   } else if(  ! map[0]  ) {
         old = fst % MAXIND;
         neww = snd-1;
   };
   cnt = 0 ;
   for(fst = 0 ; fst <= MAXIND-1 ; fst++) {
         if(map[fst]) {
            cnt = cnt + 1;
         };
   };
   gendata = new GenGenbyte();
   gendata.oldGen = old;
   gendata.newGen = neww;
   gendata.cnt = cnt;
   return gendata;
}



/****************************************************************************/

/* Tidy up the memory. This operation should be used once only, upon
   restart.  */

public void DTidy()
       throws CardTearException,
              UnusedTagException

 { System.out.println ( "DTidy" ) ;
   short   tag ;
   int   gen ;
   int    loc ;
   boolean   ok ;
   int    ver ;
   boolean[] map ;
   GenGenbyte gendata ;

  map = new boolean[MAXIND];
   
      /* Detect and free the locations that are marked as being in use
         by a tag that is itself marked as not in use. */

   for (loc = 0 ; loc < MSIZE ; loc++ ) {
     if (dmem[loc].pageInUse) { 
         if ( !ddata[dmem[loc].tag].tagInUse || !ddata[dmem[loc].tag].committed ) {
            dmem[loc].pageInUse = false;
         };
     }
   };
  
      /* Detect whether page 0 has been recorded fordo all combinations
         of tag, generation, and version. Then remove any locations fordo
         such a combination when no page 0 is present. */

      for (tag = (short)0 ; tag <= TSIZE-1 ; tag++ ) {
         for (gen = 0 ; gen <= MAXIND-1 ; gen++ ) {
            for (ver = DPage.VA ; ver <= DPage.VC ; ver++ ) {
               ok = false ;
               for (loc = 0 ; loc < MSIZE ; loc++ ) {
                  if (dmem[loc].pageInUse && dmem[loc].tag==tag 
                                    && dmem[loc].generation==gen 
                                    && dmem[loc].version==ver 
                                    && dmem[loc].pageNumber==0) {
                     ok = true ;
                     break;
                  };
               };
               if (!ok) {
                  for (loc = 0 ; loc < MSIZE ; loc++ ) {
                     if (dmem[loc].pageInUse && dmem[loc].tag==tag 
                                          && dmem[loc].generation==gen
                                          && dmem[loc].version==ver) {
                        dmem[loc].pageInUse = false;
                     };
                  };
               };
            };
         };
      };

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
               if (dmem[loc].pageInUse && dmem[loc].tag==tag 
                                       && dmem[loc].generation==gen) {
                  map[dmem[loc].version] = true;
               };
            };
            for (ver = DPage.VA ; ver <= DPage.VC ; ver++ ) {
               if(  map[ver] && map[next(ver)]  ) {
                  for (loc = 0 ; loc < MSIZE ; loc++ ) {
                     if (dmem[loc].pageInUse && dmem[loc].tag==tag 
                                       && dmem[loc].generation==gen
                                       && dmem[loc].version==ver  ) {
                        dmem[loc].pageInUse = false;
                     };
                  };
               };
            };
         };
      };

      /* Discover if there is one more complete generation than the
         maximum allowed. Remove the locations associated with the
         oldest generation. */

      for (tag = (short)0; tag <= TSIZE-1 ; tag++ ) {
         gendata = DGeneration(tag);
         if (gendata.cnt > MAXGEN  ) {
            for (loc = 0 ; loc < MSIZE ; loc++ ) {
               if (dmem[loc].pageInUse && dmem[loc].tag==tag 
                                 && dmem[loc].generation==gendata.oldGen) {
                  dmem[loc].pageInUse = false;
               };
            };
         };
      };
 }

/****************************************************************************/

/* Return an unused tag and associate the given info sequence size with
   the tag. The result of this operation is undefined if the memory is
   full. */

public short DNewTag(int size)
             throws CardTearException,
                    OutOfTagsException

 { System.out.println ( "DNewTag" ) ;
   short tag;
   for (tag = (short)0; tag <= TSIZE-1; tag++) {
     if (! ddata[tag].tagInUse) { 
       CardTearException.possibleCardTear();
       ddata[tag] = new DTagData (true,size,false);
       CardTearException.possibleCardTear();
       break;
     };
   };
   return tag;
 }


/****************************************************************************/

/* Read the information sequence of a given tag and generation.  The
   resulting information sequence is undefined if the tag is not in
   use. */


public InfoSeq DReadGeneration(short tag, int gen)
               throws // CardTearException, ?? probably no need for this ??
                      UnusedTagException
/*@ requires  0 <= tag && tag < TSIZE;
     ensures  \result != null;
  */

 { System.out.println ( "DReadGeneration" ) ;
   int loc;
   InfoSeq is = new InfoSeq();

   if (ddata[tag].tagInUse) {
      is.seq = ddata[tag].size; //??
      for (loc = 0; loc <= MSIZE-1; loc++) {
         if (dmem[loc].pageInUse && dmem[loc].tag == tag 
                                 && dmem[loc].generation == gen ) {
               is.data[dmem[loc].pageNumber] = dmem[loc].info;
         };
      };
   };
   return is;
 }

/****************************************************************************/

/* Read the information sequence of the current generation that is
   associated with the given tag. The resulting information sequence is
   undefined if the tag is not in use. */

public InfoSeq DRead(short tag)
               throws // CardTearException, ?? probably no need for this ??
                      UnusedTagException
/*@ requires  0 <= tag && tag < TSIZE;
     ensures  \result != null;
  */

 { System.out.println ( "DRead" ) ;
   GenGenbyte gendata;

   gendata = DGeneration( tag );
   return DReadGeneration( tag, gendata.newGen );
 }

/****************************************************************************/

/* Release all the information associated with the given tag. */

public void DRelease(short tag)
            throws CardTearException
/*@ requires  0 <= tag && tag < TSIZE;
  */


 { System.out.println ( "DRelease" ) ;
   int loc;

   if (ddata[tag].tagInUse) {
      CardTearException.possibleCardTear();
      ddata[tag].tagInUse = false;
      CardTearException.possibleCardTear();
      for (loc = 0; loc < MSIZE; loc++) {
          if (dmem[loc].pageInUse && dmem[loc].tag == tag ) {
               CardTearException.possibleCardTear();
               dmem[loc].pageInUse = false;
          };
      };
      CardTearException.possibleCardTear();
   };
 }

/****************************************************************************/

/* Commit the current generation for the given tag.  */

public void DCommit(short tag)
            throws CardTearException
/*@ requires  0 <= tag && tag < TSIZE;
  */

 { System.out.println ( "DCommit" ) ;
   int loc;
   if (ddata[tag].tagInUse) {
      for (loc = 0; loc < MSIZE; loc++) {
      if (dmem[loc].pageInUse && dmem[loc].tag == tag ) {
          CardTearException.possibleCardTear();
          ddata[tag].committed = true;
          break;
          };
      };
      CardTearException.possibleCardTear();
   };
 }

/****************************************************************************/

/* Write the to a tag immediately after the DNewTag operation. The
   DWriteFirst operation will write the complete information sequence
   only if sufficient space is available. */

public void DWriteFirst(short tag, InfoSeq is)
            throws CardTearException,
                   UnusedTagException,
                   OutOfTransactedMemoryException
/*@ requires  0 <= tag && tag < TSIZE;
  */

 { System.out.println ( "DWriteFirst" ) ;
   int loc;
   int cnt;
   int cur;
   int pno;

   if (ddata[tag].tagInUse && ddata[tag].size == is.seq) {
      
         /*  Count the number of locations associated with the given tag. */

         cnt = 0 ;
         for (loc = 0; loc < MSIZE; loc++) {
             if (dmem[loc].pageInUse && dmem[loc].tag == tag ) {
                cnt = cnt +1;
             };
         };
         if (cnt ==0) {
            cur = 0;
            
            /* The tag is indeed to be written to for the first time.
               Write the information sequence to a set of free pages as
               the 'A' version of the first generation. */

            for(pno = 1; pno <= is.seq ; pno++) {

               for(loc = cur; loc < MSIZE ; loc++) {
                  if ( !dmem[loc].pageInUse) { 
                   dmem[loc].update(true, 
                                    tag, 
                                    is.data[is.seq-pno], 
                                    0, 
                                    is.seq-pno, 
                                    DPage.VA);
                     break;
                  };
               };
               cur = cnt + 1;
            };
         };
   };
 }

/****************************************************************************/

/* Write to a tag whose current generation is uncommitted. The
   operation will write the complete information sequence only if
   sufficient space is available. */

public void DWriteUncommitted(short tag, InfoSeq is)
            throws CardTearException,
                   OutOfTransactedMemoryException,
                   UnusedTagException
/*@ requires  0 <= tag && tag < TSIZE;
  */

 { System.out.println ( "DWriteUncommitted" ) ;
   GenGenbyte gendata;
   int loc;
   int pno;
   int ver = 0;
   int cur;

   if (ddata[tag].tagInUse && ddata[tag].size == is.seq ) {
         gendata = DGeneration (tag) ; ;
         if (gendata.cnt!=0 && ! ddata[tag].committed) {
                 
            /* Find the version of the newest generation. */
            for (loc = 0 ; loc < MSIZE ; loc++ ) {
               if( dmem[loc].pageInUse && dmem[loc].tag==tag 
                                       && dmem[loc].generation==gendata.newGen  ) {
                  ver = dmem[loc].version ;
                  break;
               };
            };
                 
            /* Write a new version of the newest generation. */
            cur = 0 ;
            for (pno = 1 ; pno <= is.seq ; pno++ ) {
               for (loc = cur ; loc < MSIZE ; loc++ ) {
                  if (! dmem[loc].pageInUse) {
                   dmem[loc].update(true, tag, is.data[is.seq-pno], 
                                    0, is.seq-pno, next(ver));
                     break;
                  };
               };
               cur = loc + 1; // was cur = cur + 1, bug according to Pieter
            };
             
            /* Release all the information associated with the previous
               version of the newest generation. */
            for (loc = 0 ; loc < MSIZE ; loc++ ) {
               if(  dmem[loc].pageInUse && dmem[loc].tag==tag 
                                  && dmem[loc].generation==gendata.newGen
                                  && dmem[loc].version==ver  ) {
                  dmem[loc].pageInUse = false;
               };
            };
         };
   };
 }

/****************************************************************************/

/* Write to a tag whose current generation has been committed. We add a
   new generation to those associated with the tag. If this results in
   more than the maximum allowed number of generations the oldest
   generation must be dropped.

   If we have not reached the maximum allowed number of generations we
   needn't drop the oldest generation. */

public void DWriteCommittedAddGen(short tag, InfoSeq is)
            throws CardTearException,
                   UnusedTagException
/*@ requires  0 <= tag && tag < TSIZE;
  */

 { System.out.println ( "DWriteCommittedAddGen" ) ;
   GenGenbyte gendata;
   int loc ;
   int pno ;
   int cur ;

   if (ddata[tag].tagInUse && ddata[tag].size==is.seq) {
         gendata = DGeneration (tag); 
         if( gendata.cnt > 0 && gendata.cnt < MAXGEN && ddata[tag].committed ){ 
            /* Write a committed 'A' version of a new generation. */
            cur = 0 ;
            for (pno = 1 ; pno <= is.seq ; pno++ ) {
               for (loc = cur ; loc < MSIZE ; loc++ ) {
                  if (!dmem[loc].pageInUse) {
                   dmem[loc].update(true, tag, is.data[is.seq-pno], 
                                    incrind(gendata.newGen), 
                                    is.seq-pno, 
                                    DPage.VA);
                     break;
                  };
               };
               cur = loc + 1;
            };
         };
      };
 }


/****************************************************************************/

/* If we have reached the maximum allowed number of generations we must
   drop the oldest generation. */

 public void DWriteCommittedMaxGen (short tag, InfoSeq is) 
             throws CardTearException,
                    UnusedTagException
/*@ requires  0 <= tag && tag < TSIZE;
  */

 { System.out.println ( "DWriteCommittedMaxGen" ) ;
   GenGenbyte gendata;
   int loc ;
   int cur ;
   int pno ;

   if (ddata[tag].tagInUse && ddata[tag].size==is.seq) {
         gendata = DGeneration (tag);
         if (gendata.cnt==MAXGEN && ddata[tag].committed) {

            /* The maximum number of generations has been written.
               Write a committed, 'A' version of a new generation. */

            cur = 0;
            for (pno = 1 ; pno <= is.seq ; pno++ ) {
               for (loc = cur ; loc < MSIZE ; loc++ ) {
                  if (! dmem[loc].pageInUse) {
                   dmem[loc].update(true, tag, is.data[is.seq-pno], 
                                    incrind(gendata.newGen), 
                                    is.seq-pno, 
                                    DPage.VA);
                     break;
                  };
               };
               cur = loc + 1;
            };

            /* Release all the information associated with the oldest
               generation. */


            for (loc = 0 ; loc < MSIZE ; loc++ ) {
               if(  dmem[loc].pageInUse && dmem[loc].tag==tag 
                                  && dmem[loc].generation==gendata.oldGen  ) {
                  dmem[loc].pageInUse = false;
                  };
            };
         };
      };
 }

/****************************************************************************/

public void Dinitialise () 
 { System.out.println ( "Dinitialise" ) ;
   byte cur ;
   for(cur = 0; cur < MSIZE; cur++) {
         dmem[cur] = new DPage(false, 0, 0, 0, 0, 0);
   };

   for(cur = 0; cur <= TSIZE-1-1; cur++) {
         ddata[cur] = new DTagData(false, 0, false);
   };
}

}


