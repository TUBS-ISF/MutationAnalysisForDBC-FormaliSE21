package appointments;

import commons.Common;
//@ model import org.jmlspecs.models.*;

public interface Appointment extends Common{
		
	//Model variables that must have a representation somewhere in the concrete class.
    //@ public model instance byte id_model;
    //@ public model instance byte status_model;
	//@ public model instance byte type_model;
	
	//@ invariant 0x00 <= id_model && id_model <= 0x7F;
	//@ invariant status_model == AppointmentsSetup.STATUS_SCHEDULE ||
	//@			  status_model == AppointmentsSetup.STATUS_CHECK_IN ||
	//@			  status_model == AppointmentsSetup.STATUS_DONE;
	//@ invariant 0x00 <= type_model && type_model <= AppointmentsSetup.MAX_TYPE_CODES;
	
	/*@ assignable \nothing;
	  @ ensures \result == id_model;
	  @*/
    public /*@ pure @*/ byte getID ();

    /*@ assignable \nothing;
	  @ ensures toJMLValueSequence(\result).equals(date_model);
	  @*/
    public /*@ pure @*/ byte[] getDate ();

    /*@ assignable \nothing;
	  @ ensures toJMLValueSequence(\result).equals(hour_model);
	  @*/
    public /*@ pure @*/ byte[] getHour ();

    /*@ assignable \nothing;
	  @ ensures toJMLValueSequence(\result).equals(doctor_model);
	  @*/
    public /*@ pure @*/ byte[] getDoctor ();

    /*@ assignable \nothing;
	  @ ensures toJMLValueSequence(\result).equals(local_model);
	  @*/
    public /*@ pure @*/ byte[] getLocal ();

    /*@ assignable \nothing;
	  @ ensures \result == status_model;
	  @*/
    public /*@ pure @*/ byte getStatus ();
    
    /*@ assignable \nothing;
	  @ ensures \result == type_model;
	  @*/
    public /*@ pure @*/ byte getType ();

    /*@ assignable id_model;	
      @ ensures id_model == id;
      @*/
    public void setID (byte id);

    /*@ public normal_behavior
      @ 	requires date != null && date.length == DATE_LENGTH;
	  @ 	assignable date_model;
	  @ 	ensures (\forall int i; 0 <= i && i < date.length; date_model.itemAt(i).equals(new JMLByte(date[i])));
      @*/
    public void setDate (byte[] date);

    /*@ public normal_behavior
      @ 	requires hour != null && hour.length == HOUR_LENGTH;
	  @ 	assignable hour_model;
	  @ 	ensures (\forall int i; 0 <= i && i < hour.length; hour_model.itemAt(i).equals(new JMLByte(hour[i])));
      @*/
    public void setHour (byte[] hour);

    /*@ public normal_behavior
      @ 	requires local != null && local.length == LOCAL_CODE_LENGTH;
	  @ 	assignable local_model;
	  @ 	ensures (\forall int i; 0 <= i && i < local.length; local_model.itemAt(i).equals(new JMLByte(local[i])));
      @*/
    public void setLocal (byte[] local);

    /*@ public normal_behavior
      @ 	requires AppointmentsSetup.STATUS_SCHEDULE < status && status <= AppointmentsSetup.STATUS_DONE;
      @ 	assignable status_model;	
      @ 	ensures status_model == status;
      @*/
    public void setStatus (byte status);

    /*@ public normal_behavior
      @ 	requires doctor != null && doctor.length == DOCTOR_CODE_LENGTH;
	  @ 	assignable doctor_model;
	  @ 	ensures (\forall int i; 0 <= i && i < doctor.length; doctor_model.itemAt(i).equals(new JMLByte(doctor[i])));
      @*/
    public void setDoctor (byte[] doctor);
    
    /*@ public normal_behavior
      @ 	requires 0x00 < type && type < AppointmentsSetup.MAX_TYPE_CODES;
      @ 	assignable type_model;	
      @ 	ensures type_model == type;
      @*/
    public void setType (byte type);
    

}

