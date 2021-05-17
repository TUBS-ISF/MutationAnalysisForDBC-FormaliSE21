package Examples_from_Chapter_7;
public class LimitedIntegerSet { // Listing 7.3
  public final int limit;
  /*@ spec_public @*/ private int arr[];
  /*@ spec_public @*/ private int size = 0;

  public LimitedIntegerSet(int limit) {
    this.limit = limit;
    this.arr = new int[limit];
  }

  /*@ requires size < limit && !contains(elem);
    @ ensures \result == true;
    @ ensures contains(elem); 
    @ ensures (\forall int e;
    @                  e != elem;
    @                  contains(e) <==> \old(contains(e)));
    @ ensures size == \old(size) + 1;
    @
    @ also
    @
    @ requires size == limit || contains(elem);
    @ ensures \result == false; 
    @ ensures (\forall int e;
    @                  contains(e) <==> \old(contains(e)));
    @ ensures size == \old(size);
    @*/
  public boolean add(int elem) {/*...*/}
    
  /*@ ensures !contains(elem); 
    @ ensures (\forall int e;
    @                  e != elem;
    @                  contains(e) <==> \old(contains(e)));
    @ ensures \old(contains(elem))
    @         ==> size == \old(size) - 1;
    @ ensures !\old(contains(elem))
    @         ==> size == \old(size);
    @*/
  public void remove(int elem) {/*...*/}

  /*@ ensures \result == (\exists int i;
    @                             0 <= i && i < size;
    @                             arr[i] == elem);
    @*/
  public /*@ pure @*/ boolean contains(int elem) {/*...*/}

  //...
}
