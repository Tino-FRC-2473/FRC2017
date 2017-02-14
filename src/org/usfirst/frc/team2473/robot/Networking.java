package org.usfirst.frc.team2473.robot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.LinkedList;

import org.usfirst.frc.team2473.robot.Database.Value;

public class Networking extends Thread {
	//MUST CHANGE ON COMPETITION DAY
	private final String HOST = "10.60.38.97";
	private final int PORT = 5812;
	private final String SEND = "CV()";
	private char[] cbuf = new char[4096];
	private Socket s = null;
	private BufferedReader stdIn = null;
	private BufferedWriter stdOut = null;
	private Database d = Database.getInstance();
	Value[] values = {Value.CV_DISTANCE, Value.CV_ANGLE_A, Value.CV_BEARING, Value.CV_L_OR_R, Value.CV_TIME_STAMP};
	
	static Networking instance;
	static {
		instance = new Networking();
	}

	public static Networking getInstance() {
		return instance;
	}

	public void start() {
		try {
			s = new Socket(HOST, PORT);
			stdIn = new BufferedReader(new InputStreamReader(s.getInputStream()));
			stdOut = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
		}catch(IOException e){
			System.exit(0);
		}
		super.start();
	}

	public void run() {
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
			stdOut.write(SEND);
			String st = stdIn.readLine();
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
