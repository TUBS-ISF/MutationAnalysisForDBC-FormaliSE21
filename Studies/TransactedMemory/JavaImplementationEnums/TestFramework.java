package JavaImplementationEnums;

/*************************************************************
 * A framework for testing of {@link TransactedMemory}
 * <p>
 * Idea behind this test scenario:
 * <ul>
 *
 * <li> All tags are initialised to [0 ,.., 0]
 *
 * <li> We keep repeating the following 3 steps
 * <ol>
 * <li> choose a random tag
 * <li> if it's commited, start a new generation, increasing the 
 *      last integer in its information sequence by 1, using 
 *      the method <code>Increase()</code>.
 * <li> if it's uncommited, then choose between
 *     <ul>
 *     <li> permuting/rotating its information sequence, using 
 *         the method  <code>Rotate()</code>.
 *     <li> increasing the last integer in its information sequence by 1
 *          and committing, using the method
 *          <code>increaseAndCommit()</code>.
 *     </ul>
 * and go back to 1.
 * </ol>
 *
 * <li> We can then test if things work ok using the method 
 *      <code>verify</code>by testing that 
 *    <ul>
 *    <li> all committed generations have an information sequence with an
 *         even sum 
 *    <li> the difference between these sums for two succeeding committed 
 *         generations is 2
 *    <li> if the last generation is uncommitted, it is 1 bigger than
 *         the prededing (committed) generation
 * </ul>
 * </ul>
 *
 * A CardTearException may be thrown before and after every write 
 * to EEPROM, _except_ between the writing of the new generation and 
 * the subsequent uncomitting in the method DWriteCommittedAddGen and 
 * DWriteCommittedMaxGen.
 *
 * @author Erik Poll
 ***************************************************************/

public class TestFramework {
 
 /**
  * The instance of <code>TransactedMemory</code> that all 
  * methods act on.
  */

 public static TransactedMemory theTransactedMemory 
                   = TransactedMemory.theTransactedMemory;

 /***************************************************************
  * Keep tidying until it has succeeded.
  * I.e. if a card tear occurs during tidying, repeat the process.
  ***************************************************************/

  public static void ReallyDTidy () 
                     throws UnusedTagException
  {
   System.out.println ( "ReallyDTidy " ) ;
   boolean done = false;
   do { try { theTransactedMemory.DTidy();
              done = true ;
            }
        catch (CardTearException ee) {
           };
        }
   while (!done);
  }

 /***************************************************************
  * Keep releasing <code>tag</code> until it has succeeded.
  * I.e. if a card tear occurs during tidying, repeat the process.
  ***************************************************************/

  public static void ReallyDRelease (Tag tag)
                     throws UnusedTagException
  {
   System.out.println ( "ReallyDRelease" ) ;
   boolean done = false;
   do { try { theTransactedMemory.DRelease(tag);
              done = true ;
            }
        catch (CardTearException ee) {
              ReallyDTidy();
           };
        }
   while (!done);
  }


 /***************************************************************
  * Read the <code>InfoSeq</code> that is the current value for 
  * <code>tag</code>, increase the last element in this information 
  * sequence, and write it back.
  ***************************************************************/

 public static void Increase(Tag tag) 
                     throws UnusedTagException,
                            OutOfTransactedMemoryException ,
                            CardTearException
 { System.out.print ( "TestFramework. Increasing tag ");
   System.out.print ( tag.value);
   System.out.print ( ". Old value : " ) ;
   InfoSeq is = theTransactedMemory.DRead(tag);
   is.print();
   is.data[is.seq-1] += 1;
   theTransactedMemory.DWrite(tag,is);
 }

 /***************************************************************
  * Read the <code>InfoSeq</code> that is the current value for 
  * <code>tag</code>, rotate the elements in this information 
  * sequence, and write it back.
  ***************************************************************/

 public static void Rotate(Tag tag) 
                             throws UnusedTagException,
                                    OutOfTransactedMemoryException ,
                                    CardTearException
 { System.out.print ( "TestFramework. Rotating tag " ) ;
   System.out.print ( tag.value);
   System.out.print ( ". Old value : " ) ;
   InfoSeq is = theTransactedMemory.DRead(tag);
   is.print();
   int     k  = is.data[0];
   for ( int i = 0; i < is.seq-1; i++ ) { is.data[i] = is.data[i+1]; };
   is.data[is.seq-1] = k;
   theTransactedMemory.DWrite(tag,is); // was DWriteUncommitted
 }

 /***************************************************************
  * Read the <code>InfoSeq</code> that is the current value for 
  * <code>tag</code>, increase the last element in this information
  * sequence, write it back, and commit <code>tag</code>.
  ***************************************************************/

 public static void IncreaseAndCommit(Tag tag) 
                             throws UnusedTagException,
                                      OutOfTransactedMemoryException ,
                                      CardTearException
 { System.out.print ( "TestFramework. IncreaseAndCommiting tag" ) ;
   System.out.print ( tag.value);
   System.out.print ( ". Old value : " ) ;
   boolean done = false;
   InfoSeq is =null;

   is = theTransactedMemory.DRead(tag);
   is.print();
   is.data[is.seq-1] += 1;
   theTransactedMemory.DWrite(tag,is);
   theTransactedMemory.DCommit(tag);
 }


 /***************************************************************
  * Verify for a given <code>tag</code> that 
  * <ul>
  * <li> (a) all committed generations have an even sum 
  * <li> (b) the difference between two succeeding committed generations 
  *          is 2
  * <li> (c) if the last generation is uncommitted, it is 1 bigger than
  *          the prededing generation
  * <ul>
  ***************************************************************/

 public static void Verify(Tag tag) 
                             throws UnusedTagException 
 {
    GenGenbyte gendata ;
    Generation gen;
    InfoSeq  is, previousIs ;
    byte     ind ;
    int      sum, previousSum ;

   previousSum = 0;
   previousIs = null;

   /* Read info associated with tag */
   gendata = theTransactedMemory.DGeneration(tag);

   gen = new Generation(gendata.oldGen.value);

   /* for all generations that exist for this tag */
   for (ind = 1; ind <= gendata.cnt; ind++){

         /* Read a generation */
         is = theTransactedMemory.DReadGeneration (tag, gen);

         /* Calculate the sum of this generation */
         sum = 0;
         for ( int i = 0; i < is.seq ; i++)  { sum += is.data[i]; };
    
         /* All committed generations - ie all older generations, and
            the newest generation if it is committed - should have an
            even sum */
         if ( (ind < gendata.cnt)  
            ||
            (ind == gendata.cnt &&
              theTransactedMemory.ddata[tag.value].committed ) ) { 
            if (sum % 2 != 0) {
                 System.out.print ( "Problem with tag ");
                 System.out.print ( tag.value );
                 System.out.print ( " generation " );
                 System.out.print ( gen.value );
                 is.print();
                 System.out.print ( " Sum " );
                 System.out.print ( sum );
                 System.out.println ( " should be even-" );
            }
            //@ assert sum % 2 == 0; 
         };

         /* If newest generation is uncommitted, it should have odd sum */
         if (ind == gendata.cnt && 
             !theTransactedMemory.ddata[tag.value].committed) { 
            if (java.lang.Math.abs(sum % 2) != 1) {
                 System.out.print ( "Problem with tag ");
                 System.out.print ( tag.value );
                 System.out.print ( " generation " );
                 System.out.print ( gen.value );
                 is.print();
                 System.out.print ( " Sum " );
                 System.out.print ( sum );
                 System.out.println ( " should be odd-" );
            }
            //@ assert java.lang.Math.abs(sum % 2) == 1; 
         };
         /* Test difference with previous generations, except for the oldest 
            generation */
         if (1 < ind) {
           if ( (ind < gendata.cnt)  
               ||
               (ind == gendata.cnt &&
                theTransactedMemory.ddata[tag.value].committed ) ) { 
                 if (sum-previousSum != 2) {
                      System.out.print ( "Problem with tag ");
                      System.out.print ( tag.value );
                      System.out.print ( " generation " );
                      System.out.print ( gen.value );
                      System.out.print ( " : " );
                      System.out.print ( " Sum " );
                      System.out.print ( sum );
                      System.out.print ( " " );
                      is.print();
                      System.out.print ( "Diff with previous : " ); 
                      previousIs.print();
                      System.out.println ( "should be 2" ); 
                 }   
                 //@ assert sum-previousSum == 2;
           };

           if (ind == gendata.cnt && 
               !theTransactedMemory.ddata[tag.value].committed) { 
                 if (sum-previousSum != 1) {
                      System.out.print ( "Problem with tag ");
                      System.out.print ( tag.value );
                      System.out.print ( " generation " );
                      System.out.print ( gen.value );
                      System.out.print ( " : " );
                      System.out.print ( " Sum " );
                      System.out.print ( sum );
                      System.out.print ( " " );
                      is.print();
                      System.out.print ( "Diff with previous : "); 
                      previousIs.print();
                      System.out.println ( "should be 1" ); 
                 }
                 //@ assert sum - previousSum == 1; 
           };
        };
      
        gen = Generation.incrind(gen);
        previousSum = sum;
        previousIs = is;
   };
 }
 /** */
}
