package org.usfirst.frc.team2473.robot.subsystems;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import org.usfirst.frc.team2473.robot.RobotMap;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.command.Subsystem;

public class ClimberSystem extends Subsystem {

	private CANTalon ropeCAN_1;
	private CANTalon ropeCAN_2;

	private double initEncValue;		//Starting encoder value on climb motor 1
	
	private PrintWriter writer;

	public ClimberSystem() {
		super();

		ropeCAN_1 = new CANTalon(RobotMap.ropeClimbMotor_1);
		ropeCAN_2 = new CANTalon(RobotMap.ropeClimbMotor_2);

		initEncValue = ropeCAN_1.getEncPosition();
		
		try{
			writer = new PrintWriter(new FileWriter(new File("/home/lvuser/Output_Log.csv"), false));
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	protected void initDefaultCommand() {
		// setDefaultCommand(new Climber());
	}

	public void climb(double value) {
		ropeCAN_1.changeControlMode(TalonControlMode.PercentVbus);
		ropeCAN_1.set(value);

		ropeCAN_2.changeControlMode(TalonControlMode.PercentVbus);
		ropeCAN_2.set(value);
	}

	public double getEncValue() {
		return ropeCAN_1.getPosition() - initEncValue;
	}

	public double getCurrent() {
		return ropeCAN_1.getOutputCurrent();
	}
	
	public void log(String message){
		writer.println(message);
	}
	
	public void close(){
		writer.println();
		writer.close();
	}
}
