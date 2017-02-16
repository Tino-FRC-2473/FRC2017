package org.usfirst.frc.team2473.robot.commands;

import java.util.ArrayList;

import org.usfirst.frc.team2473.robot.Database;
import org.usfirst.frc.team2473.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class Climber extends Command {

	private boolean climbingRope;
	private boolean finished;
	private boolean lastPress;
	
	private double slowSpeed;
	private double fastSpeed;
	private double encoderValue;
	
	private int numValues;		//number of current values to record
	private int timeLimit;
	private int encoderTicks;	// Number of encoder ticks to keep running the
								// motor after hitting the touch pad (ms)
	
	private long prevTime;
	private long time;
	
	private ArrayList<Double> currentList;

	public Climber() {
		requires(Robot.climbSystem);
	}

	@Override
	protected void initialize() {
		super.initialize();
		
		climbingRope = false;
		finished = false;
		lastPress = false;
		
		slowSpeed = 0.3;
		fastSpeed = 0.60;
		encoderValue = Double.MAX_VALUE;
		
		numValues = 20;
		timeLimit = 250;
		encoderTicks = 30;
		
		prevTime = Long.MAX_VALUE;
		time = Long.MAX_VALUE;
		
		currentList = new ArrayList<Double>();
	}

	/**
	 * This method will start the climbing process according to the procedure
	 * below:
	 * 
	 * 1. Turn climber motor slowly to catch rope; when Driver manually hits
	 * button __, speed up motor to climb up the rope 2. When touch sensor is
	 * pressed, run motor for __ encoder ticks to ensure button press. 3. Stop
	 * motor.
	 * 
	 */
	@Override
	protected void execute() {
		super.execute();
		
		String logMessage = String.format("Cur: %.3f, Avg: %.3f, Enc: %.3f  |||  ", Robot.climbSystem.getCurrent(), getCurrentAverage(), Robot.climbSystem.getEncValue());
		Robot.climbSystem.log(logMessage);
		System.out.println(logMessage);
		
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
		
		
		if (Database.getInstance().getButton(Database.ButtonName.CLIMBER_1_SEC).get()) {
			time = System.currentTimeMillis();
		}

		//TODO Make sure there is a delay for switching speeds
		boolean currPressed = Database.getInstance().getButton(Database.ButtonName.CLIMBER_SPEED_TOGGLE).get();
		if (currPressed && !lastPress) {
			climbingRope = !climbingRope;
		}
		lastPress = currPressed;
		if (!climbingRope) {
			// System.out.println("Slow");
			Robot.climbSystem.climb(slowSpeed);
		} else {
			// System.out.println("Fast");
			Robot.climbSystem.climb(fastSpeed);
		}

		if (System.currentTimeMillis() - time >= 1000) {
			finished = true;
		}
		//System.out.println("Enc: " + Robot.climbSystem.getEncValue());

		if (Database.getInstance().getButton(Database.ButtonName.STOP_CLIMBER).get()) {
			// System.out.println("Stopping");
			finished = true;
		}
		
		
		
		//	WIP Code to a state machine
		
		/*
		 * switch(state):
		 * 
		 * case LOOKING_FOR_ROPE:
		 * 		if(first threshold) -> state = CLIMBING ROPE
		*/
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
		// return finished;
	}

	@Override
	protected void end() {
		super.end();
		Robot.climbSystem.climb(0);
		Robot.climbSystem.close();
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
