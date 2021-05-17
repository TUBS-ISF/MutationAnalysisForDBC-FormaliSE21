package vaccines;

import commons.Common;
//@ model import org.jmlspecs.models.*;

public interface Vaccine extends Common{

	//Model variables that must have a representation somewhere in the concrete class.
    //@ public model instance non_null JMLValueSequence designation_model;
	
	//Specification Invariants of the model variables. 
    /*@ invariant designation_model != null && designation_model.int_length() == VaccinesSetup.VACCINE_CODE_LENGTH;
  	  @*/
	
	/*@ public normal_behavior
      @		requires_redundantly designation != null && designation.length == VaccinesSetup.VACCINE_CODE_LENGTH;
      @		assignable designation_model;
      @		ensures (\forall int i; 0 <= i && i < designation.length; designation_model.itemAt(i).equals(new JMLByte(designation[i])));
      @*/
    public void setDesignation (byte[] designation);

    
   /*@ assignable commons.CardUtil;
	 @ ensures toJMLValueSequence(\result).equals(designation_model);
	 @*/
    public byte[] getDesignation ();

    
  /*@ public normal_behavior
    @ 	requires_redundantly date != null && date.length == DATE_LENGTH;
	@ 	assignable date_model;
	@ 	ensures (\forall int i; 0 <= i && i < date.length; date_model.itemAt(i).equals(new JMLByte(date[i])));
    @*/
    public void setVaccinationDate (byte[] date);

  
  /*@ assignable commons.CardUtil;
    @ ensures toJMLValueSequence(\result).equals(date_model);
	@*/
    public byte[] getVaccinationDate ();

}

