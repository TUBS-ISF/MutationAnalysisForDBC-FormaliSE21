package medicines;

import java.rmi.RemoteException;

import commons.CardUtil;

import javacard.framework.UserException;
import treatments.Treatment;
import treatments.Treatment_Impl;
import treatments.Treatments_Impl;
//@ model import org.jmlspecs.models.*;

public class Medicines_Impl implements Medicines {



	private /*@ spec_public @*/ Medicine[] medicines; //@ in medicines_model;
	/*@ public represents
	  @ 	medicines_model <- JMLObjectSequence.convertFrom(medicines);
	  @*/

	//For Test:
	private /*@ spec_public @*/ Treatment[] treatments;//@ in treatments_model;
	/*@ public represents
	  @ 	treatments_model <- JMLObjectSequence.convertFrom(treatments);
	  @*/


	public Medicines_Impl (/*SecurityService security*/) {
		super();
		medicines = new Medicine[(short) MedicinesSetup.MAX_MEDICINE_ITEMS];

		
		
		//testing code:
		try {
			this.treatments = new Treatments_Impl().getTreatments();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (UserException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}

		medicines[0] = new Medicine_Impl((byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00);
		medicines[1] = new Medicine_Impl((byte) 0x00, (byte) 0x01, (byte) 0x00,(byte) 0x02);
		medicines[2] = new Medicine_Impl((byte) 0x00, (byte) 0x01, (byte) 0x01,(byte) 0x04);

	}


	public byte addMedicine (byte appointmentID, byte diagnosticID, byte treatmentID) {
		boolean notInUse = true;
		byte medicineID = 0;
		for(byte code = 0x00; code <= 0x7F; code++){
			for (short j = (short)0; j < medicines.length; j++) {
				if(medicines[j] != null){
					if(medicines[j].getMedicineID() == code 
							&& medicines[j].getAppointmentID()== appointmentID
							&& medicines[j].getDiagnosticID()== diagnosticID
							&& medicines[j].getTreatmentID() == treatmentID){
						notInUse = false;
						break;
					}
				}
			}		
			if(notInUse){ 
				medicineID = code;
				break;
			}
			notInUse = true;
		}   

		for (short i = (short)0; i < (short)MedicinesSetup.MAX_MEDICINE_ITEMS; i++) {
			if (medicines[i] == null) {
				Medicine m = new Medicine_Impl(appointmentID, diagnosticID, treatmentID, medicineID);
				medicines[i] = m;
				break;
			}            
		}
		return medicineID;
	}


	public void removeMedicine (byte appointmentID, byte diagnosticID, byte treatmentID, byte medicineID) {
		short  position = 0;

		for (short j = (short)0; j < MedicinesSetup.MAX_MEDICINE_ITEMS; j++) {
			if(medicines[j] != null){
				if(medicines[j].getMedicineID() == medicineID 
						&& medicines[j].getTreatmentID() == treatmentID 
						&& medicines[j].getDiagnosticID() == diagnosticID  
						&& medicines[j].getAppointmentID()== appointmentID){
					position = j;
					medicines[position] = null;
					break;
				}
			}
		}

		if((short)position+1 < MedicinesSetup.MAX_MEDICINE_ITEMS){
			short c = CardUtil.countNotNullObjects(this.medicines);
			for (short i = position; i < c ; i++) {
				medicines[i] = medicines[(short)(i+1)];
			}
			medicines[c] = null;
		}
	}


	public short countMedicines (byte appointmentID, byte diagnosticID, byte treatmentID) {
		byte count = 0;
		for (short j = (short)0; j < (short)MedicinesSetup.MAX_MEDICINE_ITEMS; j++) {
			if(medicines[j] != null){
				if(medicines[j].getAppointmentID()== appointmentID 
						&& medicines[j].getDiagnosticID()== diagnosticID
						&& medicines[j].getTreatmentID()== treatmentID){
					count++;
				}
			}
		}
		return count; 
	}

	public short countAllMedicines () {
		return CardUtil.countNotNullObjects(medicines);
	}


	public byte[] getMedicineIDs(short position) /*throws RemoteException, UserException*/ {
		byte[] ids = new byte[4];
		for (short j = (short)0; j < (short)countAllMedicines(); j++) {
			if(j == position){
				ids[0] = medicines[j].getMedicineID();
				ids[1] = medicines[j].getTreatmentID();
				ids[2] = medicines[j].getDiagnosticID();
				ids[3] = medicines[j].getAppointmentID();
				break;
			}
		}
		return ids;
	}


	public void setDesignation (byte appointmentID, byte diagnosticID, byte treatmentID, byte medicineID, byte[] designation) {
		for (short j = (short)0; j < (short)MedicinesSetup.MAX_MEDICINE_ITEMS; j++) {
			if(medicines[j] != null){
				if(medicines[j].getMedicineID() == medicineID
						&& medicines[j].getDiagnosticID() == diagnosticID
						&& medicines[j].getTreatmentID() == treatmentID
						&& medicines[j].getAppointmentID()== appointmentID){
					medicines[j].setDesignation(designation);
					break;
				}
			}
		}
	}


	public byte[] getDesignation (byte appointmentID, byte diagnosticID, byte treatmentID, byte medicineID) {
		byte[] designation = null;
		for (short j = (short)0; j < (short)MedicinesSetup.MAX_MEDICINE_ITEMS; j++) {
			if(medicines[j] != null){
				if(medicines[j].getDiagnosticID() == diagnosticID  && medicines[j].getAppointmentID()== appointmentID){
					designation = medicines[j].getDesignation();
					break;
				}
			}
		}
		return CardUtil.clone(designation);
	}


	public void setAdministrationDescription (byte appointmentID, byte diagnosticID, byte treatmentID, byte medicineID, byte[] administration) {
		for (short j = (short)0; j < (short)MedicinesSetup.MAX_MEDICINE_ITEMS; j++) {
			if(medicines[j] != null){
				if(medicines[j].getMedicineID() == medicineID
						&& medicines[j].getDiagnosticID() == diagnosticID
						&& medicines[j].getTreatmentID() == treatmentID
						&& medicines[j].getAppointmentID()== appointmentID){
					medicines[j].setDesignation(administration);
					break;
				}
			}
		}
	}


	public byte[] getAdministrationDescription (byte appointmentID, byte diagnosticID, byte treatmentID, byte medicineID) {
		byte[] administration = null;
		for (short j = (short)0; j < (short)MedicinesSetup.MAX_MEDICINE_ITEMS; j++) {
			if(medicines[j] != null){
				if(medicines[j].getDiagnosticID() == diagnosticID  && medicines[j].getAppointmentID()== appointmentID){
					administration = medicines[j].getDesignation();
					break;
				}
			}
		}
		return CardUtil.clone(administration);
	}


	public void setPeriod (byte appointmentID, byte treatmentID, byte diagnosticID, byte medicineID, short period_in_days) {
		for (short j = (short)0; j < (short)MedicinesSetup.MAX_MEDICINE_ITEMS; j++) {
			if(medicines[j] != null){
				if(medicines[j].getMedicineID() == medicineID
						&& medicines[j].getDiagnosticID() == diagnosticID
						&& medicines[j].getTreatmentID() == treatmentID
						&& medicines[j].getAppointmentID()== appointmentID){
					medicines[j].setPeriod(period_in_days);
					break;
				}
			}
		}
	}


	public short getPeriod (byte appointmentID, byte diagnosticID, byte treatmentID, byte medicineID) {
		short days = 0;
		for (short j = (short)0; j < (short)MedicinesSetup.MAX_MEDICINE_ITEMS; j++) {
			if(medicines[j] != null){
				if(medicines[j].getDiagnosticID() == diagnosticID  && medicines[j].getAppointmentID()== appointmentID){
					days = medicines[j].getPeriod();
					break;
				}
			}
		}
		return days;
	}


	public void setDate (byte appointmentID, byte diagnosticID, byte treatmentID, byte medicineID, byte[] date) {
		for (short j = (short)0; j < (short)MedicinesSetup.MAX_MEDICINE_ITEMS; j++) {
			if(medicines[j] != null){
				if(medicines[j].getMedicineID() == medicineID
						&& medicines[j].getDiagnosticID() == diagnosticID
						&& medicines[j].getTreatmentID() == treatmentID
						&& medicines[j].getAppointmentID()== appointmentID){
					medicines[j].setDate(date);
					break;
				}
			}
		}
	}


	public byte[] getDate (byte appointmentID, byte diagnosticID, byte treatmentID, byte medicineID) {
		byte[] date = null;
		for (short j = (short)0; j < (short)MedicinesSetup.MAX_MEDICINE_ITEMS; j++) {
			if(medicines[j] != null){
				if(medicines[j].getDiagnosticID() == diagnosticID  && medicines[j].getAppointmentID()== appointmentID){
					date = medicines[j].getDate();
					break;
				}
			}
		}
		return CardUtil.clone(date);
	}


}

