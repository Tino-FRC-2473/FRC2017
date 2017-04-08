package org.usfirst.frc.team2473.robot.commands;

import org.usfirst.frc.team2473.robot.Database;
import org.usfirst.frc.team2473.robot.Robot;
import org.usfirst.frc.team2473.robot.Database.Value;

import edu.wpi.first.wpilibj.command.Command;

public class SweepCommand extends Command {

	boolean done, right;

	int counter = 0;

	double startingGyro, degrees;

	public SweepCommand(boolean right, double degrees) {
		this.right = right;
		this.degrees = degrees;
		startingGyro = Database.getInstance().getValue(Value.GYRO_POSITION);
		requires(Robot.driveTrain);
	}

	@Override
	public void execute() {
		Robot.driveTrain.driveArcade(0, 0.5);
		counter++;
	}

	@Override
	protected boolean isFinished() {
		return Database.getInstance().getValue(Value.CV_SUCCESS) == 1
				|| Math.abs(Database.getInstance().getValue(Value.GYRO_POSITION) - startingGyro) > degrees;
	}

}
