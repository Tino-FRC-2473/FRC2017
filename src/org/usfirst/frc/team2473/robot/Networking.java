package org.usfirst.frc.team2473.robot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.usfirst.frc.team2473.robot.Database.Value;

public class Networking extends Thread {
	// MUST CHANGE ON COMPETITION DAY
	private final String HOST = "10.60.38.97";
	private final int PORT = 5812;
	private final String SEND = "CV()";
	private SocketChannel socketChannel = null;
	private Socket s = null;
	private BufferedReader stdIn = null;
	private BufferedWriter stdOut = null;
	private Database d = Database.getInstance();
	private final static int TIME_OUT = 500;
	Value[] values = { Value.CV_DISTANCE, Value.CV_ANGLE_A, Value.CV_BEARING, Value.CV_L_OR_R, Value.CV_TIME_STAMP };

	static Networking instance;
	static {
		instance = new Networking();
	}

	public static Networking getInstance() {
		return instance;
	}

	public void start() {
		int i = 0;
		try {
			socketChannel = SocketChannel.open(new InetSocketAddress(HOST, PORT));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while (!socketChannel.isConnected() && i < TIME_OUT) {
			try {
				socketChannel.connect(new InetSocketAddress(HOST, PORT));
				d.setValue(Value.CV_PI_CONNECTED, 0);
			} catch (Exception e) {
				d.setValue(Value.CV_PI_CONNECTED, 1);
			}
			i++;
		}
		try {
			s = socketChannel.socket();
			stdIn = new BufferedReader(new InputStreamReader(s.getInputStream()));
			stdOut = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
		} catch (IOException e) {
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
			int i = 0;
			while (!socketChannel.isConnected() && i < TIME_OUT) {
				try {
					socketChannel.connect(new InetSocketAddress(HOST, PORT));
					d.setValue(Value.CV_PI_CONNECTED, 0);
				} catch (Exception e) {
					d.setValue(Value.CV_PI_CONNECTED, 1);
				}finally{
					i++;
				}
				if(socketChannel.isConnected()){
					s = socketChannel.socket();
				}			}
			update();
		}
	}

	public void update() {
		try {
			stdOut.write(SEND);
			String st = stdIn.readLine();
            Matcher matcher = Pattern.compile("\\d*(\\.\\d*)").matcher(st);
            for(Value v : values){
            		matcher.find();
            		d.setValue(v, Double.parseDouble(matcher.group()));
            }
		} catch (Exception e) {
			d.setValue(Value.CV_PI_CONNECTED, 1);
		}
	}
}
