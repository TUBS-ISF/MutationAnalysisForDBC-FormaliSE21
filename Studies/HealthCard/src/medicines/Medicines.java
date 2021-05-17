package medicines;

import commons.Common;

//import java.rmi.Remote;
import java.rmi.RemoteException;

import javacard.framework.UserException;

//@ model import org.jmlspecs.models.*;
//@ model import treatments.TreatmentsSetup;
//@ model import treatments.Treatment;
//@ model import medicines.Medicine;

public interface Medicines extends Common /*, Remote */{
	
	//Model variables that must have a representation somewhere in the concrete class.
    //@ public model instance non_null JMLObjectSequence medicines_model;
	//@ public model instance non_null JMLObjectSequence treatments_model;
	
 
    //Specification invariants:
    
    // Invariant 1: Specification of the size of the data structure that must hold the medicine objects.
    /*@ invariant medicines_model != null && 
      @			  medicines_model.int_length() == MedicinesSetup.MAX_MEDICINE_ITEMS;
      @			  
      @*/

	
    // Invariant 2: All objects in the data structure of medicines must contain instances of Medicine objects or null values.
    /*@ invariant instancesInvariant(medicines_model, Medicine.class);
      @*/
      
    
    // Invariant 3: In the data structure there must not be any null values in positions before one with an object. 
    /*@	invariant nullsInvariant(medicines_model);			
      @*/
    
	
    // Invariant 4: All medicine objects must have existing internal appointment, diagnostic and treatment identifications referencing
    //			  treatment objects.
    /*@ invariant codesInvariant(medicines_model, treatments_model);								
      @*/
	
	 /*@
	   @ public pure model boolean codesInvariant(JMLObjectSequence sequence1, JMLObjectSequence sequence2){ 
	   @		for (int i = 0; i < sequence1.int_length(); i++){
	   @			if(	sequence1.itemAt(i) != null){
	   @				for (int j = 0; j < sequence2.int_length(); j++){
	   @					if(	sequence2.itemAt(j) != null){
	   @						if( ((Medicine)sequence1.itemAt(i)).getAppointmentID() == ((Treatment)sequence2.itemAt(j)).getAppointmentID()
	   @							&& ((Medicine)sequence1.itemAt(i)).getDiagnosticID() == ((Treatment)sequence2.itemAt(j)).getDiagnosticID()
	   @							&& ((Medicine)sequence1.itemAt(i)).getTreatmentID() == ((Treatment)sequence2.itemAt(j)).getTreatmentID()){
	   @								break;
	   @						}
	   @					}	
	   @                         if(j == sequence2.int_length() - 1)
	   @							return false;
	   @				}
	   @			}				
	   @		 }
	   @		return true;
	   @	}
	   @*/

	
	//JML auxiliary Methods --------------------------------------------------------------------------
	
	/*@ public pure model boolean existsIDs(JMLObjectSequence treatments, byte appointmentID, byte diagnosticID, byte treatmentID){
      @ 	for(int i = 0; i < treatments.int_length(); i++){
      @			if(treatments.itemAt(i) != null){
      @				if(((Treatment)treatments.itemAt(i)).getAppointmentID() == appointmentID &&
      @					((Treatment)treatments.itemAt(i)).getDiagnosticID() == diagnosticID &&
      @					((Treatment)treatments.itemAt(i)).getTreatmentID() == treatmentID)
      @						return true;
      @			}
      @		}
      @		return false;
      @  }
      @*/

	
	/*@ public pure model boolean existsMedicineID(JMLObjectSequence medicines, byte appointmentID, byte diagnosticID, byte treatmentID, byte medicineID){
      @ 	for(int i = 0; i < medicines.int_length(); i++){
      @			if(medicines.itemAt(i) != null){
      @				if(((Medicine)medicines.itemAt(i)).getAppointmentID() == appointmentID
      @				   && ((Medicine)medicines.itemAt(i)).getDiagnosticID() == diagnosticID
      @				   && ((Medicine)medicines.itemAt(i)).getTreatmentID() == treatmentID
      @				   && ((Medicine)medicines.itemAt(i)).getMedicineID() == medicineID){
      @						return true;	
      @				}		
      @			}
      @		}
      @		return false;
      @  }
      @*/
	
	/*@ public pure model boolean existsMedicine(JMLObjectSequence medicines, byte appointmentID, byte diagnosticID, byte treatmentID){
    @ 	for(int i = 0; i < medicines.int_length(); i++){
    @			if(medicines.itemAt(i) != null){
    @				if(((Medicine)medicines.itemAt(i)).getAppointmentID() == appointmentID
    @				   && ((Medicine)medicines.itemAt(i)).getDiagnosticID() == diagnosticID
    @				   && ((Medicine)medicines.itemAt(i)).getTreatmentID() == treatmentID){
    @						return true;	
    @				}			
    @			}
    @		}
    @		return false;
    @  }
    @*/
	
  /*@ public pure model int getPositionMedicineID(JMLObjectSequence medicines, byte appointmentID, byte diagnosticID, byte treatmentID, byte medicineID){
    @ 	for(int i = 0; i < medicines.int_length(); i++){
    @			if(medicines.itemAt(i) != null){
    @				if(((Medicine)medicines.itemAt(i)).getAppointmentID() == appointmentID
    @				   && ((Medicine)medicines.itemAt(i)).getDiagnosticID() == diagnosticID
    @				   && ((Medicine)medicines.itemAt(i)).getTreatmentID() == treatmentID
    @				   && ((Medicine)medicines.itemAt(i)).getMedicineID() == medicineID){
    @						return i;	
    @				}		
    @			}
    @		}
    @		return -1;
    @  }
    @*/
	
	
	//Methods --------------------------------------------------------------------------
    
  /*@ public normal_behavior
    @		requires medicines_model.count(null) > 0;
    @		requires existsIDs(treatments_model, appointmentID, diagnosticID, treatmentID);
    @		assignable medicines_model;
    @		ensures medicines_model.count(null) == \old(medicines_model.count(null)-1);
    @		ensures_redundantly ((Medicine)lastNonNullObject(medicines_model)).appointmentID_model 
    @							== appointmentID;	
    @		ensures_redundantly ((Medicine)lastNonNullObject(medicines_model)).diagnosticID_model 
    @							== diagnosticID;
    @		ensures_redundantly ((Medicine)lastNonNullObject(medicines_model)).treatmentID_model 
    @							== treatmentID;
    @		ensures ((Medicine)lastNonNullObject(medicines_model)).medicineID_model 
    @							== \result;	
    @		ensures_redundantly lastNonNullObject(medicines_model) instanceof Medicine;
    @ also
    @ public exceptional_behavior
    @			requires treatments_model.count(null) == 0
    @				 	|| !existsIDs(treatments_model, appointmentID, diagnosticID, treatmentID);
    @     assignable \nothing;
    @		signals_only UserException;
	@		signals_redundantly (UserException e) medicines_model.equals(\old(medicines_model));
    @*/
    public byte addMedicine (byte appointmentID, byte diagnosticID, byte treatmentID) throws RemoteException, UserException;

    
    /*@ public normal_behavior 	 
      @	requires existsMedicineID(medicines_model, appointmentID, diagnosticID, treatmentID, medicineID);						
      @ 	assignable medicines_model;
      @		ensures medicines_model.count(null) == \old(medicines_model.count(null)+1);
      @		ensures !existsMedicineID(medicines_model, appointmentID, diagnosticID, treatmentID, medicineID);
      @ also
      @ public exceptional_behavior
      @	requires !existsMedicineID(medicines_model, appointmentID, diagnosticID, treatmentID, medicineID);
      @   assignable \nothing;
      @   signals_only UserException;
	  @	signals_redundantly (UserException e) medicines_model.equals(\old(medicines_model)); 
      @*/
    public void removeMedicine (byte appointmentID, byte diagnosticID, byte treatmentID, byte medicineID) throws RemoteException, UserException;

    
    /*@ public normal_behavior
      @	requires  existsMedicine(medicines_model, appointmentID, diagnosticID, treatmentID);
      @   assignable \nothing;
      @	ensures \result >= 0;
      @ also
      @ public exceptional_behavior
      @ 	requires !existsMedicine(medicines_model, appointmentID, diagnosticID, treatmentID);
      @ 	assignable \nothing;
      @		signals_only UserException;
	  @		signals_redundantly (UserException e) medicines_model.equals(\old(medicines_model)); 	
      @*/
    public short countMedicines (byte appointmentID, byte diagnosticID, byte treatmentID) throws RemoteException, UserException;

  
  /*@ public normal_behavior
	@		assignable \nothing;
	@		ensures \result >= 0;
	@*/
    public short countAllMedicines () throws RemoteException, UserException;

    
    
    /*@ public normal_behavior
    @		requires position >= 0 && position < medicines_model.int_length();
    @		requires medicines_model.itemAt(position) != null;	
    @		assignable commons.CardUtil;
    @       ensures ((Medicine) treatments_model.itemAt(position)).medicineID_model == \result[0]
    @				&& ((Medicine) treatments_model.itemAt(position)).treatmentID_model == \result[1]
    @				&& ((Medicine) treatments_model.itemAt(position)).diagnosticID_model == \result[2]
    @				&& ((Medicine) treatments_model.itemAt(position)).appointmentID_model == \result[3];
    @ also
    @ public exceptional_behavior
    @		requires position < 0 ||  position >= medicines_model.int_length()
    @				 || medicines_model.itemAt(position) == null;
    @       assignable \nothing;
    @		signals_only UserException;
	@		signals_redundantly (UserException e) medicines_model.equals(\old(medicines_model)); 	     		    		    		    		     		     		    	 		    
    @*/
    public byte[] getMedicineIDs(short position) throws RemoteException, UserException;
    
    
    
  /*@ public normal_behavior
    @   	requires existsMedicineID(medicines_model, appointmentID, diagnosticID, treatmentID, medicineID);
    @		requires designation != null && designation.length ==  MedicinesSetup.MEDICINE_CODE_LENGTH;
    @ 		requires (\forall int i; 0 <= i && i < MedicinesSetup.MEDICINE_CODE_LENGTH; 
	@					((byte)0x41 <= designation[i] &&  designation[i] <= (byte)0x5A )
    @					|| ((byte)0x61 <= designation[i] &&  designation[i] <= (byte)0x7A )
    @					|| ((byte)0x30 <= designation[i] &&  designation[i] <= (byte)0x39 ));
    @		assignable medicines_model;
    @		ensures (\forall int i; 0 <= i && i < designation.length;
    @					(((Medicine)medicines_model.itemAt(getPositionMedicineID(medicines_model, appointmentID, diagnosticID, treatmentID, medicineID))).designation_model).itemAt(i).equals(toJMLValueSequence(designation).itemAt(i)));
    @ also 
    @ public exceptional_behavior
    @		requires !existsMedicineID(medicines_model, appointmentID, diagnosticID, treatmentID, medicineID)
    @				 || designation == null || designation.length !=  MedicinesSetup.MEDICINE_CODE_LENGTH
    @				 || !(\forall int i; 0 <= i && i < MedicinesSetup.MEDICINE_CODE_LENGTH; 
	@						((byte)0x41 <= designation[i] &&  designation[i] <= (byte)0x5A )
    @						|| ((byte)0x61 <= designation[i] &&  designation[i] <= (byte)0x7A )
    @						|| ((byte)0x30 <= designation[i] &&  designation[i] <= (byte)0x39 ));	
    @	assignable \nothing;
    @	signals_only UserException;
	@	signals_redundantly (UserException e) medicines_model.equals(\old(medicines_model)); 		
    @		
    @*/
    public void setDesignation (byte appointmentID, byte diagnosticID, byte treatmentID, byte medicineID, byte[] designation) throws RemoteException, UserException;

    
    /*@ public normal_behavior
    @   	requires existsMedicineID(medicines_model, appointmentID, diagnosticID, treatmentID, medicineID);
    @       assignable commons.CardUtil;
    @		ensures (\forall int i; 0 <= i && i < ((Medicine)medicines_model.itemAt(getPositionMedicineID(medicines_model, appointmentID, diagnosticID, treatmentID, medicineID))).designation_model.int_length();
    @					(((Medicine)medicines_model.itemAt(getPositionMedicineID(medicines_model, appointmentID, diagnosticID, treatmentID, medicineID))).designation_model).itemAt(i).equals(toJMLValueSequence(\result).itemAt(i)));
    @ also
    @ public exceptional_behavior
    @		requires !existsMedicineID(medicines_model, appointmentID, diagnosticID, treatmentID, medicineID);
    @       assignable \nothing;
    @		signals_only UserException;
	@		signals_redundantly (UserException e) medicines_model.equals(\old(medicines_model)); 	
    @*/
    public byte[] getDesignation (byte appointmentID, byte diagnosticID, byte treatmentID, byte medicineID) throws RemoteException, UserException;
 
    
    /*@ public normal_behavior
      @   	requires existsMedicineID(medicines_model, appointmentID, diagnosticID, treatmentID, medicineID);
      @		requires administration != null && administration.length <=  MedicinesSetup.MAX_ADMIN_DESCRIPTION_LENGTH;
      @ 	assignable medicines_model;
      @		ensures (\forall int i; 0 <= i && i < administration.length;
      @					(((Medicine)medicines_model.itemAt(getPositionMedicineID(medicines_model, appointmentID, diagnosticID, treatmentID, medicineID))).administration_description_model).itemAt(i).equals(toJMLValueSequence(administration).itemAt(i)));
      @ also 
      @ public exceptional_behavior
      @		requires !existsMedicineID(medicines_model, appointmentID, diagnosticID, treatmentID, medicineID)
      @				 || administration == null || administration.length >  MedicinesSetup.MAX_ADMIN_DESCRIPTION_LENGTH;	
      @	assignable \nothing;
      @	signals_only UserException;
	  @	signals_redundantly (UserException e) medicines_model.equals(\old(medicines_model)); 		
      @		
      @*/
    public void setAdministrationDescription (byte appointmentID, byte diagnosticID, byte treatmentID, byte medicineID, byte[] administration) throws RemoteException, UserException;

    
    /*@ public normal_behavior
      @   	requires existsMedicineID(medicines_model, appointmentID, diagnosticID, treatmentID, medicineID);
      @       assignable commons.CardUtil;
      @		ensures (\forall int i; 0 <= i && i < ((Medicine)medicines_model.itemAt(getPositionMedicineID(medicines_model, appointmentID, diagnosticID, treatmentID, medicineID))).administration_description_model.int_length();
      @					(((Medicine)medicines_model.itemAt(getPositionMedicineID(medicines_model, appointmentID, diagnosticID, treatmentID, medicineID))).administration_description_model).itemAt(i).equals(toJMLValueSequence(\result).itemAt(i)));
      @ also
      @ public exceptional_behavior
      @		requires !existsMedicineID(medicines_model, appointmentID, diagnosticID, treatmentID, medicineID);
      @       assignable \nothing;
      @		signals_only UserException;
	  @		signals_redundantly (UserException e) medicines_model.equals(\old(medicines_model)); 	
      @*/
    public byte[] getAdministrationDescription (byte appointmentID, byte diagnosticID, byte treatmentID, byte medicineID) throws RemoteException, UserException;

    /*@ public normal_behavior
      @   	requires existsMedicineID(medicines_model, appointmentID, diagnosticID, treatmentID, medicineID);
      @		requires period_in_days >= 0; 
      @ 		assignable medicines_model;
      @		ensures period_in_days == ((Medicine)medicines_model.itemAt(getPositionMedicineID(medicines_model, appointmentID, diagnosticID, treatmentID, medicineID))).period_days_model;
      @ also 
      @ public exceptional_behavior
      @		requires !existsMedicineID(medicines_model, appointmentID, diagnosticID, treatmentID, medicineID)
      @				 || period_in_days  < 0;	
      @	assignable \nothing;
      @	signals_only UserException;
	  @	signals_redundantly (UserException e) medicines_model.equals(\old(medicines_model)); 		
      @		
      @*/
    public void setPeriod (byte appointmentID, byte diagnosticID, byte treatmentID, byte medicineID, short period_in_days) throws RemoteException, UserException;

    
    /*@ public normal_behavior
      @   	requires existsMedicineID(medicines_model, appointmentID, diagnosticID, treatmentID, medicineID);
      @     assignable commons.CardUtil;
      @		ensures \result == ((Medicine)medicines_model.itemAt(getPositionMedicineID(medicines_model, appointmentID, diagnosticID, treatmentID, medicineID))).period_days_model;
      @ also
      @ public exceptional_behavior
      @		requires !existsMedicineID(medicines_model, appointmentID, diagnosticID, treatmentID, medicineID);
      @     assignable \nothing;
      @		signals_only UserException;
	  @		signals_redundantly (UserException e) medicines_model.equals(\old(medicines_model)); 	
      @*/
    public short getPeriod (byte appointmentID, byte diagnosticID, byte treatmentID, byte medicineID) throws RemoteException, UserException;

    
    /*@ public normal_behavior
    @ 		requires existsMedicineID(medicines_model, appointmentID, diagnosticID, treatmentID, medicineID);
    @		requires date != null && date.length == DATE_LENGTH;
    @		assignable medicines_model; 
    @		ensures (\forall int i; 0 <= i && i < date.length;
    @					(((Medicine)medicines_model.itemAt(getPositionMedicineID(medicines_model, appointmentID, diagnosticID, treatmentID, medicineID))).date_model).itemAt(i).equals(toJMLValueSequence(date).itemAt(i)));
    @ also
    @  public exceptional_behavior
    @		requires !existsMedicineID(medicines_model, appointmentID, diagnosticID, treatmentID, medicineID)
    @				 || date == null || date.length != DATE_LENGTH;	
    @	assignable \nothing;
    @	signals_only UserException;
	@	signals_redundantly (UserException e) medicines_model.equals(\old(medicines_model)); 	
    @*/
    public void setDate (byte appointmentID, byte diagnosticID, byte treatmentID, byte medicineID, byte[] date) throws RemoteException, UserException;

    /*@ public normal_behavior
      @ 		requires existsMedicineID(medicines_model, appointmentID, diagnosticID, treatmentID, medicineID);
      @		assignable medicines_model; 
      @		ensures (\forall int i; 0 <= i && i < ((Medicine)medicines_model.itemAt(getPositionMedicineID(medicines_model, appointmentID, diagnosticID, treatmentID, medicineID))).administration_description_model.int_length();
      @					(((Medicine)medicines_model.itemAt(getPositionMedicineID(medicines_model, appointmentID, diagnosticID, treatmentID, medicineID))).date_model).itemAt(i).equals(toJMLValueSequence(\result).itemAt(i)));
      @ also
      @  public exceptional_behavior
      @		requires !existsMedicineID(medicines_model, appointmentID, diagnosticID, treatmentID, medicineID);
      @	assignable \nothing;
      @	signals_only UserException;
	  @	signals_redundantly (UserException e) medicines_model.equals(\old(medicines_model)); 	
      @*/
    public byte[] getDate (byte appointmentID, byte diagnosticID, byte treatmentID, byte medicineID) throws RemoteException, UserException;

}

