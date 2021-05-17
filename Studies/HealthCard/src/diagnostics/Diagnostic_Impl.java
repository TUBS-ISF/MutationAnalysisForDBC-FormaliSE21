package diagnostics;

import commons.CardUtil;
import commons.Common;

public class Diagnostic_Impl implements Diagnostic {

  
    private /*@ spec_public @*/ byte appointmentID; //@ in appointmentID_model;
    /*@ public represents
      @			appointmentID_model <- appointmentID;
      @*/
  
    private /*@ spec_public @*/ byte diagnosticID; //@ in diagnosticID_model;
    /*@ public represents
      @			diagnosticID_model <- diagnosticID;
      @*/

   
    private /*@ spec_public @*/ byte[] title; //@ in title_model;
    /*@ public represents
      @			title_model <- toJMLValueSequence(title);
      @*/

   
    private /*@ spec_public @*/ byte[] description; //@ in description_model;
    /*@ public represents
      @			description_model <- toJMLValueSequence(description);
      @*/

    private byte[] buffer = new byte[Common.MAX_BUFFER_BYTES];
  
    public Diagnostic_Impl (byte appointmentID, byte diagnosticID) {
    	super();
    	this.appointmentID = appointmentID;
    	
    	this.diagnosticID = diagnosticID;
    	
    	this.title = new byte[DiagnosticsSetup.MAX_TITLE_LENGTH];
    	this.description = new byte[DiagnosticsSetup.MAX_DESCRIPTION_LENGTH];
    }
    
    public byte getAppointmentID () {
    	return this.appointmentID;
    }

    
    public void setAppointmentID (byte appointmentID) {
    	this.appointmentID = appointmentID;
    }

   
    public short getDescriptionSize() {
		short count = (short)0;
    	for(short i = (short)0; i < this.description.length; i++){
    		if(this.description[i] != 0){
    			count++;
    		}
    	}	
    	return count;
	}
    
   
    public byte[] getDescription (short offset, short size) {
    	//Util doesnt's work on runtime test.
		System.arraycopy(this.description, offset, buffer, (short)0, size);
    	return buffer;
    }

    public void setDescription (byte[] description_buffer, short size_buffer, short offset, boolean firstBlock) {
    	if(firstBlock)
    		CardUtil.cleanField(this.description);
    	//Util doesnt's work on runtime test.
    	System.arraycopy(description_buffer, (short)0, this.description, offset, size_buffer);
    }

 
    public byte getDiagnosticID () {
        return this.diagnosticID;
    }

  
    public void setDiagnosticID (byte diagnosticID) {
    	this.diagnosticID = diagnosticID;
    }

    public byte[] getTitle () {
        return this.title;
    }

    public void setTitle (byte[] title) {
    	CardUtil.cleanField(this.title);
    	CardUtil.arrayCopy(title,this.title);
    }
    
    
	
	//Test support code: (Only to support the tests) -------------------

    public String toString(){
    	StringBuffer a = new StringBuffer("");
    	StringBuffer b = new StringBuffer(""); 
    	StringBuffer c = new StringBuffer("");
    	StringBuffer d = new StringBuffer("");
    	
    	a.append(" appointment id: " + appointmentID );
    	
    	b.append(" diagnostic id: " + diagnosticID );
    	   	
    	c.append(" title: ");
    	for(int i=0; i < title.length;i++){
    		c.append( " "+ title[i]);
    	}
    	
    	d.append(" description: ");
    	for(int i=0; i < description.length;i++){
    		d.append( " "+ description[i]);
    	}
    	
    	return a.toString() + "\n"+ b.toString() 
    			+"\n" + c.toString() + "\n" 
    			+ d.toString() + "\n";
    }

}

