package org.usfirst.frc.team2473.robot.subsystems;

import org.usfirst.frc.team2473.robot.RobotMap;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.command.Subsystem;

public class ClimberSystem extends Subsystem {

	private CANTalon ropeCAN_1;
	private CANTalon ropeCAN_2;

	private double initEncValue; // Starting encoder value on climb motor 1

	//private FileWriter writer;

	public ClimberSystem() {
		super();

		ropeCAN_1 = new CANTalon(RobotMap.ropeClimbMotor_1);
		ropeCAN_2 = new CANTalon(RobotMap.ropeClimbMotor_2);

		resetEncoder();
		/*
		 * NOT WORKING
		try {
			writer = (new FileWriter(new File("Valuer_LOLOLOLOL_Log.txt")));
			System.out.println("Instantialized");
			writer.write((LocalDateTime.now()+"\n"));
			writer.write("\n");
		} catch (IOException e) {
			// do something
			System.out.println("Not Working");
			e.printStackTrace();
		}
		*/
	}

	@Override
	protected void initDefaultCommand() {
		// setDefaultCommand(new Climber());
	}

	public void climb(double value) {
		ropeCAN_1.changeControlMode(TalonControlMode.PercentVbus);
		ropeCAN_1.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		ropeCAN_1.set(value);

		ropeCAN_2.changeControlMode(TalonControlMode.PercentVbus);
		ropeCAN_2.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		ropeCAN_2.set(value);
	}

	public double getEncValue() {
		return ropeCAN_2.getEncPosition() - initEncValue;
	}

	public double getCurrent() {
		return ropeCAN_1.getOutputCurrent();
	}
	
	public void resetEncoder(){
		initEncValue = ropeCAN_1.getEncPosition();
	}
	
	/*
	 * Unused
	public void log(String message){
		System.out.println(message);
		try {
			writer.write(message+"\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Done");
	}
	public void close(){
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	*/
	
}
