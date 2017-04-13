package bpbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class UserServer implements Runnable {
	int port;
	BpBook currentSession;
	ServerSocket srvr=null;
	Socket connection=null;
	ObjectInputStream in = null;
	ObjectOutputStream out=null;
	ServerCommands command=null;
	String path="C:\\Users\\Evangelos\\Desktop\\athens";
	
	UserServer(int port,BpBook currentSession){
		this.port=port;
		this.currentSession=currentSession;
		
	}
	
	void alterPath(String p){
		path=p;
	}
	
	
	
	void setUp(){
		try {
			srvr=new ServerSocket(port);
		} catch (IOException e) {
			System.out.println("Problem in setUp");
		}
	}
	
	
	 void listen(){
		try {
			connection=srvr.accept();
			in = new ObjectInputStream(connection.getInputStream());
			command= (ServerCommands) in.readObject();
			synchronized(currentSession){
				assertCommand(command);
			}
			connection.close();
		} catch (IOException e) {
			System.out.println("Problem in accept");
		} catch (ClassNotFoundException e) {
			System.out.println("Problem in readObject");
		}
		
	}
	
	void assertCommand(ServerCommands com){
		switch(com){
		case SaveFile:
			saveBook();
			break;
		case GetAvailableFiles: 
			getAvailableFiles();
			break;
		case GetAllMeasurements:
			getAllMeasurements();
			break;
		case LoadFile:
			loadFile();
			break;
		case GetOnSpecificDate:
			specificDate();
			break;
		case GetOnSpecificTimePeriod:
			specificTime();
			break;
		case RemoveData:
			removeData();
			break;
		default:
			System.out.println("No such functionality");
			break;
		}
	}
	
	void getAvailableFiles(){
		String names="";
		File folder=new File(path);
		File[] list=folder.listFiles();
		for(File file:list){
			names=names+file.getName();
			names=names+"\n";
			names=names+"------------";
			names=names+"\n";
		}
		if(names.equals("")) {
			sendString("No files");
			return;
		}
		sendString(names);
		
	}
	
	void saveBook(){
		ObjectOutputStream fileOut;
		try {
			String filename=readString();
			fileOut = new ObjectOutputStream(
					new FileOutputStream(path+"\\"+ filename + ".dat"));
			fileOut.writeObject(currentSession);
			fileOut.close();
			sendString("Successfully saved");
		}catch(FileNotFoundException ex){
			sendString("Could not save the book.\nProbably because of the filename specified ");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	void loadFile() {
		try {
			String filename =readString();
			ObjectInputStream fileIn = new ObjectInputStream(
					new FileInputStream(path+"\\"+filename + ".dat"));
			currentSession = (BpBook) fileIn.readObject();
			fileIn.close();
			sendString("Successfully loaded");
		}catch(FileNotFoundException ex){
			sendString("Could not load the book.\nProbably because the name does not correspond to a book");
			//
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();

		}
	}
	
	void specificDate(){
		String date=readString();
	try{	
		LocalDate d=LocalDate.parse(date);
		sendString(currentSession.dateSpecific(d));
	}catch(DateTimeParseException ex){
		sendString("could not be parsed");
	}
		
	}
	
	void specificTime(){
		String start=readString();
		String end=readString();
	try{
		LocalTime st=LocalTime.parse(start);
		LocalTime en=LocalTime.parse(end);
		sendString(currentSession.timeSpecific(st,en));
	}catch(DateTimeParseException e){
		sendString("could not be parsed");
	}	
	
	}
	
	void removeData(){
		String heartRate=readString();
		if(heartRate.equals("Problem in read")){
			sendString("Problem in read");
			return;
		}
		double hR=Double.parseDouble(heartRate);
		String r=currentSession.removeBpData(hR);
		sendString(r);
	}
	
	void getAllMeasurements(){
		String ret=currentSession.showAllBpData();
		sendString(ret);
	}
	
	
	void sendString(String s){
		try {
			out=new ObjectOutputStream(connection.getOutputStream());
			out.writeObject(s);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	String readString(){
		String s;
		try {
			s = (String) in.readObject();
		} catch (ClassNotFoundException | IOException e) {
			return "Problem in read";
		}
		return s;
	}
	
	
	
	public void run(){
		setUp();
		while(true){
			listen();
		}
	}
}
