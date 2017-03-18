package org.usfirst.frc.team2473.robot;

import org.usfirst.frc.team2473.robot.Database.Value;

public class MathUtil {
	
	private MathUtil(){}
	
	public static double getTurnOne(){
		boolean cameraOnLeft = Database.getInstance().getValue(Value.CV_L_OR_R) == 1;
		double cameraBearing = Database.getInstance().getValue(Value.CV_BEARING);
		double cameraA = Database.getInstance().getValue(Value.CV_ANGLE_A);
		double cameraToLiftDistance = Database.getInstance().getValue(Value.CV_DISTANCE);
		double cameraToCenterAngle = 68.12;
		double cameraToCenterDistance = 14.22;
		double cameraB = (cameraOnLeft)?90 - cameraBearing: 90 + cameraBearing;
    	cameraA = Math.toRadians(cameraA);
    	cameraB = Math.toRadians(cameraB);
    	cameraToCenterAngle = Math.toRadians(cameraToCenterAngle);
    	
    	double distanceX;
    	double distanceY;
    	
    	if(cameraOnLeft){
    		distanceX = -cameraToLiftDistance*Math.cos(cameraA) + cameraToCenterDistance*Math.cos(cameraA - cameraB - cameraToCenterAngle);
    		distanceY = -cameraToLiftDistance*Math.sin(cameraA) + cameraToCenterDistance*Math.sin(cameraA - cameraB - cameraToCenterAngle);
    	}else{
    		distanceX = cameraToLiftDistance*Math.cos(cameraA) + cameraToCenterDistance*Math.cos(cameraB - cameraA - cameraToCenterAngle);
    		distanceY = -cameraToLiftDistance*Math.sin(cameraA) + cameraToCenterDistance*Math.sin(cameraB - cameraA - cameraToCenterAngle);
    	}
    	
    	boolean robotOnLeft = distanceX < 0;
    	double centerToLiftDistance = Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY, 2));
    	double centerA = Math.toDegrees(Math.abs(Math.atan(distanceY/distanceX)));
    	double centerB = Math.toDegrees(Math.toRadians(centerA) - cameraA + cameraB);
    	
    	return (robotOnLeft)?90+centerA-centerB:centerB-90-centerA;
    	
	}
	
	public static double getTurnTwo(){
		boolean cameraOnLeft = Database.getInstance().getValue(Value.CV_L_OR_R) == 1;
		double cameraBearing = Database.getInstance().getValue(Value.CV_BEARING);
		double cameraA = Database.getInstance().getValue(Value.CV_ANGLE_A);
		double cameraToLiftDistance = Database.getInstance().getValue(Value.CV_DISTANCE);
		double cameraToCenterAngle = 68.12;
		double cameraToCenterDistance = 14.22;
		double cameraB = (cameraOnLeft)?90 - cameraBearing: 90 + cameraBearing;
    	cameraA = Math.toRadians(cameraA);
    	cameraB = Math.toRadians(cameraB);
    	cameraToCenterAngle = Math.toRadians(cameraToCenterAngle);
    	
    	double distanceX;
    	double distanceY;
    	
    	if(cameraOnLeft){
    		distanceX = -cameraToLiftDistance*Math.cos(cameraA) + cameraToCenterDistance*Math.cos(cameraA - cameraB - cameraToCenterAngle);
    		distanceY = -cameraToLiftDistance*Math.sin(cameraA) + cameraToCenterDistance*Math.sin(cameraA - cameraB - cameraToCenterAngle);
    	}else{
    		distanceX = cameraToLiftDistance*Math.cos(cameraA) + cameraToCenterDistance*Math.cos(cameraB - cameraA - cameraToCenterAngle);
    		distanceY = -cameraToLiftDistance*Math.sin(cameraA) + cameraToCenterDistance*Math.sin(cameraB - cameraA - cameraToCenterAngle);
    	}
    	
    	boolean robotOnLeft = distanceX < 0;
    	double centerToLiftDistance = Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY, 2));
    	double centerA = Math.toDegrees(Math.abs(Math.atan(distanceY/distanceX)));
    	double centerB = Math.toDegrees(Math.toRadians(centerA) - cameraA + cameraB);
    	
		
		return (robotOnLeft)?-90:90;
		
	}
	
	public static double getTurnThree(){
//		boolean cameraOnLeft = Database.getInstance().getValue(Value.CV_L_OR_R) == 1;
//		double cameraBearing = Database.getInstance().getValue(Value.CV_BEARING);
//		double cameraA = Database.getInstance().getValue(Value.CV_ANGLE_A);
//		double cameraToLiftDistance = Database.getInstance().getValue(Value.CV_DISTANCE);
//		double cameraToCenterAngle = 68.12;
//		double cameraToCenterDistance = 14.22;
//		double cameraB = (cameraOnLeft)?90 - cameraBearing: 90 + cameraBearing;
//    	cameraA = Math.toRadians(cameraA);
//    	cameraB = Math.toRadians(cameraB);
//    	cameraToCenterAngle = Math.toRadians(cameraToCenterAngle);
//    	
//    	double distanceX;
//    	double distanceY;
//    	
//    	if(cameraOnLeft){
//    		distanceX = -cameraToLiftDistance*Math.cos(cameraA) + cameraToCenterDistance*Math.cos(cameraA - cameraB - cameraToCenterAngle);
//    		distanceY = -cameraToLiftDistance*Math.sin(cameraA) + cameraToCenterDistance*Math.sin(cameraA - cameraB - cameraToCenterAngle);
//    	}else{
//    		distanceX = cameraToLiftDistance*Math.cos(cameraA) + cameraToCenterDistance*Math.cos(cameraB - cameraA - cameraToCenterAngle);
//    		distanceY = -cameraToLiftDistance*Math.sin(cameraA) + cameraToCenterDistance*Math.sin(cameraB - cameraA - cameraToCenterAngle);
//    	}
//    	
//    	boolean robotOnLeft = distanceX < 0;
//    	double centerToLiftDistance = Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY, 2));
//    	double centerA = Math.toDegrees(Math.abs(Math.atan(distanceY/distanceX)));
//    	double centerB = Math.toDegrees(Math.toRadians(centerA) - cameraA + cameraB);
//    	
//    	
//		System.out.println((robotOnLeft)?90 - centerB:centerB - 90);
//		return (robotOnLeft)?90 - centerB:centerB - 90;
		
		double cameraBearing = Database.getInstance().getValue(Value.CV_BEARING);
		double cameraToLiftDistance = Database.getInstance().getValue(Value.CV_DISTANCE);
		double cameraToCenterAngle = 68.12;
		double cameraToCenterDistance = 14.22;
    	cameraBearing = Math.toRadians(cameraBearing);
    	cameraToCenterAngle = Math.toRadians(cameraToCenterAngle);
    	
    	double centerToLiftDistance = Math.sqrt(Math.pow(cameraToCenterDistance, 2)+Math.pow(cameraToLiftDistance, 2)-2*cameraToCenterDistance*cameraToLiftDistance*Math.cos(Math.PI/2-cameraBearing+cameraToCenterAngle));
    	double centerBearing = Math.toDegrees(Math.asin(cameraToLiftDistance/centerToLiftDistance*Math.sin(Math.PI/2-cameraBearing+cameraToCenterAngle)))-(90 - Math.toDegrees(cameraToCenterAngle));
    	
    	
		System.out.println(centerBearing);
		return centerBearing;
		
	}
	
	public static double getDistanceOne(){
		boolean cameraOnLeft = Database.getInstance().getValue(Value.CV_L_OR_R) == 1;
		double cameraBearing = Database.getInstance().getValue(Value.CV_BEARING);
		double cameraA = Database.getInstance().getValue(Value.CV_ANGLE_A);
		double cameraToLiftDistance = Database.getInstance().getValue(Value.CV_DISTANCE);
		double cameraToCenterAngle = 68.12;
		double cameraToCenterDistance = 14.22;
		double cameraB = (cameraOnLeft)?90 - cameraBearing: 90 + cameraBearing;
    	cameraA = Math.toRadians(cameraA);
    	cameraB = Math.toRadians(cameraB);
    	cameraToCenterAngle = Math.toRadians(cameraToCenterAngle);
    	
    	double distanceX;
    	double distanceY;
    	
    	if(cameraOnLeft){
    		distanceX = -cameraToLiftDistance*Math.cos(cameraA) + cameraToCenterDistance*Math.cos(cameraA - cameraB - cameraToCenterAngle);
    		distanceY = -cameraToLiftDistance*Math.sin(cameraA) + cameraToCenterDistance*Math.sin(cameraA - cameraB - cameraToCenterAngle);
    	}else{
    		distanceX = cameraToLiftDistance*Math.cos(cameraA) + cameraToCenterDistance*Math.cos(cameraB - cameraA - cameraToCenterAngle);
    		distanceY = -cameraToLiftDistance*Math.sin(cameraA) + cameraToCenterDistance*Math.sin(cameraB - cameraA - cameraToCenterAngle);
    	}
    	
    	boolean robotOnLeft = distanceX < 0;
    	double centerToLiftDistance = Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY, 2));
    	double centerA = Math.toDegrees(Math.abs(Math.atan(distanceY/distanceX)));
    	double centerB = Math.toDegrees(Math.toRadians(centerA) - cameraA + cameraB);
    	
    	return centerToLiftDistance*Math.cos(Math.toRadians(centerA));//return centerToLiftDistance*Math.cos(Math.toRadians(centerA))/Math.cos(Math.toRadians(((robotOnLeft)?90+centerA-centerB:centerB-90-centerA)/2));
	}
	
	public static double getDistanceTwo(){
		boolean cameraOnLeft = Database.getInstance().getValue(Value.CV_L_OR_R) == 1;
		double cameraBearing = Database.getInstance().getValue(Value.CV_BEARING);
		double cameraA = Database.getInstance().getValue(Value.CV_ANGLE_A);
		double cameraToLiftDistance = Database.getInstance().getValue(Value.CV_DISTANCE);
		double cameraToCenterAngle = 68.12;
		double cameraToCenterDistance = 14.22;
		double cameraB = (cameraOnLeft)?90 - cameraBearing: 90 + cameraBearing;
    	cameraA = Math.toRadians(cameraA);
    	cameraB = Math.toRadians(cameraB);
    	cameraToCenterAngle = Math.toRadians(cameraToCenterAngle);
    	
    	double distanceX;
    	double distanceY;
    	
    	if(cameraOnLeft){
    		distanceX = -cameraToLiftDistance*Math.cos(cameraA) + cameraToCenterDistance*Math.cos(cameraA - cameraB - cameraToCenterAngle);
    		distanceY = -cameraToLiftDistance*Math.sin(cameraA) + cameraToCenterDistance*Math.sin(cameraA - cameraB - cameraToCenterAngle);
    	}else{
    		distanceX = cameraToLiftDistance*Math.cos(cameraA) + cameraToCenterDistance*Math.cos(cameraB - cameraA - cameraToCenterAngle);
    		distanceY = -cameraToLiftDistance*Math.sin(cameraA) + cameraToCenterDistance*Math.sin(cameraB - cameraA - cameraToCenterAngle);
    	}
    	
    	boolean robotOnLeft = distanceX < 0;
    	double centerToLiftDistance = Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY, 2));
    	double centerA = Math.toDegrees(Math.abs(Math.atan(distanceY/distanceX)));
    	double centerB = Math.toDegrees(Math.toRadians(centerA) - cameraA + cameraB);
    	
    	return centerToLiftDistance*Math.sin(Math.toRadians(centerA));
    	
	}
	
}
