package Examples_from_Chapter_7;
class Average { // Listing 7.8

    /*@ spec_public @*/ private Student[] sl;

    /*@ signals_only ArithmeticException;
      @ signals (ArithmeticException e) sl.length == 0;
      @*/
    public int averageCredits() {
	int sum = 0;
	for (int i = 0; i < sl.length; i++) {
	    sum = sum + sl[i].getCredits();
	};
	return sum/sl.length;
    }
}
