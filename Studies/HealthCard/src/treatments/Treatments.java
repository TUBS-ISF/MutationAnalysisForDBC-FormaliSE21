package treatments;

//import java.rmi.Remote;
import java.rmi.RemoteException;

import javacard.framework.UserException;
import commons.Common;

//@ model import org.jmlspecs.models.*;
//@ model import diagnostics.DiagnosticsSetup;
//@ model import diagnostics.Diagnostic;

public interface Treatments extends Common /*, Remote */ {

	//Model variables that must have a representation somewhere in the concrete class.
    //@ public model instance non_null JMLObjectSequence treatments_model;
	//@ public model instance non_null JMLObjectSequence diagnostics_model;
	
 
    //Specification invariants:
    
    // Invariant 1: Specification of the size of the data structure that must hold the treatments objects.
    /*@ invariant treatments_model != null && 
      @			  treatments_model.int_length() == TreatmentsSetup.MAX_TREATMENT_ITEMS;
      @*/
      
    // Invariant 2: All objects in the data structure of treatments must contain instances of Treatment objects or null values.
    /*@ invariant instancesInvariant(treatments_model, Treatment.class);
      @*/ 
    
    // Invariant 3: In the data structure there must not be any null values in positions before one with an object. 
    /*@	invariant nullsInvariant(treatments_model);
      @*/
    
    // Invariant 4: All treatment objects must have existing internal appointment and diagnostic identifications referencing
    //			  diagnostic objects.
    /*@ invariant codesInvariant(treatments_model, diagnostics_model);								
      @*/
	
	/*@ public pure model boolean codesInvariant(JMLObjectSequence sequence1, JMLObjectSequence sequence2){ 
	  @		for (int i = 0; i < sequence1.int_length(); i++){
	  @			if(	sequence1.itemAt(i) != null){
	  @				for (int j = 0; j < sequence2.int_length(); j++){
	  @					if(	sequence2.itemAt(j) != null){
	  @						if( ((Treatment)sequence1.itemAt(i)).getAppointmentID() == ((Diagnostic)sequence2.itemAt(j)).getAppointmentID()
	  @							&& ((Treatment)sequence1.itemAt(i)).getDiagnosticID() == ((Diagnostic)sequence2.itemAt(j)).getDiagnosticID()){
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
	
	/*@ public pure model boolean existsIDs(JMLObjectSequence diagnostics, byte appointmentID, byte diagnosticID){
      @ 	for(int i = 0; i < diagnostics.int_length(); i++){
      @			if(diagnostics.itemAt(i) != null){
      @				if(((Diagnostic)diagnostics.itemAt(i)).getAppointmentID() == appointmentID &&
      @					((Diagnostic)diagnostics.itemAt(i)).getDiagnosticID() == diagnosticID)
      @						return true;
      @			}
      @		}
      @		return false;
      @  }
      @*/
	
	
	/*@ public pure model int getPositionDiagnostic(JMLObjectSequence diagnostics, byte appointmentID, byte diagnosticID){
      @ 	for(int i = 0; i < diagnostics.int_length(); i++){
      @			if(diagnostics.itemAt(i) != null){
      @				if(((Diagnostic)diagnostics.itemAt(i)).getAppointmentID() == appointmentID &&
      @					((Diagnostic)diagnostics.itemAt(i)).getDiagnosticID() == diagnosticID)
      @						return i;
      @			}
      @		}
      @		return -1;
      @  }
      @*/
	
	/*@ public pure model boolean existsTreatmentID(JMLObjectSequence treatments, byte appointmentID, byte diagnosticID, byte treatmentID){
      @ 	for(int i = 0; i < treatments.int_length(); i++){
      @			if(treatments.itemAt(i) != null){
      @				if(((Treatment)treatments.itemAt(i)).getAppointmentID() == appointmentID
      @				   && ((Treatment)treatments.itemAt(i)).getDiagnosticID() == diagnosticID
      @				   && ((Treatment)treatments.itemAt(i)).getTreatmentID() == treatmentID){
      @						return true;	
      @				}		
      @			}
      @		}
      @		return false;
      @  }
      @*/
	
	/*@ public pure model boolean existsTreatment(JMLObjectSequence treatments, byte appointmentID, byte diagnosticID){
      @ 	for(int i = 0; i < treatments.int_length(); i++){
      @			if(treatments.itemAt(i) != null){
      @				if(((Treatment)treatments.itemAt(i)).getAppointmentID() == appointmentID
      @				   && ((Treatment)treatments.itemAt(i)).getDiagnosticID() == diagnosticID){
      @						return true;	
      @				}		
      @			}
      @		}
      @		return false;
      @  }
      @*/
	
	/*@ public pure model int getPositionTreatmentID(JMLObjectSequence treatments, byte appointmentID, byte diagnosticID, byte treatmentID){
      @ 	for(int i = 0; i < treatments.int_length(); i++){
      @			if(treatments.itemAt(i) != null){
      @				if(((Treatment)treatments.itemAt(i)).getAppointmentID() == appointmentID
      @				   && ((Treatment)treatments.itemAt(i)).getDiagnosticID() == diagnosticID
      @				   && ((Treatment)treatments.itemAt(i)).getTreatmentID() == treatmentID){
      @						return i;	
      @				}		
      @			}
      @		}
      @		return -1;
      @  }
      @*/
	

	//Methods --------------------------------------------------------------------------
    
    /*@ public normal_behavior
    @		requires treatments_model.count(null) > 0;
    @		requires existsIDs(diagnostics_model, appointmentID, diagnosticID);
    @		assignable treatments_model;
    @		ensures treatments_model.count(null) == \old(treatments_model.count(null)-1);
    @		ensures_redundantly ((Treatment)lastNonNullObject(treatments_model)).appointmentID_model 
    @							== appointmentID;
    @		ensures_redundantly ((Treatment)lastNonNullObject(treatments_model)).diagnosticID_model 
    @							== diagnosticID;
    @		ensures ((Treatment)lastNonNullObject(treatments_model)).treatmentID_model 
    @							== \result;	
    @		ensures_redundantly lastNonNullObject(treatments_model) instanceof Treatment;
    @ also
    @ public exceptional_behavior
    @			requires treatments_model.count(null) == 0
    @				 	|| !existsIDs(diagnostics_model, appointmentID, diagnosticID);
    @     assignable \nothing;
    @		signals_only UserException;
	@		signals_redundantly (UserException e) treatments_model.equals(\old(treatments_model));
    @*/
    public byte addTreatment(byte appointmentID, byte diagnosticID) throws RemoteException, UserException;

  /*@ public normal_behavior 	 
    @	requires existsTreatmentID(treatments_model, appointmentID, diagnosticID, treatmentID);						
    @ 	assignable treatments_model;
    @	ensures treatments_model.count(null) == \old(treatments_model.count(null)+1);
    @	ensures !existsTreatmentID(treatments_model, appointmentID, diagnosticID, treatmentID);
    @ also
    @ public exceptional_behavior
    @	requires !existsTreatmentID(treatments_model, appointmentID, diagnosticID, treatmentID);	
    @   assignable \nothing;
    @   signals_only UserException;
	@	signals_redundantly (UserException e) treatments_model.equals(\old(treatments_model)); 
    @*/
    public void removeTreatment(byte appointmentID, byte diagnosticID, byte treatmentID) throws RemoteException, UserException;

    
  /*@ public normal_behavior
    @		requires position >= 0 && position < treatments_model.int_length();
    @		requires treatments_model.itemAt(position) != null;	
    @		assignable commons.CardUtil;
    @       ensures ((Treatment) treatments_model.itemAt(position)).treatmentID_model == \result[0]
    @				&& ((Treatment) treatments_model.itemAt(position)).diagnosticID_model == \result[1]
    @				&& ((Treatment) treatments_model.itemAt(position)).appointmentID_model == \result[2];
    @ also
    @ public exceptional_behavior
    @		requires position < 0 || position >= treatments_model.int_length()
    @				 || treatments_model.itemAt(position) == null;
    @       assignable \nothing;
    @		signals_only UserException;
	@		signals_redundantly (UserException e) treatments_model.equals(\old(treatments_model)); 	     		    		    		    		     		     		    	 		    
    @*/
    public byte[] getTreatmentIDs(short position) throws RemoteException, UserException;
    
    
    /*@ public normal_behavior
      @   	requires existsTreatmentID(treatments_model, appointmentID, diagnosticID, treatmentID);
      @		requires healthProblem != null && healthProblem.length <= TreatmentsSetup.MAX_HEALTHPROBLEM_LENGTH;
      @ 		assignable treatments_model;
      @		ensures (\forall int i; 0 <= i && i < healthProblem.length;
      @					(((Treatment)treatments_model.itemAt(getPositionTreatmentID(treatments_model, appointmentID, diagnosticID, treatmentID))).healthProblem_model).itemAt(i).equals(toJMLValueSequence(healthProblem).itemAt(i)));
      @ also 
      @ public exceptional_behavior
      @		requires !existsTreatmentID(treatments_model, appointmentID, diagnosticID, treatmentID)
      @				 || healthProblem == null || healthProblem.length >  TreatmentsSetup.MAX_HEALTHPROBLEM_LENGTH;	
      @	assignable \nothing;
      @	signals_only UserException;
	  @	signals_redundantly (UserException e) treatments_model.equals(\old(treatments_model)); 		
      @		
      @*/
    public void setHealthProblem(byte appointmentID, byte diagnosticID, byte treatmentID, byte[] healthProblem) throws RemoteException, UserException;

    
  /*@ public normal_behavior
    @   	requires existsTreatmentID(treatments_model, appointmentID, diagnosticID, treatmentID);
    @       assignable commons.CardUtil;
    @		ensures (\forall int i; 0 <= i && i < ((Treatment)treatments_model.itemAt(getPositionTreatmentID(treatments_model, appointmentID, diagnosticID, treatmentID))).healthProblem_model.int_length();
      @					(((Treatment)treatments_model.itemAt(getPositionTreatmentID(treatments_model, appointmentID, diagnosticID, treatmentID))).healthProblem_model).itemAt(i).equals(toJMLValueSequence(\result).itemAt(i)));
    @ also
    @ public exceptional_behavior
    @		requires !existsTreatmentID(treatments_model, appointmentID, diagnosticID, treatmentID);
    @       assignable \nothing;
    @		signals_only UserException;
	@		signals_redundantly (UserException e) treatments_model.equals(\old(treatments_model)); 	
    @*/
    public byte[] getHealthProblem(byte appointmentID, byte diagnosticID, byte treatmentID) throws RemoteException, UserException;
 
    
    /*@ public normal_behavior
    @   	requires existsTreatmentID(treatments_model, appointmentID, diagnosticID, treatmentID);
    @		requires recommendation_buffer != null && recommendation_buffer.length <=  Common.MAX_BUFFER_BYTES;
    @ 		requires 0 <= offset && offset <= TreatmentsSetup.MAX_MEDICALRECOMMENDATION_LENGTH;		 		
    @		requires 0 <= size_buffer && size_buffer <= Common.MAX_BUFFER_BYTES;
    @		assignable treatments_model;
    @		ensures (\forall int i; offset <= i && i < recommendation_buffer.length;
    @					(((Treatment)treatments_model.itemAt(getPositionTreatmentID(treatments_model, appointmentID, diagnosticID, treatmentID))).recommendation_model).itemAt(i).equals(toJMLValueSequence(recommendation_buffer).itemAt(i)));
    @ also 
    @ public exceptional_behavior
    @		requires !existsTreatmentID(treatments_model, appointmentID, diagnosticID, treatmentID)
    @				 || recommendation_buffer == null || recommendation_buffer.length > Common.MAX_BUFFER_BYTES
    @				 || 0 > offset || offset > TreatmentsSetup.MAX_MEDICALRECOMMENDATION_LENGTH
    @				 || 0 > size_buffer || size_buffer > Common.MAX_BUFFER_BYTES;	
    @	assignable \nothing;
    @	signals_only UserException;
	@	signals_redundantly (UserException e) treatments_model.equals(\old(treatments_model)); 		
    @		
    @*/
    public void setMedicalRecommendation(byte appointmentID, byte diagnosticID, byte treatmentID, byte[] recommendation_buffer, short size_buffer, short offset, boolean firstBlock) throws RemoteException, UserException;

    
  /*@ public normal_behavior
    @   	requires existsTreatmentID(treatments_model, appointmentID, diagnosticID, treatmentID);
    @		requires 0 <= offset && offset < TreatmentsSetup.MAX_MEDICALRECOMMENDATION_LENGTH;		 		
    @		requires size >= 0 && size < TreatmentsSetup.MAX_MEDICALRECOMMENDATION_LENGTH;
    @       assignable commons.CardUtil;
    @		ensures (\forall int i; offset <= i && i < (short)(offset + Common.MAX_BUFFER_BYTES) && i < (((Treatment)treatments_model.itemAt(getPositionTreatmentID(treatments_model, appointmentID, diagnosticID, treatmentID))).recommendation_model).int_length();
    @					(((Treatment)treatments_model.itemAt(getPositionTreatmentID(treatments_model, appointmentID, diagnosticID, treatmentID))).recommendation_model).itemAt(i).equals(toJMLValueSequence(\result).itemAt(i)));
    @ also
    @ public exceptional_behavior
    @		requires !existsTreatmentID(treatments_model, appointmentID, diagnosticID, treatmentID)
    @				 || offset < 0 || offset > TreatmentsSetup.MAX_MEDICALRECOMMENDATION_LENGTH
    @				 || size < 0 || size >= TreatmentsSetup.MAX_MEDICALRECOMMENDATION_LENGTH;
    @       assignable \nothing;
    @		signals_only UserException;
	@		signals_redundantly (UserException e) treatments_model.equals(\old(treatments_model)); 	
    @*/
    public byte[] getMedicalRecommendation(byte appointmentID, byte diagnosticID, byte treatmentID, short offset, short size) throws RemoteException, UserException;
    
    
    /*@ public normal_behavior
    @   	requires existsTreatmentID(treatments_model, appointmentID, diagnosticID, treatmentID);
    @       assignable commons.CardUtil;
    @		ensures \result >= 0 && \result < TreatmentsSetup.MAX_MEDICALRECOMMENDATION_LENGTH;		
    @ also
    @ public exceptional_behavior
    @		requires !existsTreatmentID(treatments_model, appointmentID, diagnosticID, treatmentID);
    @       assignable \nothing;
    @		signals_only UserException;
	@		signals_redundantly (UserException e) diagnostics_model.equals(\old(diagnostics_model)); 	
    @*/
    public short getMedicalRecommendationSize(byte appointmentID, byte diagnosticID, byte treatmentID) throws RemoteException, UserException;
    
    
  /*@ public normal_behavior
    @	requires existsTreatment(treatments_model, appointmentID, diagnosticID);
    @   assignable \nothing;
    @	ensures \result >= 0;
    @ also
    @ public exceptional_behavior
    @ 	requires !existsTreatment(treatments_model, appointmentID, diagnosticID);
    @ 	assignable \nothing;
    @		signals_only UserException;
	@		signals_redundantly (UserException e) treatments_model.equals(\old(treatments_model)); 	
    @*/
    public /*@ pure @*/ short countTreatments(byte appointmentID, byte diagnosticID) throws RemoteException, UserException;

    
	/*@ public normal_behavior
	@		assignable \nothing;
	@		ensures \result >= 0;
	@*/
	public short countAllTreatments() throws RemoteException, UserException;

}

