package org.usfirst.frc.team2473.robot;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.DoubleSupplier;

import org.usfirst.frc.team2473.robot.Database.Value;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.AnalogInput;
import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;


public class SensorThread extends Thread{

	//add new sensors here
	AnalogGyro gyro;
	CANTalon leftEncoder, rightEncoder;//, twoEncoder, threeEncoder, fourEncoder, fiveEncoder;
	private volatile boolean alive = true;
	long lastTime;
	double leftEncoderZero, rightEncoderZero;
	int delay;

	private Map<Database.Value, Double> tempMap;

	//a map of how each value is called
	private Map<Database.Value, DoubleSupplier> callMap;

	public SensorThread(int delay) {
		
		this.delay = delay;
		
		//add new sensors here
		this.gyro = Robot.gyro;
		this.leftEncoder = new CANTalon(RobotMap.leftBackMotor);
		this.rightEncoder = new CANTalon(RobotMap.rightBackMotor);

		leftEncoder.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		rightEncoder.setFeedbackDevice(FeedbackDevice.QuadEncoder);

		resetEncoders();

		tempMap = new HashMap<>();

		callMap = new HashMap<>();

		//add the sensor name in the Values enum and the method of the sensor that returns the sensor value.
		callMap.put(Value.GYRO_POSITION, () -> gyro.getAngle());
		callMap.put(Value.GYRO_VELOCITY, () -> gyro.getRate());
		callMap.put(Value.RIGHT_ENCODER, () -> rightEncoder.getEncPosition());
		callMap.put(Value.LEFT_ENCODER, () -> leftEncoder.getEncPosition());
//		callMap.put(Value.RIGHT_ENCODER, () -> (rightEncoder.getEncPosition() - rightEncoderZero )* Database.RIGHT_ENC_CONSTANT);
//		callMap.put(Value.LEFT_ENCODER, () -> -(leftEncoder.getEncPosition() - leftEncoderZero )* Database.LEFT_ENC_CONSTANT);
		
		
		
		callMap = Collections.unmodifiableMap(callMap);
		super.setDaemon(true);
	}
	
	/**
	 * this method simulates the thread methods Thread.pause() and Thread.kill(). 
	 * It continuously polls sensors and then sleeps for delay length while alive and running.
	 * When it is not running it simply waits and stops running when it is not alive
	 */
	@Override
	public void run() {
		while (alive) {

				// System.out.println(System.currentTimeMillis() - lastTime);
				
				updateSensors();
				
				lastTime = System.currentTimeMillis();
				// Thread.yield();
				try {
					Thread.sleep(delay);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

		}
	}

	private void updateSensors()
	{
		//snapshots a value for every sensor
		for (Database.Value v : callMap.keySet()) {
			tempMap.put(v, callMap.get(v).getAsDouble());
		}
		
		//push those values to the database
		for(Database.Value v : tempMap.keySet())
		{
			Database.getInstance().setValue(v, tempMap.get(v));
		}
	}
	

	/**
	 * kills this thread. It may run one last loop. Stops any future looping.
	 */
	public void kill() {
		alive = false;
		notify();
	}

	public boolean isDead() {
		return !alive;
	}

	public void resetEncoders() {
		rightEncoder.setEncPosition(0);
		leftEncoder.setEncPosition(0);
//		leftEncoderZero = leftEncoder.getEncPosition();
//		rightEncoderZero = rightEncoder.getEncPosition();
	}

	public void resetGyro() {
		gyro.reset();
	}
}
