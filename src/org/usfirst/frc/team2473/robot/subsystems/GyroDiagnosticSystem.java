package org.usfirst.frc.team2473.robot.subsystems;

import org.usfirst.frc.team2473.robot.RobotMap;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.command.Subsystem;

public class GyroDiagnosticSystem extends Subsystem {
	AnalogGyro gyro;
	
	public GyroDiagnosticSystem() {
		gyro = new AnalogGyro(RobotMap.gyro);
	}
	
    public void initDefaultCommand() {
        
    }
    
    public double getValue() {
    	return gyro.getAngle();
    }
    
    public void reset() {
    	gyro.reset();
    }
}

