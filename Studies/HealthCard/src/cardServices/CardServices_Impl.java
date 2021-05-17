package cardServices;

import java.rmi.RemoteException;

import medicines.Medicines;

import diagnostics.Diagnostics;

import personal.Personal;
import treatments.Treatments;
import vaccines.Vaccines;

//import javacard.framework.service.CardRemoteObject;
//import javacard.framework.service.SecurityService;

import allergies.Allergies;
import allergies.Allergies_Impl;
import appointments.Appointments;
import appointments.Appointments_Impl;
import personal.Personal;
import personal.Personal_Impl;



public class CardServices_Impl /*extends CardRemoteObject*/ implements CardServices {

	// private SecurityService security;
	private Personal personal;
	private Allergies allergies;
	private Appointments appointments;
	private Diagnostics diagnostics;
	private Treatments treatments;
	private Medicines medicines;
	private Vaccines vaccines;
	
	
	public CardServices_Impl(/*SecurityService security*/) {
		super();
		//this.security = security;
		personal = new Personal_Impl();//security);
		allergies = new Allergies_Impl();
		this.appointments = new Appointments_Impl(/*security*/);
	}

	public Personal getPersonal() throws RemoteException {
		return personal;
	}

	public Allergies getAllergies() throws RemoteException {
		return allergies;
	}

	public Appointments getAppointments() throws RemoteException {
		return this.appointments;
	}
	
	public Diagnostics getDiagnostics() throws RemoteException {
		return diagnostics;
	}
	
	public Treatments getTreatments() throws RemoteException {
		return treatments;
	}
	
	public Medicines getMedicines() throws RemoteException {
		return medicines;
	}

	public Vaccines getVaccines() throws RemoteException {
		return vaccines;
	}
	
}
