package JavaImplementationEnums;

/**
 * A record giving information about about some tag
 * namely 
 *  <ul>
 *  <li> the number of generations <code>cnt</code> , and
 *  <li> the oldest and newest generation, <code>oldGen</code> and
 *       <code>newGen</code> 
 *  </ul>
 * that exist for a some tag.
 *
 * @author Erik Poll
 */

public class GenGenbyte{

 /** number of generations for a given tag */
 public int cnt; // number of generations for a given tag
 
 //@ invariant 0 <= cnt && cnt <= TransactedMemory.MAXIND;
  
 /** oldest and newest generation */
 public Generation oldGen, newGen ; 

 /* invariant cnt == ((oldGen.value <= newGen.value )
                         ? (newGen.value - oldGen.value) 
                         : (newGen.value + TransactedMemory.MAXIND - oldGen.value));

 */

 /** FOR TESTING PURPOSES ONLY */
 public void print () {
   System.out.print ( " oldGen=" ) ;
   System.out.print ( oldGen.value ) ;
   System.out.print ( ", newGen=" ) ;
   System.out.print ( newGen.value ) ;
   System.out.print ( ", cnt=" ) ;
   System.out.println ( cnt ) ;
 }

}
