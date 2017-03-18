package org.usfirst.frc.team2473.robot.commands;

import org.usfirst.frc.team2473.robot.Database;
import org.usfirst.frc.team2473.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/*
 *	Front limit switch refers to the limit switch initially pressed.
 *	Back limit switch is the other one.
 */
public class ActiveGear extends Command {

	private boolean prevFrontLSPressed;
	private boolean prevBackLSPressed;
	private boolean prevHESPressed;

	private boolean lookingForHallEffect;

	/*
	 * True represents section between hall effect sensor and front limit
	 * switch. False represents section between hall effect sensor and back
	 * limit switch.
	 */
	private boolean waitingForVishal;

	private boolean finished;

	private double powerMagnitude;

	/*
	 * Positive represents moving from front to back limit switch. Negative
	 * represents moving from back to front limit switch.
	 */
	private double direction;

	public ActiveGear() {
		System.out.println("Creating AG");
		requires(Robot.AGSystem);

		prevFrontLSPressed = false;
		prevBackLSPressed = false;
		prevHESPressed = false;
		lookingForHallEffect = false;
		waitingForVishal = true;
		finished = false;

		powerMagnitude = 0.1;
		direction = 0;
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		System.out.println("Initializing.");
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		// System.out.println("Executing");
		// Robot.AGSystem.moveArm(-0.2);

		// if(Robot.AGSystem.getFrontLS()){
		// System.out.println("Front LS Pressed.");
		// }
		//
		// if(Robot.AGSystem.getBackLS()){
		// System.out.println("Back LS Pressed");
		// }
		//
		// if(Robot.AGSystem.getHallEffect()){
		// System.out.println("HES ON");
		// }

		if (!Robot.AGSystem.getFrontLS() && Database.getInstance().getButton(Database.ButtonName.AGForward).get()) {
			direction = 1;
			System.out.println("Moving forward");
		} else if (Robot.AGSystem.getFrontLS() && !Database.getInstance().getButton(Database.ButtonName.AGForward)) {
			// If currently pressing front limit switch, move backwards
			// Else if currently pressing back limit switch, move forwards
			// Else, already moving.
			if (Robot.AGSystem.getFrontLS()) {
				direction = -1;
			} else if (Robot.AGSystem.getBackLS()) {
				direction = 1;
			} else {
				if (waitingForVishal) {
					direction = 1;
				} else {
					direction = -1;
				}
			}

			lookingForHallEffect = true;
		} else if (!Robot.AGSystem.getBackLS()
				&& Database.getInstance().getButton(Database.ButtonName.AGBackward).get()) {
			direction = -1;
			System.out.println("Moving backward");
		}

		if (!prevHESPressed && Robot.AGSystem.getHallEffect()) {
			System.out.println("Switching vishal.");
			waitingForVishal = !waitingForVishal;
		}
		// System.out.println("Moving at " + direction*powerMagnitude);
		Robot.AGSystem.moveArm(direction * powerMagnitude);

		if (!prevFrontLSPressed && Robot.AGSystem.getFrontLS()) {
			System.out.println("Front LS Pressed");
			direction = 0;
		} else if (!prevBackLSPressed && Robot.AGSystem.getBackLS()) {
			System.out.println("Back LS Pressed");
			direction = 0;
		} else if (lookingForHallEffect && Robot.AGSystem.getHallEffect()) {
			System.out.println("Found hall effect");
			direction = 0;
			lookingForHallEffect = false;
		}

		prevFrontLSPressed = Robot.AGSystem.getFrontLS();
		prevBackLSPressed = Robot.AGSystem.getBackLS();
		prevHESPressed = Robot.AGSystem.getHallEffect();
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return finished;
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
