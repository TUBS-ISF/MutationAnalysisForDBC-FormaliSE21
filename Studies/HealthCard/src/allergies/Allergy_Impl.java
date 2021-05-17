package allergies;

import javacard.framework.Util;
import commons.CardUtil;

//@ model import org.jmlspecs.models.*;

public class Allergy_Impl implements Allergy{
	
    private /*@ spec_public @*/ byte[] designation; //@ in designation_model;
    /*@ public represents
	  @ 	designation_model <- toJMLValueSequence(designation);
	  @*/
    
    private /*@ spec_public @*/  byte[] date; //@ in date_model;
    /*@ public represents
	  @ 	date_model <- toJMLValueSequence(date);
	  @*/

    public Allergy_Impl (byte[] designation, byte[] date) {
    	super();
		this.designation = new byte[(short)AllergiesSetup.ALLERGY_CODE_LENGTH];
		System.arraycopy(designation, (short)0, this.designation, (short)0, (short)designation.length);
		
		this.date = new byte[(short)DATE_LENGTH];
		System.arraycopy(date, (short)0, this.date, (short)0, (short)date.length);
    }
    
    public Allergy_Impl () {
    	super();
		this.designation = new byte[(short)AllergiesSetup.ALLERGY_CODE_LENGTH];		
		this.date = new byte[(short)DATE_LENGTH];
    }

    public void setDesignation (byte[] designation) {
    	CardUtil.cleanField(this.designation);
    	CardUtil.arrayCopy(designation, this.designation);
    }

    public byte[] getDesignation () {
       return this.designation;
    }


    public void setIdentificationDate (byte[] date) {
    	CardUtil.arrayCopy(date, this.date);	
    }


    public byte[] getIdentificationDate () {
    	return this.date;
    }

    
    //Testing code: ...........................................................................
    public String toString(){
    	StringBuffer a = new StringBuffer("");
    	StringBuffer b = new StringBuffer(""); 
    	StringBuffer c = new StringBuffer("");
    	
    	for(int i=0; i < designation.length;i++){
    		a.append( " "+ designation[i]);
    	}
    	
    	for(int i=0; i < date.length;i++){
    		c.append( " "+ date[i]);
    	}
    	
    	return a.toString() + "\n"+ b.toString() +"\n" + c.toString() + "\n";
    }
    
}

