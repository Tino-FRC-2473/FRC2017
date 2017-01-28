package org.usfirst.frc.team2473.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoAlign extends CommandGroup {

    public AutoAlign(double cameraA, double cameraBearing, double cameraToLiftDistance, double cameraToCenterDistance, double cameraToCenterAngle, boolean robotOnLeft) {
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
    	double cameraB = (robotOnLeft)?90 - cameraBearing: 90 + cameraBearing;
    	cameraA = Math.toRadians(cameraA);
    	cameraB = Math.toRadians(cameraB);
    	cameraToCenterAngle = Math.toRadians(cameraToCenterAngle);
    	
    	double distanceX;
    	double distanceY;
    	
    	if(robotOnLeft){
    		distanceX = -cameraToLiftDistance*Math.cos(cameraA) + cameraToCenterDistance*Math.cos(cameraA - cameraB - cameraToCenterAngle);
    		distanceY = -cameraToLiftDistance*Math.sin(cameraA) + cameraToCenterDistance*Math.sin(cameraA - cameraB - cameraToCenterAngle);
    	}else{
    		distanceX = cameraToLiftDistance*Math.cos(cameraA) + cameraToCenterDistance*Math.cos(cameraB - cameraA - cameraToCenterAngle);
    		distanceY = -cameraToLiftDistance*Math.sin(cameraA) + cameraToCenterDistance*Math.sin(cameraB - cameraA - cameraToCenterAngle);
    	}
    	
    	double centerToLiftDistance = Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY, 2));
    	double centerA = Math.toDegrees(((robotOnLeft)?Math.atan(distanceY/distanceX):Math.atan(-distanceY/distanceX)));
    	double centerB = Math.toDegrees(Math.toRadians(centerA) - cameraA + cameraB);
		
    	addSequential(new Turn((robotOnLeft)?90+centerA-centerB:centerB-90-centerA));
    	addSequential(new DriveStraightForward(centerToLiftDistance*Math.acos(Math.toRadians(centerA))));
    	addSequential(new Turn((robotOnLeft)?-90:90));
    	addSequential(new DriveStraightForward(centerToLiftDistance*Math.asin(Math.toRadians(centerA))));
    }
}
