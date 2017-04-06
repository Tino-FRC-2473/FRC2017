package org.usfirst.frc.team2473.robot.commands;



import org.usfirst.frc.team2473.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class BreakbeamDiagnosticCommand extends Command {
	public BreakbeamDiagnosticCommand() {
		// Use requires() here to declare subsystem dependencies
		// eg. requires(chassis);
		requires(Robot.breakbeamDiagnostic);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		System.out.println("Place an object in front of the breakbeam sensor."); //initial message to instruct user
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		if(!isFinished()) { //if the command is not yet finished
			System.out.println("Waiting..."); //print waiting
		}
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() { 
		if(Robot.breakbeamDiagnostic.getBreakbeamValue() == 2) { //if the breakbeam sensor returns true
			return true; //the command is complete
		} else {
			return false; //the command continues
		}
	}

	// Called once after isFinished returns true
	protected void end() {
		System.out.println("Breakbeam system operational."); //prints status once test is complete
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		System.out.println("A fatal excaption 0E has occured at 0028:C0011E36 in UXD UMM(01)" + "\n"
				+ "The current application will be terminated."); //troll message symbolizing interruption
	}
}

