package org.usfirst.frc.team2473.robot.subsystems;

import org.usfirst.frc.team2473.robot.RobotMap;
import org.usfirst.frc.team2473.robot.commands.Climber;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;

public class ClimberSystem extends Subsystem{

	private SpeedController ropeCAN;
	
	public ClimberSystem() {
		super();
		
		ropeCAN = new CANTalon(RobotMap.ropeClimbMotor);
	}
	
	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new Climber());
	}
	
	
	public void climb(double value){
		ropeCAN.set(value);
	}
}
