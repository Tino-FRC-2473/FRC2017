package org.usfirst.frc.team2473.robot.commands;

import org.usfirst.frc.team2473.robot.Database;
import org.usfirst.frc.team2473.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class Climber extends Command {

	private boolean climbingRope;
	private boolean LSPressed;
	private double slowSpeed;
	private double fastSpeed;
	private double encoderValue;
	private double timeLimit;
	private int encoderTicks; // Number of encoder ticks to keep running the
								// motor after hitting the touch pad (ms)
	private boolean finished;
	private long prevTime;
	private long time;

	public Climber() {
		requires(Robot.climbSystem);
	}

	@Override
	protected void initialize() {
		super.initialize();
		climbingRope = false;
		LSPressed = false;
		slowSpeed = 0.3;
		fastSpeed = 0.6;
		encoderValue = Double.MAX_VALUE;
		timeLimit = 250;
		encoderTicks = 30;
		finished = false;
		prevTime = Long.MAX_VALUE;
		time = Long.MAX_VALUE;
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
		
		if (Database.getInstance().getButton(Database.ButtonName.CLIMBER_1_SEC).get()) {
			time = System.currentTimeMillis();
		}

		if (Database.getInstance().getButton(Database.ButtonName.CLIMBER_SPEED_TOGGLE).get()) {
			climbingRope = !climbingRope;
		}

		if (!climbingRope) {
			// System.out.println("Slow");
			Robot.climbSystem.climb(slowSpeed);
		} else {
			// System.out.println("Fast");
			Robot.climbSystem.climb(fastSpeed);
		}

		// If the limit switch is pressed
		if (Robot.climbSystem.getLimitSwitch()) {
			if (LSPressed == false) {
				encoderValue = Robot.climbSystem.getEncValue();
				prevTime = System.currentTimeMillis();
			}
			LSPressed = true;
		} else {
			LSPressed = false;
		}


		// if(LSPressed){
		// Robot.climbSystem.climb(0);
		// }

		if (System.currentTimeMillis() - time >= 1000) {
			finished = true;
		}
		//System.out.println("Enc: " + Robot.climbSystem.getEncValue());

		if (Database.getInstance().getButton(Database.ButtonName.STOP_CLIMBER).get()) {
			// System.out.println("Stopping");
			finished = true;
		}
		
		
		/*
		 	Code to test if the two motors run in same direction.
		 
		if(Database.getInstance().getButton(Database.ButtonName.START_CLIMBER).get()){
			Robot.climbSystem.climb(0.3);
			Robot.climbSystem.climb2(0);
		}
		
		if(Database.getInstance().getButton(Database.ButtonName.CLIMBER_SPEED_TOGGLE).get()){
			Robot.climbSystem.climb(0);
			Robot.climbSystem.climb2(0);
		}
		
		if(Database.getInstance().getButton(Database.ButtonName.STOP_CLIMBER).get()){
			Robot.climbSystem.climb(0);
			Robot.climbSystem.climb2(0.3);
		}
		*/
	}

	@Override
	protected boolean isFinished() {
		//TODO Fix condition checking, doesn't work.
		boolean reachedEncoderCount = Robot.climbSystem.getEncValue() - encoderValue >= encoderTicks;
		boolean reachedTimeLimit = System.currentTimeMillis() - prevTime >= timeLimit;
		//System.out.println("Time Limit: " + System.currentTimeMillis() + ", " + prevTime);
		//System.out.println("Finished: " + finished);
		System.out.println("Fin: " + finished + ", Enc: " + reachedEncoderCount + ", Time: " + reachedTimeLimit);
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
		finished = false;
	}

	@Override
	protected void interrupted() {
		super.interrupted();
		end();
	}

}
