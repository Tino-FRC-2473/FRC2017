package org.usfirst.frc.team2473.robot.commands;

import org.usfirst.frc.team2473.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class Climber extends Command{

	public Climber() {
		requires(Robot.climbSystem);
	}
	
	@Override
	protected void initialize() {
		super.initialize();
	}
	
	@Override
	protected void execute() {
		super.execute();
		Robot.climbSystem.climb(0.5);
	}

	@Override
	protected boolean isFinished() {
		
		return false;
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
