package appointments;

import java.rmi.RemoteException;

import javacard.framework.UserException;
import commons.CardUtil;

public class Appointments_Impl implements Appointments {

    private /*@ spec_public @*/ Appointment[] appointments; //@ in appointments_model;
    /*@ public represents
	  @ 	appointments_model <- toJMLObjectSequence(appointments);
	  @*/
    
    public Appointments_Impl (/*SecurityService security*/) {
    	super();
		appointments = new Appointment[(short)AppointmentsSetup.MAX_APPOINTMENT_ITEMS];
		
		//testing code: 
		//NOTICE: you have to insert the current date, otherwise it will give you an invariant violation.
		/* 1. The date must be a current date or a future date. 
		 * 2. The date is inserted in the second argument of the Appointment_Impl constructor.
		 * 3. The date must be inserted in hexadecimal values in the format of {<month>,<day>,<year(first 2 digits)>, <year(last 2 digits)>}
		 */
		appointments[0] = new Appointment_Impl((byte) 0x01, 
												new byte[]{0x0B,0x13,0x14,0x09},
												new byte[] {0x09,0x00},
												new byte[] {0x41,0x45,0x62,0x7A,0x69,0x35,0x67,0x39},
												new byte[] {0x69,0x69,0x69,0x69,0x69},
												(byte) 0x01);
//		appointments[1] = new Appointment_Impl((byte) 0x01, 
//												new byte[]{0x05,0x01,0x14,0x0A},
//												new byte[] {0x09,0x00},
//												new byte[] {0x41,0x45,0x62,0x7A,0x69,0x35,0x67,0x39},
//												new byte[] {0x34,0x34,0x69,0x69,0x33},
//												(byte) 0x04);		
		appointments[0].setStatus((byte)0x01);
//		appointments[1].setStatus((byte)0x02);
//		appointments[2].setStatus((byte)0x02);
		
    }
    
    
    public void addAppointment (byte[] date, byte[] hour, byte[] local, byte[] doctor, byte type) throws RemoteException, UserException {
    	//	<--- This line for security verification ---> 
    	boolean notInUse = true;
    	byte id = 0;
    	for(byte code = 0x00; code <= 0x7F; code++){
    		for (short j = (short)0; j < (short)AppointmentsSetup.MAX_APPOINTMENT_ITEMS; j++) {
    			if(appointments[j] != null){
    				if(appointments[j].getID() == code){
    					notInUse = false;
    					break;
    				}
    			}
    		}		
    		if(notInUse){ 
    			id = code;
    			break;
    		}
    		notInUse = true;
    	}

    	for (short i = (short)0; i < (short)AppointmentsSetup.MAX_APPOINTMENT_ITEMS; i++) {
			if (appointments[i] == null) {
				Appointment a = new Appointment_Impl(id, date, hour, local, doctor, type);
//				a.setDate(date);
//				a.setHour(hour);
//				a.setLocal(local);
//				a.setDoctor(doctor);
//				a.setType(type);
				appointments[i] = a;
				break;
			}            
		}
    }
    

    public void removeAppointment (short position) throws RemoteException, UserException {
    	//	<--- This line for security verification ---> 
    	
    	appointments[position] = null;
    	
    	if(position+1 < AppointmentsSetup.MAX_APPOINTMENT_ITEMS){
    		short c = CardUtil.countNotNullObjects(appointments);
    		for (short i = position; i < c ; i++) {
    			appointments[i] = appointments[(short)(i+1)];
    		}
    		appointments[c] = null;
    	} 
    }

    
    public void setAppointmentDate (short position, byte[] date) throws RemoteException, UserException {
    	// <--- This line for security verification ---> 
		appointments[position].setDate(date);	  
    }


    public void setAppointmentHour (short position, byte[] hour) throws RemoteException, UserException {
    	// 	<--- This line for security verification ---> 
		appointments[position].setHour(hour);
    }


    public void setAppointmentDoctor (short position, byte[] doctor) throws RemoteException, UserException {
    	//  <--- This line for security verification ---> 
		appointments[position].setDoctor(doctor);
    }


    public void setAppointmentLocal (short position, byte[] local) throws RemoteException, UserException {
    	//  <--- This line for security verification ---> 
		appointments[position].setLocal(local);
    }


    public void setAppointmentStatus (short position, byte status) throws RemoteException, UserException {
    	//  <--- This line for security verification --->     	
    	appointments[position].setStatus(status);	
    }
    
    
    public void setAppointmentType (short position, byte type) throws RemoteException, UserException {
    	// <--- This line for security verification --->    	
    	appointments[position].setType(type);    
    }
    

	public byte getAppointmentID (short position) throws RemoteException, UserException {
		return appointments[position].getID();
	}


    public byte[] getAppointmentDate (short position) throws RemoteException, UserException {
    	// <--- This line for security verification --->
    	return CardUtil.clone(appointments[position].getDate());
    }

  
    public byte[] getAppointmentHour (short position) throws RemoteException, UserException {
    	//  <--- This line for security verification --->
    	return CardUtil.clone(appointments[position].getHour());
    }

  
    public byte[] getAppointmentDoctor (short position) throws RemoteException, UserException {
    	//  <--- This line for security verification --->
    	return CardUtil.clone(appointments[position].getDoctor());
    }


    public byte[] getAppointmentLocal (short position) throws RemoteException, UserException {
    	//  <--- This line for security verification --->
    	return CardUtil.clone(appointments[position].getLocal());
    }


    public byte getAppointmentStatus (short position) throws RemoteException, UserException {
    	//  <--- This line for security verification --->
    	return appointments[position].getStatus();
    }
    

	public byte getAppointmentType (short position) throws RemoteException, UserException {
		//	<--- This line for security verification --->
    	return appointments[position].getType();
	}

	
	public Appointment[] getAppointments()  throws RemoteException, UserException {
		return appointments;
	}


	public short countAppointments() throws RemoteException, UserException {
	//  <--- This line for security verification --->
    	return CardUtil.countNotNullObjects(appointments);
	}


	public boolean validateAppointmentPosition(short position) {
		return CardUtil.validateObjectArrayPosition(appointments, position);	
	}

	
	//Testing Method: --------------------------------------------------------
    public String toString() {
    	StringBuffer s = new java.lang.StringBuffer("");
    	
    	for(int i=0; i<appointments.length; i++)
    				s.append(" "+appointments[i]);
    		
    	return s.toString();
    }
    
}

