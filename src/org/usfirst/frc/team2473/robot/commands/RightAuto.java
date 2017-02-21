package org.usfirst.frc.team2473.robot.commands;

import java.util.function.DoubleSupplier;

import org.usfirst.frc.team2473.robot.MathUtil;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.TimedCommand;

public class RightAuto extends CommandGroup {

    public RightAuto() {
    	
    	addSequential(new DriveStraightForward(78));
    	addSequential(new Turn(-70));
    	addSequential(new AutoAlign());
    }
}