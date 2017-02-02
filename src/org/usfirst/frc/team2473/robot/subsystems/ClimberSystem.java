package org.usfirst.frc.team2473.robot.subsystems;

import org.usfirst.frc.team2473.robot.RobotMap;
import org.usfirst.frc.team2473.robot.commands.Climber;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.CANSpeedController;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Subsystem;

public class ClimberSystem extends Subsystem{

	private CANTalon ropeCAN;
	private DigitalInput climberLS;
	
	public ClimberSystem() {
		super();
		
		ropeCAN = new CANTalon(RobotMap.ropeClimbMotor);
		climberLS = new DigitalInput(RobotMap.climberLS);
		//TODO Double check if this function actually resets encoder value, not written in API
		//ropeCAN.reset();
	}
	
	@Override
	protected void initDefaultCommand() {
		//setDefaultCommand(new Climber());
	}
	
	
	public void climb(double value){
		ropeCAN.changeControlMode(TalonControlMode.PercentVbus);
		ropeCAN.set(value);
	}
	
	public double getEncValue(){
		return ropeCAN.getPosition();
	}
	
	public boolean getLimitSwitch(){
		return climberLS.get();
	}
}
