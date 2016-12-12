package org.usfirst.frc.team1024.robot;

import org.usfirst.frc.team1024.robot.subsystems.Driver;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
    public static Joystick logi = new Joystick(0);
    public static JoystickButton shift = new JoystickButton(logi, 6);
    public Driver driver = new Driver();
}

