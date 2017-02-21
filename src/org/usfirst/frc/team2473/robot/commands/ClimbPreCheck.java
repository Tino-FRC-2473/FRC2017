package org.usfirst.frc.team2473.robot.commands;

import java.util.ArrayList;

import org.usfirst.frc.team2473.robot.Database;
import org.usfirst.frc.team2473.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class Climb2 extends Command {

	private int numValues;
	
	private boolean finished;
	
	private ArrayList<Double> currentList;
	
    public ClimbPreCheck() {
       requires(Robot.climbSystem)
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	numValues = 4;
    	finished = false;
    	currentList = new ArrayList<Double>();
    	
    	Robot.climbSystem.climb(0.3);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	currentList.add(Robot.climbSystem.getCurrent());
    	
    	if(currentList.size() == numValues){
    		double sum = 0;
    		for(int i=0; i<numValues; i++){
    			sum += currentList.get(i);
    		}
    		if(sum / numValues > 9){
    			Climb.ratchetCorrect = false;
    		}else{
    			Climb.ratchetCorrect = true;
    		}
    		finished = true;
		}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return finished;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.climbSystem.climb(0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
