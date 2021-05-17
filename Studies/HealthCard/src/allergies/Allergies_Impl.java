package allergies;

import java.rmi.RemoteException;
import commons.CardUtil;

import javacard.framework.UserException;

//import javacard.framework.service.CardRemoteObject;

public class Allergies_Impl /* extends CardRemoteObject */  implements Allergies {

    private /*@ spec_public @*/ Allergy[] allergies; //@ in allergies_model;
    /*@ public represents
	  @ 	allergies_model <- toJMLObjectSequence(allergies);
	  @*/

    public Allergies_Impl (/*SecurityService security*/) {
    	super();
		allergies = new Allergy[(short)AllergiesSetup.MAX_ALLERGY_ITEMS];
		
		//testing code: ------------------------------------
		allergies[0] = new Allergy_Impl(new byte[]{0x42,0x4A,0x31,0x35}, new byte[]{0x01,0x01,0x14,0x09});
		allergies[1] = new Allergy_Impl(new byte[]{0x43,0x4A,0x31,0x36}, new byte[]{0x02,0x02,0x14,0x09});
		allergies[2] = new Allergy_Impl(new byte[]{0x44,0x4A,0x31,0x37}, new byte[]{0x03,0x03,0x14,0x09});
		// -------------------------------------------------
    }

    public void addAllergy (byte[] designation, byte[] date) throws RemoteException, UserException {
    	//  <--- This line for security verification --->
    	for (short i = (short)0; i < (short)AllergiesSetup.MAX_ALLERGY_ITEMS; i++) {
			if (allergies[i] == null) {
				//Allergy a = new Allergy_Impl(designation,date);
				Allergy a = new Allergy_Impl();
				allergies[i] = a;
				allergies[i].setDesignation(designation);
				allergies[i].setIdentificationDate(date);
				break;
			}            
		}
    }   

    public void removeAllergy (short position) throws RemoteException, UserException {
    	//    <--- This line for security verification --->
    	allergies[position] = null;
    	
    	if(position+1 < AllergiesSetup.MAX_ALLERGY_ITEMS){
    		short c = CardUtil.countNotNullObjects(this.allergies);
    		for (short i = position; i < c ; i++) {
    			allergies[i] = allergies[(short)(i+1)];
    		}
    		allergies[c] = null;
    	}
    }
    

    public short countAllergies () throws RemoteException, UserException {
    	//  <--- This line for security verification --->
    	return CardUtil.countNotNullObjects(this.allergies);
    }

    public void setAllergyDesignation (short position, byte[] designation) throws RemoteException, UserException {
    	//  <--- This line for security verification --->
    	allergies[position].setDesignation(designation);
    }

    public byte[] getAllergyDesignation (short position) throws RemoteException, UserException {
    	//  <--- This line for security verification --->
    	 return CardUtil.clone(allergies[position].getDesignation());
    }

    public void setAllergyDate (short position, byte[] date) throws RemoteException, UserException {
    	//  <--- This line for security verification --->
		allergies[position].setIdentificationDate(date);	
    }

    public byte[] getAllergyDate (short position) throws RemoteException, UserException {
    	//  <--- This line for security verification --->
    	return CardUtil.clone(allergies[position].getIdentificationDate());
    }
    
    public boolean validateAllergyPosition(short position){
    	 return CardUtil.validateObjectArrayPosition(this.allergies, position);		
    }
    
   
    
    //Testing Method ........................................................................
    public String toString() {
    	StringBuffer s = new java.lang.StringBuffer("");
    	
    	for(int i=0; i<allergies.length; i++)
    				s.append(" "+allergies[i]);
    		
    	return s.toString();
    }

}

