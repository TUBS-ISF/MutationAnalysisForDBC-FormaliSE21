package diagnostics;

//import java.rmi.Remote;
import java.rmi.RemoteException;

import javacard.framework.UserException;
import commons.Common;
//@ model import org.jmlspecs.models.*;
//@ model import appointments.AppointmentsSetup;
//@ model import appointments.Appointment;

public interface Diagnostics extends Common /*, Remote */ {

   
    //Model variables that must have a representation somewhere in the concrete class.
    //@ public model instance non_null JMLObjectSequence diagnostics_model; 
    //@ public model instance non_null JMLObjectSequence appointments_model;
    
    //Specification invariants:
    
    // Invariant 1: Specification of the size of the data structure that must hold the diagnostic objects.
    /*@ invariant diagnostics_model != null && 
      @			  diagnostics_model.int_length() == DiagnosticsSetup.MAX_DIAGNOSTIC_ITEMS;
      @*/
      
    // Invariant 2: All objects in the data structure of diagnostics must contain instances of Diagnostic objects or null values.
    /*@ invariant instancesInvariant(diagnostics_model, Diagnostic.class);
      @*/ 
    
    // Invariant 3: In the data structure there must not be any null values in positions before one with an object. 
    /*@	invariant nullsInvariant(diagnostics_model);
      @*/
    
    // Invariant 4: All diagnostic objects must have existing internal appointment identifications referencing
    //			  appointment objects.
    /*@ invariant codesInvariant(diagnostics_model, appointments_model);	
      @*/
	
	/*@ public pure model boolean codesInvariant(JMLObjectSequence sequence1, JMLObjectSequence sequence2){ 
	  @		for (int i = 0; i < sequence1.int_length(); i++){
	  @			if(	sequence1.itemAt(i) != null){
	  @				for (int j = 0; j < sequence2.int_length(); j++){
	  @					if(	sequence2.itemAt(j) != null){
	  @						if( ((Diagnostic)sequence1.itemAt(i)).getAppointmentID() == ((Appointment)sequence2.itemAt(j)).getID()){
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
	
	/*@ public pure model boolean existsAppointmentID(JMLObjectSequence appointments, byte appointmentID){
      @ 	for(int i = 0; i < appointments.int_length(); i++){
      @			if(appointments.itemAt(i) != null){
      @				if(((Appointment)appointments.itemAt(i)).getID() == appointmentID)
      @					return true;
      @			}
      @		}
      @		return false;
      @  }
      @*/
	
	
	/*@ public pure model int getPositionAppointmentID(JMLObjectSequence appointments, byte appointmentID){
      @ 	for(int i = 0; i < appointments.int_length(); i++){
      @			if(appointments.itemAt(i) != null){
      @				if(((Appointment)appointments.itemAt(i)).getID() == appointmentID)
      @					return i;
      @			}
      @		}
      @		return -1;
      @  }
      @*/
	
	/*@ public pure model boolean existsDiagnosticID(JMLObjectSequence diagnostics, byte appointmentID, byte diagnosticID){
      @ 	for(int i = 0; i < diagnostics.int_length(); i++){
      @			if(diagnostics.itemAt(i) != null){
      @				if(((Diagnostic)diagnostics.itemAt(i)).getAppointmentID() == appointmentID 
      @				   && ((Diagnostic)diagnostics.itemAt(i)).getDiagnosticID() == diagnosticID){
      @						return true;	
      @				}		
      @			}
      @		}
      @		return false;
      @  }
      @*/
	
	/*@ public pure model int getPositionDiagnosticID(JMLObjectSequence diagnostics, byte appointmentID, byte diagnosticID){
      @ 	for(int i = 0; i < diagnostics.int_length(); i++){
      @			if(diagnostics.itemAt(i) != null){
      @				if(((Diagnostic)diagnostics.itemAt(i)).getAppointmentID() == appointmentID 
      @				   && ((Diagnostic)diagnostics.itemAt(i)).getDiagnosticID() == diagnosticID){
      @						return i;	
      @				}		
      @			}
      @		}
      @		return -1;
      @  }
      @*/
	

	//Methods --------------------------------------------------------------------------
    
	/*@ public normal_behavior
    @		requires diagnostics_model.count(null) > 0;
    @		requires existsAppointmentID(appointments_model, appointmentID);
    @		requires ((Appointment)appointments_model.itemAt(getPositionAppointmentID(appointments_model, appointmentID))).getStatus() != AppointmentsSetup.STATUS_SCHEDULE;
    @		assignable diagnostics_model;
    @		ensures diagnostics_model.count(null) == \old(diagnostics_model.count(null)-1);
    @		ensures_redundantly ((Diagnostic)lastNonNullObject(diagnostics_model)).appointmentID_model 
    @							== appointmentID;
    @		ensures ((Diagnostic)lastNonNullObject(diagnostics_model)).diagnosticID_model 
    @							== \result;
    @	    ensures_redundantly lastNonNullObject(diagnostics_model) instanceof Diagnostic;
    @ also
    @ public exceptional_behavior
    @			requires diagnostics_model.count(null) == 0
    @				 	|| !existsAppointmentID(appointments_model, appointmentID)
    @					|| ((Appointment)appointments_model.itemAt(getPositionAppointmentID(appointments_model, appointmentID))).getStatus() == AppointmentsSetup.STATUS_SCHEDULE;
    @     assignable \nothing;
    @		signals_only UserException;
	@		signals_redundantly (UserException e) diagnostics_model.equals(\old(diagnostics_model));
    @*/
	public byte addDiagnostic (byte appointmentID) throws RemoteException, UserException;

    
  /*@ public normal_behavior
    @	requires existsDiagnosticID(diagnostics_model, appointmentID, diagnosticID);	 						
    @ 	assignable diagnostics_model;
    @	ensures diagnostics_model.count(null) == \old(diagnostics_model.count(null)+1);
    @	ensures !existsDiagnosticID(diagnostics_model, appointmentID, diagnosticID);
    @ also
    @ public exceptional_behavior
    @	requires !existsDiagnosticID(diagnostics_model, appointmentID, diagnosticID);	
    @   assignable \nothing;
    @   signals_only UserException;
	@	signals_redundantly (UserException e) diagnostics_model.equals(\old(diagnostics_model)); 
    @*/
    public void removeDiagnostic (byte appointmentID, byte diagnosticID) throws RemoteException, UserException;
     
  /*@ public normal_behavior
    @   	requires existsDiagnosticID(diagnostics_model, appointmentID, diagnosticID);
    @		requires title != null && title.length <= DiagnosticsSetup.MAX_TITLE_LENGTH; 				  
    @ 		assignable diagnostics_model;
    @		ensures (\forall int i; 0 <= i && i < title.length;
    @					(((Diagnostic)diagnostics_model.itemAt(getPositionDiagnosticID(appointments_model, appointmentID,diagnosticID))).title_model).itemAt(i).equals(toJMLValueSequence(title).itemAt(i)));
    @ also 
    @ public exceptional_behavior
    @		requires !existsDiagnosticID(diagnostics_model, appointmentID, diagnosticID)
    @				 || title == null || title.length > DiagnosticsSetup.MAX_TITLE_LENGTH;			
    @	assignable \nothing;
    @		signals_only UserException;
	@		signals_redundantly (UserException e) diagnostics_model.equals(\old(diagnostics_model)); 		
    @		
    @*/
    public void setDiagnosticTitle (byte appointmentID, byte diagnosticID, byte[] title) throws RemoteException, UserException;
	
  /*@ public normal_behavior
    @   	requires existsDiagnosticID(diagnostics_model, appointmentID, diagnosticID);
    @		requires description_buffer != null && description_buffer.length <= Common.MAX_BUFFER_BYTES;			  
    @		requires 0 <= offset && offset < DiagnosticsSetup.MAX_DESCRIPTION_LENGTH;		 		
    @		requires 0 <= size_buffer && size_buffer <= Common.MAX_BUFFER_BYTES;
    @		assignable diagnostics_model;
    @		ensures (\forall int i; offset <= i && i < description_buffer.length;
    @					(((Diagnostic)diagnostics_model.itemAt(getPositionDiagnosticID(appointments_model, appointmentID,diagnosticID))).description_model).itemAt(i).equals(toJMLValueSequence(description_buffer).itemAt(i)));
    @ also 
    @ public exceptional_behavior
    @		requires !existsDiagnosticID(diagnostics_model, appointmentID, diagnosticID)
    @				 || description_buffer == null || description_buffer.length > Common.MAX_BUFFER_BYTES
    @				 || 0 > offset || offset > DiagnosticsSetup.MAX_DESCRIPTION_LENGTH
    @				 || 0 > size_buffer || size_buffer > Common.MAX_BUFFER_BYTES;	
    @	assignable \nothing;
    @	signals_only UserException;
	@	signals_redundantly (UserException e) diagnostics_model.equals(\old(diagnostics_model)); 		
    @		
    @*/
    public void setDiagnosticDescription (byte appointmentID, byte diagnosticID, byte[] description_buffer, short size_buffer, short offset, boolean firstBlock) throws RemoteException, UserException;

  
    
  /*@ public normal_behavior
    @		requires position >= 0 && position < diagnostics_model.int_length();
    @		requires diagnostics_model.itemAt(position) != null;	
    @		assignable commons.CardUtil;
    @       ensures ((Diagnostic) diagnostics_model.itemAt(position)).diagnosticID_model == \result[0]
    @				&& ((Diagnostic) diagnostics_model.itemAt(position)).getAppointmentID() == \result[1];
    @ also
    @ public exceptional_behavior
    @		requires position < 0 ||  position >= diagnostics_model.int_length()
    @				 || diagnostics_model.itemAt(position) == null;
    @       assignable \nothing;
    @		signals_only UserException;
	@		signals_redundantly (UserException e) diagnostics_model.equals(\old(diagnostics_model)); 	     		    		    		    		     		     		    	 		    
    @*/
    public byte[] getDiagnosticIDs(short position) throws RemoteException, UserException;
  
    
  /*@ public normal_behavior
    @   	requires existsDiagnosticID(diagnostics_model, appointmentID, diagnosticID);
    @       assignable commons.CardUtil;
    @ 		ensures (\forall int i; 0 <= i && i < (((Diagnostic)diagnostics_model.itemAt(getPositionDiagnosticID(appointments_model, appointmentID,diagnosticID))).title_model).int_length();
    @					(((Diagnostic)diagnostics_model.itemAt(getPositionDiagnosticID(appointments_model, appointmentID,diagnosticID))).title_model).itemAt(i).equals(toJMLValueSequence(\result).itemAt(i)));
    @ also
    @ public exceptional_behavior
    @		requires !existsDiagnosticID(diagnostics_model, appointmentID, diagnosticID);
    @       assignable \nothing;
    @		signals_only UserException;
	@		signals_redundantly (UserException e) diagnostics_model.equals(\old(diagnostics_model)); 	
    @*/
    public byte[] getDiagnosticTitle (byte appointmentID, byte diagnosticID) throws  RemoteException, UserException;

    /*@ public normal_behavior
    @   	requires existsDiagnosticID(diagnostics_model, appointmentID, diagnosticID);
 	@		requires offset >= 0 && offset < DiagnosticsSetup.MAX_DESCRIPTION_LENGTH;		 		
    @		requires size >= 0 && size < DiagnosticsSetup.MAX_DESCRIPTION_LENGTH;
    @       assignable commons.CardUtil;
    @		ensures (\forall int i; offset <= i && i < (short)(offset + Common.MAX_BUFFER_BYTES) && i < (((Diagnostic)diagnostics_model.itemAt(getPositionDiagnosticID(appointments_model, appointmentID,diagnosticID))).description_model).int_length();
    @					(((Diagnostic)diagnostics_model.itemAt(getPositionDiagnosticID(appointments_model, appointmentID,diagnosticID))).description_model).itemAt(i).equals(toJMLValueSequence(\result).itemAt(i)));
    @ also
    @ public exceptional_behavior
    @		requires !existsDiagnosticID(diagnostics_model, appointmentID, diagnosticID)
    @				 || offset < 0 || offset > DiagnosticsSetup.MAX_DESCRIPTION_LENGTH
    @				 || size < 0 || size > DiagnosticsSetup.MAX_DESCRIPTION_LENGTH;	
    @       assignable \nothing;
    @		signals_only UserException;
	@		signals_redundantly (UserException e) diagnostics_model.equals(\old(diagnostics_model)); 	
    @*/
    public byte[] getDiagnosticDescription (byte appointmentID, byte diagnosticID, short offset, short size) throws RemoteException, UserException;

    
    /*@ public normal_behavior
    @   	requires existsDiagnosticID(diagnostics_model, appointmentID, diagnosticID);
    @       assignable commons.CardUtil;
    @		ensures \result >= 0 && \result < DiagnosticsSetup.MAX_DESCRIPTION_LENGTH;		
    @ also
    @ public exceptional_behavior
    @		requires !existsDiagnosticID(diagnostics_model, appointmentID, diagnosticID);
    @       assignable \nothing;
    @		signals_only UserException;
	@		signals_redundantly (UserException e) diagnostics_model.equals(\old(diagnostics_model)); 	
    @*/
    public short getDiagnosticDescriptionSize(byte appointmentID, byte diagnosticID) throws RemoteException, UserException;
    
    
    /*@ public normal_behavior
      @	  requires existsAppointmentID(appointments_model, appointmentID);
      @   assignable commons.CardUtil;
      @	  ensures \result >= 0;
      @ also
      @ public exceptional_behavior
      @ requires !existsAppointmentID(appointments_model, appointmentID);
      @ assignable \nothing;
      @		signals_only UserException;
	  @		signals_redundantly (UserException e) diagnostics_model.equals(\old(diagnostics_model)); 	
      @*/
    public short countDiagnostics (byte appointmentID) throws RemoteException, UserException, java.lang.Exception;
    
    
    /*@ public normal_behavior
      @		assignable \nothing;
      @		ensures \result >= 0 && \result < DiagnosticsSetup.MAX_DIAGNOSTIC_ITEMS;
      @*/
    public short countAllDiagnostics () throws RemoteException, UserException;

}

