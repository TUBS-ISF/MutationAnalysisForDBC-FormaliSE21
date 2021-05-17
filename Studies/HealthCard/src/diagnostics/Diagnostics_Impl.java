package diagnostics;

// import java.rmi.RemoteException;

import java.rmi.RemoteException;

import javacard.framework.UserException;
import appointments.Appointment;
import appointments.Appointments;
import appointments.Appointments_Impl;

import commons.CardUtil;

//import javacard.framework.UserException;
//@ model import org.jmlspecs.models.*;

public class Diagnostics_Impl implements Diagnostics{


	//Only for testing purpose (appointments exists only for the tests):
	private /*@ spec_public @*/ Appointment[] appointments; //@ in appointments_model;
	/*@ public represents
	  @ 	appointments_model <- toJMLObjectSequence(appointments);
	  @*/
	//------------------------------------------------------------------


	private /*@ spec_public @*/ Diagnostic[] diagnostics; //@ in diagnostics_model;
	/*@ public represents
	  @ 	diagnostics_model <- toJMLObjectSequence(diagnostics);
	  @*/


	public Diagnostics_Impl (/*SecurityService security*/) {
		super();
		diagnostics = new Diagnostic[(short)DiagnosticsSetup.MAX_DIAGNOSTIC_ITEMS];

		//testing code:
		try {
			this.appointments = new Appointments_Impl().getAppointments();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (UserException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}

		//Testing diagnostics insertion:
				diagnostics[0] = new Diagnostic_Impl((byte) 0x01, (byte) 0x00);
		//		diagnostics[0].setDescription(new byte[] {0x41,0x45,0x62,0x7A,0x69,0x35,0x67,0x39});
		//		diagnostics[0].setTitle(new byte[] {0x41,0x45,0x62});
		//		
		//		diagnostics[1] = new Diagnostic_Impl((byte) 0x00, (byte) 0x01);
		//		diagnostics[1].setDescription(new byte[] {0x35,0x40,0x22,0x1A,0x60,0x30,0x60,0x30});
		//		diagnostics[1].setTitle(new byte[] {0x51,0x54,0x26});
		//		
		//		diagnostics[2] = new Diagnostic_Impl((byte) 0x01, (byte) 0x01);

	}


	public byte addDiagnostic (byte appointmentID) /*throws RemoteException, UserException */{
		//    	<--- This line for security verification ---> 

		boolean notInUse = true;
		byte diagnosticID = 0;
		for(byte code = 0x00; code <= 0x7F; code++){
			for (short j = (short)0; j < diagnostics.length; j++) {
				if(diagnostics[j] != null){
					if(diagnostics[j].getDiagnosticID() == code && diagnostics[j].getAppointmentID()== appointmentID ){
						notInUse = false;
						break;
					}
				}
			}		
			if(notInUse){ 
				diagnosticID = code;
				break;
			}
			notInUse = true;
		}   

		for (short i = (short)0; i < (short)DiagnosticsSetup.MAX_DIAGNOSTIC_ITEMS; i++) {
			if (diagnostics[i] == null) {
				Diagnostic d = new Diagnostic_Impl(appointmentID, diagnosticID);
				diagnostics[i] = d;
				break;
			}            
		}	
		return diagnosticID;
	}


	public void removeDiagnostic (byte appointmentID, byte diagnosticID)  /*throws RemoteException, UserException */  {
		short  position = 0;

		for (short j = (short)0; j < DiagnosticsSetup.MAX_DIAGNOSTIC_ITEMS; j++) {
			if(diagnostics[j] != null){
				if(diagnostics[j].getDiagnosticID() == diagnosticID  && diagnostics[j].getAppointmentID()== appointmentID){
					position = j;
					diagnostics[position] = null;
					break;
				}
			}
		}

		if((short)position+1 < DiagnosticsSetup.MAX_DIAGNOSTIC_ITEMS){
			short c = CardUtil.countNotNullObjects(this.diagnostics);
			for (short i = position; i < c ; i++) {
				diagnostics[i] = diagnostics[(short)(i+1)];
			}
			diagnostics[c] = null;
		}
	}


	public void setDiagnosticTitle (byte appointmentID, byte diagnosticID, byte[] title)  /*throws RemoteException, UserException */ {
		for (short j = (short)0; j < (short)DiagnosticsSetup.MAX_DIAGNOSTIC_ITEMS; j++) {
			if(diagnostics[j] != null){
				if(diagnostics[j].getDiagnosticID() == diagnosticID  && diagnostics[j].getAppointmentID()== appointmentID){
					diagnostics[j].setTitle(title);
					break;
				}
			}
		}
	}


	public void setDiagnosticDescription (byte appointmentID, byte diagnosticID, byte[] description_buffer, short size_buffer, short offset, boolean firstBlock) throws RemoteException, UserException {
		for (short j = (short)0; j < (short)DiagnosticsSetup.MAX_DIAGNOSTIC_ITEMS; j++) {
			if(diagnostics[j] != null){
				if(diagnostics[j].getDiagnosticID() == diagnosticID  && diagnostics[j].getAppointmentID()== appointmentID){
					diagnostics[j].setDescription(description_buffer, size_buffer, offset, firstBlock);
					break;				
				}
			}
		}
	}


	public byte[] getDiagnosticIDs(short position) /*throws RemoteException, UserException */ {

		byte[] ids = new byte[2];
		for (short j = (short)0; j < (short)countAllDiagnostics(); j++) {
			if(j == position){
				ids[0] = diagnostics[j].getDiagnosticID();
				ids[1] = diagnostics[j].getAppointmentID();
				break;
			}
		}
		return ids;
	}

	public byte[] getDiagnosticTitle (byte appointmentID, byte diagnosticID)  /*throws RemoteException, UserException */  {
		byte[] title = null;
		for (short j = (short)0; j < (short)DiagnosticsSetup.MAX_DIAGNOSTIC_ITEMS; j++) {
			if(diagnostics[j] != null){
				if(diagnostics[j].getDiagnosticID() == diagnosticID  && diagnostics[j].getAppointmentID()== appointmentID){
					title = diagnostics[j].getTitle();
					break;
				}
			}
		}
		return CardUtil.clone(title);
	}


	public byte[] getDiagnosticDescription (byte appointmentID, byte diagnosticID, short offset, short size)  throws RemoteException, UserException {
		byte[] description_buffer = null;
		for (short j = (short)0; j < (short)DiagnosticsSetup.MAX_DIAGNOSTIC_ITEMS; j++) {
			if(diagnostics[j] != null){
				if(diagnostics[j].getDiagnosticID() == diagnosticID  && diagnostics[j].getAppointmentID()== appointmentID){
					description_buffer = diagnostics[j].getDescription(offset, size);
					break;
				}
			}
		}
		return description_buffer;
	}


	public short getDiagnosticDescriptionSize(byte appointmentID, byte diagnosticID) throws RemoteException, UserException{

		short size = 0;
		for (short j = (short)0; j < (short)DiagnosticsSetup.MAX_DIAGNOSTIC_ITEMS; j++) {
			if(diagnostics[j] != null){
				if(diagnostics[j].getDiagnosticID() == diagnosticID  && diagnostics[j].getAppointmentID()== appointmentID){
					size = diagnostics[j].getDescriptionSize();
					break;
				}
			}
		}

		return size;
	}


	public short countDiagnostics (byte appointmentID)  /*throws RemoteException, UserException */  {
		byte count = 0;
		for (short j = (short)0; j < (short)DiagnosticsSetup.MAX_DIAGNOSTIC_ITEMS; j++) {
			if(diagnostics[j] != null){
				if(diagnostics[j].getAppointmentID()== appointmentID){
					count++;
				}
			}
		}
		return count; 
	}


	public short countAllDiagnostics ()  /*throws RemoteException, UserException */ {
		return CardUtil.countNotNullObjects(diagnostics);
	}


	//Testing Code: ............................................................................
	public String toString() {
		StringBuffer s = new java.lang.StringBuffer("");

		for(int i=0; i<diagnostics.length; i++)
			s.append(" "+diagnostics[i]);

		return s.toString();
	}


	public Diagnostic[] getDiagnostics()  throws RemoteException, UserException {
		return diagnostics;
	}

}

