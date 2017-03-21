package org.usfirst.frc.team2473.robot.commands;

import org.usfirst.frc.team2473.robot.Database;
import org.usfirst.frc.team2473.robot.Database.Value;

import edu.wpi.first.wpilibj.command.Command;

public class WaitForBreak extends Command{

	@Override
	protected boolean isFinished() {
		return Database.getInstance().getValue(Value.BREAK_BEAM) == 1;
	}

}
