package org.usfirst.frc.team2473.robot.commands;

import org.usfirst.frc.team2473.robot.Database;
import org.usfirst.frc.team2473.robot.Database.Value;
import org.usfirst.frc.team2473.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class DriveStraightForward extends Command{
	private double distance;
	private static final double KP = .1;
	private static final double KI = .003;
	private static final double KD = 0;
	
	private double startingGyroValue;
	private double integral;
	private double lastProportion;
	
	public DriveStraightForward(double distance){
		requires(Robot.driveTrain);
		
		this.distance = distance;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.sensorThread.resetEncoders();
    	
    	startingGyroValue = Database.getInstance().getValue(Value.GYRO);
    	integral = 0;
    	lastProportion = 0;
    	
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	double proportion = Database.getInstance().getValue(Value.GYRO) - startingGyroValue;
    	integral += proportion;
    	double derivative = proportion - lastProportion;
    	double rotate = KP * proportion + KI*integral + KD*derivative;
    	
    	if(Math.abs(rotate) > .70){
    		rotate = Math.signum(rotate) * .7;
    	}
    	
    	Robot.driveTrain.driveArcade(.05 + .45 * Math.min(1,((distance - Database.getInstance().getValue(Value.RIGHT_ENCODER))) / ((distance < 8)?distance*7/8:Math.min(10, 6.5 + (distance-10)/5))), rotate);
    	
    	lastProportion = proportion;
    	
	
    }

	protected boolean isFinished() {
		return Math.abs(Database.getInstance().getValue(Value.RIGHT_ENCODER)) > distance || Math.abs(Database.getInstance().getValue(Value.LEFT_ENCODER)) > distance;
	}
	
	protected void end() {
    	Robot.driveTrain.drive(0, 0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}