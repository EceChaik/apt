package bpbook;

import java.time.LocalDateTime;

public class ServerStarter {
	static int numberOfReads=200;
	
	public static void main(String[] args) {
		BpBook currentSession = new BpBook("CurrentPatient");

		DataServer dataServer = new DataServer(55255, currentSession);
		UserServer userServer = new UserServer(55256, currentSession);
		
		new Thread(dataServer).start();
		new Thread(userServer).start();
		
		
	}
}
