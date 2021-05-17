package allergies;

//import java.rmi.Remote;
import java.rmi.RemoteException;
import javacard.framework.UserException;
import commons.Common;
//@ model import org.jmlspecs.models.*;
//@ model import java.util.Arrays;
//@ model import allergies.Allergy;


public interface Allergies extends Common /*, Remote */{

    //Model variables that must have a representation somewhere in the concrete class.  
    //@ public model instance non_null JMLObjectSequence allergies_model;
    
    
    //Specification invariants:
    
    // Invariant 1: Specification of the size of the data structure that must hold the allergy objects.
    /*@ invariant allergies_model != null && 
      @			  allergies_model.int_length() == AllergiesSetup.MAX_ALLERGY_ITEMS;
      @*/
    
    // Invariant 2: All objects in the data structure must contain instances of Allergy objects or null values.
    /*@ invariant instancesInvariant(allergies_model, Allergy.class);
      @*/   
    
    // Invariant 3: In the data structure there must not be any null values in positions before one with an object. 
    /*@	invariant nullsInvariant(allergies_model);
      @*/
	
     
	//JML auxiliary Methods --------------------------------------------------------------------------
	
	/*@ public pure model boolean existsAllergy(JMLObjectSequence allergies, byte[] designation){
	  @ 	for(int i = 0; i < allergies.int_length(); i++){
      @			if(allergies.itemAt(i) != null){
      @				if( \readonly Arrays.equals((((Allergy)allergies.itemAt(i)).getDesignation()),designation))
      @					return true;
      @			}
      @		}
      @		return false;
	  @	}
	  @*/

    /*@ public static model pure JMLValueSequence convertJMLValueSequence(byte[] b){
	  @		JMLValueSequence _m = new JMLValueSequence();
	  @		for(int i = 0; i < b.length; i++)
	  @			_m = _m.insertBack(new JMLByte(b[i]));
	  @		return _m;
	  @ }
	  @
	  @*/
	
	//Methods --------------------------------------------------------------------------
    
    /*@ public normal_behavior
      @		requires allergies_model.count(null) > 0;		
      @		requires designation != null && designation.length == AllergiesSetup.ALLERGY_CODE_LENGTH;
      @		requires (\forall int i; 0 <= i && i < AllergiesSetup.ALLERGY_CODE_LENGTH; 
	  @					((byte)0x41 <= designation[i] &&  designation[i] <= (byte)0x5A )
      @					|| ((byte)0x61 <= designation[i] &&  designation[i] <= (byte)0x7A )
      @					|| ((byte)0x30 <= designation[i] &&  designation[i] <= (byte)0x39 ));
      @	    requires !existsAllergy(allergies_model, designation);
      @		requires date != null && date.length == DATE_LENGTH;
      @ 	assignable allergies_model;
      @		ensures allergies_model.count(null) == \old(allergies_model).count(null) - 1;
      @	    ensures (((Allergy) allergies_model.itemAt(allergies_model.int_length() -1 - allergies_model.count(null))).designation_model).equals(toJMLValueSequence(designation));
      @	    ensures (((Allergy) allergies_model.itemAt(allergies_model.int_length() -1 - allergies_model.count(null))).date_model).equals(toJMLValueSequence(date));
      @ also
      @ public exceptional_behavior
      @		requires allergies_model.count(null) == 0
      @				 || (designation == null || designation.length != AllergiesSetup.ALLERGY_CODE_LENGTH)		
      @				 || !(\forall int i; 0 <= i && i < AllergiesSetup.ALLERGY_CODE_LENGTH; 
	  @						((byte)0x41 <= designation[i] &&  designation[i] <= (byte)0x5A )
      @						|| ((byte)0x61 <= designation[i] &&  designation[i] <= (byte)0x7A )
      @						|| ((byte)0x30 <= designation[i] &&  designation[i] <= (byte)0x39 ))
      @			     || existsAllergy(allergies_model, designation)    
      @				 || (date == null || date.length != DATE_LENGTH);  		 
      @		assignable \nothing;
      @		signals_only UserException;
	  @		signals_redundantly (UserException e) allergies_model.equals(\old(allergies_model));
      @*/
   public void addAllergy (byte[] designation, byte[] date) throws RemoteException, UserException;
       
   /*@ public normal_behavior
      @		requires position >= 0 && position < AllergiesSetup.MAX_ALLERGY_ITEMS;
      @     requires allergies_model.itemAt(position) != null;
      @ 	assignable allergies_model;
      @		ensures allergies_model.count(null) == \old(allergies_model).count(null)+1;
      @		ensures ((allergies_model.subsequence(position, AllergiesSetup.MAX_ALLERGY_ITEMS - 1 )).equals(\old(allergies_model).subsequence(position+1, AllergiesSetup.MAX_ALLERGY_ITEMS ))
      @					&& allergies_model.itemAt(AllergiesSetup.MAX_ALLERGY_ITEMS - 1) == null) 
      @				|| allergies_model.itemAt(AllergiesSetup.MAX_ALLERGY_ITEMS - 1) == null;
      @	also
      @	public exceptional_behavior
      @		requires position >= AllergiesSetup.MAX_ALLERGY_ITEMS
      @				 || position < 0 
      @				 || (allergies_model.itemAt(position) == null);
      @     assignable \nothing;
      @ 	signals_only UserException;
	  @ 	signals_redundantly (UserException e) allergies_model.equals(\old(allergies_model));
      @*/
    public void removeAllergy (short position) throws RemoteException, UserException;
    
    /*@ public normal_behavior
      @ 	assignable commons.CardUtil;
      @		ensures \result == AllergiesSetup.MAX_ALLERGY_ITEMS - allergies_model.count(null);
      @*/
    public short countAllergies () throws RemoteException, UserException;
    
    /*@ public normal_behavior
      @		requires position >=  0 && position < AllergiesSetup.MAX_ALLERGY_ITEMS;
      @		requires allergies_model.itemAt(position) != null;	
      @ 	requires designation != null && designation.length == AllergiesSetup.ALLERGY_CODE_LENGTH;
      @		requires (\forall int i; 0 <= i && i < AllergiesSetup.ALLERGY_CODE_LENGTH; 
	  @					((byte)0x41 <= designation[i] &&  designation[i] <= (byte)0x5A )
      @					|| ((byte)0x61 <= designation[i] &&  designation[i] <= (byte)0x7A )
      @					|| ((byte)0x30 <= designation[i] &&  designation[i] <= (byte)0x39 ));
      @		assignable allergies_model;
      @		ensures (((Allergy) allergies_model.itemAt(position)).designation_model).equals(toJMLValueSequence(designation));
      @ also
      @ public exceptional_behavior
      @		requires (position < 0 || position >= AllergiesSetup.MAX_ALLERGY_ITEMS)
      @			     || allergies_model.itemAt(position) == null
      @				 || designation == null || designation.length != AllergiesSetup.ALLERGY_CODE_LENGTH
      @              || !(\forall int i; 0 <= i && i < AllergiesSetup.ALLERGY_CODE_LENGTH; 
	  @						((byte)0x41 <= designation[i] &&  designation[i] <= (byte)0x5A )
      @						|| ((byte)0x61 <= designation[i] &&  designation[i] <= (byte)0x7A )
      @						|| ((byte)0x30 <= designation[i] &&  designation[i] <= (byte)0x39 ));
      @     assignable \nothing;
      @		signals_only UserException;
	  @		signals_redundantly (UserException e) allergies_model.equals(\old(allergies_model));
      @*/
    public void setAllergyDesignation (short position, byte[] designation) throws RemoteException, UserException;

    /*@ public normal_behavior
      @		requires position >= 0 && position < AllergiesSetup.MAX_ALLERGY_ITEMS;
      @		requires allergies_model.itemAt(position) != null;	
      @		assignable commons.CardUtil;
      @     ensures (((Allergy) allergies_model.itemAt(position)).designation_model).equals(toJMLValueSequence(\result));
      @ also
      @ public exceptional_behavior
      @		requires (position < 0 || position >= AllergiesSetup.MAX_ALLERGY_ITEMS)
      @					|| allergies_model.itemAt(position) == null;
      @     assignable \nothing;
      @		signals_only UserException;
	  @		signals_redundantly (UserException e) allergies_model.equals(\old(allergies_model)); 		    		      		     		    
      @*/
    public byte[] getAllergyDesignation (short position) throws RemoteException, UserException;

    /*@ public normal_behavior
      @		requires position >= 0 && position < AllergiesSetup.MAX_ALLERGY_ITEMS;
      @ 	requires date != null && date.length == DATE_LENGTH;
      @		requires allergies_model.itemAt(position) != null;	
      @		assignable allergies_model; 
      @		ensures (((Allergy) allergies_model.itemAt(position)).date_model).equals(toJMLValueSequence(date));
      @ also
      @ public exceptional_behavior
      @		requires (position < 0 || position >= AllergiesSetup.MAX_ALLERGY_ITEMS)
      @					|| allergies_model.itemAt(position) == null
      @					|| date == null || date.length != DATE_LENGTH;
      @     assignable \nothing;
      @		signals_only UserException;
	  @		signals_redundantly (UserException e) allergies_model.equals(\old(allergies_model));
      @*/
    public void setAllergyDate (short position, byte[] date) throws RemoteException, UserException;

    /*@ public normal_behavior
      @		requires position >= 0 && position < AllergiesSetup.MAX_ALLERGY_ITEMS;
      @		requires allergies_model.itemAt(position) != null;	
      @		assignable commons.CardUtil;
      @		ensures (((Allergy) allergies_model.itemAt(position)).date_model).equals(toJMLValueSequence(\result));
      @ also
      @ public exceptional_behavior
      @		requires (position < 0 || position >= AllergiesSetup.MAX_ALLERGY_ITEMS)
      @				|| allergies_model.itemAt(position) == null;
      @     assignable \nothing;
      @		signals_only UserException;
	  @		signals_redundantly (UserException e) allergies_model.equals(\old(allergies_model));	    		      		     		     		    		      		    		    
      @*/
    public byte[] getAllergyDate (short position) throws RemoteException, UserException;

    /*@ public normal_behavior
      @		requires position >= 0;
      @		assignable \nothing;
      @ 	ensures (position < 0 || position >= AllergiesSetup.MAX_ALLERGY_ITEMS) ==> (\result == false);
      @		ensures ((position >= 0 && position < AllergiesSetup.MAX_ALLERGY_ITEMS)
      @           && allergies_model.itemAt(position) == null) ==> (\result == false);
      @		ensures ((position >= 0 && position < AllergiesSetup.MAX_ALLERGY_ITEMS)
      @           && allergies_model.itemAt(position) != null) ==> (\result == true);
      @	also
      @	public exceptional_behavior
      @		requires position < 0;
      @		assignable \nothing;
      @		signals_only UserException;
      @*/
    public boolean validateAllergyPosition(short position) throws RemoteException, UserException;
}

