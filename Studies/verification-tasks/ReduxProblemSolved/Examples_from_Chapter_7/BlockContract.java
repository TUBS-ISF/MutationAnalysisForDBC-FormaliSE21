package Examples_from_Chapter_7;
public class BlockContract { // Listing 7.16

   private int x;
   
   private int y;
   
public void swapInBetween() {
    // ...
    /*@ ensures x == \old(y);
      @ ensures y == \old(x);
      @ assignable x, y;
      @ signals_only \nothing;
      @*/
    {
        y = x + y;
        x = y - x;
        y = y - x;
    }
    // ...   
}

}
