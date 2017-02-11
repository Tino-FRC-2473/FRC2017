package org.usfirst.frc.team2473.robot.commands;

import org.usfirst.frc.team2473.robot.Database;
import org.usfirst.frc.team2473.robot.Database.Value;
import org.usfirst.frc.team2473.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class EncoderTest extends Command {

	public EncoderTest() {
		requires(Robot.driveTrain);
	}
	
	@Override
	public void execute() {
		System.out.println("Encoder 2: " + Database.getInstance().getValue(Value.TWO_ENCODER));
		System.out.println("Encoder 3: " + Database.getInstance().getValue(Value.THREE_ENCODER));
		System.out.println("Encoder 4: " + Database.getInstance().getValue(Value.FOUR_ENCODER));
		System.out.println("Encoder 5: " + Database.getInstance().getValue(Value.FIVE_ENCODER));
	}
	
	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}

}
