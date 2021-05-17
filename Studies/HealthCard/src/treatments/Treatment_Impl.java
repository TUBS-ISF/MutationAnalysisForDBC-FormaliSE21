package treatments;

import commons.CardUtil;
import commons.Common;


public class Treatment_Impl implements Treatment {

    private /*@ spec_public @*/ byte appointmentID; //@ in appointmentID_model;
	/*@ public represents
	  @			appointmentID_model <- appointmentID;
	  @*/
    
    private /*@ spec_public @*/ byte diagnosticID; //@ in diagnosticID_model;
	/*@ public represents
	  @			diagnosticID_model <- diagnosticID;
	  @*/
	 
    private /*@ spec_public @*/ byte id;//@ in treatmentID_model;
    /*@ public represents
    @			treatmentID_model <- id;
    @*/

    private /*@ spec_public @*/ byte[] healthProblem;//@ in healthProblem_model;
  /*@ public represents
    @			healthProblem_model <- toJMLValueSequence(healthProblem);
    @*/

    private /*@ spec_public @*/  byte[] recommendation;//@ in recommendation_model;
  /*@ public represents
    @			recommendation_model <- toJMLValueSequence(recommendation);
    @*/

    private byte[] buffer = new byte[Common.MAX_BUFFER_BYTES];
    
    public Treatment_Impl (byte appointmentID, byte diagnosticID, byte treatmentID) {
    	super();
    	this.appointmentID = appointmentID;
    	this.diagnosticID = diagnosticID;
    	this.id = treatmentID;
    	
    	this.healthProblem = new byte[TreatmentsSetup.MAX_HEALTHPROBLEM_LENGTH];
    	this.recommendation = new byte[TreatmentsSetup.MAX_MEDICALRECOMMENDATION_LENGTH];
    	
    }
 
    public void setAppointmentID (byte appointmentID) {
    	this.appointmentID = appointmentID;
    }

    public byte getAppointmentID () {
        return this.appointmentID;
    }

    public void setTreatmentID (byte id) {
    	this.id = id;
    }

    public byte getTreatmentID () {
        return this.id;
    }

    public void setHealthProblem (byte[] healthProblem) {
    	CardUtil.cleanField(this.healthProblem);
    	CardUtil.arrayCopy(healthProblem,this.healthProblem);
    }

    public byte[] getHealthProblem () {
        return this.healthProblem;
    }

    public void setMedicalRecommendation (byte[] recommendation_buffer, short size_buffer, short offset, boolean firstBlock) {   	
    	if(firstBlock)
    		CardUtil.cleanField(this.recommendation);
    	//Util doesnt's work on runtime test.
    	System.arraycopy(recommendation_buffer, (short)0, this.recommendation, offset, size_buffer);
    }

    public byte[] getMedicalRecommendation (short offset, short size) {
    	//Util doesnt's work on runtime test.
		System.arraycopy(this.recommendation, offset, buffer, (short)0, size);
    	return buffer;
    }
    
    
    public short getRecommendationSize() {
    	short count = (short)0;
    	for(short i = (short)0; i < this.recommendation.length; i++){
    		if(this.recommendation[i] != 0){
    			count++;
    		}
    	}	
    	return count;
	}
    

	public byte getDiagnosticID() {
		return this.diagnosticID;
	}

	public void setDiagnosticID(byte diagnosticID) {
		this.diagnosticID = diagnosticID;
	}


}

