public class HeapObject {


    // as starter we only consider graphs with an out-degree 
    // of at most two
    private /*@ spec_public @*/ final HeapObject[] children = 
        new HeapObject[2];

    /*@ invariant children != null && children.length == 2; @*/


    // used by the graph marking algorithm to 
    // keep track of the next child to visit and
    // if this heap object has been already visited
    /*@ invariant nextChild >= 0; @*/
    
    private /*@ spec_public @*/ int nextChild;
    private /*@ spec_public @*/ boolean visited;   

    /** creates a new heap object */
    public HeapObject() {
	nextChild = 0;
	visited = false;
    }
   

    /**
     * sets <code>obj</code> as <code>pos</code>-th child
     */
    /*@ public normal_behavior 
      @ requires pos>=0 && pos<children.length;
      @ ensures children[pos] == obj;
      @ assignable children[pos];
      @ also
      @ public exceptional_behavior
      @ requires pos < 0 || pos>=children.length;
      @ signals (IndexOutOfBoundsException);
      @*/
    public void setChild(int pos, HeapObject obj) {
	if (pos < 0  || pos >= children.length) {
	    throw new IndexOutOfBoundsException();
	}
	children[pos] = obj;
    }


    /**
     * sets the 'visited' marker     
     */
    /*@ public normal_behavior
      @ ensures this.visited == mark;
      @ assignable this.visited;
      @*/
    public void setMark(boolean mark) {
	this.visited = mark;
    }
    
    /**
     * sets the tracker of the next child to be visited
     */
     /*@ public normal_behavior
       @ ensures this.nextChild == \old(this.nextChild) + 1;
       @ assignable nextChild;
       @*/
    public void incIndex() {      
	this.nextChild++;
    }


    /**
     * true if the mark is set
     */
    /*@ public normal_behavior
      @ ensures \result == visited;
      @ assignable \nothing;
      @*/
    public boolean isMarked() {
	return visited;
    }

    /**
     * true if there is a next child to be visited
     */
    /*@ public normal_behavior    
      @ ensures \result == (nextChild<children.length);
      @ assignable \nothing;
      @*/
    public boolean hasNext() {
	return nextChild<children.length;
    }

    /**
     * returns the index of the next child to visit
     */
    /*@ public normal_behavior
      @ ensures \result == nextChild;
      @ assignable \nothing;
      @*/
    public int getIndex() {
        return nextChild;
    }


    /**
     * returns the <code>pos</code>-th child
     */
    /*@ public normal_behavior 
      @ requires pos>=0 && pos<children.length;
      @ ensures \result == children[pos];
      @ assignable \nothing;
      @ also
      @ public exceptional_behavior
      @ requires pos < 0 || pos>=children.length;
      @ signals (IndexOutOfBoundsException);      
      @*/
    public HeapObject getChild(int pos) {        
        return children[pos];
    }


    /**
     * returns the number of children at this node
     * @return the numbe of children at this node
     */
    /*@ public normal_behavior
      @ ensures \result == children.length;
      @ assignable \nothing;
      @*/
    public int getChildCount() {        
        return children.length;
    }


}
