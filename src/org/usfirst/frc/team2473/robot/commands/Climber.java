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
	private int encoderTicks; // Number of encoder ticks to keep running the
								// motor after hitting the touch pad (ms)
	private boolean finished;
	private boolean updateEncoderValue;
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
		encoderValue = 0;
		encoderTicks = 30;
		finished = false;
		updateEncoderValue = true;
		time = 0;
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
				updateEncoderValue = false;
			}
			LSPressed = true;
		} else {
			LSPressed = false;
			updateEncoderValue = true;
		}

		if (updateEncoderValue) {
			encoderValue = Robot.climbSystem.getEncValue();
			prevTime = System.currentTimeMillis();
		}

		// if(LSPressed){
		// Robot.climbSystem.climb(0);
		// }

		if (System.currentTimeMillis() - time >= 1000) {
			finished = true;
		}
		System.out.println("Enc: " + encoderValue);

		if (Database.getInstance().getButton(Database.ButtonName.STOP_CLIMBER).get()) {
			// System.out.println("Stopping");
			finished = true;
		}
	}

	@Override
	protected boolean isFinished() {
		boolean reachedEncoderCount = Robot.climbSystem.getEncValue() - encoderValue >= encoderTicks;
		boolean reachedTimeLimit = System.currentTimeMillis() - prevTime >= 250;
		// System.out.println("is finished: " + (LSPressed &&
		// (reachedEncoderCount || reachedTimeLimit)));
		return (finished || (LSPressed && (reachedEncoderCount || reachedTimeLimit)));
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
