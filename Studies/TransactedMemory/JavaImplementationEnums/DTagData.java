package JavaImplementationEnums;

/************************************************************************
 *
 * Record giving information about a tag, telling
 * <ul>
 * <li> whether it's in use
 * <li> its size, ie. the length of info sequences associated with it
 * <li> whether it's committed
 * </ul>
 *
 * @author Erik Poll
 *
 */
public class DTagData{

 public boolean tagInUse;  // called dtdu in original C/SPIN code
 
 public int size;          // called dtds in original C/SPIN code
 //@ invariant  tagInUse ==> 0 < size;
 
 public boolean committed; // called dtdc in original C/SPIN code

 public DTagData(boolean tagInUse, int size, boolean committed)
 { this.tagInUse = tagInUse;
   this.size = size;
   this.committed = committed;
 }

 private void update (boolean newTagInUse, int newSize, boolean newCommitted)
  { 
    this.tagInUse = newTagInUse;
    this.size = newSize;
    this.committed = newCommitted;
    System.out.print ( "Writing DTagData." ) ;
    System.out.print ( "tagInUse=" ) ;
    System.out.print ( newTagInUse ) ;
    System.out.print ( " size=" ) ;
    System.out.print ( newSize ) ;
    System.out.print ( " info=" ) ;
    System.out.println ( newCommitted ) ;
 }
 
 public void resetTagInUse () 
 {  
    update(false, this.size, this.committed);
 }

 /** set committed flag to true */
 public void commit () 
 {  
    update(this.tagInUse, this.size, true);
 }

 /** set committed flag to false */
 public void decommit () throws CardTearException
 {  
    update(this.tagInUse, this.size, false);
 }

/** FOR TESTING  PURPOSES ONLY */

 public /*@ pure @*/ Object clone () {
  try {
   DTagData deepClone = (DTagData) super.clone();
   return deepClone;
     
  } catch (CloneNotSupportedException e) {
     return null; // never invoked
  }
 }

}
