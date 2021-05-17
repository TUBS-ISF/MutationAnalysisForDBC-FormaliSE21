package personal;

//import java.rmi.Remote;
import java.rmi.RemoteException;

import commons.Common;
import javacard.framework.UserException;
//@ model import org.jmlspecs.models.*;

public interface Personal extends Common/*, Remote*/{
	 	 
	//Model variables that must have a representation somewhere in the concrete class.
	//@ public model instance non_null JMLValueSequence name_model;
	//@ public model instance non_null JMLValueSequence passportID_model;
	//@ public model instance non_null JMLValueSequence id_model;
	//@ public model instance non_null JMLValueSequence birthplace_model;
	//@ public model instance non_null JMLValueSequence nationality_model;
	//@ public model instance non_null JMLValueSequence phone_model;
	//@ public model instance non_null JMLValueSequence relativePhone_model;
	//@ public model instance non_null JMLValueSequence address_model;
	//@ public model instance non_null JMLValueSequence city_model;
	//@ public model instance non_null JMLValueSequence zipCode_model;
	//@ public model instance non_null JMLValueSequence country_model;
	//@ public model instance non_null JMLValueSequence socialSecurity_model;
	//@ public model instance non_null JMLValueSequence language_model;
	//@ public model instance byte gender_model; 
	//@ public model instance byte bloodtype_model;	
	
	//Specification Invariants  
	/*@ invariant name_model != null && name_model.int_length() == MAX_NAME_LENGTH;
	  @ invariant passportID_model != null && passportID_model.int_length() == MAX_PASSPORT_LENGTH;
	  @ invariant id_model != null && id_model.int_length() == MAX_ID_LENGTH;	  
	  @ invariant nationality_model != null && nationality_model.int_length() == COUNTRY_CODE_LENGTH;
	  @ invariant birthplace_model != null && birthplace_model.int_length() == COUNTRY_CODE_LENGTH;
	  @ invariant phone_model != null && phone_model.int_length() == MAX_PHONE_LENGTH;
	  @ invariant relativePhone_model != null && relativePhone_model.int_length() == MAX_PHONE_LENGTH;
	  @ invariant address_model != null && address_model.int_length() == MAX_ADDRESS_LENGTH;
	  @ invariant city_model != null && city_model.int_length() == MAX_CITY_LENGTH;
	  @ invariant zipCode_model != null && zipCode_model.int_length() == MAX_ZIPCODE_LENGTH;
	  @ invariant country_model != null && country_model.int_length() == COUNTRY_CODE_LENGTH;
	  @ invariant socialSecurity_model != null && socialSecurity_model.int_length() == MAX_SOCIALSECURITY_LENGTH;
	  @ invariant language_model != null && language_model.int_length() == LANGUAGE_LENGTH;
	  @
	  @ invariant gender_model >= 0x00 && gender_model <= MAX_GENDER_CODES;
	  @ invariant bloodtype_model >= 0x00 && bloodtype_model <= MAX_BLOODTYPE_CODES;
	  @*/
	
	  	// 0x01 = Male, 0x02 = Female
	  	public static final byte MAX_GENDER_CODES = (byte)0x02;
 	
	   	/*blood types
	   	A_POSITIVE = (byte)0x01;
	   	A_NEGATIVE = (byte)0x02;
	   	B_POSITIVE = (byte)0x03;
	   	B_NEGATIVE = (byte)0x04;
	   	AB_POSITIVE = (byte)0x05;
	   	AB_NEGATIVE = (byte)0x06;
	   	O_POSITIVE = (byte)0x07;
	    O_NEGATIVE = (byte)0x08;	  */  	
		public static final byte MAX_BLOODTYPE_CODES = (byte)0x08; 	
	   	public static final short MAX_NAME_LENGTH = (short)30;
	   	public static final short MAX_PASSPORT_LENGTH = (short)10;
	   	public static final short MAX_ID_LENGTH = (short)10;
	   	public static final short COUNTRY_CODE_LENGTH = (short)2;  	
	   	public static final short MAX_PHONE_LENGTH = (short)20;   	
	   	public static final short MAX_ADDRESS_LENGTH = (short)100;
		public static final short MAX_CITY_LENGTH = (short)25;
		public static final short MAX_ZIPCODE_LENGTH = (short)10; //this can be anything. letters, spaces, numbers and other special caracters.
		public static final short MAX_SOCIALSECURITY_LENGTH = (short)15; //10 in USA, 11 in Portugal, 15 for safety...
		public static final short LANGUAGE_LENGTH = (short)3;
		
		
//METHODS with JML SPECIFICATIONS	
		
//Mutation methods ---------------------------------------------------------------------------------------------
		
		/*@ public normal_behavior
		  @ 	requires name != null && name.length <= MAX_NAME_LENGTH;
		  @		requires !(\exists int i; 0 <= i && i < MAX_ID_LENGTH; 
	      @					((byte)0x30 <= name[i] && name[i] <= (byte)0x39 ));
		  @ 	assignable name_model;
		  @ 	ensures name_model.equals(toJMLValueSequence(name));
		  @ also
		  @ public exceptional_behavior
		  @		requires name.length > MAX_NAME_LENGTH
		  @				 || name == null
		  @				 || (\exists int i; 0 <= i && i < MAX_ID_LENGTH; 
	      @							((byte)0x30 <= name[i] && name[i] <= (byte)0x39 ));
		  @     assignable \nothing;
		  @		signals_only UserException;
		  @		signals_redundantly (UserException e) name_model.equals(\old(name_model));
		  @*/
		public void setName (/*@ non_null @*/ byte[] name) throws RemoteException, UserException;	

		
		/*@ public normal_behavior
		  @ 	requires (\exists byte b; 0x00 < b && b <= MAX_GENDER_CODES; gender == b);
		  @ 	assignable gender_model;
		  @ 	ensures gender_model == gender;
		  @ also 
		  @ public exceptional_behavior
		  @ 	requires (\forall byte b; 0x00 < b && b <= MAX_GENDER_CODES; gender != b);
		  @		assignable \nothing;
		  @		signals_only UserException;
		  @		signals (UserException e)
		  @			gender_model == \old(gender_model);
		  @*/
	    public void setGender (byte gender) throws RemoteException, UserException;

	     
	    /*@ public normal_behavior
	      @		requires (\exists byte b; 0x00 < b && b <= MAX_BLOODTYPE_CODES; btype == b);
		  @ 	assignable bloodtype_model;
		  @ 	ensures bloodtype_model == btype;
		  @ also
		  @	public exceptional_behavior
		  @		requires (\forall byte b; 0x00 < b && b <= MAX_BLOODTYPE_CODES; btype != b);
		  @		assignable \nothing;
		  @		signals_only UserException;
		  @		signals (UserException e)
		  @			bloodtype_model == \old(bloodtype_model);	
		  @*/
	    public void setBloodType (byte btype) throws RemoteException, UserException;

	    
	    /*@ public normal_behavior
	      @ 	requires date != null && date.length == DATE_LENGTH;
		  @ 	assignable date_model;
		  @ 	ensures date_model.equals(toJMLValueSequence(date));
		  @	also
		  @	public exceptional_behavior
		  @		requires date.length != DATE_LENGTH
		  @				 || date == null;
		  @		assignable \nothing;
		  @		signals_only UserException;
		  @		signals_redundantly (UserException e) date_model.equals(\old(date_model));
		  @*/
	    public void setBirthdate (/*@ non_null @*/ byte[] date) throws RemoteException, UserException;

	    
	    /*@ public normal_behavior
	      @ 	requires birthplace != null && birthplace.length == COUNTRY_CODE_LENGTH;
	      @		requires (\forall int i; 0 <= i && i < COUNTRY_CODE_LENGTH; 
	      @					((byte)0x41 <= birthplace[i] &&  birthplace[i] <= (byte)0x5A )
          @					|| ((byte)0x61 <= birthplace[i] &&  birthplace[i] <= (byte)0x7A ));
		  @ 	assignable birthplace_model;
		  @ 	ensures birthplace_model.equals(toJMLValueSequence(birthplace));
		  @	also
		  @	public exceptional_behavior
		  @		requires birthplace.length != COUNTRY_CODE_LENGTH
		  @				 || birthplace == null
		  @				 || !(\forall int i; 0 <= i && i < COUNTRY_CODE_LENGTH; 
	      @					((byte)0x41 <= birthplace[i] &&  birthplace[i] <= (byte)0x5A )
          @					|| ((byte)0x61 <= birthplace[i] &&  birthplace[i] <= (byte)0x7A ));
		  @		assignable \nothing;
		  @		signals_only UserException;
		  @		signals_redundantly (UserException e) birthplace_model.equals(\old(birthplace_model));
		  @*/
	    public void setBirthplace (/*@ non_null @*/ byte[] birthplace) throws RemoteException, UserException;

	 
	    
	    
	    /*@ public normal_behavior
	      @ 	requires passportID != null && passportID.length <= MAX_PASSPORT_LENGTH;
	      @		requires (\forall int i; 0 <= i && i < MAX_PASSPORT_LENGTH; 
	      @					((byte)0x41 <= passportID[i] &&  passportID[i] <= (byte)0x5A )
          @					|| ((byte)0x61 <= passportID[i] &&  passportID[i] <= (byte)0x7A )
          @					|| ((byte)0x30 <= passportID[i] &&  passportID[i] <= (byte)0x39 ));
		  @ 	assignable passportID_model;
		  @ 	ensures passportID_model.equals(toJMLValueSequence(passportID));
		  @ also
		  @	public exceptional_behavior
		  @		requires passportID.length > MAX_PASSPORT_LENGTH
		  @				 || passportID == null
		  @				 || !(\forall int i; 0 <= i && i < MAX_PASSPORT_LENGTH; 
	      @					((byte)0x41 <= passportID[i] &&  passportID[i] <= (byte)0x5A )
          @					|| ((byte)0x61 <= passportID[i] &&  passportID[i] <= (byte)0x7A )
          @					|| ((byte)0x30 <= passportID[i] &&  passportID[i] <= (byte)0x39 ));
		  @		assignable \nothing;
		  @		signals_only UserException;
		  @		signals_redundantly (UserException e) passportID.equals(\old(passportID));						
		  @*/
	    public void setPassportID (/*@ non_null @*/ byte[] passportID) throws RemoteException, UserException;

	    
	    /*@ public normal_behavior
	      @ 	requires id != null && id.length <= MAX_ID_LENGTH;
	      @		requires (\forall int i; 0 <= i && i < MAX_ID_LENGTH; 
	      @					((byte)0x30 <= id[i] && id[i] <= (byte)0x39 ));
		  @ 	assignable id_model;
		  @ 	ensures id_model.equals(toJMLValueSequence(id));
		  @	also
		  @	public exceptional_behavior
		  @		requires id.length > MAX_ID_LENGTH
		  @				 || id == null
		  @				 || !(\forall int i; 0 <= i && i < MAX_ID_LENGTH; 
	      @						((byte)0x30 <= id[i] && id[i] <= (byte)0x39 ));
		  @		assignable \nothing;
		  @		signals_only UserException;
		  @		signals_redundantly (UserException e) id_model.equals(\old(id_model));				
		  @*/
	    public void setID (/*@ non_null @*/ byte[] id) throws RemoteException, UserException;
	 
	    	    
	    /*@ public normal_behavior
	      @ 	requires nationality != null && nationality.length == COUNTRY_CODE_LENGTH;
	      @		requires (\forall int i; 0 <= i && i < COUNTRY_CODE_LENGTH; 
	      @					((byte)0x41 <= nationality[i] &&  nationality[i] <= (byte)0x5A )
          @					|| ((byte)0x61 <= nationality[i] &&  nationality[i] <= (byte)0x7A ));
		  @ 	assignable nationality_model;
		  @ 	ensures nationality_model.equals(toJMLValueSequence(nationality));
		  @	also
		  @	public exceptional_behavior
		  @		requires nationality.length != COUNTRY_CODE_LENGTH
		  @				 || nationality == null
		  @				 || !(\forall int i; 0 <= i && i < COUNTRY_CODE_LENGTH; 
	      @					((byte)0x41 <= nationality[i] &&  nationality[i] <= (byte)0x5A )
          @					|| ((byte)0x61 <= nationality[i] &&  nationality[i] <= (byte)0x7A ));
		  @		assignable \nothing;
		  @		signals_only UserException;
		  @		signals_redundantly (UserException e) nationality_model.equals(\old(nationality_model));
		  @*/
	    public void setNationality (/*@ non_null @*/ byte[] nationality) throws RemoteException, UserException;

	    
	    /*@ public normal_behavior
	      @ 	requires phone != null && phone.length <= MAX_PHONE_LENGTH;
	      @		requires (\forall int i; 0 <= i && i < MAX_PHONE_LENGTH; 
	      @					((byte)0x30 <= phone[i] && phone[i] <= (byte)0x39 ));
		  @ 	assignable phone_model;
		  @ 	ensures phone_model.equals(toJMLValueSequence(phone));
		  @	also
		  @	public exceptional_behavior
		  @		requires phone.length > MAX_PHONE_LENGTH
		  @				 || phone == null
		  @ 			 || !(\forall int i; 0 <= i && i < MAX_PHONE_LENGTH; 
	      @						((byte)0x30 <= phone[i] && phone[i] <= (byte)0x39 ));
		  @		assignable \nothing;
		  @		signals_only UserException;
		  @		signals_redundantly (UserException e) phone_model.equals(\old(phone_model));
		  @*/
	    public void setPhoneContact (/*@ non_null @*/ byte[] phone) throws RemoteException, UserException;

	    
	    /*@ public normal_behavior
	      @ 	requires phone != null && phone.length <= MAX_PHONE_LENGTH;
	      @		requires (\forall int i; 0 <= i && i < MAX_PHONE_LENGTH; 
	      @					((byte)0x30 <= phone[i] && phone[i] <= (byte)0x39 ));
		  @ 	assignable relativePhone_model;
		  @ 	ensures relativePhone_model.equals(toJMLValueSequence(phone));
		  @	also
		  @	public exceptional_behavior
		  @		requires phone.length > MAX_PHONE_LENGTH
		  @				 || phone == null
		  @				 || !(\forall int i; 0 <= i && i < MAX_PHONE_LENGTH; 
	      @					((byte)0x30 <= phone[i] && phone[i] <= (byte)0x39 ));
		  @		assignable \nothing;
		  @		signals_only UserException;
		  @		signals_redundantly (UserException e) relativePhone_model.equals(\old(relativePhone_model));
		  @*/
	    public void setRelativeContact (/*@ non_null @*/ byte[] phone) throws RemoteException, UserException;

	    
	    /*@ public normal_behavior
	      @ 	requires city != null && city.length <= MAX_CITY_LENGTH;
		  @ 	assignable city_model;
		  @ 	ensures city_model.equals(toJMLValueSequence(city));
		  @	also
		  @	public exceptional_behavior
		  @		requires city.length > MAX_CITY_LENGTH
		  @				 || city == null;
		  @		assignable \nothing;
		  @		signals_only UserException, RemoteException;
		  @		signals_redundantly (UserException e) city_model.equals(\old(city_model));
		  @*/
	    public void setAddress_City (/*@ non_null @*/ byte[] city) throws RemoteException, UserException;


	    /*@ public normal_behavior 
	      @ 	requires zipCode != null && zipCode.length <= MAX_ZIPCODE_LENGTH;
		  @ 	assignable zipCode_model;
		  @ 	ensures zipCode_model.equals(toJMLValueSequence(zipCode));
		  @	also
		  @	public exceptional_behavior
		  @		requires zipCode.length > MAX_ZIPCODE_LENGTH
		  @				 || zipCode == null;
		  @		assignable \nothing;
		  @		signals_only UserException, RemoteException;
		  @		signals_redundantly (UserException e) zipCode_model.equals(\old(zipCode_model));
		  @*/
	    public void setAddress_ZipCode (/*@ non_null @*/ byte[] zipCode) throws RemoteException, UserException; 
	    
	    
	    /*@ public normal_behavior  
	      @ 	requires address != null && address.length <= MAX_ADDRESS_LENGTH;
		  @ 	assignable address_model;
		  @ 	ensures address_model.equals(toJMLValueSequence(address));
		  @ also
		  @	public exceptional_behavior
		  @		requires address.length > MAX_ADDRESS_LENGTH
		  @				 || address == null;
		  @		assignable \nothing;
		  @		signals_only UserException, RemoteException;
		  @		signals_redundantly (UserException e) address_model.equals(\old(address_model));
		  @*/
	    public void setAddress (/*@ non_null @*/ byte[] address) throws RemoteException, UserException;
	    
	    
	    /*@ public normal_behavior  
	      @ 	requires country != null && country.length == COUNTRY_CODE_LENGTH;
	      @		requires (\forall int i; 0 <= i && i < COUNTRY_CODE_LENGTH; 
	      @					((byte)0x41 <= country[i] &&  country[i] <= (byte)0x5A )
          @					|| ((byte)0x61 <= country[i] &&  country[i] <= (byte)0x7A ));
		  @ 	assignable country_model;
		  @ 	ensures country_model.equals(toJMLValueSequence(country));
		  @ also
		  @	public exceptional_behavior
		  @		requires country.length != COUNTRY_CODE_LENGTH
		  @				 || country == null
		  @				 || !(\forall int i; 0 <= i && i < COUNTRY_CODE_LENGTH; 
	      @					 	((byte)0x41 <= country[i] &&  country[i] <= (byte)0x5A )
          @					 	|| ((byte)0x61 <= country[i] &&  country[i] <= (byte)0x7A ));
		  @		assignable \nothing;
		  @		signals_only UserException, RemoteException;
		  @		signals_redundantly (UserException e) country_model.equals(\old(country_model));
		  @*/
	    public void setAddress_Country (/*@ non_null @*/ byte[] country) throws RemoteException, UserException;

	    
	    /*@ public normal_behavior 
	      @ 	requires socialNum != null && socialNum.length <= MAX_SOCIALSECURITY_LENGTH;
		  @ 	assignable socialSecurity_model;
		  @ 	ensures socialSecurity_model.equals(toJMLValueSequence(socialNum));
		  @	also
		  @	public exceptional_behavior
		  @		requires socialNum.length > MAX_SOCIALSECURITY_LENGTH
		  @				 || socialSecurity_model == null;
		  @		assignable \nothing;
		  @		signals_only UserException, RemoteException;
		  @		signals_redundantly (UserException e) socialSecurity_model.equals(\old(socialSecurity_model));		
		  @*/
	    public void setSocialSecurityNum (/*@ non_null @*/ byte[] socialNum) throws RemoteException, UserException;

	    
	    /*@ public normal_behavior
	      @ 	requires language != null && language.length == LANGUAGE_LENGTH;
	      @		requires (\forall int i; 0 <= i && i < LANGUAGE_LENGTH; 
	      @					((byte)0x41 <= language[i] &&  language[i] <= (byte)0x5A )
          @					|| ((byte)0x61 <= language[i] &&  language[i] <= (byte)0x7A ));
		  @ 	assignable language_model;
		  @ 	ensures language_model.equals(toJMLValueSequence(language));
		  @	also
		  @	public exceptional_behavior
		  @		requires language.length != LANGUAGE_LENGTH
		  @				 || language_model == null
		  @				 || !(\forall int i; 0 <= i && i < LANGUAGE_LENGTH; 
	      @					((byte)0x41 <= language[i] &&  language[i] <= (byte)0x5A )
          @					|| ((byte)0x61 <= language[i] &&  language[i] <= (byte)0x7A ));
		  @		assignable \nothing;
		  @		signals_only UserException, RemoteException;
		  @		signals_redundantly (UserException e) language_model.equals(\old(language_model));
		  @*/
	    public void setLanguage (/*@ non_null @*/ byte[] language) throws RemoteException, UserException;
	    
	    
//Query methods ---------------------------------------------------------------------------------	    
	    
	    /*@ assignable java.lang.Object;
		  @ ensures toJMLValueSequence(\result).equals(name_model);
		  @*/
		public byte[] getName () throws RemoteException, UserException;
		
		
		/*@ assignable \nothing;
		  @ ensures \result == gender_model;
		  @*/
		public byte getGender ()  throws RemoteException, UserException;
	   
		
	    /*@ assignable \nothing;
	      @ ensures \result == bloodtype_model;
		  @*/
	    public byte getBloodType ()  throws RemoteException, UserException;
	    
	    
	    /*@ assignable java.lang.Object;
	      @ ensures toJMLValueSequence(\result).equals(date_model);
		  @*/
	    public byte[] getBirthdate ()  throws RemoteException, UserException;
	    
	    
	    /*@ assignable java.lang.Object;
	      @ ensures toJMLValueSequence(\result).equals(birthplace_model);
		  @*/
	    public byte[] getBirthplace ()  throws RemoteException, UserException;
	    
	    
	    /*@ assignable java.lang.Object;
	      @ ensures toJMLValueSequence(\result).equals(passportID_model);
		  @*/
	    public byte[] getPassportID ()  throws RemoteException, UserException;
	    
	    
	    /*@ assignable java.lang.Object;
	      @ ensures toJMLValueSequence(\result).equals(id_model);
		  @*/
	    public byte[] getID ()  throws RemoteException, UserException;
	    
	    
	    /*@ assignable java.lang.Object;
	      @ ensures toJMLValueSequence(\result).equals(nationality_model);
		  @*/
	    public byte[] getNationality ()  throws RemoteException, UserException;
	    
	    
	    /*@ assignable java.lang.Object;
	      @ ensures toJMLValueSequence(\result).equals(phone_model);
		  @*/
	    public byte[] getPhoneContact ()  throws RemoteException, UserException;
	    
	    
	    /*@ assignable java.lang.Object; 
	      @ ensures toJMLValueSequence(\result).equals(relativePhone_model);
		  @*/
	    public byte[] getRelativeContact ()  throws RemoteException, UserException;
	    
	    
	    /*@ assignable java.lang.Object;
	      @ ensures toJMLValueSequence(\result).equals(address_model);
		  @*/
	    public byte[] getAddress ()  throws RemoteException, UserException;
	    
	    /*@ assignable java.lang.Object; 
	      @ ensures toJMLValueSequence(\result).equals(city_model);
		  @*/
	    public byte[] getAddress_City ()  throws RemoteException, UserException;
	    
	    /*@ assignable java.lang.Object; 
	      @ ensures toJMLValueSequence(\result).equals(zipCode_model);
		  @*/
	    public byte[] getAddress_ZipCode ()  throws RemoteException, UserException;
	    
	    /*@ assignable java.lang.Object;
	      @ ensures toJMLValueSequence(\result).equals(country_model);
		  @*/
	    public byte[] getAddress_Country ()  throws RemoteException, UserException;

	    /*@ assignable java.lang.Object; 
	      @ ensures toJMLValueSequence(\result).equals(socialSecurity_model);
		  @*/
	    public byte[] getSocialSecurityNum ()  throws RemoteException, UserException;

	    /*@ assignable java.lang.Object;
	      @ ensures toJMLValueSequence(\result).equals(language_model);
		  @*/
	    public byte[] getLanguage ()  throws RemoteException, UserException;
	  
}
