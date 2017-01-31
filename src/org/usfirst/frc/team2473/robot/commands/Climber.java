package org.usfirst.frc.team2473.robot.commands;

import org.usfirst.frc.team2473.robot.Database;
import org.usfirst.frc.team2473.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class Climber extends Command{

	private boolean climbingRope;
	private boolean LSPressed;
	private double slowSpeed;
	private double fastSpeed;
	private double startEncoderValue;
	private int encoderTicks;		//Number of encoder ticks to keep running the motor after hitting the touch pad (ms)
	
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
		startEncoderValue = 0;
		encoderTicks = 500;
	}
	
	/**
	 * This method will start the climbing process according to the procedure below:
	 * 
	 * 1. Turn climber motor slowly to catch rope; when Driver manually hits button __,
	 * speed up motor to climb up the rope
	 * 2. When touch sensor is pressed, run motor for __ encoder ticks to ensure button press.
	 * 3. Stop motor.
	 * 
	 */
	@Override
	protected void execute() {
		super.execute();

		if(Database.getInstance().getButton(Database.ButtonName.STOP_CLIMBER).get()){
			end();
		}
		
		//TODO Double check if get() returns if the button is pressed or not.
		if(Database.getInstance().getButton(Database.ButtonName.CLIMBER_SPEED_TOGGLE).get()){
			climbingRope = !climbingRope;
		}
		
		if(!climbingRope){
			Robot.climbSystem.climb(slowSpeed);
		}else{
			Robot.climbSystem.climb(fastSpeed);
		}
		
		//If the limit switch is pressed
		if(climbingRope && Robot.climbSystem.getLimitSwitch()){
			if(LSPressed == false){
				startEncoderValue = Robot.climbSystem.getEncValue();
			}
			LSPressed = true;
		}else{
			LSPressed = false;
		}
		
	}

	@Override
	protected boolean isFinished() {
		return (LSPressed && startEncoderValue + encoderTicks >= Robot.climbSystem.getEncValue());
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
