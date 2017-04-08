package org.usfirst.frc.team2473.robot.subsystems;

import org.usfirst.frc.team2473.robot.Database;
import org.usfirst.frc.team2473.robot.RobotMap;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Subsystem;

public class BreakbeamDiagnosticSubsystem extends Subsystem {
	
	public BreakbeamDiagnosticSubsystem() {
	}
	
	@Override
	protected void initDefaultCommand() {

	}

	public double getBreakbeamValue() {
		if(Database.getInstance().getValue(Database.Value.BREAK_BEAM) == 1) { 
			return 1;
		} else {
			return 0;
		}
	}
}
