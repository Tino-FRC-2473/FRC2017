package org.usfirst.frc.team2473.robot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.LinkedList;

import org.usfirst.frc.team2473.robot.Database.Value;

public class Networking extends Thread {

	private final String HOST = "10.24.73.15";
	private final int PORT = 8080;
	private final String FUNCTION = "detect()";
	private final String SEND = "{\"run\":\'" + FUNCTION + "\"}";
	private char[] cbuf = new char[4096];
	private Socket s = null;
	private ServerSocket ss = null;
	private BufferedReader stdIn = null;
	private Database d = Database.getInstance();
	private static LinkedList<String> names = new LinkedList<>();// Happy
																	// Pramukh?
																	// Empty
																	// Angled
																	// Brackets.
																	// Happy?
	private HashMap<Value, String> values = new HashMap<>();
	public boolean run = false;
	static Networking instance;
	static {
		instance = new Networking();
	}

	public static Networking getInstance() {
		return instance;
	}

	public void start() {
		names.addLast("\"Distance\"");
		names.addLast("\"Angle A\"");
		names.addLast("\"Bearing\"");
		names.addLast("\"Left or Right\"");
		names.addLast("\"Time Stamp\"");
		values.put(Value.CV_DISTANCE, "Distance");
		values.put(Value.CV_ANGLE_A, "Angle A");
		values.put(Value.CV_BEARING, "Bearing");
		values.put(Value.CV_L_OR_R, "Left or Right");
		values.put(Value.CV_TIME_STAMP, "Time Stamp");
		super.start();
	}

	public void run() {
		try {
			ss = new ServerSocket(PORT);
			s = ss.accept();
			System.out.println("Connecting to host");
		} catch (IOException e) {
			System.out.println("lolsies can't connect");
		}
		try{
			System.out.println("Setting up STDin");
			stdIn = new BufferedReader(new InputStreamReader(s.getInputStream()));

		}catch(IOException e){
			System.out.println("lolsies can't STDIN");
		}
		while (true) {
			try {
				synchronized (this) {

					wait();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			update();
		}
	}

	public void update() {
		try {
			s.getOutputStream().write(SEND.getBytes(Charset.defaultCharset()));
			stdIn.read(cbuf);
			String st = String.copyValueOf(cbuf);
			HashMap<String, Double> h = new HashMap<>();
			names.stream().forEach((s) -> {
				double d = Double.parseDouble(
						st.substring(st.indexOf(s) + s.length()+2, (st.indexOf(',', st.indexOf(s)) >= 0)
								? st.indexOf(',', st.indexOf(s)) : st.indexOf('}')));
				h.put(s.substring(1, s.lastIndexOf('\"')), d);
			});
			for (Value v : values.keySet()) {
				d.setValue(v, h.get(values.get(v)));
			}
			System.out.println(h);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}
}
