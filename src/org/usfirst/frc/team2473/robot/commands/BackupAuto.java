package org.usfirst.frc.team2473.robot.commands;

import org.usfirst.frc.team2473.robot.Database;
import org.usfirst.frc.team2473.robot.Database.Value;
import org.usfirst.frc.team2473.robot.RobotMap;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.TimedCommand;

/**
 *
 */
public class BackupAuto extends CommandGroup {

    public BackupAuto() {
        // Add Commands here:
        // e.g. addSequential(new Command1());
        //      addSequential(new Command2());
    	
    	
        // these will run in order.

        // To run multiple commands at the same time,
        // use addParallel()
        // e.g. addParallel(new Command1());
        //      addSequential(new Command2());
        // Command1 and Command2 will run in parallel.

        // A command group will require all of the subsystems that each member
        // would require.
        // e.g. if Command1 requires chassis, and Command2 requires arm,
        // a CommandGroup containing them would require both the chassis and the
        // arm.
    	
    	//what alliance we are, determined by the switch. Off is red, on is blue
    	boolean blue = new DigitalInput(RobotMap.firstSwitch).get();
    	
    	
    	addSequential(new DriveStraightForward(48));
    	
    	//waits either 4 seconds or until the breakbeam is broken
    	addSequential(new TimedCommand(4) {
    		@Override
    		protected boolean isFinished() {
    			return super.isFinished() || ! ((Database.getInstance().getValue(Value.BREAK_BEAM) == 0? false :true));
    		}
    	});
    	addSequential(new TimedCommand(2));
    	addSequential(new DriveStraightForward(-12));
    	//positive = clockwise
    	addSequential(new Turn( blue? 90 : -90));
    	addSequential(new DriveStraightForward(84));
    	addSequential(new Turn(blue ? -90 : 90));
    	addSequential(new DriveStraightForward(240));
    	
    	
    	
    }
}
