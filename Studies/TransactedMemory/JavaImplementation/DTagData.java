package JavaImplementation;

public class DTagData{

 public boolean tagInUse;
 
 public int size;
 //@ invariant  0 < size;
 
 public boolean committed;

 public DTagData(boolean tagInUse, int size, boolean committed)
 { this.tagInUse = tagInUse;
   this.size = size;
   this.committed = committed;
 }

}
