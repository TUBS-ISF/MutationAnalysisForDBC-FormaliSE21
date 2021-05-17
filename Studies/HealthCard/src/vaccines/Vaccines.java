package vaccines;

//import java.rmi.Remote;
import java.rmi.RemoteException;
import javacard.framework.UserException;
import commons.Common;

//@ model import org.jmlspecs.models.*;

public interface Vaccines extends Common /*, Remote */{

	//Model variables that must have a representation somewhere in the concrete class.  
    //@ public model instance non_null JMLObjectSequence vaccines_model;
    
    
    //Specification invariants:
    
    // Invariant 1: Specification of the size of the data structure that must hold the vaccine objects.
    /*@ invariant vaccines_model != null && 
      @			  vaccines_model.int_length() == VaccinesSetup.MAX_VACCINE_ITEMS;
      @*/
    
    // Invariant 2: All objects in the data structure must contain instances of Vaccine objects or null values.
    /*@ invariant instancesInvariant(vaccines_model, Vaccine.class);
      @*/   
    
    // Invariant 3: In the data structure there must not be any null values in positions before one with an object. 
    /*@	invariant nullsInvariant(vaccines_model);
      @*/
	
	
	//Methods --------------------------------------------------------------------------
    
  /*@ public normal_behavior
    @		requires vaccines_model.count(null) > 0;		
    @		requires designation != null && designation.length == VaccinesSetup.VACCINE_CODE_LENGTH;
    @		requires (\forall int i; 0 <= i && i < VaccinesSetup.VACCINE_CODE_LENGTH; 
    @					((byte)0x41 <= designation[i] &&  designation[i] <= (byte)0x5A )
    @					|| ((byte)0x61 <= designation[i] &&  designation[i] <= (byte)0x7A )
    @					|| ((byte)0x30 <= designation[i] &&  designation[i] <= (byte)0x39 ));
    @		requires date != null && date.length == DATE_LENGTH;
    @ 	assignable vaccines_model;
    @		ensures vaccines_model.count(null) == \old(vaccines_model).count(null) - 1;
    @	    ensures (((Vaccine) vaccines_model.itemAt(vaccines_model.int_length() -1 - vaccines_model.count(null))).designation_model).equals(toJMLValueSequence(designation));
    @	    ensures (((Vaccine) vaccines_model.itemAt(vaccines_model.int_length() -1 - vaccines_model.count(null))).date_model).equals(toJMLValueSequence(date));
    @ also
    @ public exceptional_behavior
    @		requires vaccines_model.count(null) == 0
    @				 || (designation == null || designation.length != VaccinesSetup.VACCINE_CODE_LENGTH)		
    @				 || !(\forall int i; 0 <= i && i < VaccinesSetup.VACCINE_CODE_LENGTH; 
	@						((byte)0x41 <= designation[i] &&  designation[i] <= (byte)0x5A )
    @						|| ((byte)0x61 <= designation[i] &&  designation[i] <= (byte)0x7A )
    @						|| ((byte)0x30 <= designation[i] &&  designation[i] <= (byte)0x39 )) 
    @				 || (date == null || date.length != DATE_LENGTH);  		 
    @		assignable \nothing;
    @		signals_only UserException;
	@		signals_redundantly (UserException e) vaccines_model.equals(\old(vaccines_model));
    @*/
    public void addVaccine (byte[] designation, byte[] date)  throws RemoteException, UserException;

    
    /*@ public normal_behavior
    @		requires position >= 0 && position < VaccinesSetup.MAX_VACCINE_ITEMS;
    @     	requires vaccines_model.itemAt(position) != null;
    @ 	  	assignable vaccines_model;
    @	  	ensures vaccines_model.count(null) == \old(vaccines_model).count(null)+1;
    @	 	ensures ((vaccines_model.subsequence(position, VaccinesSetup.MAX_VACCINE_ITEMS - 1 )).equals(\old(vaccines_model).subsequence(position+1, VaccinesSetup.MAX_VACCINE_ITEMS))
    @					&& vaccines_model.itemAt(VaccinesSetup.MAX_VACCINE_ITEMS - 1) == null)
    @				|| vaccines_model.itemAt(VaccinesSetup.MAX_VACCINE_ITEMS - 1) == null;
    @ 	also
    @ 	public exceptional_behavior
    @ 	  	requires position >= VaccinesSetup.MAX_VACCINE_ITEMS
    @ 				 || position < 0
    @ 				 || (vaccines_model.itemAt(position) == null);
    @     	assignable \nothing;
    @ 	  	signals_only UserException;
	@ 	  	signals_redundantly (UserException e) vaccines_model.equals(\old(vaccines_model));
    @*/
    public void removeVaccine (short position) throws RemoteException, UserException;

    
  /*@ public normal_behavior
    @ 	assignable commons.CardUtil;
    @		ensures \result == VaccinesSetup.MAX_VACCINE_ITEMS - vaccines_model.count(null);
    @*/
    public short countVaccines () throws RemoteException, UserException;

    
  /*@ public normal_behavior
    @		requires position >=  0 && position < VaccinesSetup.MAX_VACCINE_ITEMS;
    @		requires vaccines_model.itemAt(position) != null;	
    @ 	requires designation != null && designation.length == VaccinesSetup.VACCINE_CODE_LENGTH;
    @		requires (\forall int i; 0 <= i && i < VaccinesSetup.VACCINE_CODE_LENGTH; 
	@					((byte)0x41 <= designation[i] &&  designation[i] <= (byte)0x5A )
    @					|| ((byte)0x61 <= designation[i] &&  designation[i] <= (byte)0x7A )
    @					|| ((byte)0x30 <= designation[i] &&  designation[i] <= (byte)0x39 ));
    @		assignable vaccines_model;
    @		ensures (((Vaccine) vaccines_model.itemAt(position)).designation_model).equals(toJMLValueSequence(designation));
    @ also
    @ public exceptional_behavior
    @		requires (position < 0 || position >= VaccinesSetup.MAX_VACCINE_ITEMS)
    @			     || vaccines_model.itemAt(position) == null
    @				 || designation == null || designation.length != VaccinesSetup.VACCINE_CODE_LENGTH
    @                || !(\forall int i; 0 <= i && i < VaccinesSetup.VACCINE_CODE_LENGTH; 
	@						((byte)0x41 <= designation[i] &&  designation[i] <= (byte)0x5A )
    @						|| ((byte)0x61 <= designation[i] &&  designation[i] <= (byte)0x7A )
    @						|| ((byte)0x30 <= designation[i] &&  designation[i] <= (byte)0x39 ));
    @     assignable \nothing;
    @		signals_only UserException;
	@		signals_redundantly (UserException e) vaccines_model.equals(\old(vaccines_model));
    @*/
    public void setVaccineDesignation (short position, byte[] designation) throws RemoteException, UserException;

    
  /*@ public normal_behavior
    @		requires position >= 0 && position < VaccinesSetup.MAX_VACCINE_ITEMS;
    @		requires vaccines_model.itemAt(position) != null;	
    @		assignable commons.CardUtil;
    @     ensures (((Vaccine) vaccines_model.itemAt(position)).designation_model).equals(toJMLValueSequence(\result));
    @ also
    @ public exceptional_behavior
    @		requires (position < 0 || position >= VaccinesSetup.MAX_VACCINE_ITEMS)
    @					|| vaccines_model.itemAt(position) == null;
    @     assignable \nothing;
    @		signals_only UserException;
	@		signals_redundantly (UserException e) vaccines_model.equals(\old(vaccines_model)); 		    		      		     		    
    @*/
    public byte[] getVaccineDesignation (short position) throws RemoteException, UserException;

    
  /*@ public normal_behavior
    @     requires position >= 0 && position < VaccinesSetup.MAX_VACCINE_ITEMS;
    @ 	  requires date != null && date.length == DATE_LENGTH;
    @	  requires vaccines_model.itemAt(position) != null;	
    @	  assignable vaccines_model; 
    @	  ensures (((Vaccine) vaccines_model.itemAt(position)).date_model).equals(toJMLValueSequence(date));
    @ also
    @ public exceptional_behavior
    @	  requires (position < 0 || position >= VaccinesSetup.MAX_VACCINE_ITEMS)
    @					|| vaccines_model.itemAt(position) == null
    @					|| date == null || date.length != DATE_LENGTH;
    @     assignable \nothing;
    @	  signals_only UserException;
	@	  signals_redundantly (UserException e) vaccines_model.equals(\old(vaccines_model));
    @*/
    public void setVaccinationDate (short position, byte[] date) throws RemoteException, UserException;

    
  /*@ public normal_behavior
    @		requires position >= 0 && position < VaccinesSetup.MAX_VACCINE_ITEMS;
    @		requires vaccines_model.itemAt(position) != null;	
    @		assignable commons.CardUtil;
    @		ensures (((Vaccine) vaccines_model.itemAt(position)).date_model).equals(toJMLValueSequence(\result));
    @ also
    @ public exceptional_behavior
    @		requires (position < 0 || position >= VaccinesSetup.MAX_VACCINE_ITEMS)
    @				|| vaccines_model.itemAt(position) == null;
    @     assignable \nothing;
    @		signals_only UserException;
	@		signals_redundantly (UserException e) vaccines_model.equals(\old(vaccines_model));	    		      		     		     		    		      		    		    
    @*/
    public byte[] getVaccinationDate (short position) throws RemoteException, UserException;

    
  /*@ public normal_behavior
    @	requires position >= 0;
    @	assignable \nothing;
    @ 	ensures (position < 0 || position >= VaccinesSetup.MAX_VACCINE_ITEMS) ==> (\result == false);
    @	ensures ((position >= 0 && position < VaccinesSetup.MAX_VACCINE_ITEMS)
    @           && vaccines_model.itemAt(position) == null) ==> (\result == false);
    @	ensures ((position >= 0 && position < VaccinesSetup.MAX_VACCINE_ITEMS)
    @           && vaccines_model.itemAt(position) != null) ==> (\result == true);
    @	also
    @	public exceptional_behavior
    @		requires position < 0;
    @		assignable \nothing;
    @		signals_only UserException;
    @*/
  public boolean validateVaccinePosition(short position) throws RemoteException, UserException;

}

