package org.usfirst.frc.team2473.robot;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

import java.util.Timer;
import java.util.TimerTask;

import org.usfirst.frc.team2473.robot.commands.*;
import org.usfirst.frc.team2473.robot.subsystems.*;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot{

	Command autonomousCommand;
	boolean timerRunning;

	public static DriveTrain driveTrain;
	public static Command auto;
	public static OI oi;
	public static AnalogGyro gyro;
	public static SensorThread sensorThread;
	
	public static ClimberSystem climbSystem;
	
	Timer robotControlLoop;

	
	double lastTime;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	public void robotInit() {
		driveTrain = new DriveTrain();
		gyro = new AnalogGyro(RobotMap.gyro);
		oi = new OI();
		
		climbSystem = new ClimberSystem();
		
		sensorThread = new SensorThread(5);
		sensorThread.start();
		robotControlLoop = new Timer(false);
		timerRunning = false;
		
		CameraServer server = CameraServer.getInstance();
		server.startAutomaticCapture("router side", 0);
		server.startAutomaticCapture("other side", 1);
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

		/*
		 * String autoSelected = SmartDashboard.getString("Auto Selector",
		 * "Default"); switch(autoSelected) { case "My Auto": autonomousCommand
		 * = new MyAutoCommand(); break; case "Default Auto": default:
		 * autonomousCommand = new ExampleCommand(); break; }
		 */

		// schedule the autonomous command (example)
		if (autonomousCommand != null)
			autonomousCommand.start();
	}

	/**
	 * This function is called periodically during autonomous
	 */
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	public void teleopInit() {
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.

	}

	/**
	 * This function is called periodically during operator control
	 */
	//CANTalon c = new CANTalon(4);
	
	public void teleopPeriodic() {

		//System.out.println(System.currentTimeMillis() - lastTime);

		if (!timerRunning) {
			robotControlLoop.scheduleAtFixedRate(new TimerTask(){

				@Override
				public void run() {
					Scheduler.getInstance().run();
				}
			}, 0, 20);
			timerRunning = true;
		}
		
		Database.getInstance().getButton(Database.ButtonName.START_CLIMBER).whenPressed(new Climber());
		
		oi.updateButtons();
		oi.updateJoysticks();
		
		log();
		lastTime = System.currentTimeMillis();

		

		//climbSystem.climb(0.5);

	}

	/**
	 * This function is called periodically during test mode
	 */
	public void testPeriodic() {
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
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// set motors to 0
	}
}