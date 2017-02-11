package org.usfirst.frc.team2473.robot.subsystems;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

import org.usfirst.frc.team2473.robot.RobotMap;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Subsystem;

public class ClimberSystem extends Subsystem{

	private CANTalon ropeCAN_1;
	private CANTalon ropeCAN_2;
	private DigitalInput climberLS;
	
	private PrintWriter writer;
	
	public ClimberSystem() {
		super();
		
		ropeCAN_1 = new CANTalon(RobotMap.ropeClimbMotor_1);
		ropeCAN_2 = new CANTalon(RobotMap.ropeClimbMotor_2);
		climberLS = new DigitalInput(RobotMap.climberLS);
		try{
		    writer = new PrintWriter(new FileWriter(new File("Value_Log.txt"), true));
		    writer.println(LocalDateTime.now());
		    writer.println();
		} catch (IOException e) {
		   // do something
		}
	}
	
	@Override
	protected void initDefaultCommand() {
		//setDefaultCommand(new Climber());
	}
	
	
	public void climb(double value){
		ropeCAN_1.changeControlMode(TalonControlMode.PercentVbus);
		ropeCAN_1.set(value);
		
		ropeCAN_2.changeControlMode(TalonControlMode.PercentVbus);
		ropeCAN_2.set(value);
	}
	
	public double getEncValue(){
		return ropeCAN_1.getPosition();
	}
	
	public double getCurrent(){
		return ropeCAN_1.getOutputCurrent();
	}
	
	public boolean getLimitSwitch(){
		return climberLS.get();
	}
	
	public void log(String message){
		writer.println(message);
	}
	
	public void close(){
		writer.println();
		writer.close();
	}
}
