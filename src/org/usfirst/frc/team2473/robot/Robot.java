package org.usfirst.frc.team2473.robot;

import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

import org.opencv.core.Mat;
import org.usfirst.frc.team2473.robot.Database.Value;
import org.usfirst.frc.team2473.robot.subsystems.BreakbeamDiagnosticSubsystem;
import org.usfirst.frc.team2473.robot.subsystems.ClimberDiagnosticSubsystem;
import org.usfirst.frc.team2473.robot.subsystems.ClimberSystem;
import org.usfirst.frc.team2473.robot.subsystems.DriveTrain;
import org.usfirst.frc.team2473.robot.subsystems.DriveTrainDiagnostic;
import org.usfirst.frc.team2473.robot.subsystems.GyroDiagnosticSystem;
import org.usfirst.frc.team2473.robot.commands.AutoAlign;
import org.usfirst.frc.team2473.robot.commands.ClimberDiagnosticCommand;
import org.usfirst.frc.team2473.robot.commands.AutoAlignCenter;
import org.usfirst.frc.team2473.robot.commands.BreakbeamDiagnosticCommand;
import org.usfirst.frc.team2473.robot.commands.CenterAuto;
import org.usfirst.frc.team2473.robot.commands.DriveStraightForward;
import org.usfirst.frc.team2473.robot.commands.GyroDiagnosticTest;
import org.usfirst.frc.team2473.robot.commands.LeftAuto;
import org.usfirst.frc.team2473.robot.commands.MotorDiagnosticTest;
import org.usfirst.frc.team2473.robot.commands.Network;
import org.usfirst.frc.team2473.robot.commands.RightAuto;
import org.usfirst.frc.team2473.robot.commands.Turn;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	Command autonomousCommand;
	boolean timerRunning;

	public static BreakbeamDiagnosticCommand beamTest;
	public static GyroDiagnosticTest gyroTest;
	public static MotorDiagnosticTest motorTest;
	public ClimberDiagnosticCommand climber_test1, climber_test2;
	public static int diagnosticMode;
	
	public static DriveTrain driveTrain;
	public static DriveTrainDiagnostic diagnosticTrain;
	public static ClimberSystem climbSystem;
	public static GyroDiagnosticSystem gyroDiagnostic;
	public static Command auto;
	public static OI oi;
	public static AnalogGyro gyro;
	public static Relay led;
	public static SensorThread sensorThread;
	Timer robotControlLoop;
	public Database d;
	public static Networking networking;
	public static ClimberDiagnosticSubsystem climbDiagnostic;
	public static BreakbeamDiagnosticSubsystem breakbeamDiagnostic;	
	public static boolean ran = false;
	double lastTime;
	

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	public void robotInit() {
		driveTrain = new DriveTrain();
		diagnosticTrain = new DriveTrainDiagnostic();
		climbSystem = new ClimberSystem();
		climbDiagnostic = new ClimberDiagnosticSubsystem();
		breakbeamDiagnostic = new BreakbeamDiagnosticSubsystem();
		gyro = new AnalogGyro(RobotMap.gyro);
		led = new Relay(RobotMap.relay);
		oi = new OI();
		gyroDiagnostic = new GyroDiagnosticSystem();

		sensorThread = new SensorThread(5);
		sensorThread.start();
		d = Database.getInstance();
		robotControlLoop = new Timer(false);
		timerRunning = false;
		
		beamTest = new BreakbeamDiagnosticCommand();
		gyroTest = new GyroDiagnosticTest();
		motorTest = new MotorDiagnosticTest();
		climber_test1 = new ClimberDiagnosticCommand(this,1);
		climber_test2 = new ClimberDiagnosticCommand(this,2);
		diagnosticMode = 0;

		// UsbCamera cam0 = CameraServer.getInstance().startAutomaticCapture(0);
		// cam0.setResolution(640, 480);
		//
		// CvSink cvSink = CameraServer.getInstance().getVideo();
		// CvSource oStream = CameraServer.getInstance().putVideo("Gear Side",
		// 640, 480);
		// Mat source = new Mat();
		//// UsbCamera cam1 =
		// CameraServer.getInstance().startAutomaticCapture(1);
		////
		//// CvSink cvSink1 = CameraServer.getInstance().getVideo();
		//// CvSource oStream1 = CameraServer.getInstance().putVideo("Climber
		// Side", 640, 480);
		//// Mat src1 = new Mat();
		//
		// new Thread(() -> {
		// while (!Thread.interrupted()) {
		// cvSink.grabFrame(source);
		// oStream.putFrame(source);
		//// cvSink1.grabFrame(src1);
		//// oStream1.putFrame(src1);
		// }
		//
		// }).start();

		CameraServer server = CameraServer.getInstance();
		server.startAutomaticCapture("Gear Side", 0);
		server.startAutomaticCapture("Climber Side", 1);

		SmartDashboard.putData(driveTrain);
		SmartDashboard.putData(climbSystem);
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString code to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons
	 * to the switch structure below with additional strings & commands.
	 */
	public void autonomousInit() {

		// autonomousCommand = new
		// Turn(Double.parseDouble(SmartDashboard.getString("Auto Selector",
		// "10")));
		led.set(Relay.Value.kForward);
		if (!ran) {
			networking = Networking.getInstance();
			networking.start();
			ran = true;
		}
		if (Database.getInstance().getValue(Value.SWITCH_ONE) == 1) {
			autonomousCommand = new LeftAuto();
		} else if (Database.getInstance().getValue(Value.SWITCH_TWO) == 1) {
			autonomousCommand = new CenterAuto();
		} else if (Database.getInstance().getValue(Value.SWITCH_THREE) == 1) {
			autonomousCommand = new RightAuto();
		} else if (Database.getInstance().getValue(Value.SWITCH_FOUR) == 1) {
			autonomousCommand = new AutoAlignCenter();
		} else {
			autonomousCommand = new DriveStraightForward(130);
		}

		/*
		 * String autoSelected = SmartDashboard.getString("Auto Selector",
		 * "Default"); switch(autoSelected) { case "My Auto": autonomousCommand
		 * = new MyAutoCommand(); break; case "Default Auto": default:
		 * autonomousCommand = new ExampleCommand(); break; }
		 */

		// schedule the autonomous command (example)
		if (autonomousCommand != null) {
			autonomousCommand.start();
		}

	}

	/**
	 * This function is called periodically during autonomous
	 */
	public void autonomousPeriodic() {
		if (!timerRunning) {
			robotControlLoop.scheduleAtFixedRate(new TimerTask() {

				@Override
				public void run() {
					Scheduler.getInstance().run();
				}
			}, 0, 20);
			timerRunning = true;
		}
		log();
	}

	public void teleopInit() {
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		// led.set(Relay.Value.kForward);

	}

	/**
	 * This function is called periodically during operator control
	 */
	public void teleopPeriodic() {

		// System.out.println(System.currentTimeMillis() - lastTime);

		if (!timerRunning) {
			robotControlLoop.scheduleAtFixedRate(new TimerTask() {

				@Override
				public void run() {
					Scheduler.getInstance().run();
				}
			}, 0, 20);
			timerRunning = true;
		}

		oi.updateButtons();
		oi.updateJoysticks();

		log();
		lastTime = System.currentTimeMillis();

	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testInit() {
		try {
			networking.end();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void testPeriodic() {
		switch (diagnosticMode) {
		case 0:
			motorTest.start();
			break;
		case 1:
			beamTest.start();
			break;
		case 2:
			gyroTest.start();
			break;
		case 3:
			climber_test1.start();
			break;
		case 4:
			climber_test2.start();
			break;
		default:
			break;
		}
	}

	@Override
	public void disabledInit() {
		if (autonomousCommand != null)
			autonomousCommand.cancel();

		led.set(Relay.Value.kOff);
		// synchronized(networking){
		// networking.end();
		// }
	}

	@Override
	public void disabledPeriodic() {

		if (timerRunning) {
			// ends the timer and stops it from executing any tasks
			robotControlLoop.cancel();
			robotControlLoop = new Timer();
			timerRunning = false;
		}
	}

	public void log() {
		Database.getInstance().log();
	}

	@Override
	public void finalize() {
		try {
			super.finalize();
			networking.end();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// set motors to 0
	}
}
