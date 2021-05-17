package Examples_from_Chapter_7;
interface CallBack { // Listing 7.5

    //@ instance invariant getX() > 0;
	
    //@ instance invariant getY() > 0;

    /*@ pure @*/ public int getX();
    /*@ pure @*/ public int getY();

    //@ ensures getX() == x;
    public void setX(int x);

    //@ ensures getY() == y;
    public void setY(int y);

    //@ ensures \result == getX() % getY();
    public int remainder();

    public int longComputation();

}


    
