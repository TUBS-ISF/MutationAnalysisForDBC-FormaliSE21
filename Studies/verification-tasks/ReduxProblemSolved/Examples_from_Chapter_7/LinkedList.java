package Examples_from_Chapter_7;
public class LinkedList { // Listing 7.12
    private /*@ spec_public @*/ int value;
    private /*@ spec_public nullable @*/ LinkedList next;

    //@ public ghost \bigint length;
    
    //@ public invariant 0 < length;
    
    //@ public invariant next == null || next.length+1 == length;
    
}
