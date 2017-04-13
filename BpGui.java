package bpbook;

import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalTime;

public class BpGui extends Frame {
	private TextField startTimeEnter;
	private TextField endTimeEnter;
	private TextField dateEnter;
	private TextField heartRateEnter;
	private TextField fileNameEnter;
	private TextArea responseData;
	int xStart = 50;
	int yStart = 50;
	private Socket connection;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private int port;
	private String ip;

	BpGui(String ip, int port) {
		super("BpBookGUI");
		this.ip = ip;
		this.port = port;
		initializeGui();
		createAuthorField();
		createFields();
		createButtons();
		setUpConnection();
	}

	private void initializeGui() {
		setLayout(null);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		setVisible(true);
		setSize(1000, 1000);
		toFront();
		setLocation(300, 10);
		setBackground(Color.RED);
		setResizable(false);
		responseData = new TextArea("Welcome to Blood Pressure Monitor...", 200,
				100);
		responseData.setBounds(500, 30, 500, 1000);
		responseData.setEditable(false);
		responseData.setFont(new Font("Arial Bold", 1, 16));
		add(responseData);

	}

	private void createFields() {
		TextField date = new TextField("Date", 5);
		date.setEditable(false);
		date.setBounds(xStart, yStart, 70, 50);
		add(date);

		dateEnter = new TextField("", 25);
		dateEnter.setEditable(true);
		dateEnter.setBounds(xStart + 100, yStart, 150, 50);
		add(dateEnter);
		// ===========================================
		TextField heartRate = new TextField("Heart Rate", 5);
		heartRate.setEditable(false);
		heartRate.setBounds(xStart, yStart + 60, 70, 50);
		add(heartRate);

		heartRateEnter = new TextField("", 25);
		heartRateEnter.setEditable(true);
		heartRateEnter.setBounds(xStart + 100, yStart + 60, 150, 50);
		add(heartRateEnter);
		// ===========================================

		TextField startTime = new TextField("Start Time", 5);
		startTime.setEditable(false);
		startTime.setBounds(xStart, yStart + 2 * 60, 70, 50);
		add(startTime);

		startTimeEnter = new TextField("", 25);
		startTimeEnter.setEditable(true);
		startTimeEnter.setBounds(xStart + 100, yStart + 2 * 60, 150, 50);
		add(startTimeEnter);
		// ===========================================
		TextField endTime = new TextField("End Time", 5);
		endTime.setEditable(false);
		endTime.setBounds(xStart, yStart + 3 * 60, 70, 50);
		add(endTime);

		endTimeEnter = new TextField("", 25);
		endTimeEnter.setEditable(true);
		endTimeEnter.setBounds(xStart + 100, yStart + 3 * 60, 150, 50);
		add(endTimeEnter);
		// ===========================================

		TextField fileName = new TextField("File Name", 5);
		fileName.setEditable(false);
		fileName.setBounds(xStart, yStart + 4 * 60, 70, 50);
		add(fileName);

		fileNameEnter = new TextField("", 25);
		fileNameEnter.setEditable(true);
		fileNameEnter.setBounds(xStart + 100, yStart + 4 * 60, 150, 50);
		add(fileNameEnter);

	}

	private void createButtons() {
		createDateButton();
		createTimeButton();
		createHeartRateButton();
		showAllMeasurementsButton();
		showAvailableFilesButton();
		loadFileButton();
		saveFileButton();
	}

	private void createDateButton() {
		Button b = new Button("Find Date");
		b.setLocation(xStart + 20, yStart + 5 * 60);
		b.setSize(200, 50);
		b.addActionListener((action) -> {

			sendCommand(ServerCommands.GetOnSpecificDate);
			sendString(dateEnter.getText());
			responseData.setText(receiveString());

		}

		);
		add(b);
	}

	private void createTimeButton() {
		Button b = new Button("Find Time");
		b.setLocation(xStart + 20, yStart + 6 * 60);
		b.setSize(200, 50);
		b.addActionListener((action) -> {
			sendCommand(ServerCommands.GetOnSpecificTimePeriod);
			sendString(startTimeEnter.getText());
			sendString(endTimeEnter.getText());
			responseData.setText(receiveString());

		}

		);

		add(b);

	}

	private void createHeartRateButton() {
		Button b = new Button("Delete Heart Rate");
		b.setLocation(xStart + 20, yStart + 7 * 60);
		b.setSize(200, 50);
		b.addActionListener((action) -> {
			sendCommand(ServerCommands.RemoveData);
			sendString(heartRateEnter.getText());
			responseData.setText(receiveString());

		}

		);

		add(b);
	}

	private void showAllMeasurementsButton() {
		Button b = new Button("Show All Measurements");
		b.setLocation(xStart + 20, yStart + 8 * 60);
		b.setSize(200, 50);
		b.addActionListener((action) -> {
			sendCommand(ServerCommands.GetAllMeasurements);
			responseData.setText(receiveString());

		}

		);
		add(b);
	}

	private void showAvailableFilesButton() {
		Button b = new Button("Show Available Files");
		b.setLocation(xStart + 20, yStart + 9 * 60);
		b.setSize(200, 50);
		b.addActionListener((action) -> {
			sendCommand(ServerCommands.GetAvailableFiles);
			responseData.setText(receiveString());
		}

		);
		add(b);

	}

	private void loadFileButton() {
		Button b = new Button("Load File");
		b.setLocation(xStart + 20, yStart + 10 * 60);
		b.setSize(200, 50);
		b.addActionListener((action) -> {
			sendCommand(ServerCommands.LoadFile);
			sendString(fileNameEnter.getText());
			responseData.setText(receiveString());

		}

		);
		add(b);

	}

	private void saveFileButton() {

		Button b = new Button("Save File");
		b.setLocation(xStart + 20, yStart + 11 * 60);
		b.setSize(200, 50);
		b.addActionListener((action) -> {
			sendCommand(ServerCommands.SaveFile);
			sendString(fileNameEnter.getText());
			responseData.setText(receiveString());

		}

		);
		add(b);

	}

	private void setUpConnection() {
		try {
			connection = new Socket(ip, port);
			responseData
			.setText("Connection Established to  " + connection.getPort());
		} catch (IOException e) {
			responseData.setText("Cannot Connect to Sever...Possibly Offline");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException exp) {

			}

			System.exit(0);
		}

		
	}

	private void sendCommand(ServerCommands command) {
		try {
			out = new ObjectOutputStream(connection.getOutputStream());
			out.writeObject(command);
			out.flush();
		} catch (IOException e) {
			responseData.setText("Problem Occured upon sending the Command");
		}

	}

	private String receiveString() {
		String s = null;
		try {
			in = new ObjectInputStream(connection.getInputStream());
			s = (String) in.readObject();
		} catch (IOException e) {
			responseData.setText("Problem on String receive");
		} catch (ClassNotFoundException e) {
			responseData.setText("Problem on String receive");
		}
		refreshConnection();
		return s;
	}

	private void sendString(String s) {
		try {
			out.writeObject(s);
			out.flush();
		} catch (IOException e) {
			responseData.setText("Problem On sending String");
		}

		System.out.println(s);
	}

	private void refreshConnection() {
		try {
			out.close();
			in.close();
			connection.close();
		} catch (IOException e) {
			responseData.setText("Error on Refreshing");
		}
		setUpConnection();
	}

	private void createAuthorField() {
		TextArea authors = new TextArea(
				" Piliouras 228479 \n Chaikalis 228560 \n\n BloodPressureBook GUI",
				5, 15);
		authors.setEditable(false);
		authors.setBounds(xStart + 50, yStart + 800, 250, 100);
		authors.setFont(new Font("Arial Bold", 1, 14));
		add(authors);

	}

	// ------------------------------------------------

	
}
