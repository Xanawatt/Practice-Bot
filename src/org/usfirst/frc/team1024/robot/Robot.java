
package org.usfirst.frc.team1024.robot;

import org.usfirst.frc.team1024.robot.commands.CameronFollowLine;
import org.usfirst.frc.team1024.robot.subsystems.Driver;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.livewindow.LiveWindowSendable;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	public static OI oi;

	Command autonomousCommand;
	SendableChooser chooser;
	Command followLine;
	public static boolean isDone;
	public static byte[] pixyValues;
	public static int xPosition = 0;
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	public void robotInit() {
		oi = new OI();
		chooser = new SendableChooser();
		isDone = false;
		followLine = new CameronFollowLine();
		pixyValues = new byte[64];
		//        chooser.addObject("My Auto", new MyAutoCommand());
		SmartDashboard.putData("Auto mode", chooser);

		Driver.frontLeft.setInverted(false);
		Driver.frontRight.setInverted(true);
		Driver.backLeft.setInverted(false);
		Driver.backRight.setInverted(true);

		Driver.frontLeft.enableBrakeMode(false);
		Driver.frontRight.enableBrakeMode(false);
		Driver.backLeft.enableBrakeMode(false);
		Driver.backRight.enableBrakeMode(false);

		Driver.pixyPower.set(true);
		//LiveWindow.addSensor("Pixy", "Pixy", (LiveWindowSendable) Driver.pixyi2c);
	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	public void disabledInit(){

	}

	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select between different autonomous modes
	 * using the dashboard. The sendable chooser code works with the Java SmartDashboard. If you prefer the LabVIEW
	 * Dashboard, remove all of the chooser code and uncomment the getString code to get the auto name from the text box
	 * below the Gyro
	 *
	 * You can add additional auto modes by adding additional commands to the chooser code above (like the commented example)
	 * or additional comparisons to the switch structure below with additional strings & commands.
	 */
	public void autonomousInit() {
		autonomousCommand = (Command) chooser.getSelected();

		/* String autoSelected = SmartDashboard.getString("Auto Selector", "Default");
		switch(autoSelected) {
		case "My Auto":
			autonomousCommand = new MyAutoCommand();
			break;
		case "Default Auto":
		default:
			autonomousCommand = new ExampleCommand();
			break;
		} */

		// schedule the autonomous command (example)
		if (autonomousCommand != null) autonomousCommand.start();
	}

	/**
	 * This function is called periodically during autonomous
	 */
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
		while(!isDone){
			followLine.start();
			isDone = true;
		}
	}

	public void teleopInit() {
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to 
		// continue until interrupted by another command, remove
		// this line or comment it out.
		if (autonomousCommand != null) autonomousCommand.cancel();
	}

	/**
	 * This function is called periodically during operator control
	 */
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
		printPixyStuff();
		Driver.drive(-OI.logi.getRawAxis(1), -OI.logi.getRawAxis(3));
		if(OI.logi.getRawButton(6) == true){
			Driver.shifter.set(true);
		}
		if(OI.logi.getRawButton(8) == true){
			Driver.shifter.set(false);
		}
	}

	/**
	 * This function is called periodically during test mode
	 */
	public void testPeriodic() {
		LiveWindow.run();
	}
	public static void printPixyStuff(){
		pixyValues[0] = (byte) 0b01010101;
		pixyValues[1] = (byte) 0b10101010;

		Driver.pixyi2c.readOnly(pixyValues, 64);
		if(pixyValues != null){
			int i = 0;
			while(!(pixyValues[i] == 85 && pixyValues[i + 1] == -86) && i < 50){
				i++;
			}
			i++;
			if(i > 50)
				i = 49;
			while(!(pixyValues[i] == 85 && pixyValues[i + 1] == -86) && i < 50){
				i++;
			}
			char xPosition = (char) (((pixyValues[i + 7] & 0xff) << 8) | (pixyValues[i + 6] & 0xff));
			char yPosition = (char) (((pixyValues[i + 9] & 0xff) << 8) | (pixyValues[i + 8] & 0xff));
			char width = (char) (((pixyValues[i + 11] & 0xff) << 8) | (pixyValues[i + 10] & 0xff));
			char height = (char) (((pixyValues[i + 13] & 0xff) << 8) | (pixyValues[i + 12] & 0xff));
			
			SmartDashboard.putNumber("xPosition", xPosition);
			SmartDashboard.putNumber("yPosition", yPosition);
			SmartDashboard.putNumber("width", width);
			SmartDashboard.putNumber("height", height);
		}
	}

}

