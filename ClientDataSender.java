package bpbook;

import java.io.*;
import java.net.*;

public class ClientDataSender {
	private String ip;
	private int port;
	private Socket connection;
	private ObjectOutputStream out;
	private double systolicBp;
	private double diastolicBp;
	private double heartRate;

	ClientDataSender(String ip, int port, double systolicBp, double diastolicBp,
			double heartRate) {

		this.systolicBp = systolicBp;
		this.diastolicBp = diastolicBp;
		this.heartRate = heartRate;
		this.ip = ip;
		this.port = port;
		setUpConnection();
		sendData();
	}

	private void setUpConnection() {
		try {
			connection = new Socket(ip, port);
		} catch (UnknownHostException e) {
			System.out.print("Cannot resolve host");
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
		

	}

	private void sendData() {
		System.out.println("Sending data.....");
		BpData data = new BpData(systolicBp, diastolicBp, heartRate);
		try {

			out = new ObjectOutputStream(connection.getOutputStream());
			out.writeObject(data);
			out.close();
			connection.close();
		} catch (IOException e) {
			e.printStackTrace();

		}
	}

}
