package JavaImplementation;
//import edu.iastate.cs.jml.models.*;

public class DPage{

 final public static int MAXGEN = TransactedMemory.MAXGEN; 
 final public static int VA = 0; 
 final public static int VB = 1; 
 final public static int VC = 2; 

 public boolean pageInUse;
 
 public int tag; 
 //@ invariant 0 <= tag && tag < TransactedMemory.TSIZE ;
 
 public int info;
 
 public int generation;
 //@ invariant  0 <= generation && generation < MAXGEN;

 public int pageNumber;
 //@ invariant  0 <= pageNumber && pageNumber < InfoSeq.SSIZE;

 public int version;
 //@ invariant  version == VA || version == VB || version == VC;


/************************************************************************/
 
 public DPage (boolean pageInUse, 
               int     tag, 
               int     info,                
               int     generation,                
               int     pageNumber, 
               int     version)
 { this.pageInUse = pageInUse;
   this.tag = tag;
   this.info = info;
   this.generation = generation;
   this.pageNumber = pageNumber;
   this.version = version;
 }

/************************************************************************/
// A CardTear may happen before or after the update.
// The update itself is considered to be atomic.
 
 public void update (boolean pageInUse,
               int     tag,
               int     info,
               int     generation,
               int     pageNumber,
               int     version)
        throws CardTearException

 //@ requires 0 <= generation && generation < MAXGEN;
 //@ requires 0 <= pageNumber && pageNumber < TransactedMemory.MSIZE;
 //@ requires version == VA || version == VB || version == VC;

 { CardTearException.possibleCardTear();
   this.pageInUse = pageInUse;
   this.tag = tag;
   this.info = info;
   this.generation = generation;
   this.pageNumber = pageNumber;
   this.version = version;
   System.out.print ( "writing ; inuse=" ) ;
   System.out.print ( pageInUse ) ;
   System.out.print ( " tag=" ) ;
   System.out.print ( tag ) ;
   System.out.print ( " info=" ) ;
   System.out.print ( info ) ;
   System.out.print ( " pagenumber=" ) ;
   System.out.print ( pageNumber ) ;
   System.out.print ( " version=" ) ;
   System.out.println ( version ) ;
   CardTearException.possibleCardTear();
 }
 
}
