package diagnostics;

import commons.Common;
//@ model import org.jmlspecs.models.*;

public interface Diagnostic extends Common{


	//Model variables that must have a representation somewhere in the concrete class.
	//@ public model instance non_null JMLValueSequence description_model;
	//@ public model instance non_null JMLValueSequence title_model;
	//@ public model instance byte appointmentID_model;
	//@ public model instance byte diagnosticID_model;

	//Specification Invariants of the model variables. 
	/*@ invariant description_model != null && description_model.int_length() == DiagnosticsSetup.MAX_DESCRIPTION_LENGTH;
      @ invariant title_model != null && title_model.int_length() == DiagnosticsSetup.MAX_TITLE_LENGTH;
      @ invariant 0x00 <= appointmentID_model && appointmentID_model <= 0x7F;
      @ invariant 0x00 <= diagnosticID_model && diagnosticID_model <= 0x7F;
      @*/


	/*@ assignable \nothing;
	  @ ensures \result == appointmentID_model;
	  @*/
	public /*@ pure @*/ byte getAppointmentID();

	/*@ assignable appointmentID_model;	
      @ ensures appointmentID_model == appointmentID;
      @*/
	public void setAppointmentID (byte appointmentID);


	/*@ assignable \nothing;
	  @*/
	public short getDescriptionSize();


	/*@ assignable java.lang.Object;
	  @*/
	public byte[] getDescription (short offset, short size);

	/*@ public normal_behavior
	  @ 	assignable description_model;
      @*/
	public void setDescription (byte[] description_buffer, short size_buffer, short offset, boolean firstBlock);

	/*@ assignable \nothing;
      @ ensures \result == diagnosticID_model;
	  @*/
	public /*@ pure @*/ byte getDiagnosticID ();

	/*@ assignable diagnosticID_model;	
      @ ensures diagnosticID_model == id;
      @*/
	public void setDiagnosticID (byte id);

	/*@ assignable \nothing;
	  @ ensures toJMLValueSequence(\result).equals(title_model);
	  @*/
	public byte[] getTitle ();

	/*@ public normal_behavior
      @ 	requires title != null && title.length <= DiagnosticsSetup.MAX_TITLE_LENGTH;
	  @ 	assignable title_model;
	  @ 	ensures (\forall int i; 0 <= i && i < title.length; title_model.itemAt(i).equals(new JMLByte(title[i])));
      @*/
	public void setTitle (byte[] title);

}

