package cardServices;

//import java.rmi.Remote;
import java.rmi.RemoteException;

import medicines.Medicines;

import diagnostics.Diagnostics;

import personal.Personal;
import treatments.Treatments;
import vaccines.Vaccines;

//import allergies.Allergies;
//import personal.Personal;
import allergies.Allergies;
import appointments.Appointments; 

public interface CardServices /* extends Remote */{
	
	public  /*@ pure @*/ Personal getPersonal() throws RemoteException;
	public /*@ pure @*/  Allergies getAllergies() throws RemoteException;
	public /*@ pure @*/ Appointments getAppointments() throws RemoteException;
	public /*@ pure @*/ Diagnostics getDiagnostics() throws RemoteException;
	public /*@ pure @*/ Treatments getTreatments() throws RemoteException;
	public  /*@ pure @*/ Medicines getMedicines() throws RemoteException;
	public  /*@ pure @*/ Vaccines getVaccines() throws RemoteException;
}
