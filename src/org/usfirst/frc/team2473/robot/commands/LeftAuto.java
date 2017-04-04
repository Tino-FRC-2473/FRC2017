package org.usfirst.frc.team2473.robot.commands;

import java.util.function.DoubleSupplier;

import org.usfirst.frc.team2473.robot.MathUtil;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.TimedCommand;

public class LeftAuto extends CommandGroup {

    public LeftAuto() {
    	
    	addSequential(new DriveStraightForward(97));
    	addSequential(new TimedCommand(.2));
    	addSequential(new Turn(60));
    	addSequential(new TimedCommand(.2));
    	addSequential(new DriveStraightForward(-4));
    	addSequential(new AutoAlignCenter());
//    	addSequential(new WaitForBreak());
//    	addSequential(new TimedCommand(1.5));
//    	addSequential(new DriveForwardWithoutPid(-55));
//    	addSequential(new TimedCommand(.2));
//    	addSequential(new Turn(-40));
//    	addSequential(new TimedCommand(.2));
//    	addSequential(new DriveForwardWithoutPid(85));
    }
    
}