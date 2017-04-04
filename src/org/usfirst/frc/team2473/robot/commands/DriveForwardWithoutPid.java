package org.usfirst.frc.team2473.robot.commands;

import java.util.ArrayList;
import java.util.function.DoubleSupplier;

import org.usfirst.frc.team2473.robot.Database;
import org.usfirst.frc.team2473.robot.Database.Value;
import org.usfirst.frc.team2473.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveForwardWithoutPid extends Command{
	private double distance;
	private static final double KPRotate = .07;
	private static final double KIRotate = .003;
	private static final double KDRotate = 0;
	
	private double startingGyroValue;
	private double integralRotate;
	private double lastProportionRotate;
	
	public DriveForwardWithoutPid(double distance){
		requires(Robot.driveTrain);
		
		this.distance = distance;
    }
	

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.sensorThread.resetEncoders();
    	
    	startingGyroValue = Database.getInstance().getValue(Value.GYRO_POSITION);
    	integralRotate = 0;
    	lastProportionRotate = 0;
    	
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	double proportionRotate = Database.getInstance().getValue(Value.GYRO_POSITION) - startingGyroValue;
    	integralRotate += proportionRotate;
    	double derivativeRotate = proportionRotate - lastProportionRotate;
    	double rotate = KPRotate * proportionRotate + KIRotate*integralRotate + KDRotate*derivativeRotate;
    	

    	if(Math.abs(rotate) > .70){
    		rotate = Math.signum(rotate) * .7;
    	}
    	
    	//base speed
    	
    	Robot.driveTrain.driveArcade((distance > 0)?.85:-.85,rotate);
    	
    	lastProportionRotate = proportionRotate;
    }

	protected boolean isFinished() {
		return Math.abs(Database.getInstance().getValue(Value.LEFT_ENCODER_POSITION)) > Math.abs(distance);
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