package org.usfirst.frc.team2473.robot.subsystems;

import org.usfirst.frc.team2473.robot.RobotMap;
import org.usfirst.frc.team2473.robot.commands.Climber;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.CANSpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;

public class ClimberSystem extends Subsystem{

	private CANSpeedController ropeCAN;
	
	public ClimberSystem() {
		super();
		
		ropeCAN = new CANTalon(RobotMap.ropeClimbMotor);
		//TODO Double check if this function actually resets encoder value, not written in API
		ropeCAN.reset();
	}
	
	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new Climber());
	}
	
	
	public void climb(double value){
		ropeCAN.set(value);
	}
	
	public double getEncValue(){
		return ropeCAN.getPosition();
	}
}
