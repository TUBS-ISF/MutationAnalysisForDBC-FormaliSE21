package appointments;

//import java.rmi.Remote;
import java.rmi.RemoteException;

import javacard.framework.UserException;
import commons.Common;
//@ model import org.jmlspecs.models.*;

public interface Appointments extends Common /*, Remote */ {

    //Model variables that must have a representation somewhere in the concrete class.
    //@ public model instance non_null JMLObjectSequence appointments_model;   
	
    
    //Specification invariants:
	   
    // Invariant 1: Specification of the size of the data structure that must hold the appointment objects.
    /*@ invariant appointments_model != null && 
      @			  appointments_model.int_length() == AppointmentsSetup.MAX_APPOINTMENT_ITEMS;
      @*/
      
    // Invariant 2: All objects in the data strucute of appointments must contain instances of Appointment objects or null values.
    /*@ invariant instancesInvariant(appointments_model, Appointment.class);
      @*/
	
	// Invariant 3: In the data structure there must not be any null values in positions before one with an object. 
    /*@	invariant nullsInvariant(appointments_model);
      @*/
        
	// Invariant 4: There must not exist any appointment with check-in and schedule status 
	//			  24 hours after the scheculed appointment date/time.
	/*@	invariant verifyOutofDate(this.appointments_model, (short)24);
	  @*/
	
	 // Invariant 5: All appointment objects must have different internal identifications.
     //@ invariant hasAllDifferentIDs(appointments_model);
	
	/*@ pure model boolean hasAllDifferentIDs(JMLObjectSequence appointments){
      @ 	for(int i = 0; i < appointments.int_length(); i++){
      @			if((Appointment)appointments.itemAt(i) != null)
      @				for(int j = 0; j < appointments.int_length(); j++){
      @					if( i != j && appointments.itemAt(j) != null){
      @						if( ((Appointment)appointments.itemAt(j)).getID() 
      @								== ((Appointment)appointments.itemAt(i)).getID())
      @							return false;
      @					}
      @				}
      @		}
      @		return true;
      @ }
      @*/
	
	// Invariant 6: There must not exist appointments with the same sheduled hour and date.
	//@ invariant hasAllDifferentTimeDate(appointments_model);
      
	
	//JML auxiliary Methods -------------------------------------------------------------------------
	
    /*@ pure model boolean hasAllDifferentTimeDate(JMLObjectSequence appointments){
      @ 	for(int i = 0; i < appointments.int_length(); i++){
      @			if((Appointment)appointments.itemAt(i) != null)
      @				for(int j = 0; j < appointments.int_length(); j++){
      @					if( i != j && appointments.itemAt(j) != null){
      @						if( (((Appointment)appointments.itemAt(j)).getDate() 
      @								== ((Appointment)appointments.itemAt(i)).getDate())
      @							&& 
      @							(((Appointment)appointments.itemAt(j)).getHour() 
      @								== ((Appointment)appointments.itemAt(i)).getHour()))
      @							return false;
      @					}
      @				}
      @		}
      @		return true;
      @ }
      @*/
	
	
    //Methods --------------------------------------------------------------------------
    
    /*@ public normal_behavior
      @		requires appointments_model.count(null) > 0;
      @		requires date != null 
      @				 && hour != null 
      @				 && local != null
      @				 && doctor != null;
      @		requires date.length == DATE_LENGTH 
      @				 && hour.length == HOUR_LENGTH  
      @				 && local.length == LOCAL_CODE_LENGTH
      @				 && doctor.length == DOCTOR_CODE_LENGTH
      @				 && 0x00 < type && type <= AppointmentsSetup.MAX_TYPE_CODES;
      @		requires_redundantly (\forall int i; 0 <= i && i < DOCTOR_CODE_LENGTH; 
	  @					((byte)0x41 <= doctor[i] &&  doctor[i] <= (byte)0x5A )
      @					|| ((byte)0x61 <= doctor[i] &&  doctor[i] <= (byte)0x7A )
      @					|| ((byte)0x30 <= doctor[i] &&  doctor[i] <= (byte)0x39 ));
      @ 	assignable appointments_model;
      @		ensures appointments_model.count(null) == \old(appointments_model.count(null)-1);
      @		ensures_redundantly lastNonNullObject(appointments_model) instanceof Appointment;
      @		ensures (((Appointment) appointments_model.itemAt(appointments_model.int_length() -1 - appointments_model.count(null))).date_model).equals(toJMLValueSequence(date));
      @		ensures (((Appointment) appointments_model.itemAt(appointments_model.int_length() -1 - appointments_model.count(null))).hour_model).equals(toJMLValueSequence(hour));
      @		ensures (((Appointment) appointments_model.itemAt(appointments_model.int_length() -1 - appointments_model.count(null))).local_model).equals(toJMLValueSequence(local));		
      @		ensures (((Appointment) appointments_model.itemAt(appointments_model.int_length() -1 - appointments_model.count(null))).doctor_model).equals(toJMLValueSequence(doctor));	
      @		ensures (((Appointment) appointments_model.itemAt(appointments_model.int_length() -1 - appointments_model.count(null))).type_model) == type;	
      @ also
      @ public exceptional_behavior
      @		requires appointments_model.count(null) == 0 
      @				 || date == null || date.length != DATE_LENGTH
      @				 || hour == null || hour.length != HOUR_LENGTH
      @				 || local == null || local.length != LOCAL_CODE_LENGTH
      @			     || doctor == null || doctor.length != DOCTOR_CODE_LENGTH
      @				 || !(0x00 < type && type <= AppointmentsSetup.MAX_TYPE_CODES)
      @				 || !(\forall int i; 0 <= i && i < DOCTOR_CODE_LENGTH; 
	  @					((byte)0x41 <= doctor[i] &&  doctor[i] <= (byte)0x5A )
      @					|| ((byte)0x61 <= doctor[i] &&  doctor[i] <= (byte)0x7A )
      @					|| ((byte)0x30 <= doctor[i] &&  doctor[i] <= (byte)0x39 ));
      @     assignable \nothing;
      @		signals_only UserException;
	  @		signals_redundantly (UserException e) appointments_model.equals(\old(appointments_model));
      @*/
    public void addAppointment (byte[] date, byte[] hour, byte[] local, byte[] doctor, byte type) throws RemoteException, UserException;

    /*@ public normal_behavior
      @		requires position >= 0 && position < AppointmentsSetup.MAX_APPOINTMENT_ITEMS;
      @     requires appointments_model.itemAt(position) != null;
      @		requires ((Appointment) appointments_model.itemAt(position)).status_model == AppointmentsSetup.STATUS_SCHEDULE ||
      @				 ((Appointment) appointments_model.itemAt(position)).status_model == AppointmentsSetup.STATUS_CHECK_IN;
      @ 	assignable appointments_model;
      @		ensures appointments_model.count(null) == \old(appointments_model.count(null)+1);
      @		ensures ((appointments_model.subsequence(position, AppointmentsSetup.MAX_APPOINTMENT_ITEMS - 1 )).equals(\old(appointments_model).subsequence(position+1, AppointmentsSetup.MAX_APPOINTMENT_ITEMS ))
      @					&& appointments_model.itemAt(AppointmentsSetup.MAX_APPOINTMENT_ITEMS - 1) == null) 
      @				|| appointments_model.itemAt(AppointmentsSetup.MAX_APPOINTMENT_ITEMS - 1) == null;
      @	also
      @	public exceptional_behavior
      @		requires position < 0 || position >= AppointmentsSetup.MAX_APPOINTMENT_ITEMS
      @				 || appointments_model.itemAt(position) == null
      @				 ||  ((Appointment) appointments_model.itemAt(position)).status_model == AppointmentsSetup.STATUS_DONE;
      @     assignable \nothing;
      @		signals_only UserException;
	  @		signals_redundantly (UserException e) appointments_model.equals(\old(appointments_model));
      @*/
    public void removeAppointment (short position) throws RemoteException, UserException;

    /*@ public normal_behavior
      @		requires position >= 0 && position < appointments_model.int_length();
      @ 	requires appointments_model.itemAt(position) != null;
      @		requires ((Appointment) appointments_model.itemAt(position)).status_model == AppointmentsSetup.STATUS_SCHEDULE;	
      @		requires date != null && date.length == DATE_LENGTH;
      @		assignable appointments_model; 
      @		ensures (((Appointment) appointments_model.itemAt(position)).date_model).equals(toJMLValueSequence(date));
      @ also
      @ public exceptional_behavior
      @		requires position < 0 || position >= appointments_model.int_length()
      @				 || appointments_model.itemAt(position) == null
      @				 || date == null || date.length != DATE_LENGTH
      @				 || ((Appointment) appointments_model.itemAt(position)).status_model != AppointmentsSetup.STATUS_SCHEDULE;
      @     assignable \nothing;
      @		signals_only UserException;
	  @		signals_redundantly (UserException e) appointments_model.equals(\old(appointments_model));
      @*/
    public void setAppointmentDate (short position, byte[] date) throws RemoteException, UserException;

    /*@ public normal_behavior
      @		requires position >= 0 && position < appointments_model.int_length();
      @		requires appointments_model.itemAt(position) != null;
      @ 	requires hour != null && hour.length == HOUR_LENGTH;
      @		requires ((Appointment) appointments_model.itemAt(position)).status_model == AppointmentsSetup.STATUS_SCHEDULE;		
      @		assignable appointments_model; 
      @		ensures (((Appointment) appointments_model.itemAt(position)).hour_model).equals(toJMLValueSequence(hour));
      @ also
      @ public exceptional_behavior
      @		requires position < 0 || position >= appointments_model.int_length()
      @				 || appointments_model.itemAt(position) == null
      @				 || hour == null || hour.length != HOUR_LENGTH
      @				 || ((Appointment) appointments_model.itemAt(position)).status_model != AppointmentsSetup.STATUS_SCHEDULE;
      @     assignable \nothing;
      @		signals_only UserException;
	  @		signals_redundantly (UserException e) appointments_model.equals(\old(appointments_model));
      @*/
    public void setAppointmentHour (short position, byte[] hour) throws RemoteException, UserException;

    /*@ public normal_behavior
      @		requires position >= 0 && position < appointments_model.int_length();
      @ 	requires doctor != null && doctor.length == DOCTOR_CODE_LENGTH;
      @		requires_redundantly (\forall int i; 0 <= i && i < DOCTOR_CODE_LENGTH; 
	  @					((byte)0x41 <= doctor[i] &&  doctor[i] <= (byte)0x5A )
      @					|| ((byte)0x61 <= doctor[i] &&  doctor[i] <= (byte)0x7A )
      @					|| ((byte)0x30 <= doctor[i] &&  doctor[i] <= (byte)0x39 ));
      @		requires appointments_model.itemAt(position) != null;
      @		requires ((Appointment) appointments_model.itemAt(position)).status_model == AppointmentsSetup.STATUS_SCHEDULE;			
      @ 	assignable appointments_model;
      @ 	ensures (((Appointment) appointments_model.itemAt(position)).doctor_model).equals(toJMLValueSequence(doctor));
      @ also
      @ public exceptional_behavior
      @		requires position < 0 || position >= appointments_model.int_length()
      @				 || appointments_model.itemAt(position) == null
      @				 || doctor == null || doctor.length != DOCTOR_CODE_LENGTH
      @				 || !(\forall int i; 0 <= i && i < DOCTOR_CODE_LENGTH; 
	  @					((byte)0x41 <= doctor[i] &&  doctor[i] <= (byte)0x5A )
      @					|| ((byte)0x61 <= doctor[i] &&  doctor[i] <= (byte)0x7A )
      @					|| ((byte)0x30 <= doctor[i] &&  doctor[i] <= (byte)0x39 ))
      @				 || ((Appointment) appointments_model.itemAt(position)).status_model != AppointmentsSetup.STATUS_SCHEDULE;
      @     assignable \nothing;
      @		signals_only UserException;
	  @		signals_redundantly (UserException e) appointments_model.equals(\old(appointments_model));
      @*/
    public void setAppointmentDoctor (short position, byte[] doctor) throws RemoteException, UserException;

    /*@ public normal_behavior
      @		requires position >= 0 && position < appointments_model.int_length();
      @ 	requires local != null && local.length == LOCAL_CODE_LENGTH;
      @		requires appointments_model.itemAt(position) != null;
      @		requires ((Appointment) appointments_model.itemAt(position)).status_model == AppointmentsSetup.STATUS_SCHEDULE;			
      @ 	assignable appointments_model;
      @ 	ensures (((Appointment) appointments_model.itemAt(position)).local_model).equals(toJMLValueSequence(local));
      @ also
      @ public exceptional_behavior
      @		requires position < 0 || position >= appointments_model.int_length()
      @				 || appointments_model.itemAt(position) == null
      @				 || local == null || local.length != LOCAL_CODE_LENGTH
      @				 || ((Appointment) appointments_model.itemAt(position)).status_model != AppointmentsSetup.STATUS_SCHEDULE;
      @     assignable \nothing;
      @		signals_only UserException;
	  @		signals_redundantly (UserException e) appointments_model.equals(\old(appointments_model));  		     		     		    
      @*/
    public void setAppointmentLocal (short position, byte[] local) throws RemoteException, UserException;

    /*@ public normal_behavior
      @		requires position >= 0 && position < appointments_model.int_length();
      @		requires appointments_model.itemAt(position) != null;
      @		requires AppointmentsSetup.STATUS_SCHEDULE < status && status <= AppointmentsSetup.STATUS_DONE;
      @		requires ((Appointment) appointments_model.itemAt(position)).status_model == status - 1;
      @		requires status != ((Appointment) appointments_model.itemAt(position)).status_model;	
      @ 	assignable appointments_model;
      @ 	ensures ((Appointment) appointments_model.itemAt(position)).status_model == status;
      @ also
      @ public exceptional_behavior
      @		requires position < 0 || position >= appointments_model.int_length()
      @				 || appointments_model.itemAt(position) == null
      @				 || status < AppointmentsSetup.STATUS_SCHEDULE || status > AppointmentsSetup.STATUS_DONE
      @				 || status == ((Appointment) appointments_model.itemAt(position)).status_model	
      @				 || ((Appointment) appointments_model.itemAt(position)).status_model != status - 1;
      @     assignable \nothing;
      @		signals_only UserException;
	  @		signals_redundantly (UserException e) appointments_model.equals(\old(appointments_model));		     		    		     		     		        
      @*/
    public void setAppointmentStatus (short position, byte status) throws RemoteException, UserException;
    
    /*@ public normal_behavior
      @	    requires position >= 0 && position < appointments_model.int_length();
      @	    requires appointments_model.itemAt(position) != null;
      @		requires ((Appointment) appointments_model.itemAt(position)).status_model == AppointmentsSetup.STATUS_SCHEDULE;		
      @	    requires 0x00 < type && type <= AppointmentsSetup.MAX_TYPE_CODES;
      @ 	assignable appointments_model;
      @ 	ensures ((Appointment) appointments_model.itemAt(position)).type_model == type;
      @ also
      @ public exceptional_behavior
      @		requires position < 0 || position >= appointments_model.int_length()
      @				|| appointments_model.itemAt(position) == null
      @				|| type < 0 || type >  AppointmentsSetup.MAX_TYPE_CODES;
      @     assignable \nothing;
      @		signals_only UserException;
	  @		signals_redundantly (UserException e) appointments_model.equals(\old(appointments_model));
      @*/
    public void setAppointmentType (short position, byte type) throws RemoteException, UserException;

    /*@ public normal_behavior
      @		requires position >= 0 && position < appointments_model.int_length();
      @		requires appointments_model.itemAt(position) != null;	
      @		assignable commons.CardUtil;
      @     ensures ((Appointment) appointments_model.itemAt(position)).id_model == \result;
      @ also
      @ public exceptional_behavior
      @		requires position < 0 || position >= appointments_model.int_length()
      @				 || appointments_model.itemAt(position) == null;
      @     assignable \nothing;
      @		signals_only UserException;
      @		signals_redundantly (UserException e) appointments_model.equals(\old(appointments_model));
	  @*/
    public byte getAppointmentID (short position) throws RemoteException, UserException;
    
    /*@ public normal_behavior
      @		requires position >= 0 && position < appointments_model.int_length();
      @		requires appointments_model.itemAt(position) != null;	
      @		assignable commons.CardUtil;
      @     ensures (((Appointment) appointments_model.itemAt(position)).date_model).equals(toJMLValueSequence(\result));
      @ also
      @ public exceptional_behavior
      @		requires position < 0 || position >= appointments_model.int_length()
      @				 || appointments_model.itemAt(position) == null;
      @     assignable \nothing;
      @		signals_only UserException;
	  @		signals_redundantly (UserException e) appointments_model.equals(\old(appointments_model));      		    		    		    		     		    
      @*/
    public byte[] getAppointmentDate (short position) throws RemoteException, UserException;

    /*@ public normal_behavior
      @		requires position >= 0 && position < appointments_model.int_length();
      @		requires appointments_model.itemAt(position) != null;	
      @		assignable commons.CardUtil;
      @		ensures (((Appointment) appointments_model.itemAt(position)).hour_model).equals(toJMLValueSequence(\result));
      @ also
      @ public exceptional_behavior
      @		requires position < 0 || position >= appointments_model.int_length()
      @				 || appointments_model.itemAt(position) == null;
      @     assignable \nothing;
      @		signals_only UserException;
	  @		signals_redundantly (UserException e) appointments_model.equals(\old(appointments_model));	     		    		      		    		    		    		     		     		    	 		    
      @*/
    public byte[] getAppointmentHour (short position) throws RemoteException, UserException;

    /*@ public normal_behavior
      @		requires position >= 0 && position < appointments_model.int_length();
      @		requires appointments_model.itemAt(position) != null;	
      @		assignable commons.CardUtil;
      @ ensures (((Appointment) appointments_model.itemAt(position)).doctor_model).equals(toJMLValueSequence(\result));
      @ also
      @ public exceptional_behavior
      @		requires position < 0 || position >= appointments_model.int_length()
      @				 || appointments_model.itemAt(position) == null;
      @     assignable \nothing;
      @		signals_only UserException;
	  @		signals_redundantly (UserException e) appointments_model.equals(\old(appointments_model));     		    		      		    		    		    		     		     		    	 		    
      @*/
    public byte[] getAppointmentDoctor (short position) throws RemoteException, UserException;
 
    /*@ public normal_behavior
      @		requires position >= 0 && position < appointments_model.int_length();
      @		requires appointments_model.itemAt(position) != null;	
      @		assignable commons.CardUtil; 
      @		ensures (((Appointment) appointments_model.itemAt(position)).local_model).equals(toJMLValueSequence(\result));
      @ also
      @ public exceptional_behavior
      @		requires position < 0 || position >= appointments_model.int_length()
      @				 || appointments_model.itemAt(position) == null;
      @     assignable \nothing;
      @		signals_only UserException;
	  @		signals_redundantly (UserException e) appointments_model.equals(\old(appointments_model));
      @*/
    public byte[] getAppointmentLocal (short position) throws RemoteException, UserException;

    /*@ public normal_behavior
      @		requires position >= 0 && position < appointments_model.int_length();
      @		requires appointments_model.itemAt(position) != null;	
      @		assignable \nothing;
      @		ensures ((Appointment) appointments_model.itemAt(position)).status_model == \result;
      @ also
      @ public exceptional_behavior
      @		requires position < 0 || position >= appointments_model.int_length()
      @				 || appointments_model.itemAt(position) == null;
      @     assignable \nothing;
      @		signals_only UserException;
	  @		signals_redundantly (UserException e) appointments_model.equals(\old(appointments_model));    		    		      		 		    	 		    
      @*/
    public byte getAppointmentStatus (short position) throws RemoteException, UserException;
    
    /*@ public normal_behavior
      @		requires position >= 0 && position < appointments_model.int_length();
      @		requires appointments_model.itemAt(position) != null;	
      @		assignable \nothing;
      @		ensures ((Appointment) appointments_model.itemAt(position)).type_model == \result;
      @ also
      @ public exceptional_behavior
      @		requires position < 0 || position >= appointments_model.int_length()
      @				 || appointments_model.itemAt(position) == null;
      @     assignable \nothing;
      @		signals_only UserException;
	  @		signals_redundantly (UserException e) appointments_model.equals(\old(appointments_model));
      @*/
    public byte getAppointmentType (short position) throws RemoteException, UserException;
    
  
  /*@ public normal_behavior
    @ 	assignable commons.CardUtil;
    @		ensures \result == appointments_model.int_length() - appointments_model.count(null);
    @*/
    public short countAppointments() throws RemoteException, UserException;
    
    
    public /*@ pure @*/ Appointment[] getAppointments() throws RemoteException, UserException;
    
    
  /*@ public normal_behavior
    @		requires position >= 0;
    @		assignable \nothing;
    @ ensures (position < 0 || position >= appointments_model.int_length()) ==> (\result == false);
    @ ensures ((position >= 0 && position < appointments_model.int_length())
    @           && appointments_model.itemAt(position) == null) ==> (\result == false);
    @ ensures ((position >= 0 && position < appointments_model.int_length())
    @           && appointments_model.itemAt(position) != null) ==> (\result == true);
    @	also
	@	public exceptional_behavior
	@		requires position < 0;
	@		assignable \nothing;
	@		signals_only UserException;
	@*/
    public boolean validateAppointmentPosition(short position)  throws RemoteException, UserException;
    
}

