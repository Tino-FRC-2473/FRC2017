package org.usfirst.frc.team2473.robot.commands;

import org.usfirst.frc.team2473.robot.Database;
import org.usfirst.frc.team2473.robot.Robot;
import org.usfirst.frc.team2473.robot.Database.Value;

import edu.wpi.first.wpilibj.command.Command;

public class Turn extends Command{
	private double bearing;
	
	private static final double KP = .1;
	private static final double KI = .003;
	private static final double KD = 0;
	
	private double integral;
	private double lastProportion;
	
	public Turn(double bearing){
		requires(Robot.driveTrain);
		
		this.bearing = bearing;
	}
	
	protected void initialize() {
    	Robot.sensorThread.resetEncoders();
    	Robot.sensorThread.resetGyro();
    	
    	integral = 0;
    	lastProportion = 0;
    	
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	double proportion = Database.getInstance().getValue(Value.GYRO);
    	integral += proportion;
    	double derivative = proportion - lastProportion;
    	double rotate = KP * proportion + KI*integral + KD*derivative;
    	
    	if(Math.abs(rotate) > .70){
    		rotate = Math.signum(rotate) * .7;
    	}
    	
    	Robot.driveTrain.driveArcade(0, rotate);
    	
    	lastProportion = proportion;
    }

	protected boolean isFinished() {
		return Math.abs(Database.getInstance().getValue(Value.GYRO) - bearing) < 2 ;
	}
	
	protected void end() {
    	Robot.driveTrain.drive(0, 0);
    }
	
}
