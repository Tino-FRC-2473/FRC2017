package org.usfirst.frc.team2473.robot.subsystems;

import org.usfirst.frc.team2473.robot.Database;
import org.usfirst.frc.team2473.robot.Robot;
import edu.wpi.first.wpilibj.command.Subsystem;

public class GyroDiagnosticSystem extends Subsystem {
    public void initDefaultCommand() {
        
    }
    
    public double getValue() {
    	return Database.getInstance().getValue(Database.Value.GYRO_POSITION);
    }
    
    public void reset() {
    	Robot.gyro.reset();
    }
}

