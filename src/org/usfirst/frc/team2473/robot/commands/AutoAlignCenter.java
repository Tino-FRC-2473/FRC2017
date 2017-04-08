package org.usfirst.frc.team2473.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.TimedCommand;
import java.util.function.DoubleSupplier;

import org.usfirst.frc.team2473.robot.MathUtil;

/**
 *
 */
public class AutoAlignCenter extends CommandGroup {

    public AutoAlignCenter() {
        DoubleSupplier turnOne = () -> MathUtil.getTurnOne();
    	DoubleSupplier distanceOne = () -> MathUtil.getDistanceOne();
    	DoubleSupplier turnTwo = () -> MathUtil.getTurnTwo();
    	DoubleSupplier turnThree = () -> MathUtil.getTurnThree()-.8;
    	DoubleSupplier distanceTwo = () -> MathUtil.getDistanceTwo()/2.5;
    	DoubleSupplier distanceThree = () -> MathUtil.getDistanceTwo()+40;
    	
//    	addSequential(new Network());
//    	addSequential(new Turn(turnOne));
//    	addSequential(new TimedCommand(.1));
//    	addSequential(new DriveStraightForward(distanceOne));
//    	addSequential(new TimedCommand(.1));
//    	addSequential(new Turn(turnTwo));
//    	addSequential(new TimedCommand(.1));
//    	addSequential(new DriveStraightForward(-12));
    	addSequential(new Network());
    	
    	addSequential(new SweepCommand(false, 10));
    	addSequential(new Network());
    	
    	addSequential(new SweepCommand(true, 20));
    	addSequential(new Network());
    	
    	
    	addSequential(new Turn(turnThree));
    	addSequential(new TimedCommand(.2));
    	addSequential(new DriveStraightForwardSlowly(distanceThree));
    }
}
