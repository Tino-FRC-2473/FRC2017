package org.usfirst.frc.team2473.robot.subsystems;

import java.util.ArrayList;
import java.util.Collections;

import org.usfirst.frc.team2473.robot.Database;
import org.usfirst.frc.team2473.robot.RobotMap;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.command.Subsystem;

public class DriveTrainDiagnostic extends Subsystem {
	public CANTalon motor_fr, motor_fl, motor_br, motor_bl;

	public DriveTrainDiagnostic() {
		motor_fr = new CANTalon(RobotMap.rightFrontMotor);
		motor_fl = new CANTalon(RobotMap.leftFrontMotor);
		motor_br = new CANTalon(RobotMap.rightBackMotor);
		motor_bl = new CANTalon(RobotMap.leftBackMotor);
	}

	public void initDefaultCommand() {

	}

	public double getEncPosition(String motor) {
		switch (motor) {
		case "fr":
			return motor_fr.getEncPosition();
		case "fl":
			return motor_fl.getEncPosition();
		case "br":
			return motor_br.getEncPosition();
		case "bl":
			return motor_bl.getEncPosition();
		default:
			return -1;
		}
	}

	public void runMotors(double pow) { //run all 4 motors
		motor_fr.set(pow);
		motor_fl.set(pow);
		motor_br.set(pow);
		motor_bl.set(pow);
	}

	public void turnLeft(double pow) { // turn the drive train to the left
		motor_fr.set(pow);
		motor_fl.set(pow);
		motor_br.set(pow);
		motor_bl.set(pow);
	}

	public void turnRight(double pow) { // turn the drive train to the right
		motor_fr.set(-pow);
		motor_fl.set(-pow);
		motor_br.set(-pow);
		motor_bl.set(-pow);
	}

	public void stop() { // stop the drive train motors
		motor_fr.set(0);
		motor_fl.set(0);
		motor_br.set(0);
		motor_bl.set(0);
	}
	
	public double diff() {
		//add the encoder values to a list if they are > 0
		ArrayList<Double> list = new ArrayList<>();
		if(Database.getInstance().getValue(Database.Value.LEFT_ENCODER_POSITION) > 0) {
			list.add(Database.getInstance().getValue(Database.Value.LEFT_ENCODER_POSITION));
		}
		if(Database.getInstance().getValue(Database.Value.RIGHT_ENCODER_POSITION) > 0) {
			list.add(Database.getInstance().getValue(Database.Value.RIGHT_ENCODER_POSITION));
		}
		System.out.println(list); //print list for testing purposes
		Collections.sort(list); //sort the list
		System.out.println(list); //print again for testing
		double returner = list.get(list.size() - 1) - list.get(0); //difference between largest and smallest entities in list
		System.out.println("Encoder difference: " + returner); //print the difference
		return returner; //return the difference
	}
}
