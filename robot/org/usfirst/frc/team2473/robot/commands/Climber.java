package org.usfirst.frc.team2473.robot.commands;

import java.util.ArrayList;

import org.usfirst.frc.team2473.robot.Database;
import org.usfirst.frc.team2473.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

//Code to start the climber is in Robot.java, teleopPeriodic(), using Database button name START_CLIMBER.
//All button values can be found in OI.java.

public class Climber extends Command {

	private boolean climbingRope;		//speed toggle boolean, when true, run at fast speed, else, slow speed
	private boolean finished;			//when to stop the command
	private boolean lastPress;			//boolean tracking last speed toggle button press
	
	private double slowSpeed;			//default/starting speed (percent)
	private double fastSpeed;			//fast/toggled speed (percent)
	private double encoderValue;		//UNUSED
	
	private int numValues;		//number of current values to record
	private int timeLimit;		//UNUSED
	private int encoderTicks;	//UNUSED; Number of encoder ticks to keep running the motor after hitting the touch pad (ms)
	
	private long prevTime;		//UNUSED
	private long time;			//UNUSED
	
	private ArrayList<Double> currentList;		//list of current values to average

	public Climber() {
		requires(Robot.climbSystem);
	}

	@Override
	protected void initialize() {
		super.initialize();
		
		climbingRope = false;
		finished = false;
		lastPress = false;
		
		slowSpeed = 0.30;
		fastSpeed = 0.60;
		encoderValue = Double.MAX_VALUE;
		
		numValues = 20;
		timeLimit = 250;
		encoderTicks = 30;
		
		prevTime = Long.MAX_VALUE;
		time = Long.MAX_VALUE;
		
		currentList = new ArrayList<Double>();
	}

	@Override
	protected void execute() {
		super.execute();
		
		//Prints out current, average current, and encoder value
		String logMessage = String.format("Cur: %.3f, Avg: %.3f, Enc: %.3f  |||  ", Robot.climbSystem.getCurrent(), getCurrentAverage(), Robot.climbSystem.getEncValue());
		System.out.println(logMessage);
		
		//Checking current and if hit threshold.
		if(getCurrentAverage() > 6.8) {
			System.out.println("Hitting top");
			finished = true;
		}else if(getCurrentAverage() > 2){
			System.out.println("Climbing rope.");
		}else{
			System.out.println("Not climbing");
		}
		
		currentList.add(Robot.climbSystem.getCurrent());
		
		if(currentList.size() > numValues){
			currentList.remove(0);
		}
		
		//UNUSED, WIP
		if (Database.getInstance().getButton(Database.ButtonName.CLIMBER_1_SEC).get()) {
			time = System.currentTimeMillis();
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

		//UNUSED, WIP
		if (System.currentTimeMillis() - time >= 1000) {
			finished = true;
		}
		
		//"Emergency" Stop button, immediately stops the climber.
		if (Database.getInstance().getButton(Database.ButtonName.STOP_CLIMBER).get()) {
			finished = true;
		}
	}

	@Override
	protected boolean isFinished() {
		//TODO Fix condition checking, doesn't work. Time Limit always true.
		boolean reachedEncoderCount = Robot.climbSystem.getEncValue() - encoderValue >= encoderTicks;
		boolean reachedTimeLimit = System.currentTimeMillis() - prevTime >= timeLimit;
		//System.out.println("Time Limit: " + System.currentTimeMillis() + ", " + prevTime);
		//System.out.println("Finished: " + finished);
		//System.out.println("Fin: " + finished + ", Enc: " + reachedEncoderCount + ", Time: " + reachedTimeLimit);
		// System.out.println("is finished: " + (LSPressed &&
		// (reachedEncoderCount || reachedTimeLimit)));
		//return (finished || (reachedEncoderCount || reachedTimeLimit));
		return finished;
	}

	@Override
	protected void end() {
		super.end();
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
