package org.usfirst.frc.team2473.robot.commands;

import java.util.Scanner;

import org.usfirst.frc.team2473.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;


public class MotorDiagnosticTest extends Command {
	long mill;
	Scanner scan;

    public MotorDiagnosticTest() {
        requires(Robot.diagnosticTrain);
        mill = System.currentTimeMillis();
        scan = new Scanner(System.in);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.diagnosticTrain.runMotors(0.2);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	testFinished();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
		return scan.nextLine().equals("\n");
	}

	void testFinished() {
		String status = "testing..."; //set status default value
		if(System.currentTimeMillis() - mill > 5000) { //if the elapsed time is over 5 seconds
			if(Robot.diagnosticTrain.diff() <= 200000) { //if the difference between the least and greatest encoder value is <= 200000
				status = "DriveTrain hardware functional..."; //update status
			} else {
				status = "DriveTrain hardware faulty..."; //update status
			}
		}
		System.out.println(status); //print status
	}

    // Called once after isFinished returns true
    protected void end() {
    	Robot.diagnosticMode++;
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
