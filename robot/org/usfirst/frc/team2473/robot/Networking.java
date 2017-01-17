package org.usfirst.frc.team2473.robot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.Charset;
import org.usfirst.frc.team2473.robot.Database.Value;

public class Networking extends Thread{
	
	private final String HOST = "localhost";
	private final int PORT = 8080;
	private final String FUNCTION = "detect()";
	private final String SEND = "{\"run\":\'" + FUNCTION +"\"}";
	private char[] cbuf = new char[4096];
	private Socket s = null;
	private BufferedReader stdIn = null;
	private Database d = Database.getInstance(); 
	public void run(){
		try{
			s = new Socket(HOST, PORT);
			stdIn = new BufferedReader(new InputStreamReader(s.getInputStream()));
		}catch(IOException e){
			System.out.println("Thread Failed.");
		}
		update();
	}
	
	public void update() {
			try {
				s.getOutputStream().write(SEND.getBytes(Charset.defaultCharset()));
				stdIn.read(cbuf);
				String st = String.copyValueOf(cbuf);
				double dist = Double.parseDouble(st.substring(st.indexOf("\"Distance\"")+12, (st.indexOf(',', st.indexOf("\"Distance\"")) >= 0) ? st.indexOf(',', st.indexOf("\"Distance\"")):st.indexOf('}')));
				double angle_a = Double.parseDouble(st.substring(st.indexOf("\"Angle A\"")+11, (st.indexOf(',', st.indexOf("\"Angle A\"")) >= 0) ? st.indexOf(',', st.indexOf("\"Angle A\"")):st.indexOf('}')));
				double angle_b = Double.parseDouble(st.substring(st.indexOf("\"Angle B\"")+11, (st.indexOf(',', st.indexOf("\"Angle B\"")) >= 0) ? st.indexOf(',', st.indexOf("\"Angle B\"")):st.indexOf('}')));
				double lr = Double.parseDouble(st.substring(st.indexOf("\"Left or Right\"")+16, (st.indexOf(',', st.indexOf("\"Left or Right\"")) >= 0) ? st.indexOf(',', st.indexOf("\"Left or Right\"")):st.indexOf('}')));
				d.setValue(Value.CV_DISTANCE, dist);
				d.setValue(Value.CV_ANGLE_A, angle_a);
				d.setValue(Value.CV_ANGLE_B, angle_b);
				d.setValue(Value.CV_L_OR_R, lr);
			} catch (Exception e) {
				e.printStackTrace(System.out);
			}
		}
	}

