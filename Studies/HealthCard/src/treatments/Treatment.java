package treatments;

import commons.Common;
//@ model import org.jmlspecs.models.*;

public interface Treatment extends Common{
	
	//Model variables that must have a representation somewhere in the concrete class.
    //@ public model instance byte appointmentID_model;
	//@ public model instance byte diagnosticID_model;
    //@ public model instance byte treatmentID_model;
    //@ public model instance non_null JMLValueSequence healthProblem_model;
    //@ public model instance non_null JMLValueSequence recommendation_model;


    //Specification Invariants of the model variables. 
    /*@ invariant 0x00 <= appointmentID_model && appointmentID_model <= 0x7F;
      @ invariant 0x00 <= diagnosticID_model && diagnosticID_model <= 0x7F;
      @ invariant 0x00 <= treatmentID_model && treatmentID_model <= 0x7F;
      @ invariant healthProblem_model != null && healthProblem_model.int_length() == TreatmentsSetup.MAX_HEALTHPROBLEM_LENGTH;
      @ invariant recommendation_model!= null && recommendation_model.int_length() == TreatmentsSetup.MAX_MEDICALRECOMMENDATION_LENGTH;
      @*/
	
    
  /*@ assignable appointmentID_model;	
    @ ensures appointmentID_model == appointmentID;
    @*/
    public void setAppointmentID (byte appointmentID);  
    
    
    /*@ assignable \nothing;
	  @ ensures \result == appointmentID_model;
	  @*/
    public /*@ pure @*/  byte getAppointmentID ();
    
        
    /*@ assignable diagnosticID_model;	
      @ ensures diagnosticID_model == diagnosticID;
      @*/
    public void setDiagnosticID (byte diagnosticID);
    
    
    /*@ assignable \nothing;
	  @ ensures \result == diagnosticID_model;
	  @*/
   public /*@ pure @*/  byte getDiagnosticID ();
    
    
    /*@ assignable treatmentID_model;	
      @ ensures treatmentID_model == id;
      @*/
    public void setTreatmentID (byte id);

    
    /*@ assignable \nothing;
	  @ ensures \result == treatmentID_model;
	  @*/
    public /*@ pure @*/ byte getTreatmentID ();

    
	/*@ public normal_behavior
      @ 	requires healthProblem != null && healthProblem.length <= TreatmentsSetup.MAX_HEALTHPROBLEM_LENGTH;
	  @ 	assignable healthProblem_model;
   	  @ 	ensures (\forall int i; 0 <= i && i < healthProblem.length; healthProblem_model.itemAt(i).equals(new JMLByte(healthProblem[i])));
	  @*/
    public void setHealthProblem (byte[] healthProblem);

    
    /*@ assignable \nothing;
	  @ ensures toJMLValueSequence(\result).equals(healthProblem_model);
	  @*/
    public /*@ pure @*/ byte[] getHealthProblem ();

    
    /*@ public normal_behavior
	  @ 	assignable recommendation_model;
	  @*/
    public void setMedicalRecommendation (byte[] recommendation_buffer, short size_buffer, short offset, boolean firstBlock);

    
    /*@ assignable java.lang.Object;
	  @*/
    public byte[] getMedicalRecommendation (short offset, short size);
    
    /*@ assignable \nothing;
	  @*/
	public short getRecommendationSize();

}

