package Examples_from_Chapter_7;
interface Student { //Listing 7.4
    
    public static final int bachelor = 0;
    public static final int master = 1;

    /*@ instance invariant getCredits() >= 0;
      @ instance invariant getStatus() == bachelor || 
      @                    getStatus() == master;
      @ instance invariant getStatus() == master ==> 
      @                    getCredits() >= 180;
      @
      @ instance initially getCredits() == 0;
      @ instance initially getStatus() == bachelor;
      @
      @ instance constraint getCredits() >= \old(getCredits());
      @ instance constraint \old(getStatus()) == master ==> 
      @                     getStatus() == master;
      @ instance constraint \old(getName()) == getName();
      @*/

    public /*@ pure @*/ String getName(); 

    public /*@ pure @*/ int getStatus();

    public /*@ pure @*/ int getCredits();

   /*@ requires c >= 0;
     @ ensures getCredits() == \old(getCredits()) + c;
     @*/
    public void addCredits(int c);

    /*@ requires getCredits() >= 180;
      @ requires getStatus() == bachelor;
      @ ensures getCredits() == \old(getCredits());
      @ ensures getStatus() == master;
      @*/
    public void changeStatus();

		
}
	
