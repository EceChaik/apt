package bpbook;


import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class BpBook implements Serializable {
	private java.util.ArrayList<BpData> book;
	private String patientName;

	BpBook(String name) {
		this.patientName = name;
		book = new java.util.ArrayList<BpData>();

	}

	String getName() {
		return patientName;
	}

	ArrayList<BpData> getBook() {

		return book;
	}

	void addBpData(BpData data) {
		book.add(data);
	}

	String removeBpData(double heartRate) {
		BpData target=null;
		for (BpData data : book) {
			if (data.getHeartRate() == heartRate){
				target=data;
				break;
			}
		}
		if(target==null) {
			return "No such heart rate";
		}
		book.remove(target);
		return "Successfully removed";
	}

	String dateSpecific(LocalDate date){
		String retval="";
		for(BpData data:book){
			if(data.getDate().equals(date)){
				retval=retval+data.printData();
				retval=retval+"\n";
				retval=retval+"----------";
				retval=retval+"\n";
			}
		}
		if(retval.equals("")){
			return "No measurement in this date";
		}
		return retval;
	}
	
	String timeSpecific(LocalTime start,LocalTime end){
		String retval="";
		for(BpData data:book){
			if(data.getTime().isAfter(start) && data.getTime().isBefore(end)){
				retval=retval+data.printData();
				retval=retval+"\n";
				retval=retval+"----------";
				retval=retval+"\n";
			}
		}
		if(retval.equals("")){
			return "No measurements in this time period";
		}
		return retval;
	}
	
	
	
	
	String showAllBpData() {
		String s = "";
		for (BpData data : book) {
			s = s + data.printData();
			s = s + "\n";
			s = s + "--------------";
			s = s + "\n";

		}

		return s;
	}

}
