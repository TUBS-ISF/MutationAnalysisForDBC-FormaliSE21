
/**
 * Pot of the table.
 */
public  /* nullable_by_default */class  Pot {
	
  /** The Pot size. */
  private/*@ spec_public @*/int size;

	

  //@ public invariant 0 <= size;

  /** Max pot size. */
  public static final int MAX_POT_SIZE = Integer.MAX_VALUE;

	

  /**
   * Instantiates a new pot.
   * @param initSize
   *        size to initialize pot.
   */
  /*@ requires initSize >= 0;
      assignable size;
      ensures size == initSize;
   */
  public Pot(final int initSize) {
    this.size = initSize;
  }

	
  

  /**
   * @param newSize the new size
   */
  /*@ public normal_behavior
      requires newSize >= 0;
      assignable size;
      ensures  size == newSize;
   */
  public final void setSize(final int newSize) {
    this.size = newSize;
    // assert false;
    //assert false;
  }

	

  /**
   * @return the pot size
   */
  /*@ public normal_behavior
      ensures \result >= 0;
      ensures \result == size;
   */
  public final/*@ pure @*/int getSize() {
    // assert false;
    //assert false;
    return size;
  }

	

  /**
   * Adds the value to pot size.
   *
   * @param addedValue The added value to the pot.
   */
  /*@ public normal_behavior
      requires addedValue >=0;
      requires getSize()+addedValue >= 0;
      assignable size;
   */
   private final void  addToPotSize__wrappee__Wager  (final int addedValue) {
    //TODO ensures code
    //setSize(getSize() + addedValue);
    // assert false;
    //assert false;
  }

	
  
//TODO: replace assignabel size with original call
  /**
   * Adds the value to pot size.
   *
   * @param addedValue The added value to the pot.
   */
  /*@ public normal_behavior
    	
      requires \original_clause && addedValue <= size;
      assignable size;
   */
  public final void addToPotSize(final int addedValue) {
    addToPotSize__wrappee__Wager(addedValue);
  }


}
