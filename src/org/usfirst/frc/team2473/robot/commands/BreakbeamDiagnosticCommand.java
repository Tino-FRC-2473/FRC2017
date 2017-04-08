package org.usfirst.frc.team2473.robot.commands;



import java.util.Scanner;

import org.usfirst.frc.team2473.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class BreakbeamDiagnosticCommand extends Command {
	Scanner scan;
	public BreakbeamDiagnosticCommand() {
		requires(Robot.breakbeamDiagnostic);
		scan = new Scanner(System.in);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		System.out.println("Place an object in front of the breakbeam sensor."); //initial message to instruct user
	}
	
	protected void execute() {
		if (Robot.breakbeamDiagnostic.getBreakbeamValue() == 1) {
			System.out.println("Activated");
		}
		else {
			System.out.println("Not Activated");
		}
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() { 
		return scan.nextLine().equals("\n");
	}

	// Called once after isFinished returns true
	protected void end() {
		Robot.diagnosticMode++;
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		System.out.println("A fatal excaption 0E has occured at 0028:C0011E36 in UXD UMM(01)" + "\n"
				+ "The current application will be terminated."); //troll message symbolizing interruption
	}
}

