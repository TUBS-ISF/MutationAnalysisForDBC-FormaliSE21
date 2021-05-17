package commons;
//@ model import org.jmlspecs.models.*;
//@ model import java.util.Calendar;
//@ model import java.util.Date;
//@ model import appointments.Appointment;
//@ model import appointments.AppointmentsSetup;


public interface Common {

	//Buffer constant:
	public static final short MAX_BUFFER_BYTES = 80;
	
	
	/*//1. Error Codes ---------------------------------------------------------------------
	public static final short BAD_ARGUMENT = (short) 0x6002;
	public static final short REQUEST_FAILED = (short) 0x0102;
	public static final short ARRAY_FULL = (short) 0x0103;
	public static final short INVALID_ARRAY_POSITION = (short) 0x0104;*/
	
	
	//2. Data constraints ----------------------------------------------------------------
	
	public static final short DATE_LENGTH = 4;
	//@ public model instance JMLValueSequence date_model; 
	
	// date length
	//@ invariant date_model.int_length() == DATE_LENGTH;
	
	
	// Month, Day or Year (firt 2 digits) can't have one of them a value == 0x00 while other has a value != 0x00.
	// When a date is initialized on the constructor, all bytes are 0x00.
	/*@ constraint ((\exists int i; 0 <= i && i < date_model.int_length()-1; 
	  @													getByte(date_model,i) == 0x00)
	  @				&&
	  @            (\exists int i; 0 <= i && i < date_model.int_length()-1; 
	  @													getByte(date_model,i) != 0x00)) 
	  @				==> false;			
	  @
	  @*/
	
	//Constraint for months(i.e., 1 to 12)
	/*@ constraint (byte) 0x00 <= getByte(date_model,0) 
	  @							 && getByte(date_model,0) <= (byte) 0x0C;
	  @*/

	//Constraint for number of month days (i.e., 1 to 31)
	/*@ constraint (byte) 0x00 <= getByte(date_model,1) 
	  @							 && getByte(date_model,1) <= (byte) 0x1F; 				
	  @*/
	
	//Constraint for two first digits of the year (i.e., 18xx, 19xx, 20xx)
	/*@ constraint (getByte(date_model,2) == 0x00) || (\exists byte y1; (byte) 0x12 <= y1 && y1 <= (byte) 0x14; 
	  @													getByte(date_model,2) == y1);
	  @*/
	
	//Constraint for two last digits of the year (i.e., xx00 to xx99)
	/*@ constraint (getByte(date_model,2) == 0x00) || (\exists byte y2; (byte) 0x00 <= y2 && y2 <= (byte) 0x63; 
	  @													getByte(date_model,3) == y2);
	  @*/
	
	//Constraint for 29 of February and Leap Year
	/*@ constraint (getByte(date_model,0) == 0x02 
	  @				&& getByte(date_model,1) == 0x1D) 
	  @				==> isLeapYear( Year( getByte(date_model,2) , getByte(date_model,3) ) );
	  @*/
	
	//Constraint for number of days of February(i.e., no more than 29 days)
	/*@ constraint (getByte(date_model,0) == 0x02 
	  @ 				&& getByte(date_model,1) > 0x1D) ==> false;
	  @*/
	
	//Maximum days for April (30 days)
	//@ constraint (getByte(date_model,0) == 0x04 && getByte(date_model,1) > 0x1E) ==> false; 
	
	//Maximum days for June (30 days)
	//@ constraint (getByte(date_model,0) == 0x06 && getByte(date_model,1) > 0x1E) ==> false; 
	
	//Maximum days for September (30 days)
	//@ constraint (getByte(date_model,0) == 0x09 && getByte(date_model,1) > 0x1E) ==> false; 
	
	//Maximum days for November (30 days)
	//@ constraint (getByte(date_model,0) == 0x0B && getByte(date_model,1) > 0x1E) ==> false; 	
	

    public static final short HOUR_LENGTH = 2;
    //@ public model instance JMLValueSequence hour_model; 
	
	// date length
	//@ invariant hour_model.int_length() == HOUR_LENGTH;
    
    // Hour and Minutes (2 bytes, 1 byte for each)
	// When a hour/minute is initialized on the constructor, all bytes are 0x00.     
    
    //Constraint for Hour(i.e., 0 to 23)
	/*@ constraint (byte) 0x00 <= getByte(hour_model,0) 
	  @							 && getByte(hour_model,0) <= (byte) 0x17;
	  @*/

	//Constraint for Minutes(i.e., 0 to 59)
	/*@ constraint (byte) 0x00 <= getByte(hour_model,1) 
	  @							 && getByte(hour_model,1) <= (byte) 0x3B; 				
	  @*/

    public static final short DOCTOR_CODE_LENGTH = 5;
    //@ public model instance JMLValueSequence doctor_model;
    //@ invariant doctor_model.int_length() == DOCTOR_CODE_LENGTH;
    // The doctor is represented by a code that must be bytes representing characters(A-Z, a-z, 0-9)
    /*@ constraint (\forall int i; 0 <= i && i < DOCTOR_CODE_LENGTH; getByte(doctor_model,i) == (byte)0x00)
      @			   || 
      @			   (\forall int i; 0 <= i && i < DOCTOR_CODE_LENGTH;
      @					((byte)0x41 <= getByte(doctor_model,i) &&  getByte(doctor_model,i) <= (byte)0x5A )
      @					|| ((byte)0x61 <= getByte(doctor_model,i) &&  getByte(doctor_model,i) <= (byte)0x7A )
      @					|| ((byte)0x30 <= getByte(doctor_model,i) &&  getByte(doctor_model,i) <= (byte)0x39 ));
      @*/

    public static final short LOCAL_CODE_LENGTH = 8;
    //@ public model instance JMLValueSequence local_model;
    //@ invariant local_model.int_length() == LOCAL_CODE_LENGTH;
    // First two digits (country) of the local code must be bytes representing characters (A-Z, uppercase).
    /*@ constraint (\forall int i; 0 <= i && i < LOCAL_CODE_LENGTH; getByte(local_model,i) == (byte)0x00) 
      @			   || 
      @			   (((byte)0x41 <= getByte(local_model,0) 
	  @					  && getByte(local_model,0) <= (byte)0x5A)
	  @					  &&
	  @						((byte)0x41 <= getByte(local_model,1) 
	  @							 && getByte(local_model,1) <= (byte)0x5A));
	  @*/
    // the three next digits (city) of the local code must be bytes representing characters (a-z, lowercase).
    /*@ constraint 	(\forall int i; 0 <= i && i < LOCAL_CODE_LENGTH; getByte(local_model,i) == (byte)0x00) 
      @			    ||
      @					 (((byte)0x61 <= getByte(local_model,2) 
	  @							 && getByte(local_model,2) <= (byte)0x7A)
	  @					  &&
	  @						((byte)0x61 <= getByte(local_model,3) 
	  @							 && getByte(local_model,3) <= (byte)0x7A)
	  @					  &&
	  @						((byte)0x61 <= getByte(local_model,4) 
	  @							 && getByte(local_model,4) <= (byte)0x7A));
	  @*/
    // the three, and last, next digits (place: hospital or medical center) of the local code must be bytes representing characters (0-9 or a-z, lowercase).
    /*@ constraint (\forall int i; 0 <= i && i < LOCAL_CODE_LENGTH; getByte(local_model,i) == (byte)0x00) 
      @			   || 
      @				( ((byte)0x30 <= getByte(local_model,5) 
	  @							 && getByte(local_model,5) <= (byte)0x39) ||
	  @				((byte)0x61 <= getByte(local_model,5) 
	  @							 && getByte(local_model,5) <= (byte)0x7A) )
	  @				&&
	  @				( ((byte)0x30 <= getByte(local_model,6) 
	  @							 && getByte(local_model,6) <= (byte)0x39) ||
	  @				((byte)0x61 <= getByte(local_model,6) 
	  @							 && getByte(local_model,6) <= (byte)0x7A) ) 
	  @				&&
	  @				( ((byte)0x30 <= getByte(local_model,7) 
	  @							 && getByte(local_model,7) <= (byte)0x39) ||
	  @				((byte)0x61 <= getByte(local_model,7) 
	  @							 && getByte(local_model,7) <= (byte)0x7A) );
	  @*/
    
    
    //3. Specification helper methods -----------------------------------------------------
    
    /*@ public static model pure JMLValueSequence toJMLValueSequence(byte[] b){
	  @		JMLValueSequence _m = new JMLValueSequence();
	  @		for(int i = 0; i < b.length; i++)
	  @			_m = _m.insertBack(new JMLByte(b[i]));
	  @		return _m;
	  @ }
	  @
	  @*/
    
    /*@ public model pure JMLObjectSequence toJMLObjectSequence(Object[] o){
	  @		JMLObjectSequence _m = new JMLObjectSequence();
	  @		for(int i = 0; i < o.length; i++)
	  @			_m = _m.insertBack(o[i]);
	  @		return _m;
	  @ }
	  @
	  @*/
    
    /*@ public model pure int countNotNullObjects(JMLObjectSequence jmlObjects){
      @		int count = 0;
	  @		for (int i = 0; i < jmlObjects.int_length() ;i++) {
	  @			if (jmlObjects.itemAt(i) != null) {
	  @				count += 1;
	  @			}
	  @		}
	  @		return count;
      @ }
      @
      @*/
    
    /*@ public model pure Object lastNonNullObject( JMLObjectSequence jmlObjects){
	  @		return jmlObjects.itemAt( countNotNullObjects(jmlObjects) - 1 );
      @ }
      @*/
    
    /*@ public model pure byte getByte(JMLValueSequence jmlValueSequence, int position){
	  @		byte b;
	  @		return b = ((JMLByte)(jmlValueSequence.itemAt(position))).byteValue();
	  @ }
	  @*/ 
    
    
    //3.1. Specification helper methods for a date specification -----------
   
    /*@ public model pure short Year(byte y1, byte y2){
	  @ 	return (short)(y1*100+y2);
	  @ }
	  @*/
    
    /*@ public model pure boolean isLeapYear(short year){
	  @		boolean isLeap = (year % 400 == 0 ? true : 
	  @							(year % 100 == 0 ? false :
	  @								(year % 4 == 0 ? true : false)));
	  @		return isLeap;
	  @ }
	  @*/    
    
    
    
    
//3.2. INVARIANTS Specification helper methods ----------------------------------------------------
  
    
    //3.2.1. Used in Appointments invariant:    
    
    
    /*@ public pure model boolean verifyOutofDate(JMLObjectSequence appointments, short limitHours){
	  @		for(int i = 0; i < appointments.int_length(); i++){
	  @			if(appointments.itemAt(i) != null){
	  @				if(((Appointment)appointments.itemAt(i)).getStatus() == AppointmentsSetup.STATUS_CHECK_IN
	  @					|| ((Appointment)appointments.itemAt(i)).getStatus() == AppointmentsSetup.STATUS_SCHEDULE){	
	  @
	  @						byte[] date_temp = ((Appointment)appointments.itemAt(i)).getDate();
	  @						byte[] date = new byte[Common.DATE_LENGTH];
	  @						for(int x = 0; x < Common.DATE_LENGTH; x++)
	  @							date[x] = date_temp[x];
	  @
	  @						byte[] hour_temp = ((Appointment)appointments.itemAt(i)).getHour();
	  @						byte[] hour = new byte[Common.HOUR_LENGTH];
	  @						for(int y = 0; y < Common.HOUR_LENGTH; y++)
	  @							hour[y] = hour_temp[y];
	  @						
	  @						if( calculateDiff(byteTime(date, hour)) > limitHours)
	  @							return false;
	  @				}
	  @			}
	  @		}
	  @		return true;
	  @ }
	  @*/
    
    
    
    /*@	public pure model byte[] byteTime(byte[] date, byte[] hour){
      @		byte[] byteTime = new byte[Common.DATE_LENGTH + Common.HOUR_LENGTH];
      @		for(int i = 0; i < date.length; i++)
      @			byteTime[i] = date[i];
      @		byteTime[4] = hour[0];
      @		byteTime[5] = hour[1];
      @		return byteTime;
      @	}
      @*/ 
    
    /*@	public pure model long calculateDiff(byte[] cardDateTime){
      @
      @		Calendar date = Calendar.getInstance();
	  @		/// Creates two calendars instances
	  @		Calendar cal1 = Calendar.getInstance();
	  @		Calendar cal2 = Calendar.getInstance();
      @
	  @		///Get current date
	  @		int DAY_OF_MONTH= 1 + date.get(Calendar.MONTH);
	  @		int DAY_OF_YEAR = date.get(Calendar.YEAR);
	  @		int DAY_OF_DAY= date.get(Calendar.DAY_OF_MONTH);
	  @		int DAY_HOUR = date.get(Calendar.HOUR_OF_DAY);
	  @		int DAY_MIN = date.get(Calendar.MINUTE);
	  @	
	  @		byte[] p = new byte[cardDateTime.length];
	  @		for(int i = 0; i < p.length; i++)
      @			p[i] = cardDateTime[i];
	  @	
	  @		///Card date
	  @		String s = convertByteHexaToDecimal(p);
	  @		int _month = Integer.parseInt(s.substring(0,2));
	  @		int _day = Integer.parseInt(s.substring(2,4));
	  @		int _year = Integer.parseInt(s.substring(4,8));
	  @		int _hour = Integer.parseInt(s.substring(8,10));
	  @		int _min = Integer.parseInt(s.substring(10,12));
	  @	
	  @		/// Set the date for both of the calendar instance
      @		Date d1= new Date(_year, _month, _day, _hour ,_min);
      @		cal1.setTime(d1);
      @		Date d2= new Date(DAY_OF_YEAR, DAY_OF_MONTH,DAY_OF_DAY,DAY_HOUR,DAY_MIN);
      @		cal2.setTime(d2);
	  @ 	/// Get the represented date in milliseconds
	  @		long milis1 = cal1.getTimeInMillis();
	  @		long milis2 = cal2.getTimeInMillis();
      @
	  @		/// Calculate difference in milliseconds
	  @		long diff = milis2 - milis1;
      @
	  @		/// Calculate difference in hours
	  @		long diffHours = diff / (60 * 60 * 1000);
	  @		return diffHours;
	  @ }
	  @*/
	 
    /*@
	  @	public pure model String convertByteHexaToDecimal( byte[] b){
	  @ 		String result = "";
	  @			for (int i=0; i < b.length; i++) {
	  @		String s = Integer.toString(b[i],10);
	  @		if(s.length() == 1){
	  @			result += 0+Integer.toString(b[i],10);
	  @		} else { result += Integer.toString(b[i],10);}
	  @	}
	  @	return result;
	  @	}
      @
	  @*/
    
    
    //3.2.2. Generic INVARIANT helper methods: ---------------------------------------------------------
    
    /*@ public pure model boolean instancesInvariant(JMLObjectSequence sequence, Class className){
	  @		 for (int i = 0; i < sequence.int_length(); i++){
	  @			if(!  (className.isInstance(sequence.itemAt(i)) || sequence.itemAt(i) == null))
	  @				return false;
	  @		 }
	  @		return true;
	  @	}
	  @*/
    
    
    /*@ public pure model boolean nullsInvariant(JMLObjectSequence sequence){
	  @		 for (int i = 1; i < sequence.int_length(); i++){
	  @				if(sequence.itemAt(i) != null && sequence.itemAt(i-1) == null){
	  @ 						return false;
	  @				}					
	  @		 }
	  @		return true;
	  @	}
	  @*/
    
    

}

