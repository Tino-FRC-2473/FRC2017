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
	public Network(){
		n = Networking.getInstance();
	}
	@Override
	protected void initialize(){
		times = Database.getInstance().getValue(Value.CV_TIME_STAMP);
		synchronized(n){
		n.notify();}
	}
	@Override
	protected void end() {
		System.out.println("CV Values acquired");
		System.out.println("A = "+ Database.getInstance().getValue(Value.CV_ANGLE_A));
		System.out.println("Bearing = "+Database.getInstance().getValue(Value.CV_BEARING));
		System.out.println("Distance = " +Database.getInstance().getValue(Value.CV_DISTANCE));
		System.out.println("L&R = " + Database.getInstance().getValue(Value.CV_L_OR_R));
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
