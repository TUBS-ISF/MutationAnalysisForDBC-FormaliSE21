package vaccines;

import commons.CardUtil;

public class Vaccine_Impl implements Vaccine {

 
	private /*@ spec_public @*/ byte[] designation; //@ in designation_model;
    /*@ public represents
	  @ 	designation_model <- toJMLValueSequence(designation);
	  @*/
 
    private /*@ spec_public @*/  byte[] date; //@ in date_model;
    /*@ public represents
	  @ 	date_model <- toJMLValueSequence(date);
	  @*/


    public Vaccine_Impl (byte[] designation, byte[] date) {
    	super();
		this.designation = new byte[(short)VaccinesSetup.VACCINE_CODE_LENGTH];
		System.arraycopy(designation, (short)0, this.designation, (short)0, (short)designation.length);
		
		this.date = new byte[(short)DATE_LENGTH];
		System.arraycopy(date, (short)0, this.date, (short)0, (short)date.length);
    }
    
    
    public Vaccine_Impl () {
    	super();
		this.designation = new byte[(short)VaccinesSetup.VACCINE_CODE_LENGTH];		
		this.date = new byte[(short)DATE_LENGTH];
    }


    public void setDesignation (byte[] designation) {
    	CardUtil.arrayCopy(designation, this.designation);
    }


    public byte[] getDesignation () {
    	return CardUtil.clone(this.designation);
    }


    public void setVaccinationDate (byte[] date) {
    	CardUtil.arrayCopy(date, this.date);	
    }


    public byte[] getVaccinationDate () {
    	return CardUtil.clone(this.date);
    }
    
    //test method
    public String toString(){
    	StringBuffer a = new StringBuffer("");
    	StringBuffer b = new StringBuffer(""); 
    	
    	a.append("Vaccine: ");
    	for(int i=0; i < designation.length;i++){
    		a.append( " "+ designation[i]);
    	}
    	
    	b.append("Date: ");
    	for(int i=0; i < date.length;i++){
    		b.append( " "+ date[i]);
    	}
    	
    	return a.toString() + "\n"+ b.toString() +"\n";
    }

}

