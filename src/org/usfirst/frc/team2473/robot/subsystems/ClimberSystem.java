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
	
	/**
	 * This method will run the rope climbing motor given a value.
	 * Use positive values to climb up, 0 to stop, and negative values to climb down.
	 * 
	 * @param value Takes in doubles ranging from -1.0 to 1.0 (inclusive)
	 */
	public void climb(double value){
		ropeCAN.set(value);
	}
}
