package org.usfirst.frc.team2473.robot.commands;

import java.util.ArrayList;

import org.usfirst.frc.team2473.robot.Database;
import org.usfirst.frc.team2473.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

//Code to start the climber is in Robot.java, teleopPeriodic(), using Database button name START_CLIMBER.
//All button values can be found in OI.java.

public class Climb extends Command {

	public static boolean ratchetCorrect = true;
	
	private boolean climbingRope;		//speed toggle boolean, when true, run at fast speed, else, slow speed
	private boolean finished;			//when to stop the command
	private boolean lastPress;			//boolean tracking last speed toggle button press
	private boolean checkEncoder;		//determines whether or not to check encoder values
	
	
	private double slowSpeed;			//default/starting speed (percent)
	private double fastSpeed;			//fast/toggled speed (percent)
	private double speed;
	private double encoderValue;		//num of encoder ticks to climb up before slowing down
	
	private int numValues;		//number of current values to record
	
	private ArrayList<Double> currentList;		//list of current values to average

	public Climb() {
		requires(Robot.climbSystem);
	}

	@Override
	protected void initialize() {
		super.initialize();
		
		climbingRope = false;
		finished = false;
		lastPress = false;
		checkEncoder = false;
		
		slowSpeed = 0.40;
		fastSpeed = 0.80;
		encoderValue = 8000;
		
		numValues = 20;
		
		currentList = new ArrayList<Double>();
	}

	@Override
	protected void execute() {
		super.execute();
		if(!ratchetCorrect){
			finished = true;
			System.out.println("ERROR: Ratchet not correct, stopping...");
			return;
		}
		//Prints out current, average current, and encoder value
		String logMessage = String.format("Cur: %.3f, Avg: %.3f, Enc: %.3f", Robot.climbSystem.getCurrent(), getCurrentAverage(), Robot.climbSystem.getEncValue());
		System.out.println(logMessage);
		//Robot.climbSystem.log(logMessage);
		//System.out.println(logMessage);
		
		//System.out.println(Robot.climbSystem.getCurrent());
		
		//Checking current and if hit threshold.
		if(getCurrentAverage() > 27) {		//was 19
			System.out.println("Hitting top");
			finished = true;
		}else if(getCurrentAverage() > 4){
			//System.out.println("Climbing rope.");
			if(!climbingRope){
				Robot.climbSystem.resetEncoder();
				checkEncoder = !checkEncoder;
			}
			climbingRope = true;
		}else{
			//System.out.println("Not climbing");
		}
		
		currentList.add(Robot.climbSystem.getCurrent());
		
		if(currentList.size() > numValues){
			currentList.remove(0);
		}

		//Toggle speed code
		boolean currPressed = Database.getInstance().getButton(Database.ButtonName.CLIMBER_SPEED_TOGGLE).get();
		if (currPressed && !lastPress) {
			climbingRope = !climbingRope;
		}
		lastPress = currPressed;
		
		if (!climbingRope) {
			Robot.climbSystem.climb(slowSpeed);
		} else {
			Robot.climbSystem.climb(fastSpeed);
		}
		
		//Use encoder values to slow down the climber code
		if(checkEncoder){ 
			//If robot has climbed *encoderValue* number of ticks
			if(Robot.climbSystem.getEncValue() >= encoderValue){
				speed -= .01;
				if(speed < 0.55){
					speed = 0.55;
					checkEncoder = false;
				}
				Robot.climbSystem.climb(speed);
			}
		}
		
		//"Emergency" Stop button, immediately stops the climber.
		if (Database.getInstance().getButton(Database.ButtonName.STOP_CLIMBER).get()) {
			finished = true;
		}
	}

	@Override
	protected boolean isFinished() {
		return finished;
	}

	@Override
	protected void end() {
		super.end();
	//	Robot.climbSystem.close();
		Robot.climbSystem.climb(0);
		finished = false;
	}

	@Override
	protected void interrupted() {
		super.interrupted();
		end();
	}
	
	private double getCurrentAverage(){
		if(currentList.size() < numValues){
			return 0;
		}
		
		double sum = 0;
		for(int i=0; i<currentList.size(); i++){
			sum += currentList.get(i);
		}
		
		return sum / currentList.size();
	}
}
