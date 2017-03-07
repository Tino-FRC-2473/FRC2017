package org.usfirst.frc.team2473.robot.commands;

import org.usfirst.frc.team2473.robot.Database;
import org.usfirst.frc.team2473.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/*
 *	Front limit switch refers to the limit switch initially pressed.
 *	Back limit switch is the other one.
 */
public class ActiveGear extends Command {
	
	private boolean lookingForHallEffect;
	
	/*
	 * True represents section between hall effect sensor and front limit switch.
	 * False represents section between hall effect sensor and back limit switch.
	 */
	private boolean waitingForVishal;
	
	private double powerMagnitude;
	
	/*
	 * Positive represents moving from front to back limit switch.
	 * Negative represents moving from back to front limit switch.
	 */
	private double direction;

    public ActiveGear() {
    	requires(Robot.AGSystem);
    	
    	lookingForHallEffect = false;
    	waitingForVishal = true;
    	
    	powerMagnitude = 0.2;
    	direction = 1;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if(!Robot.AGSystem.getFrontLS() && Database.getInstance().getButton(Database.ButtonName.AGForward).get()){
    		direction = 1;
    	}else if(!Robot.AGSystem.getBackLS() && Database.getInstance().getButton(Database.ButtonName.AGBackward).get()){
    		direction = -1;
    	}else if(!Robot.AGSystem.getHallEffect() && Database.getInstance().getButton(Database.ButtonName.AGHalfway).get()){
    		
    		//If currently pressing front limit switch, move backwards
    		//Else if currently pressing back limit switch, move forwards
    		//Else, already moving.
    		if(Robot.AGSystem.getFrontLS()){
    			direction = -1;
    		}else if(Robot.AGSystem.getBackLS()){
    			direction = 1;
    		}else{
    			if(waitingForVishal){
    				direction = 1;
    			}else{
    				direction = -1;
    			}
    		}
    		
    		lookingForHallEffect = true;
    	}
    	
    	Robot.AGSystem.moveArm(direction * powerMagnitude);
    	
    	if(Robot.AGSystem.getFrontLS() || Robot.AGSystem.getBackLS()){
    		Robot.AGSystem.moveArm(0);
    	}else if(lookingForHallEffect && Robot.AGSystem.getHallEffect()){
    		Robot.AGSystem.moveArm(0);
    		lookingForHallEffect = false;
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.AGSystem.moveArm(0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
