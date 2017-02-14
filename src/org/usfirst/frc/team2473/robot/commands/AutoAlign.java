package org.usfirst.frc.team2473.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.TimedCommand;
import java.util.function.DoubleSupplier;

import org.usfirst.frc.team2473.robot.MathUtil;

/**
 *
 */
public class AutoAlign extends CommandGroup {

    public AutoAlign() {
        DoubleSupplier turnOne = () -> MathUtil.getTurnOne();
    	DoubleSupplier distanceOne = () -> MathUtil.getDistanceOne();
    	DoubleSupplier turnTwo = () -> MathUtil.getTurnTwo();
    	DoubleSupplier distanceTwo = () -> MathUtil.getDistanceTwo();
    	
    	addSequential(new Network());
    	addSequential(new Turn(turnOne));
    	addSequential(new DriveStraightForward(distanceOne));
    	addSequential(new Turn(turnTwo));
    	addSequential(new DriveStraightForward(distanceTwo));
    }
}
