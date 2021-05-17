package medicines;

import commons.Common;
//@ model import org.jmlspecs.models.*;

public interface Medicine extends Common{

	//Model variables that must have a representation somewhere in the concrete class.
	//@ public model instance byte appointmentID_model;
	//@ public model instance byte diagnosticID_model;
	//@ public model instance byte treatmentID_model;
	//@ public model instance byte medicineID_model;
	//@ public model instance non_null JMLValueSequence designation_model;
	//@ public model instance non_null JMLValueSequence administration_description_model;
	//@ public model instance short period_days_model;


	//Specification Invariants of the model variables. 
	/*@ invariant 0x00 <= appointmentID_model && appointmentID_model <= 0x7F;
      @ invariant 0x00 <= diagnosticID_model && diagnosticID_model <= 0x7F; 
      @ invariant 0x00 <= treatmentID_model && treatmentID_model <= 0x7F; 
      @ invariant 0x00 <= medicineID_model && medicineID_model <= 0x7F;
      @ invariant designation_model != null && designation_model.int_length() == MedicinesSetup.MEDICINE_CODE_LENGTH;
      @ invariant administration_description_model != null && administration_description_model.int_length() == MedicinesSetup.MAX_ADMIN_DESCRIPTION_LENGTH;
      @ invariant period_days_model >= 0;
      @*/


	/*@ assignable appointmentID_model;	
    @ ensures appointmentID_model == appointmentID;
    @*/
	public void setAppointmentID (byte appointmentID);

	/*@ assignable \nothing;
	  @ ensures \result == appointmentID_model;
	  @*/
	public /*@ pure @*/ byte getAppointmentID ();

	/*@ assignable diagnosticID_model;	
    @ ensures diagnosticID_model == diagnosticID;
    @*/
	public void setDiagnosticID (byte diagnosticID);

	/*@ assignable \nothing;
	  @ ensures \result == diagnosticID_model;
	  @*/
	public /*@ pure @*/  byte getDiagnosticID ();

	/*@ assignable treatmentID_model;	
    @ ensures treatmentID_model == treatmentID;
    @*/
	public void setTreatmentID (byte treatmentID);

	/*@ assignable \nothing;
	  @ ensures \result == treatmentID_model;
	  @*/
	public/*@ pure @*/  byte getTreatmentID ();

	/*@ assignable medicineID_model;	
    @ ensures medicineID_model == id;
    @*/
	public void setMedicineID (byte id);

	/*@ assignable \nothing;
	  @ ensures \result == medicineID_model;
	  @*/
	public/*@ pure @*/  byte getMedicineID ();

	/*@ public normal_behavior
      @ 	requires designation != null && designation.length <= MedicinesSetup.MAX_ADMIN_DESCRIPTION_LENGTH;
	  @ 	assignable designation_model;
 	  @ 	ensures (\forall int i; 0 <= i && i < designation.length; designation_model.itemAt(i).equals(new JMLByte(designation[i])));
	  @*/
	public void setDesignation (byte[] designation);

	/*@ assignable \nothing;
	  @ ensures toJMLValueSequence(\result).equals(designation_model);
	  @*/
	public /*@ pure @*/ byte[] getDesignation ();


	/*@ public normal_behavior
      @ 	requires administration != null && administration.length <= MedicinesSetup.MAX_ADMIN_DESCRIPTION_LENGTH;
	  @ 	assignable administration_description_model;
	  @ 	ensures (\forall int i; 0 <= i && i < administration.length; administration_description_model.itemAt(i).equals(new JMLByte(administration[i])));
	  @*/
	public void setAdministrationDescription (byte[] administration);

	/*@ assignable \nothing;
	  @ ensures toJMLValueSequence(\result).equals(administration_description_model);
	  @*/
	public /*@ pure @*/ byte[] getAdministrationDescription ();

	/*@ public normal_behavior
      @ 	requires_redundantly days >= 0;
	  @ 	assignable period_days_model;
	  @ 	ensures period_days_model == days;
	  @*/
	public void setPeriod (short days);

	/*@ assignable \nothing;
	  @ ensures \result == period_days_model;
	  @*/
	public /*@ pure @*/ short getPeriod ();


	/*@ public normal_behavior
      @ 	requires prescriptionDate != null && prescriptionDate.length == DATE_LENGTH;
	  @ 	assignable date_model;
	  @ 	ensures (\forall int i; 0 <= i && i < prescriptionDate.length; date_model.itemAt(i).equals(new JMLByte(prescriptionDate[i])));
      @*/
	public void setDate (byte[] prescriptionDate);

	
	/*@ assignable \nothing;
	  @ ensures toJMLValueSequence(\result).equals(date_model);
	  @*/
	public /*@ pure @*/ byte[] getDate ();



}

