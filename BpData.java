package bpbook;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class BpData implements Serializable {
	private LocalDate date;
	private LocalTime time;
	private double systolicBP;
	private double diastolicBP;
	private double heartRate;

	BpData(double systolicBP, double diastolicBP, double heartRate) {
		time = LocalTime.now();
		date = LocalDate.now();
		this.systolicBP = systolicBP;
		this.diastolicBP = diastolicBP;
		this.heartRate = heartRate;
	}

	LocalDate getDate() {

		return date;
	}
	
	LocalTime getTime(){
		return time;
	}

	double getSystolic() {

		return systolicBP;
	}

	double getDiastolic() {

		return diastolicBP;
	}

	double getHeartRate() {

		return heartRate;
	}

	String printData() {
		String s;
		s = 	"Date Created: " +getDate().toString()+"\n"+
				"Time Created:  " +getTime().toString() + "\n" + "Systolic Pressure:  " + getSystolic()
				+ "\n" + "Diastolic Pressure: " + getDiastolic() + "\n"
				+ "Heart Rate:  " + getHeartRate() + "\n";

		return s;
	}

}
