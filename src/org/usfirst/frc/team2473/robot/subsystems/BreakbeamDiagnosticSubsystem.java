package org.usfirst.frc.team2473.robot.subsystems;

import org.usfirst.frc.team2473.robot.RobotMap;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Subsystem;

public class BreakbeamDiagnosticSubsystem extends Subsystem {

	private DigitalInput breakBeam; //breakbeam object
	
	public BreakbeamDiagnosticSubsystem() {
		breakBeam = new DigitalInput(RobotMap.breakBeam); //creation of the object in constructor
	}
	
	@Override
	protected void initDefaultCommand() {

	}

	//the following approach has been employed to accomodate for ThreadingRobot v1.0; v2.0 provides more support for non-numerical data structures
	public double getBreakbeamValue() {
		if(breakBeam.get()) { //returns 1 if the breakbeam is true
			return 1;
		} else {
			return 2; //retusn 2 if the breakbeam is false
		}
	}
}
