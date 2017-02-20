package org.usfirst.frc.team2473.robot.commands;

import java.util.function.DoubleSupplier;

import org.usfirst.frc.team2473.robot.Database;
import org.usfirst.frc.team2473.robot.Database.Value;
import org.usfirst.frc.team2473.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/*public class DriveStraightForward extends Command{
	private double distance;
	private static final double KPRotate = .2;
	private static final double KIRotate = .005;
	private static final double KDRotate = 0;
	
	private static final double KPForward = .6;
	private static final double KIForward = .02;
	private static final double KDForward = 0;
	
//	private static final double KStopingDistance = 14000;
	
	private double startingGyroValue;
	private double integralRotate;
	private double lastProportionRotate;
	
	private double integralForward;
	private double lastProportionForward;
	
//	private double lastEncoderValue;
//	private double lastTime;
	
	public DriveStraightForward(double distance){
		requires(Robot.driveTrain);
		
		this.distance = distance;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.sensorThread.resetEncoders();
    	
    	startingGyroValue = Database.getInstance().getValue(Value.GYRO);
    	integralRotate = 0;
    	lastProportionRotate = 0;
    	
    	integralForward = 0;
    	lastProportionForward = 0;
    	
    	//lastEncoderValue = 0;
    	//lastTime = System.currentTimeMillis();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	double proportionRotate = Database.getInstance().getValue(Value.GYRO) - startingGyroValue;
    	integralRotate += proportionRotate;
    	double derivativeRotate = proportionRotate - lastProportionRotate;
    	double rotate = KPRotate * proportionRotate + KIRotate*integralRotate + KDRotate*derivativeRotate;
    	
    	double proportionForward = (distance - Database.getInstance().getValue(Value.LEFT_ENCODER))/distance;
    	integralForward += proportionForward;
    	double derivativeForward = proportionForward - lastProportionForward;
    	double speed = KPForward * proportionForward + KIForward*integralForward + KDForward*derivativeForward;
    	
    	//lastEncoderValue = Database.getInstance().getValue(Value.LEFT_ENCODER) - lastEncoderValue;
    	//lastTime = System.currentTimeMillis() - lastTime;
    	
    	if(Math.abs(rotate) > .70){
    		rotate = Math.signum(rotate) * .7;
    	}
    	
    	if(Math.abs(speed) > .7){
    		speed = Math.signum(speed) * .7;
    	}
    	
    	//Robot.driveTrain.driveArcade(.05 + .45 * Math.min(1,((distance - Database.getInstance().getValue(Value.RIGHT_ENCODER))) / ((distance < 8)?distance*7/8:Math.min(10, 6.5 + (distance-10)/5))), rotate);
    	Robot.driveTrain.driveArcade(speed,rotate);
    	
    	integralForward *= .98;
    	lastProportionRotate = proportionRotate;
    	lastProportionForward = proportionForward;
	
    }

	protected boolean isFinished() {
		return Math.abs(Database.getInstance().getValue(Value.RIGHT_ENCODER)) > distance - 1;
	}
	
	protected void end() {
    	Robot.driveTrain.drive(0, 0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}*/

/*public class DriveStraightForward extends Command{
	private double distance;
	private static final double KPRotate = .2;
	private static final double KIRotate = .005;
	private static final double KDRotate = 0;
	
	private static final double KPForward = .05;
	private static final double KIForward = .001;
	private static final double KDForward = .15;
	
//	private static final double KStopingDistance = 14000;
	
	private double startingGyroValue;
	private double integralRotate;
	private double lastProportionRotate;
	
	private double integralForward;
	private double lastProportionForward;
	
//	private double lastEncoderValue;
//	private double lastTime;
	
	public DriveStraightForward(double distance){
		requires(Robot.driveTrain);
		
		this.distance = distance;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.sensorThread.resetEncoders();
    	
    	startingGyroValue = Database.getInstance().getValue(Value.GYRO_POSITION);
    	integralRotate = 0;
    	lastProportionRotate = 0;
    	
    	integralForward = 0;
    	lastProportionForward = 0;
    	
    	//lastEncoderValue = 0;
    	//lastTime = System.currentTimeMillis();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	double proportionRotate = Database.getInstance().getValue(Value.GYRO_POSITION) - startingGyroValue;
    	integralRotate += proportionRotate;
    	double derivativeRotate = proportionRotate - lastProportionRotate;
    	double rotate = KPRotate * proportionRotate + KIRotate*integralRotate + KDRotate*derivativeRotate;
    	
    	double proportionForward = distance - Database.getInstance().getValue(Value.LEFT_ENCODER);
    	integralForward += proportionForward;
    	double derivativeForward = proportionForward - lastProportionForward;
    	double speed = KPForward * proportionForward + KIForward*integralForward + KDForward*derivativeForward;
    	
    	//lastEncoderValue = Database.getInstance().getValue(Value.LEFT_ENCODER) - lastEncoderValue;
    	//lastTime = System.currentTimeMillis() - lastTime;
    	
    	if(Math.abs(rotate) > .70){
    		rotate = Math.signum(rotate) * .7;
    	}
    	
    	if(Math.abs(speed) > .50){
    		speed = Math.signum(speed) * .50;
    	}
    	
    	//Robot.driveTrain.driveArcade(.05 + .45 * Math.min(1,((distance - Database.getInstance().getValue(Value.RIGHT_ENCODER))) / ((distance < 8)?distance*7/8:Math.min(10, 6.5 + (distance-10)/5))), rotate);
    	Robot.driveTrain.driveArcade(speed,rotate);
    	
    	lastProportionRotate = proportionRotate;
    	lastProportionForward = proportionForward;
    	
	
    }

	protected boolean isFinished() {
		return Math.abs(Database.getInstance().getValue(Value.RIGHT_ENCODER)) > distance - Math.min(6, distance/8); //- Math.pow(lastEncoderValue/lastTime,2) * KStopingDistance;
	}
	
	protected void end() {
    	Robot.driveTrain.drive(0, 0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}*/

public class DriveStraightForward extends Command{
	private double distance;
	private DoubleSupplier distanceSupplier;
	private static final double KPRotate = .2;
	private static final double KIRotate = .005;
	private static final double KDRotate = 0;
	
	private static final double KPForward = .3;
	private static final double KIForward = .0;
	private static final double KDForward = 0;
	
	private double startingGyroValue;
	private double integralRotate;
	private double lastProportionRotate;
	
	private double integralForward;
	private double lastProportionForward;
	
	private double lastEncoderValue;
	private double lastTime;
	
	public DriveStraightForward(double distance){
		requires(Robot.driveTrain);
		
		this.distance = distance;
    }
	
	public DriveStraightForward(DoubleSupplier distanceSupplier){
		requires(Robot.driveTrain);
		
		this.distanceSupplier = distanceSupplier;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.sensorThread.resetEncoders();
    	
    	if(distanceSupplier != null){
    		this.distance = distanceSupplier.getAsDouble();
    	}
    	
    	startingGyroValue = Database.getInstance().getValue(Value.GYRO_POSITION);
    	integralRotate = 0;
    	lastProportionRotate = 0;
    	
    	integralForward = 0;
    	lastProportionForward = 0;
    	
    	lastEncoderValue = 0;
    	lastTime = System.currentTimeMillis();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	double proportionRotate = Database.getInstance().getValue(Value.GYRO_POSITION) - startingGyroValue;
    	integralRotate += proportionRotate;
    	double derivativeRotate = proportionRotate - lastProportionRotate;
    	double rotate = KPRotate * proportionRotate + KIRotate*integralRotate + KDRotate*derivativeRotate;
    	
    	double proportionForward = (distance - Database.getInstance().getValue(Value.LEFT_ENCODER))/Math.abs(distance);
    	integralForward += proportionForward;
    	double derivativeForward = proportionForward - lastProportionForward;
    	double speed = KPForward * proportionForward + KIForward*integralForward + KDForward*derivativeForward;
    	
    	lastEncoderValue = Database.getInstance().getValue(Value.LEFT_ENCODER) - lastEncoderValue;
    	lastTime = System.currentTimeMillis() - lastTime;
    	
    	if(Math.abs(rotate) > .70){
    		rotate = Math.signum(rotate) * .7;
    	}
    	
    	speed = Math.signum(speed)*(Math.abs(speed) + .4);
    	
    	if(Math.abs(speed) > .70){
    		speed = Math.signum(speed) * .7;
    	}
    	
    	Robot.driveTrain.driveArcade(speed,rotate);
    	
    	lastProportionRotate = proportionRotate;
    	lastProportionForward = proportionForward;
    	
	
    }

	protected boolean isFinished() {
		return Math.abs(Database.getInstance().getValue(Value.LEFT_ENCODER) - distance) < 1 && Math.abs(lastEncoderValue/lastTime) < 10;
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