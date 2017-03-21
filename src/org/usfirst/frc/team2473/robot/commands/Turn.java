package org.usfirst.frc.team2473.robot.commands;

import java.util.function.DoubleSupplier;

import org.usfirst.frc.team2473.robot.Database;
import org.usfirst.frc.team2473.robot.Robot;
import org.usfirst.frc.team2473.robot.Database.Value;

import edu.wpi.first.wpilibj.command.Command;

public class Turn extends Command{
	private double bearing;
	private DoubleSupplier bearingSupplier;
	
	private static final double KP = .30;
	private static final double KI = 0;//.002;
	private static final double KD = 0;
	
	private double startingGyroValue;
	private double integral;
	private double lastProportion;
	
	public Turn(double bearing){
		requires(Robot.driveTrain);
		
		this.bearing = bearing;
	}
	
	public Turn(DoubleSupplier bearingSupplier){
		requires(Robot.driveTrain);
		
		this.bearingSupplier = bearingSupplier;
	}
	
	protected void initialize() {
    	Robot.sensorThread.resetEncoders();
    	//Robot.sensorThread.resetGyro();
    	if(bearingSupplier != null){
    		this.bearing = bearingSupplier.getAsDouble();
    	}
    	
    	startingGyroValue = Database.getInstance().getValue(Value.GYRO_POSITION);
    	
    	integral = 0;
    	lastProportion = 0;
    	
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	double proportion = ((Database.getInstance().getValue(Value.GYRO_POSITION) - startingGyroValue) - bearing)/(Math.abs(bearing)+8);
    	integral += proportion;
    	double derivative = proportion - lastProportion;
    	double rotate = KP * proportion + KI*integral + KD*derivative;
    	
    	//Base speed of turning
    	rotate = Math.signum(rotate)*(Math.abs(rotate) + .55);
    	
    	if(Math.abs(rotate) > .90){
    		rotate = Math.signum(rotate) * .9;
    	}
    	
    	Robot.driveTrain.driveArcade(0, rotate);
    	
    	lastProportion = proportion;
    }

	protected boolean isFinished() {
		return Math.abs(Database.getInstance().getValue(Value.GYRO_POSITION) - startingGyroValue - bearing) < 2 && Math.abs(Database.getInstance().getValue(Value.GYRO_VELOCITY)) < 40;
	}
	
	protected void end() {
    	Robot.driveTrain.drive(0, 0);
    }
	
	
	
}

//public class Turn extends Command{
//	private double bearing;
//	
//	private static final double KP = 0;
//	private static final double KI = .05;//.002;
//	private static final double KD = 0;
//	
//	private double startingGyroValue;
//	private double integral;
//	private double lastProportion;
//	
//	public Turn(double bearing){
//		requires(Robot.driveTrain);
//		
//		this.bearing = bearing;
//	}
//	
//	protected void initialize() {
//    	Robot.sensorThread.resetEncoders();
//    	//Robot.sensorThread.resetGyro();
//    	startingGyroValue = Database.getInstance().getValue(Value.GYRO_POSITION);
//    	
//    	integral = 0;
//    	lastProportion = 0;
//    	
//    }
//
//    // Called repeatedly when this Command is scheduled to run
//    protected void execute() {
//    	double gyroVelocity = Database.getInstance().getValue(Value.GYRO_VELOCITY);
//    	double proportion = Math.signum(gyroVelocity)*(90 - Math.abs(gyroVelocity));
//    	integral += proportion;
//    	double derivative = proportion - lastProportion;
//    	double rotate = KP * proportion + KI*integral + KD*derivative;
//    	
//    	if(Math.abs(rotate) > .90){
//    		rotate = Math.signum(rotate) * .9;
//    	}
//    	
//    	Robot.driveTrain.driveArcade(0, rotate);
//    	
//    	lastProportion = proportion;
//    }
//
//	protected boolean isFinished() {
//		return Math.abs(Database.getInstance().getValue(Value.GYRO_POSITION) - startingGyroValue - bearing) < 3 ;
//	}
//	
//	protected void end() {
//    	Robot.driveTrain.drive(0, 0);
//    }
//	
//}
