package org.usfirst.frc.team2473.robot.subsystems;

import org.usfirst.frc.team2473.robot.RobotMap;
import org.usfirst.frc.team2473.robot.commands.ActiveGear;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class ActiveGearSystem extends Subsystem {

	private CANTalon motor;
	
	private DigitalInput frontLS;
	private DigitalInput backLS;
	private DigitalInput hallEffect;
	
	public ActiveGearSystem(){
		motor = new CANTalon(RobotMap.activeGearMotor);
		motor.changeControlMode(TalonControlMode.PercentVbus);
		
		frontLS = new DigitalInput(RobotMap.AGFrontLS);
		backLS = new DigitalInput(RobotMap.AGBackLS);
		hallEffect = new DigitalInput(RobotMap.AGHallEffect);
	}
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new ActiveGear());
    }
    
    public boolean getFrontLS(){
    	return frontLS.get();
    }
    
    public boolean getBackLS(){
    	return backLS.get();
    }
    
    public boolean getHallEffect(){
    	return !hallEffect.get();
    }
    
    public void moveArm(double power){
    	if(Math.abs(power) <= 1.0){
    		motor.set(-power);
    	}else{
    		System.out.println("Warning, invalid input, setting power to 0.");
    		motor.set(0);
    	}
    }
}

