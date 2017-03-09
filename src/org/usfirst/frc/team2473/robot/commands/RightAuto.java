package org.usfirst.frc.team2473.robot.commands;

import java.util.function.DoubleSupplier;

import org.usfirst.frc.team2473.robot.MathUtil;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.TimedCommand;

public class RightAuto extends CommandGroup {

    public RightAuto() {
    	
    	addSequential(new DriveStraightForward(50));
    	addSequential(new Turn(-60));
    	addSequential(new DriveStraightForward(-10));
    	addSequential(new AutoAlign());
    	addSequential(new WaitForBreak());
    	addSequential(new DriveStraightForward(-60));
    	addSequential(new Turn(40));
    	addSequential(new DriveStraightForward(50));
    }
}