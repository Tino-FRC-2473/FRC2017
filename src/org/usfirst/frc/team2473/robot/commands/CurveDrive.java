package org.usfirst.frc.team2473.robot.commands;

import java.util.ArrayList;
import java.util.function.DoubleSupplier;

import org.usfirst.frc.team2473.robot.Database;
import org.usfirst.frc.team2473.robot.Database.Value;
import org.usfirst.frc.team2473.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class CurveDrive extends Command{
	private double distance;
	private double bearing;
	private static final double KPRotate = .07;
	private static final double KIRotate = .003;
	private static final double KDRotate = 0;
	
	
	private static final double KPForward = .30;
	private static final double KIForward = 0;
	private static final double KDForward = 0;
	
	
	private double startingGyroValue;
	private double integralRotate;
	private double lastProportionRotate;
	
	private double integralForward;
	private double lastProportionForward;
	
	private ArrayList<Double> pastEncoderVelocities;
	
	public CurveDrive(double distance, double bearing){
		requires(Robot.driveTrain);
		this.distance = distance;
		this.bearing = bearing;
    }
	

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.sensorThread.resetEncoders();
    	
    	startingGyroValue = Database.getInstance().getValue(Value.GYRO_POSITION);
    	integralRotate = 0;
    	lastProportionRotate = 0;
    	
    	integralForward = 0;
    	lastProportionForward = 0;
    	
    	pastEncoderVelocities = new ArrayList<>();
    	
    	for(int i = 0; i < 50; i++){
    		pastEncoderVelocities.add(5.0);
    	}
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	double proportionRotate = Database.getInstance().getValue(Value.GYRO_POSITION) - startingGyroValue;
    	integralRotate += proportionRotate;
    	double derivativeRotate = proportionRotate - lastProportionRotate;
    	double rotate = KPRotate * proportionRotate + KIRotate*integralRotate + KDRotate*derivativeRotate;
    	
    	if (Math.abs(proportionRotate) < Math.abs(bearing)){
	    	rotate = Math.signum(rotate) * .8;
    	} else {
    		if(Math.abs(rotate) > .70){
        		rotate = Math.signum(rotate) * .7;
        	}
    	}
    	
    	//base speed
    	double proportionForward = (distance - Database.getInstance().getValue(Value.LEFT_ENCODER_POSITION))/(Math.abs(distance)+5);
    	integralForward += proportionForward;
    	double derivativeForward = proportionForward - lastProportionForward;
    	double speed = KPForward * proportionForward + KIForward*integralForward + KDForward*derivativeForward;
    	//base speed
    	speed = Math.signum(speed)*(Math.abs(speed) + .50);
    	
    	if(Math.abs(speed) > .80){
    		speed = Math.signum(speed) * .8;
    	}
    	
    	pastEncoderVelocities.remove(0);
    	pastEncoderVelocities.add(Math.abs(Database.getInstance().getValue(Value.LEFT_ENCODER_VELOCITY)));
    	
    	Robot.driveTrain.driveArcade(speed,rotate);
    	
    	lastProportionRotate = proportionRotate;
    }

	protected boolean isFinished() {
		return Math.abs(Database.getInstance().getValue(Value.LEFT_ENCODER_POSITION)) > Math.abs(distance) && Math.abs(Database.getInstance().getValue(Value.LEFT_ENCODER_VELOCITY)) < .4
		|| averageEncoderVelocity() < 0.01;
	}
	
	protected void end() {
    	Robot.driveTrain.drive(0, 0);
    }

    private double averageEncoderVelocity(){
    	double sum = 0;
    	for(int i = 0; i < 50; i++){
    		sum += pastEncoderVelocities.get(i);
    	}
    	return sum/50;
    }
    
    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
    
}