package appointments;

import javacard.framework.Util;
import commons.CardUtil;


public class Appointment_Impl implements Appointment {

    private /*@ spec_public @*/ byte id; //@ in id_model;
    /*@ public represents
	  @ 	id_model <- id;
	  @*/

    private /*@ spec_public @*/ byte[] date; //@ in date_model;
    /*@ public represents
	  @ 	date_model <- toJMLValueSequence(date);
	  @*/

    private /*@ spec_public @*/ byte[] hour; //@ in hour_model;
    /*@ public represents
	  @ 	hour_model <- toJMLValueSequence(hour);
	  @*/

    private /*@ spec_public @*/ byte[] local; //@ in local_model;
    /*@ public represents
	  @ 	local_model <- toJMLValueSequence(local);
	  @*/

    private /*@ spec_public @*/ byte[] doctor; //@ in doctor_model;
    /*@ public represents
	  @ 	doctor_model <- toJMLValueSequence(doctor);
	  @*/

    private /*@ spec_public @*/ byte status; //@ in status_model;
    /*@ public represents
	  @ 	status_model <- status;
	  @*/
    
    private /*@ spec_public @*/ byte type; //@ in type_model;
    /*@ public represents
	  @ 	type_model <- type;
	  @*/

    public Appointment_Impl (byte id, byte[] date, byte[] hour, byte[] local, byte[] doctor, byte type) {
    	super();
    	this.id = id;
    	
    	this.date = new byte[DATE_LENGTH];
    	System.arraycopy(date, (short)0, this.date, (short)0, (short)date.length);
    	
    	this.hour = new byte[HOUR_LENGTH];
    	System.arraycopy(hour, (short)0, this.hour, (short)0, (short)hour.length);
    	
    	this.local = new byte[LOCAL_CODE_LENGTH];
    	System.arraycopy(local, (short)0, this.local, (short)0, (short)local.length);
    	
    	this.doctor = new byte[DOCTOR_CODE_LENGTH];
    	System.arraycopy(doctor, (short)0, this.doctor, (short)0, (short)doctor.length);
      	
    	this.type = type;
    	
      	this.status = 0x00;
    }

    public byte getID () {
        return this.id;
    }


    public byte[] getDate () {
    	return this.date;
    }


    public byte[] getHour () {
    	return this.hour;
    }


    public byte[] getDoctor () {
    	return this.doctor;
    }

  
    public byte[] getLocal () {
    	return this.local;
    }

 
    public byte getStatus () {
        return status;
    }
    
    
    public byte getType() {
		return type;
	}


    public void setID (byte id) {
			this.id = id;
    }

 
    public void setDate (byte[] date) {
    	CardUtil.arrayCopy(date, this.date);
    }

  
    public void setHour (byte[] hour) {
    	CardUtil.arrayCopy(hour, this.hour);
    }


    public void setLocal (byte[] local) {
    	CardUtil.arrayCopy(local, this.local);
    }

 
    public void setStatus (byte status) {
    	this.status = status;
    }


    public void setDoctor (byte[] doctor) {
    	CardUtil.arrayCopy(doctor, this.doctor);
    }
	

	public void setType(byte type) {
		this.type = type;
	}
	
	
	
	//Test support code: (Only to support the tests) -------------------
	
	public String toString(){
    	StringBuffer a = new StringBuffer("");
    	StringBuffer b = new StringBuffer(""); 
    	StringBuffer c = new StringBuffer("");
    	StringBuffer d = new StringBuffer("");
    	StringBuffer e = new StringBuffer("");
    	StringBuffer f = new StringBuffer("");
    	StringBuffer g = new StringBuffer("");
    	
    	a.append(" id: " + id );
    	
    	b.append(" date: ");
    	for(int i=0; i < date.length;i++){
    		b.append( " "+ date[i]);
    	}
    	
    	c.append(" hour: ");
    	for(int i=0; i < hour.length;i++){
    		c.append( " "+ hour[i]);
    	}
    	
    	d.append(" local: ");
    	for(int i=0; i < local.length;i++){
    		d.append( " "+ local[i]);
    	}
    	
    	e.append(" doctor: ");
    	for(int i=0; i < doctor.length;i++){
    		e.append( " "+ doctor[i]);
    	}
    	
    	f.append(" status: " + status);
    	
    	g.append(" type: " + type);
    	
    	return a.toString() + "\n"+ b.toString() 
    			+"\n" + c.toString() + "\n" 
    			+ d.toString() + "\n"
    			+ e.toString() + "\n"
    			+ f.toString() + "\n"
    			+ g.toString() + "\n";
    }

}

