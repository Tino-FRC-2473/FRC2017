package org.usfirst.frc.team2473.robot.subsystems;

import org.usfirst.frc.team2473.robot.RobotMap;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Subsystem;

public class ClimberSystem extends Subsystem{

	private CANTalon ropeCAN_1;
	private CANTalon ropeCAN_2;
	private DigitalInput climberLS;
	
	public ClimberSystem() {
		super();
		
		ropeCAN_1 = new CANTalon(RobotMap.ropeClimbMotor_1);
		ropeCAN_2 = new CANTalon(RobotMap.ropeClimbMotor_2);
		climberLS = new DigitalInput(RobotMap.climberLS);
		//TODO Double check if this function actually resets encoder value, not written in API
		//ropeCAN.reset();
	}
	
	@Override
	protected void initDefaultCommand() {
		//setDefaultCommand(new Climber());
	}
	
	
	public void climb(double value){
		ropeCAN_1.changeControlMode(TalonControlMode.PercentVbus);
		ropeCAN_1.set(value);
	}
	
	public void climb2(double value){
		ropeCAN_2.changeControlMode(TalonControlMode.PercentVbus);
		ropeCAN_2.set(value);
	}
	
	public double getEncValue(){
		return ropeCAN_1.getPosition();
	}
	
	public boolean getLimitSwitch(){
		return climberLS.get();
	}
}
