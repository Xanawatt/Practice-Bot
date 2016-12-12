package org.usfirst.frc.team1024.robot.commands;

import org.usfirst.frc.team1024.robot.Robot;
import org.usfirst.frc.team1024.robot.pixy.PixyException;
import org.usfirst.frc.team1024.robot.pixy.PixyI2C;
import org.usfirst.frc.team1024.robot.pixy.PixyPacket;
import org.usfirst.frc.team1024.robot.subsystems.Driver;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class CameronFollowLine extends Command {
	public byte[] pixyValues;
	public Timer timer;
	public boolean isDone;
	public PixyI2C pixy; //this is experimental
	public PixyPacket pixyPacketValues;

	public CameronFollowLine() {
		pixyValues = new byte[64];
		timer = new Timer();
		isDone = false;
		pixy = new PixyI2C(); //this is experimental
		pixyPacketValues = new PixyPacket();
	}

	protected void initialize() {
	}

	protected void execute() {
		timer.start();
		while(!isDone){
			while(timer.get() < 5.0){
				Robot.printPixyStuff();
				if(isLine()){
					if(isCentered()){
						Driver.drive(0.4, 0.4);
						DriverStation.reportError("Centered \n", true);
					} else if(getX() < 155){
						Driver.drive(0.3, 0.4);
						DriverStation.reportError("Less than 155 \n", true);
					} else if(getX() > 165){
						Driver.drive(0.4, 0.3);
						DriverStation.reportError("Greater than 165 \n", true);
					} else{
						Driver.drive(0.0, 0.0);
						DriverStation.reportError("isCentered == false \n", true);
					}
				}
				DriverStation.reportError("isLine == false \n", true);
				Driver.drive(0.0, 0.0);
			}
			DriverStation.reportError("Timer Exceeded 5 seconds \n", true);
			Driver.drive(0.0, 0.0);
			timer.stop();
			timer.reset();
		}
		DriverStation.reportError("I am done", true);
		isDone = true;
	}

	protected boolean isFinished() {
		return false;
	}

	protected void end() {
	}

	protected void interrupted() {
	}
	public char getX(){

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
			return (char) (((pixyValues[i + 7] & 0xff) << 8) | (pixyValues[i + 6] & 0xff));
		}
		return 0;
	}
	public char getWidth(){
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
			return (char) (((pixyValues[i + 11] & 0xff) << 8) | (pixyValues[i + 10] & 0xff));
		}
		return 0;
	}
	public char getHeight(){
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
			return (char) (((pixyValues[i + 13] & 0xff) << 8) | (pixyValues[i + 12] & 0xff));
		}
		DriverStation.reportError("I returned null", true);
		return 0;
	}
	public boolean isLine(){//Is the robot on the line?
		DriverStation.reportError("Width = " + getWidth() + "Height = " + getHeight() + "/n", true);
		if(getHeight() > 0 && getWidth() > 0){
			return true;
		}else{
			return false;
		}
	}

	public boolean isCentered(){//Is it centered on said line?
		if(getX() >= 155 && getX() <= 165){
			return true;
		}else{
			return false;
		}
	}
	//Everything below this is Experimental code and has not been tested
	public int getXWithNewCode(){
		return pixyPacketValues.X;
	}
	public int getYWithNewCode(){
		return pixyPacketValues.Y;
	}
	public int getWidthWithNewCode(){
		return pixyPacketValues.Width;
	}
	public int getHeightWithNewCode(){
		return pixyPacketValues.Height;
	}
	/**
	 * This method updates all the pixy values and stores them in a global variable set at PixyPacket.java
	 */
	public void updatePixy(){
		try {
			pixy.readPacket(1); //This is 1 because this is the ID of the object in pixymon we are detecting
								//Might actually be 0 for the first object in frame
		} catch (PixyException e) {
			e.printStackTrace();
		}
	}
}