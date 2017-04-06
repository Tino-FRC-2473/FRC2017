package org.usfirst.frc.team2473.robot.commands;


import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.TimedCommand;

public class CenterAuto extends CommandGroup {

    public CenterAuto() {
    	
    	addSequential(new DriveStraightForward(46));
    	addSequential(new AutoAlignCenter());
//    	addSequential(new WaitForBreak());
//    	addSequential(new TimedCommand(1));
//    	addSequential(new DriveForwardWithoutPid(-40));
//    	addSequential(new TimedCommand(.2));
//    	addSequential(new Turn(-90));
//    	addSequential(new TimedCommand(.2));
//    	addSequential(new DriveForwardWithoutPid(84));
//    	addSequential(new TimedCommand(.2));
//    	addSequential(new Turn(90));
//    	addSequential(new TimedCommand(.2));
//    	addSequential(new DriveForwardWithoutPid(150));
    }
}