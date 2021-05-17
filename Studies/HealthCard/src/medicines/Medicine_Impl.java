package medicines;

import commons.CardUtil;


public class Medicine_Impl implements Medicine {


	private /*@ spec_public @*/ byte appointmentID; //@ in appointmentID_model;
	/*@ public represents
      @			appointmentID_model <- appointmentID;
      @*/

	private /*@ spec_public @*/ byte diagnosticID;//@ in diagnosticID_model;
	/*@ public represents
      @			diagnosticID_model <- diagnosticID;
      @*/

	private /*@ spec_public @*/ byte treatmentID;//@ in treatmentID_model;
	/*@ public represents
      @			treatmentID_model <- treatmentID;
      @*/

	private/*@ spec_public @*/  byte id;//@ in medicineID_model;
	/*@ public represents
    @			medicineID_model <- id;
    @*/

	private/*@ spec_public @*/  byte[] designation;//@ in designation_model;
	/*@ public represents
      @			designation_model <- toJMLValueSequence(designation);
      @*/

	private /*@ spec_public @*/ byte[] administration_description;//@ in administration_description_model;
	/*@ public represents
    @			administration_description_model <- toJMLValueSequence(administration_description);
    @*/

	private/*@ spec_public @*/  byte[] date;//@ in date_model;
	/*@ public represents
    @			date_model <- toJMLValueSequence(date);
    @*/

	private /*@ spec_public @*/ short period_days;//@ in period_days_model;
	/*@ public represents
    @			period_days_model <- period_days;
    @*/



	public Medicine_Impl (byte appointmentID, byte diagnosticID, byte treatmentID ,byte medicineID) {
		super();
		this.appointmentID = appointmentID;
		this.diagnosticID = diagnosticID;
		this.treatmentID = treatmentID;
		this.id = medicineID;

		this.date = new byte[DATE_LENGTH];
		this.designation = new byte[MedicinesSetup.MEDICINE_CODE_LENGTH];
		this.administration_description = new byte[MedicinesSetup.MAX_ADMIN_DESCRIPTION_LENGTH];

	}

	public void setAppointmentID (byte appointmentID) {
		this.appointmentID = appointmentID;
	}

	public byte getAppointmentID () {
		return this.appointmentID;
	}


	public void setDiagnosticID (byte diagnosticID) {
		this.diagnosticID = diagnosticID;
	}

	public byte getDiagnosticID () {
		return this.diagnosticID;
	}


	public void setTreatmentID (byte treatmentID) {
		this.treatmentID = treatmentID;
	}

	public byte getTreatmentID () {
		return this.treatmentID;
	}


	public void setMedicineID (byte id) {
		this.id = id;
	}


	public byte getMedicineID () {
		return this.id;
	}    

	public void setDesignation (byte[] designation) {
		CardUtil.arrayCopy(designation,this.designation);
	}

	public byte[] getDesignation () {
		return this.designation;
	}

	public void setAdministrationDescription (byte[] administration) {
		CardUtil.cleanField(this.administration_description);
		CardUtil.arrayCopy(administration,this.administration_description);
	}

	public byte[] getAdministrationDescription () {
		return this.administration_description;
	}

	public void setPeriod (short days) {
		this.period_days = days;
	}

	public short getPeriod () {
		return this.period_days;
	}

	public void setDate (byte[] prescriptionDate) {
		CardUtil.arrayCopy(prescriptionDate,this.date);
	}

	public byte[] getDate () {
		return this.date;
	}


}

