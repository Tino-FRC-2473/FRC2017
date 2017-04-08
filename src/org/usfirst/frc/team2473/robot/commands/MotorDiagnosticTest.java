package org.usfirst.frc.team2473.robot.commands;

import org.usfirst.frc.team2473.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;


public class MotorDiagnosticTest extends Command {
	long mill;

    public MotorDiagnosticTest() {
        requires(Robot.diagnosticTrain);
        mill = System.currentTimeMillis();
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	System.out.println("init");
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.diagnosticTrain.runMotors(0.2);
    	Robot.diagnosticTrain.testNum++;
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
		return testFinished(); //return the testFinished() value
	}

	boolean testFinished() {
		if (Robot.diagnosticTrain.testNum <= 8) return false;
		boolean returner = false; //value to be returned is currently false
		String status = "testing..."; //set status default value
		if(System.currentTimeMillis() - mill > 5000) { //if the elapsed time is over 5 seconds
			if(Robot.diagnosticTrain.diff() <= 200000) { //if the difference between the least and greatest encoder value is <= 200000
				status = "DriveTrain hardware functional..."; //update status
				returner = true; //value to be returned is true
			} else {
				status = "DriveTrain hardware faulty..."; //update status
			}
		}
		System.out.println(status); //print status
		return returner; //return value to be returned
	}

    // Called once after isFinished returns true
    protected void end() {
    	Robot.diagnosticTrain.stop();
    	Robot.diagnosticMode++;
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
