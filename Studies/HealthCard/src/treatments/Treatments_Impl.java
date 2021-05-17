package treatments;

//import java.rmi.RemoteException;

import java.rmi.RemoteException;

import javacard.framework.UserException;
import appointments.Appointment;
import appointments.Appointments_Impl;

import commons.CardUtil;
import diagnostics.Diagnostic;
import diagnostics.Diagnostic_Impl;
import diagnostics.DiagnosticsSetup;
import diagnostics.Diagnostics_Impl;

//@ model import org.jmlspecs.models.*;


public class Treatments_Impl implements Treatments {


	private /*@ spec_public @*/ Treatment[] treatments;//@ in treatments_model;
	/*@ public represents
	  @ 	treatments_model <- toJMLObjectSequence(treatments);
	  @*/

	//For Test:
	private /*@ spec_public @*/ Diagnostic[] diagnostics; //@ in diagnostics_model;
	/*@ public represents
	  @ 	diagnostics_model <- toJMLObjectSequence(diagnostics);
	  @*/

	public Treatments_Impl (/*SecurityService security*/) {
		super();
		treatments = new Treatment[(short) TreatmentsSetup.MAX_TREATMENT_ITEMS];


		//testing code:
		try {
			this.diagnostics = new Diagnostics_Impl().getDiagnostics();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (UserException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}

		treatments[0] = new Treatment_Impl((byte) 0x01, (byte) 0x00, (byte) 0x00);
	}


	public byte addTreatment (byte appointmentID, byte diagnosticID) /*throws RemoteException, UserException */{
		boolean notInUse = true;
		byte treatmentID = 0;
		for(byte code = 0x00; code <= 0x7F; code++){
			for (short j = (short)0; j < treatments.length; j++) {
				if(treatments[j] != null){
					if(treatments[j].getTreatmentID() == code 
							&& treatments[j].getAppointmentID()== appointmentID
							&& treatments[j].getDiagnosticID()== diagnosticID){
						notInUse = false;
						break;
					}
				}
			}		
			if(notInUse){ 
				treatmentID = code;
				break;
			}
			notInUse = true;
		}   

		for (short i = (short)0; i < (short)TreatmentsSetup.MAX_TREATMENT_ITEMS; i++) {
			if (treatments[i] == null) {
				Treatment t = new Treatment_Impl(appointmentID, diagnosticID, treatmentID);
				treatments[i] = t;
				break;
			}            
		}
		return treatmentID;
	}


	public void removeTreatment (byte appointmentID, byte diagnosticID, byte treatmentID) /*throws RemoteException, UserException */{
		short  position = 0;

		for (short j = (short)0; j < TreatmentsSetup.MAX_TREATMENT_ITEMS; j++) {
			if(treatments[j] != null){
				if(treatments[j].getTreatmentID() == treatmentID && treatments[j].getDiagnosticID() == diagnosticID  && treatments[j].getAppointmentID()== appointmentID){
					position = j;
					treatments[position] = null;
					break;
				}
			}
		}

		if((short)position+1 < TreatmentsSetup.MAX_TREATMENT_ITEMS){
			short c = CardUtil.countNotNullObjects(this.treatments);
			for (short i = position; i < c ; i++) {
				treatments[i] = treatments[(short)(i+1)];
			}
			treatments[c] = null;
		}
	}


	public byte[] getTreatmentIDs(short position)  /*throws RemoteException, UserException */{
		byte[] ids = new byte[3];
		for (short j = (short)0; j < (short)countAllTreatments(); j++) {
			if(j == position){
				ids[0] = treatments[j].getTreatmentID();
				ids[1] = treatments[j].getDiagnosticID();
				ids[2] = treatments[j].getAppointmentID();
				break;
			}
		}
		return ids;
	}


	public short countTreatments (byte appointmentID, byte diagnosticID) /*throws RemoteException, UserException */{
		byte count = 0;
		for (short j = (short)0; j < (short)TreatmentsSetup.MAX_TREATMENT_ITEMS; j++) {
			if(treatments[j] != null){
				if(treatments[j].getAppointmentID()== appointmentID && treatments[j].getDiagnosticID()== diagnosticID){
					count++;
				}
			}
		}
		return count; 
	}


	public void setHealthProblem (byte appointmentID, byte diagnosticID, byte treatmentID, byte[] healthProblem) /*throws RemoteException, UserException */{
		for (short j = (short)0; j < (short)TreatmentsSetup.MAX_TREATMENT_ITEMS; j++) {
			if(treatments[j] != null){
				if(treatments[j].getDiagnosticID() == diagnosticID
						&& treatments[j].getTreatmentID() == treatmentID
						&& treatments[j].getAppointmentID()== appointmentID){
					treatments[j].setHealthProblem(healthProblem);
					break;
				}
			}
		}
	}


	public byte[] getHealthProblem (byte appointmentID, byte diagnosticID, byte treatmentID)/*throws RemoteException, UserException */ {
		byte[] healthProblem = null;
		for (short j = (short)0; j < (short)TreatmentsSetup.MAX_TREATMENT_ITEMS; j++) {
			if(treatments[j] != null){
				if(treatments[j].getDiagnosticID() == diagnosticID  && treatments[j].getAppointmentID()== appointmentID){
					healthProblem = treatments[j].getHealthProblem();
					break;
				}
			}
		}
		return CardUtil.clone(healthProblem);
	}


	public void setMedicalRecommendation (byte appointmentID, byte treatmentID, byte diagnosticID, byte[] recommendation_buffer, short size_buffer, short offset, boolean firstBlock ) /*throws RemoteException, UserException */{
		for (short j = (short)0; j < (short)TreatmentsSetup.MAX_TREATMENT_ITEMS; j++) {
			if(treatments[j] != null){
				if(treatments[j].getDiagnosticID() == diagnosticID
						&& treatments[j].getTreatmentID() == treatmentID
						&& treatments[j].getAppointmentID()== appointmentID){
					treatments[j].setMedicalRecommendation(recommendation_buffer, size_buffer, offset, firstBlock);
					break;
				}
			}
		}
	}


	public byte[] getMedicalRecommendation (byte appointmentID, byte diagnosticID, byte treatmentID, short offset, short size)/*throws RemoteException, UserException */ {
		byte[] recommendation = null;
		for (short j = (short)0; j < (short)TreatmentsSetup.MAX_TREATMENT_ITEMS; j++) {
			if(treatments[j] != null){
				if(treatments[j].getDiagnosticID() == diagnosticID  && treatments[j].getAppointmentID()== appointmentID){
					recommendation = treatments[j].getMedicalRecommendation(offset, size);
					break;
				}
			}
		}
		// return CardUtil.clone(recommendation);
		return recommendation;
	}


	public short getMedicalRecommendationSize(byte appointmentID,
			byte diagnosticID, byte treatmentID) throws RemoteException,
			UserException {

		short size = 0;
		for (short j = (short)0; j < (short)TreatmentsSetup.MAX_TREATMENT_ITEMS; j++) {
			if(treatments[j] != null){
				if(treatments[j].getTreatmentID() == treatmentID  
				   && treatments[j].getDiagnosticID() == diagnosticID 
				   && treatments[j].getAppointmentID()== appointmentID){
					size = treatments[j].getRecommendationSize();
					break;
				}
			}
		}

		return size;
	}


	public short countAllTreatments() /*throws RemoteException, UserException */ {
		return CardUtil.countNotNullObjects(treatments);
	}


	//testing code: ............................................................................
	public Treatment[] getTreatments()  throws RemoteException, UserException {
		return treatments;
	}


}

