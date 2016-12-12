package org.usfirst.frc.team1024.robot.subsystems;

import org.usfirst.frc.team1024.robot.Robot;
import org.usfirst.frc.team1024.robot.RobotMap;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Driver extends Subsystem {

	public static CANTalon frontLeft = new CANTalon(RobotMap.FrontLeft);
	public static CANTalon frontRight = new CANTalon(RobotMap.FrontRight);
	public static CANTalon backLeft = new CANTalon(RobotMap.BackLeft);
	public static CANTalon backRight = new CANTalon(RobotMap.BackRight);
	public static final Solenoid shifter = new Solenoid(RobotMap.PCM, RobotMap.shifter);
	public static I2C pixyi2c = new I2C(Port.kOnboard, RobotMap.pixyAddress);
	public static DigitalOutput pixyPower = new DigitalOutput(6);

	public Driver() {
	}

	// The motors will start up at half power and run for any set time
	//
	public static void driveForTime(double power, double delay) {

		frontLeft.set(power);
		frontRight.set(power);
		backLeft.set(power);
		backRight.set(power);

		Timer.delay(delay);

		frontLeft.set(0.0);
		frontRight.set(0.0);
		backLeft.set(0.0);
		backRight.set(0.0);

	}

	public static void drive(double lPower, double rPower) {
		frontLeft.set(lPower);
		frontRight.set(rPower);
		backLeft.set(lPower);
		backRight.set(rPower);
	}

	public void initDefaultCommand() {

	}

	public static void pixyValue(int desiredxposition) {
		drive(0.3, -0.3);
		Timer.delay(2.0);
		drive(0.0, 0.0);
		while(true){
			Robot.printPixyStuff();
			while(Robot.xPosition < 200 && Robot.xPosition > 100){
				Robot.printPixyStuff();
				drive(0.0, 0.0);
			}
			while(Robot.xPosition > desiredxposition){
				Robot.printPixyStuff();
				drive(0.0, -0.2);
			}
			while(Robot.xPosition < desiredxposition){
				Robot.printPixyStuff();
				drive(-0.2, 0.0);
			}
		}
	}
}
