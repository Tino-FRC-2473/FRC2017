package org.usfirst.frc.team2473.robot;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Networking extends Thread {
    // MUST CHANGE ON COMPETITION DAY
    private final String HOST = "10.19.92.233";
    private final int PORT = 4444;
    private final byte[] SEND = "CV()".getBytes(Charset.defaultCharset());
    private char[] cbuf = new char[4096];
    private Socket s = new Socket();
    private BufferedReader stdIn = null;
    private OutputStream stdOut = null;
    private Database d = Database.getInstance();
    private final static int TIME_OUT = 500;
    Database.Value[] values = {Database.Value.CV_DISTANCE, Database.Value.CV_ANGLE_A, Database.Value.CV_BEARING, Database.Value.CV_L_OR_R, Database.Value.CV_TIME_STAMP};

    static Networking instance;

    static {
        instance = new Networking();
    }

    public static Networking getInstance() {
        return instance;
    }

    public void start() {
        int i = 0;
        System.out.println("Good morning!");
        try {
            System.out.println("About to make socket");
            s.connect(new InetSocketAddress(HOST, PORT), 100);
            System.out.println("Made socket");
            s.setKeepAlive(true);
            System.out.println("Keep alive");
            d.setValue(Database.Value.CV_PI_CONNECTED, 0);
            System.out.println("My connection has succeeded!");
        } catch (Exception e1) {
            d.setValue(Database.Value.CV_PI_CONNECTED, 1);
            System.out.println("My connection has failed but I will go on.");
        }
        System.out.println("Before Loop!");
        boolean b = false;
        try{
            b  = s.getLocalAddress().isReachable(100);
        }catch (IOException exception){
        }
        while (!b && i < TIME_OUT) {
            try {
                s.connect(new InetSocketAddress(HOST, PORT), 100);
                s.setKeepAlive(true);
                d.setValue(Database.Value.CV_PI_CONNECTED, 0);
                sleep(1000);
                System.out.println("My connection has succeeded!");
            } catch (Exception e) {
                d.setValue(Database.Value.CV_PI_CONNECTED, 1);
                System.out.println("My connection has failed but I will go on.");
            }
            i++;
        }
        System.out.println("Completed loop");
        try {
            stdIn = new BufferedReader(new InputStreamReader(s.getInputStream()));
            stdOut = s.getOutputStream();
        } catch (IOException e) {
            d.setValue(Database.Value.CV_PI_CONNECTED, 1);
        }
        super.start();
    }

    public void run() {
        while (true) {
            System.out.println("Hello running run");
            try {
                synchronized (this) {
                    wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Done waiting!");
            int i = 0;
            boolean b = false;
            try{
                b  = s.getLocalAddress().isReachable(100);
            }catch (IOException exception){
            }
            while (!b && i < TIME_OUT) {
                try {
                    s.connect(new InetSocketAddress(HOST, PORT), 100);
                    s.setKeepAlive(true);
                    d.setValue(Database.Value.CV_PI_CONNECTED, 0);
                    System.out.println("My connection has succeeded!");
                    wait(1000);
                } catch (Exception e) {
                    d.setValue(Database.Value.CV_PI_CONNECTED, 1);
                    System.out.println("My connection has failed but I will go on.");
                } finally {
                    i++;
                }
            }
            System.out.println("Hello exited loop");
            update();
        }
    }

    public void update() {
        try {
            System.out.println("Hello update");
            stdOut.write(SEND);
            stdIn.read(cbuf);
            String st = String.copyValueOf(cbuf);
            Matcher matcher = Pattern.compile("\\d*(\\.\\d*)").matcher(st);
            for (Database.Value v : values) {
                matcher.find();
                d.setValue(v, Double.parseDouble(matcher.group()));
            }
        } catch (Exception e) {
            System.out.println("I am not doing so hot rn.");
            d.setValue(Database.Value.CV_PI_CONNECTED, 1);
        }
    }
}