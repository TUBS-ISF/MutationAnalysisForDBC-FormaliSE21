package vaccines;

//@ model import org.jmlspecs.models.*;

import java.rmi.RemoteException;

import commons.CardUtil;

import javacard.framework.UserException;

public class Vaccines_Impl /* extends CardRemoteObject */  implements Vaccines {

   
    private /*@ spec_public @*/ Vaccine[] vaccines; //@ in vaccines_model;
    /*@ public represents
	  @ 	vaccines_model <- JMLObjectSequence.convertFrom(vaccines);
	  @*/

   
    public Vaccines_Impl (/*SecurityService security*/) {
    	super();
		vaccines = new Vaccine[(short)VaccinesSetup.MAX_VACCINE_ITEMS];
		
		//testing code:
		vaccines[0] = new Vaccine_Impl(new byte[]{0x42,0x4A,0x31,0x35}, new byte[]{0x01,0x01,0x14,0x09});
		vaccines[1] = new Vaccine_Impl(new byte[]{0x43,0x4A,0x31,0x36}, new byte[]{0x02,0x02,0x14,0x09});
		vaccines[2] = new Vaccine_Impl(new byte[]{0x44,0x4A,0x31,0x37}, new byte[]{0x03,0x03,0x14,0x09});
    }

    
    
    public void addVaccine (byte[] designation, byte[] date) throws RemoteException, UserException {
    //  <--- This line for security verification --->
    	for (short i = (short)0; i < (short)VaccinesSetup.MAX_VACCINE_ITEMS; i++) {
			if (vaccines[i] == null) {
				//Vaccine v = new Vaccine_Impl(designation,date);
				Vaccine v = new Vaccine_Impl();
				vaccines[i] = v;
				vaccines[i].setDesignation(designation);
				vaccines[i].setVaccinationDate(date);
				vaccines[i] = v;
				break;
			}            
		}
    }

 
    public void removeVaccine (short position) throws RemoteException, UserException {
    	//      <--- This line for security verification --->
    	vaccines[position] = null;
    	
    	if((short)(position+1) < (short)VaccinesSetup.MAX_VACCINE_ITEMS){
    		short c = CardUtil.countNotNullObjects(this.vaccines);
    		for (short i = position; i < c ; i++) {
    			vaccines[i] = vaccines[(short)(i+1)];
    		}
    		vaccines[c] = null;
    	}
    }

    public short countVaccines () throws RemoteException, UserException {
    //  <--- This line for security verification --->
    	return CardUtil.countNotNullObjects(this.vaccines);
    }


    public void setVaccineDesignation (short position, byte[] designation) throws RemoteException, UserException {
    //  <--- This line for security verification --->
    	vaccines[position].setDesignation(designation);
    }


    public byte[] getVaccineDesignation (short position) throws RemoteException, UserException {
    	//  <--- This line for security verification --->
    	return CardUtil.clone(vaccines[position].getDesignation());
    }

    public void setVaccinationDate (short position, byte[] date) throws RemoteException, UserException {
    	//  <--- This line for security verification --->
		vaccines[position].setVaccinationDate(date);	
    }

   
    public byte[] getVaccinationDate (short position) throws RemoteException, UserException {
    	//  <--- This line for security verification --->
    	return CardUtil.clone(vaccines[position].getVaccinationDate());
    }


	public boolean validateVaccinePosition(short position) throws RemoteException, UserException {
		return CardUtil.validateObjectArrayPosition(this.vaccines, position);	
	}
	
	
	//Testing Method
    public String toString() {
    	StringBuffer s = new java.lang.StringBuffer("");
    	
    	for(int i=0; i<vaccines.length; i++)
    		if(vaccines[i] != null)
    				s.append("\n #" + i + ". \n" + vaccines[i]);
    		
    	return s.toString();
    }

}

