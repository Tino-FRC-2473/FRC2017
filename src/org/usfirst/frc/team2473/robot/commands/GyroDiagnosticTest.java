package org.usfirst.frc.team2473.robot.commands;

import org.usfirst.frc.team2473.robot.Database;
import org.usfirst.frc.team2473.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class GyroDiagnosticTest extends Command {
	private long time;
	
    public GyroDiagnosticTest() {
        time = System.currentTimeMillis();         
        requires(Robot.gyroDiagnostic);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	System.out.println("Current gyro value: " + Database.getInstance().getValue(Database.Value.GYRO_POSITION));
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return time == 10000;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.diagnosticMode++;
    	end();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
