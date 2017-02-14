package org.usfirst.frc.team2473.robot;

public class MathUtil {
	
	private MathUtil(){}
	
	public static double getTurnOne(){
		boolean robotOnLeft = ;
		double cameraBearing = ;
		double cameraA = ;
		double cameraToLiftDistance = ;
		double cameraToCenterAngle = ;
		double cameraToCenterDistance = ;
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
    	
    	return (robotOnLeft)?90+centerA-centerB:centerB-90-centerA;
	}
	
	public static double getTurnTwo(){
		boolean robotOnLeft = ;
		
		return (robotOnLeft)?-90:90;
	}
	
	public static double getDistanceOne(){
		boolean robotOnLeft = ;
		double cameraBearing = ;
		double cameraA = ;
		double cameraToLiftDistance = ;
		double cameraToCenterAngle = ;
		double cameraToCenterDistance = ;
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
    	
    	return centerToLiftDistance*Math.cos(Math.toRadians(centerA));
	}
	
	public static double getDistanceTwo(){
		boolean robotOnLeft = ;
		double cameraBearing = ;
		double cameraA = ;
		double cameraToLiftDistance = ;
		double cameraToCenterAngle = ;
		double cameraToCenterDistance = ;
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
    	
    	return centerToLiftDistance*Math.sin(Math.toRadians(centerA));
	}
	
}
