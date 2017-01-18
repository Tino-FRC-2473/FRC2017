/**
 * 
 */
package org.usfirst.frc.team2473.robot.commands;

import org.usfirst.frc.team2473.robot.Database;
import org.usfirst.frc.team2473.robot.Networking;
import org.usfirst.frc.team2473.robot.Database.Value;

import edu.wpi.first.wpilibj.command.Command;

/**
 * @author RehanDurrani
 *
 */
public class Network extends Command {
	private Networking n = null;
	private double times;
	
	@Override
	protected void initialize(){
		n = Networking.getInstance();
		times = Database.getInstance().getValue(Value.CV_TIME_STAMP);
		n.notify();
	}
	@Override
	protected void end() {
	}
	
	@Override
	protected void interrupted() {
		end();
	}

	@Override
	protected boolean isFinished() {
		return times != Database.getInstance().getValue(Value.CV_TIME_STAMP);
	}
	
	@Override
	protected void execute(){
	
	}

}
