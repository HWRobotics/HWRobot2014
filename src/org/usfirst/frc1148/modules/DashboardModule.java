/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.usfirst.frc1148.modules;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc1148.Robot;
import org.usfirst.frc1148.interfaces.RobotModule;

/**
 * Currently disabled and not working properly
 * REPEAT: DISABLED. NOTHING IN THIS CLASS DOES ANYTHING.
 * @author HW Robotics
 */
public class DashboardModule implements RobotModule {
    AutoDriveModule auto;
    AutonomousModule autonomous;
    JoyStickInputModule joy;
    MotorTestModule test;
    RobotDriver driver;
    Robot robot;

    public DashboardModule(Robot robot) {
        this.robot = robot;
    }

    public void initModule() {
        driver = (RobotDriver)robot.GetModuleByName("robotDriver");
        autonomous = (AutonomousModule)robot.GetModuleByName("autonomous");
        auto = (AutoDriveModule)robot.GetModuleByName("autodrive");
        joy = (JoyStickInputModule)robot.GetModuleByName("joystick");
        test = (MotorTestModule)robot.GetModuleByName("testmotor");
    }

    public void activateModule() {
    }

    public void deactivateModule() {
    }

    public void updateTick(int mode) {
        //Update the test motor speed
        SmartDashboard.putNumber("testMotorSpeed", test.GetSpeed());
        if(SmartDashboard.getBoolean("enableTestMotorControl", false)) {
            test.SetSpeed(SmartDashboard.getNumber("testMotorSpeed", 0));
        }

        //Update enable driving
        SmartDashboard.putNumber("driveAngle", driver.getDriveData().angle);
        SmartDashboard.putNumber("driveSpeed", driver.getDriveData().speed);
        SmartDashboard.putNumber("rotateSpeed", driver.getDriveData().rotationSpeed);
        driver.SetMotorsEnabled(SmartDashboard.getBoolean("disableDrive"));

        //Check reset gyro
        if(SmartDashboard.getBoolean("resetGyro", false))
        {
            SmartDashboard.putBoolean("resetGyro", false);
            driver.resetGyro();
        }

        //Autodrive
        SmartDashboard.putBoolean("autoOrientEnabled", auto.AutoOrientEnabled());
    }

}
