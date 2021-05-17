/**
 * This class implements the graph marking algorithm known as
 * Schorr-Waite algorithm.
 */
public class SchorrWaite {    
    
    private HeapObject previous;
    private HeapObject current;   
    
    /*@ public normal_behavior
      @ requires start != null && start.nextChild >= 0 &&      
      @ (start.children[start.nextChild] == null ||
      @ start.nextChild > start.children.length || 
      @ !start.children[start.nextChild].visited) ;
      @ ensures true;
      @ diverges true;
      @*/
    public void mark(HeapObject start) {
        previous = null;
        current  = start;           
	current.setMark(true);
        while (start != current || start.hasNext()) {
            final HeapObject tmpChild;
            if (current.hasNext()) {
		final int nextChild = current.getIndex();
                tmpChild = current.getChild(nextChild);
                if (!tmpChild.isMarked()) {// forward scan
		    current.setChild(nextChild, previous);                
		    previous = current;
		    current.incIndex();
                    current = tmpChild;
                    current.setMark(true);
                } else {
		    current.incIndex();				    
		}
            } else {
                // backward
		final int ref2restore = previous.getIndex() - 1;
                tmpChild = previous.getChild(ref2restore);
                previous.setChild(ref2restore, current);
                current  = previous;
                previous = tmpChild;                
            }
        }      
    }
}
