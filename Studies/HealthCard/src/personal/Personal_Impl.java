package personal;

import java.rmi.RemoteException;
import commons.CardUtil;

import javacard.framework.UserException;
import javacard.framework.Util;
//import javacard.framework.service.CardRemoteObject;
//@ model import org.jmlspecs.models.*;
 
public class Personal_Impl /*extends CardRemoteObject*/ implements Personal{
  
	
	//private SecurityService security; 
	 
	//PERSONAL ATTRIBUTES
	
	private /*@ spec_public @*/ byte[] name; //@ in name_model;
	/*@ public represents
	  @ name_model <- toJMLValueSequence(name);
	  @*/
	
	private /*@ spec_public @*/ byte gender; //@ in gender_model;
	/*@ public represents
	  @ gender_model <- gender;
	  @*/
	
	private /*@ spec_public @*/ byte[] birthdate; //@ in date_model;
	/*@ public represents
	  @ 	date_model <- toJMLValueSequence(birthdate);
	  @*/
	
	private /*@ spec_public @*/ byte[] birthplace; //@ in birthplace_model;
	/*@ public represents
	  @ birthplace_model <- toJMLValueSequence(birthplace);
	  @*/
	
	private /*@ spec_public @*/ byte[] nationality; //@ in nationality_model;
	/*@ public represents
	  @ nationality_model <- toJMLValueSequence(nationality);
	  @*/
	
	private /*@ spec_public @*/ byte[] id; //@ in id_model;
	/*@ public represents
	  @ id_model <- toJMLValueSequence(id);
	  @*/
	
	private /*@ spec_public @*/ byte[] passport; //@ in passportID_model;
	/*@ public represents
	  @ passportID_model <- toJMLValueSequence(passport);
	  @*/
	
	private /*@ spec_public @*/ byte[] address; //@ in address_model;
	/*@ public represents
	  @ address_model <- toJMLValueSequence(address);
	  @*/
	
	private /*@ spec_public @*/ byte[] city; //@ in city_model;
	/*@ public represents
	  @ city_model <- toJMLValueSequence(city);
	  @*/
	
	private /*@ spec_public @*/ byte[] country; //@ in country_model;
	/*@ public represents
	  @ country_model <- toJMLValueSequence(country);
	  @*/
	
	private /*@ spec_public @*/ byte[] zipCode; //@ in zipCode_model;
	/*@ public represents
	  @ zipCode_model <- toJMLValueSequence(zipCode);
	  @*/
	
	private /*@ spec_public @*/ byte bloodtype; //@ in bloodtype_model;
	/*@ public represents
	  @ bloodtype_model <- bloodtype; 
	  @*/
	
	private /*@ spec_public @*/ byte[] language; //@ in language_model;
	/*@ public represents
	  @ language_model <- toJMLValueSequence(language);
	  @*/
	
	private /*@ spec_public @*/ byte[] phone; //@ in phone_model;
	/*@ public represents
	  @ phone_model <- toJMLValueSequence(phone);
	  @*/ 
	
	private /*@ spec_public @*/ byte[] relative_phone; //@ in relativePhone_model;
	/*@ public represents
	  @ relativePhone_model <- toJMLValueSequence(relative_phone);
	  @*/
	
	private /*@ spec_public @*/ byte[] social_security; //@ in socialSecurity_model;
	/*@ public represents
	  @ socialSecurity_model <- toJMLValueSequence(social_security);
	  @*/

	
	//Constructor
	
	public Personal_Impl(/*SecurityService security*/) {
		super();

		//this.security = security;

		name = new byte[MAX_NAME_LENGTH];
		gender = (byte) 0x00; // 0x00 = undefined (default),0x01 = male, 0x02 = female.
		birthdate = new byte[DATE_LENGTH];
		id = new byte[MAX_ID_LENGTH];
		nationality = new byte[COUNTRY_CODE_LENGTH];
		passport = new byte[MAX_PASSPORT_LENGTH];
		address = new byte[MAX_ADDRESS_LENGTH];
		city = new byte[MAX_CITY_LENGTH];
		country = new byte[COUNTRY_CODE_LENGTH];
		zipCode  = new byte[MAX_ZIPCODE_LENGTH];
		bloodtype = (byte) 0x00; //0x00 = undefined
		language = new byte[LANGUAGE_LENGTH];
		phone = new byte[MAX_PHONE_LENGTH];
		relative_phone = new byte[MAX_PHONE_LENGTH];
		social_security = new byte[MAX_SOCIALSECURITY_LENGTH];

	}


	//Getters

	public byte[] getAddress() throws RemoteException, UserException {
		//checkAuthentication();
		return CardUtil.clone(this.address);
	}


	public byte[] getAddress_City() throws RemoteException, UserException {
		//checkAuthentication();
		return CardUtil.clone(this.city);
	}

	
	public byte[] getAddress_Country() throws RemoteException, UserException {
		//checkAuthentication();
		return CardUtil.clone(this.country);
	}


	public byte[] getAddress_ZipCode() throws RemoteException, UserException {
		//checkAuthentication();
		return CardUtil.clone(this.zipCode);
	}

	
	public byte[] getBirthdate() throws RemoteException, UserException {
		//checkAuthentication();
		return CardUtil.clone(this.birthdate);
	}


	public byte[] getBirthplace() throws RemoteException, UserException {
		//checkAuthentication();
		return CardUtil.clone(this.birthplace);
	}
	

	public byte getBloodType() throws RemoteException, UserException {
		//checkAuthentication();
		return bloodtype;
	}


	public byte[] getID() throws RemoteException, UserException {
		//checkAuthentication();
		return CardUtil.clone(id);
	}


	public byte[] getLanguage() throws RemoteException, UserException {
		//checkAuthentication();
		return CardUtil.clone(language);
	}


	public byte[] getName() throws RemoteException, UserException {
		//checkAuthentication();
		return CardUtil.clone(name);
	}


	public byte[] getNationality() throws RemoteException, UserException {
		//checkAuthentication();
		return CardUtil.clone(nationality);
	}


	public byte[] getPassportID() throws RemoteException, UserException {
		//checkAuthentication();
		return CardUtil.clone(passport);
	}

 
	public byte[] getPhoneContact() throws RemoteException, UserException {
		//checkAuthentication();
		return CardUtil.clone(phone);
	}


	public byte[] getRelativeContact() throws RemoteException, UserException {
		//checkAuthentication();
		return CardUtil.clone(relative_phone);
	}
 

	public byte getGender() throws RemoteException, UserException {
		//checkAuthentication();
		return  gender; 
	}


	public byte[] getSocialSecurityNum() throws RemoteException, UserException {
		//checkAuthentication();
		return CardUtil.clone(social_security);
	}


	//Setters-----------------------------------------------------

	public void setAddress(byte[] address) throws RemoteException, UserException {
		//checkAuthentication();	
		CardUtil.cleanField(this.address);
		//Util doen's work on runtime test.
		System.arraycopy(address, 0, this.address, 0, address.length);
	}
 

	public void setAddress_City(byte[] city) throws RemoteException, UserException {
		//checkAuthentication();	
		CardUtil.cleanField(this.city);
		//Util doen's work on runtime test.
		System.arraycopy(city, 0, this.city, 0, city.length);		
	}


	public void setAddress_Country(byte[] country) throws RemoteException, UserException {
		//checkAuthentication();
		System.arraycopy(country, 0, this.country, 0, country.length);
	}


	public void setAddress_ZipCode(byte[] zipCode) throws RemoteException, UserException {
		//checkAuthentication();
		CardUtil.cleanField(this.zipCode);
		//Util doen's work on runtime test.
		System.arraycopy(zipCode, (short)0, this.zipCode, (short)0, (short)zipCode.length);				
	}


	public void setBirthdate(byte[] date) throws RemoteException, UserException {
		//checkAuthentication();
		//Util.arrayCopy(date, (short)0, this.birthdate, (short)0, (short)date.length);
		System.arraycopy(date, (short)0, this.birthdate, (short)0, (short)date.length);	
	}

	
	public void setBirthplace(byte[] birthplace) throws RemoteException, UserException {
		//checkAuthentication();
		CardUtil.cleanField(this.birthplace);
		System.arraycopy(birthplace, (short)0, this.birthplace, (short)0, (short)birthplace.length);							
	}


	public void setBloodType(byte btype) throws RemoteException, UserException {
		//checkAuthentication();	
		this.bloodtype = btype;
	}


	public void setID(byte[] id) throws RemoteException, UserException {
		//checkAuthentication();	
		//Util doen's work on runtime test.
		System.arraycopy(id, (short)0, this.id, (short)0, (short)id.length);						
	}


	public void setLanguage(byte[] language) throws RemoteException, UserException {
		//checkAuthentication();	
		//Util doen's work on runtime test.
		System.arraycopy(language, (short)0, this.language, (short)0, (short)language.length);							
	}


	public void setName(byte[] name) throws RemoteException, UserException {
		//checkAuthentication();
		CardUtil.cleanField(this.name);
		//Util.arrayCopy(name, (short)(short)0, this.name, (short)0, (short)name.length);		
		System.arraycopy(name, (short)(short)0, this.name, (short)0, (short)name.length);		
	}


	public void setNationality(byte[] nationality) throws RemoteException, UserException {
		//checkAuthentication();	
		System.arraycopy(nationality, (short)0, this.nationality, (short)0, (short)nationality.length);							
	}

	public void setPassportID(byte[] passportID) throws RemoteException, UserException {
		//checkAuthentication();
		CardUtil.cleanField(this.passport);
		//Util doen's work on runtime test.
		System.arraycopy(passportID, (short)0, this.passport, (short)0, (short)passportID.length);								
	}


	public void setPhoneContact(byte[] phone) throws RemoteException, UserException {
		//checkAuthentication();
		CardUtil.cleanField(this.phone);
		//Util doen's work on runtime test.
		System.arraycopy(phone, (short)0, this.phone, (short)0, (short)phone.length);							

	}

	public void setRelativeContact(byte[] phone) throws RemoteException, UserException {
		//checkAuthentication();
		CardUtil.cleanField(this.relative_phone);
		//Util doen's work on runtime test.
		System.arraycopy(phone, (short)0, this.relative_phone, (short)0, (short)phone.length);								
	}

	public void setGender(byte gender) throws RemoteException, UserException {
		//checkAuthentication();	
		this.gender = gender;
	}


	public void setSocialSecurityNum(byte[] socialNum) throws RemoteException, UserException {
		//checkAuthentication();
		CardUtil.cleanField(this.social_security);
		//Util doen's work on runtime test.
		System.arraycopy(socialNum, (short)0, this.social_security, (short)0, (short)socialNum.length);									
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
    	StringBuffer h = new StringBuffer("");
    	StringBuffer x = new StringBuffer(""); 
    	StringBuffer j = new StringBuffer("");
    	StringBuffer k = new StringBuffer("");
    	StringBuffer l = new StringBuffer("");
    	StringBuffer m = new StringBuffer("");
    	StringBuffer n = new StringBuffer("");  
    	
    	a.append(" name: ");
    	for(int i=0; i < name.length;i++){
    		a.append( " "+ name[i]);
    	}
    	
    	b.append(" passport: ");
    	for(int i=0; i < passport.length;i++){
    		b.append( " "+ passport[i]);
    	}
    	
    	c.append(" id: ");
    	for(int i=0; i < id.length;i++){
    		c.append( " "+ id[i]);
    	}
    	
    	d.append(" nationality: ");
    	for(int i=0; i < nationality.length;i++){
    		d.append( " "+ nationality[i]);
    	}
    	
    	e.append(" phone: ");
    	for(int i=0; i < phone.length;i++){
    		e.append( " "+ phone[i]);
    	}
    	
    	f.append(" r. phone: ");
    	for(int i=0; i < relative_phone.length;i++){
    		f.append( " "+ relative_phone[i]);
    	}
    	
    	g.append(" address: ");
    	for(int i=0; i < address.length;i++){
    		g.append( " "+ address[i]);
    	}
    	
    	h.append(" city: ");
    	for(int i=0; i < city.length;i++){
    		h.append( " "+ city[i]);
    	}
    	
    	x.append(" zipCode: ");
    	for(int i=0; i < zipCode.length;i++){
    		x.append( " "+ zipCode[i]);
    	}
    	
    	j.append(" country: ");
    	for(int i=0; i < country.length;i++){
    		j.append( " "+ country[i]);
    	}
    	
    	k.append(" social: ");
    	for(int i=0; i < social_security.length;i++){
    		k.append( " "+ social_security[i]);
    	}
    	
    	l.append(" language: ");
    	for(int i=0; i < language.length;i++){
    		l.append( " "+ language[i]);
    	}
    	
    	m.append(" gender: " + gender);
    	
    	n.append(" bloodtype: " + bloodtype);
    	
    	return a.toString() + "\n"+ b.toString() 
    			+"\n" + c.toString() + "\n" 
    			+ d.toString() + "\n"
    			+ e.toString() + "\n"
    			+ f.toString() + "\n"
    			+ g.toString() + "\n"
		    	+ h.toString() + "\n"
				+ x.toString() + "\n"
				+ j.toString() + "\n"
				+ k.toString() + "\n"
    			+ l.toString() + "\n"
    			+ m.toString() + "\n"
    			+ n.toString() + "\n";
    }

}
