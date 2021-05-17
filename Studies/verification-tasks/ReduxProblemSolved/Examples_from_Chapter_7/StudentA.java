package Examples_from_Chapter_7;
public class StudentA { // Listing 7.13
    private /*@ spec_public @*/ String name;

    /*@ public invariant credits >= 0; 
      @*/
    private /*@ spec_public @*/ int credits;

    /*@ public invariant credits < 180 ==> !master && 
      @                  credits >= 180 ==> master; 
      @*/
    private /*@ spec_public @*/ boolean master;

    /*@ requires c >= 0;
      @ ensures credits == \old(credits) + c;
      @ assignable credits, master;
      @*/
    public void addCredits(int c) {
        updateCredits(c);
        if (credits >= 180) {
            changeToMaster();
        }
    }

    /*@ requires c >= 0;
      @ ensures credits == \old(credits) + c;
      @ assignable credits; 
      @*/
    private void updateCredits(int c) {
    	credits += c;
    }

    /*@ requires credits >= 180;
      @ ensures master;
      @ assignable master;
      @*/
    private void changeToMaster() {
        master = true;
    }

    /*@ ensures this.name == name;
      @ assignable this.name;
      @*/
    public void setName(String name) {
        this.name = name;
    }


    /*@ ensures \result == name; 
      @*/
    public /*@ pure @*/ String getName() {
        return name;
    }
}
