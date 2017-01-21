package org.usfirst.frc.team2473.robot.commands;

import org.usfirst.frc.team2473.robot.Database;
import org.usfirst.frc.team2473.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class Climber extends Command{

	private boolean climbingRope;
	private boolean LSPressed;
	private double slowSpeed;
	private double fastSpeed;
	private long startTime;
	private int duration;		//Time to keep running the motor after hitting the touch pad (ms)
	
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
		startTime = 0;
		duration = 500;
	}
	
	/**
	 * This method will start the climbing process according to the procedure below:
	 * 
	 * 1. Turn climber motor slowly to catch rope; when Driver manually hits button __,
	 * speed up motor to climb up the rope
	 * 2. When touch sensor is pressed, run motor for __ seconds to ensure button press.
	 * 3. Stop motor.
	 * 
	 */
	@Override
	protected void execute() {
		super.execute();

		if(!climbingRope){
			Robot.climbSystem.climb(slowSpeed);
		}else{
			Robot.climbSystem.climb(fastSpeed);
		}
		
		//If the limit switch is pressed (1.0 is true, 0.0 is false, also in SensorThread)
		if(Database.getInstance().getValue(Database.Value.CLIMBER_LS) == 1.0){
			if(LSPressed == false){
				startTime = System.currentTimeMillis();
			}
			LSPressed = true;
		}else{
			LSPressed = false;
		}
		
	}

	@Override
	protected boolean isFinished() {
		return (LSPressed && startTime + duration >= System.currentTimeMillis());
	}
	
	@Override
	protected void end() {
		super.end();
		Robot.climbSystem.climb(0);
	}
	
	@Override
	protected void interrupted() {
		super.interrupted();
		end();
	}
	

}
