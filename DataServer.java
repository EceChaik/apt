package bpbook;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ConcurrentModificationException;

public class DataServer implements Runnable {
	private int port;
	private ServerSocket srvr = null;
	private Socket connection = null;
	private ObjectInputStream in = null;
	private BpBook currentSession;
	private int counter = 0;

	DataServer(int port, BpBook currentSession) {
		this.currentSession = currentSession;
		this.port = port;
		setUpConnection();
	}

	void setUpConnection() {
		try {
			srvr = new ServerSocket(port);
		} catch (IOException e) {
			System.out.println(e);
			System.exit(0);
		}
		System.out.println("SocketServer Created");
	}

	private BpData getData() {
		BpData data = null;
		System.out.println("DataServer is listening...");
		try {
			connection = srvr.accept();
			System.out.println("Data connection accepted");
			in = new ObjectInputStream(connection.getInputStream());
			data = (BpData) in.readObject();
			connection.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("I/O Error");
		} catch (ClassNotFoundException e) {
			System.out.println("Wrong data");
		}
		System.out.println("Data received");
		return data;

	}

	public void run() {
		while (counter++ < ServerStarter.numberOfReads) {
			BpData dataRetrieved = getData();
			synchronized (currentSession) {
				currentSession.addBpData(dataRetrieved);
			}
			

		}
		System.out.println(currentSession.showAllBpData());
	}

}
