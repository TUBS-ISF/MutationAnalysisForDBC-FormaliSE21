package appointments;

public interface AppointmentsSetup {
	
	//Appointment Status
	public static final byte STATUS_SCHEDULE = 0x00;
	public static final byte STATUS_CHECK_IN = 0x01;
	public static final byte STATUS_DONE = 0x02;
	
	
	public static final short MAX_TYPE_CODES = 50;
	
    public static final short MAX_APPOINTMENT_ITEMS = 50;
}
