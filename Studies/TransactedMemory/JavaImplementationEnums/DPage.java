package JavaImplementationEnums;

//import org.jmlspecs.models.*;

/************************************************************************
 *
 * A page of EEPROM memory 
 *
 * @author Erik Poll
 *
 */

public class DPage{

 final public static int MAXGEN = TransactedMemory.MAXGEN; 
 final public static int VA = 0; 
 final public static int VB = 1; 
 final public static int VC = 2; 

 public boolean pageInUse;
 
 public Tag tag; 
 
 public int info;
 
 public Generation generation;

 public int pageNumber;

 public Version version;

/************************************************************************/
 
 public DPage (boolean pageInUse, 
               Tag     tag, 
               int     info,                
               Generation generation,                
               int     pageNumber, 
               Version version)
 { this.pageInUse = pageInUse;
   this.tag = tag;
   this.info = info;
   this.generation = generation;
   this.pageNumber = pageNumber;
   this.version = version;
 }



/************************************************************************/
 
 //@ requires 0 <= pageNumber && pageNumber < TransactedMemory.MSIZE;

 /** An atomic update of an EEPROM page */

 public void update (boolean pageInUse,
               Tag     tag,
               int     info,
               Generation     generation,
               int     pageNumber,
               Version version)
        throws CardTearException

 { 
   this.pageInUse = pageInUse;
   this.tag = tag;
   this.info = info;
   this.generation = generation;
   this.pageNumber = pageNumber;
   this.version = version;
print ();
 }


 //@ requires 0 <= pageNumber && pageNumber < TransactedMemory.MSIZE;

 public void resetPageInUse () throws CardTearException
 {  
     update(false, this.tag, this.info, this.generation, 
                  this.pageNumber, this.version);
 }
 

/***************************

  ONLY FOR TESTING PURPOSES 
 
 ****************************/

/** FOR TESTING PURPOSES ONLY */

public void print() {
System.out.print ( " inuse=" ) ;
System.out.print ( pageInUse ) ;
System.out.print ( " tag=" ) ;
System.out.print ( tag.value ) ;
System.out.print ( " page=" ) ;
System.out.print ( pageNumber ) ;
System.out.print ( " generation=" ) ;
System.out.print ( generation.value ) ;
System.out.print ( " version=" ) ;
System.out.print ( version.value ) ;
System.out.print ( " info=" ) ;
System.out.println ( info ) ;
}

/** FOR TESTING PURPOSES ONLY */

public /*@ pure @*/ Object clone () {
 try {
  DPage deepClone = (DPage) super.clone();
  deepClone.tag =  (Tag) tag.clone();
  deepClone.generation = (Generation) generation.clone();
  deepClone.version = (Version) version.clone();
  
  deepClone = new DPage(pageInUse,
                        (Tag) tag.clone(),
                        info,
                        (Generation) generation.clone(),
                        pageNumber,
                        (Version) version.clone());
  return deepClone;
    
 } catch (CloneNotSupportedException e) {
    return null; // never invoked
 }
}


}
