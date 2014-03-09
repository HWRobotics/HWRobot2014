package org.usfirst.frc1148.modules;

import org.usfirst.frc1148.Robot;
import org.usfirst.frc1148.data.MoveData;
import org.usfirst.frc1148.interfaces.RobotModule;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Joystick;

public class JoyStickInputModule implements RobotModule {

    Joystick drive;
    Joystick secControl;
    Joystick thirdControl;
    double driveY;
    double driveX;
    double rotSpeed;
    boolean isResetButtonPressed = false;
    
    boolean isRelativeTogglePressed = false;

    MoveData data;
    RobotDriver driver;
    AutoDriveModule autoDrive;
    Robot robot;
    MotorTestModule motorTester;
    Timer timer;
    CatapultModule catapult;

    public JoyStickInputModule(Robot robot) {
        this.robot = robot;
        data = new MoveData();
    }

    public void initModule() {
        System.out.println("Initializing JoyStickInputModule...");
        this.autoDrive = (AutoDriveModule)robot.GetModuleByName("autodrive");
        this.driver = (RobotDriver)robot.GetModuleByName("robotDriver");
        this.motorTester = (MotorTestModule)robot.GetModuleByName("testmotor");
        this.catapult = (CatapultModule)robot.GetModuleByName("catapult");
        drive = new Joystick(1);
        secControl = new Joystick(2);
        thirdControl = new Joystick(3);
        System.out.println("JoystickInputModule initialized...");
    }

    public void activateModule()
    {
        //SetMoveData on the driver to establish our dominance
        driver.setMoveData(data);
        System.out.println("JoyStick activateModule activated!");
    }

    public void deactivateModule()
    {
        System.out.println("JoyStick activateModule deactivated!");
    }

    public void updateTick(int mode) {
        if (mode != 2) { //Manual mode only
            return;
        }
        rotSpeed = drive.getZ()/1.5;
        if (drive.getRawButton(2)) {
            rotSpeed = 0;
        }
        //exponential
        rotSpeed = rotSpeed * Math.abs(rotSpeed);
        driveY = drive.getY() * Math.abs(drive.getY());
        driveX = drive.getX() * Math.abs(drive.getX());

        if (driveY < .01 && driveY > -.01) {
            driveY = 0;
        }
        if (driveX < .01 && driveX > -.01) {
            driveX = 0;
        }
        if (rotSpeed < .01 && rotSpeed > -.01) {
            rotSpeed = 0;
        }
        if (drive.getRawButton(8) && !isResetButtonPressed) {
            driver.resetGyro();
            isResetButtonPressed = true;
            System.out.println("Gyro reset!");
        } else if (!drive.getRawButton(8)) {
            isResetButtonPressed = false;
        }

        //driver.NotRelative();

        if (drive.getRawButton(7)) {
            driver.Relative();
        } else {
            driver.NotRelative();
        }
        /*
       if (drive.getRawButton(7) && !isRelativeTogglePressed) {
            driver.ToggleRelative();
            isRelativeTogglePressed = true;
            System.out.println("toggled relative!");
        } else if (!drive.getRawButton(7)) {
            isRelativeTogglePressed = false;
        */

        //Here add your other features
        data.speed = Math.max(Math.abs(drive.getX()), Math.abs(drive.getY()));
        data.angle = drive.getDirectionDegrees();
        data.rotationSpeed = rotSpeed;

        if (drive.getRawButton(5)) {
            data.speed = 1;
            data.angle = 0;
            data.rotationSpeed = 0;
        } else if (drive.getRawButton(6)) {
            data.speed = 1;
            data.angle = 180;
            data.rotationSpeed = 0;
        } else if (drive.getRawButton(3)) {
            data.speed = 1;
            data.angle = 270;
            data.rotationSpeed = 0;
        } else if (drive.getRawButton(4)) {
            data.speed = 1;
            data.angle = 90;
            data.rotationSpeed = 0;
        }
        
        //AUTO ORIENT JMF TAKING OUT
        /*
        if (drive.getRawButton(11)) {
            autoDrive.OrientTo(45);
        } else if (drive.getRawButton(12)) {
            autoDrive.OrientTo(360 - 45);
        } else {
            autoDrive.Disable();
        }
        * */
        
        
        
        // JMF CATAPULT
        if (drive.getRawButton(11)) {
            catapult.Load();
        } else if (drive.getRawButton(9)) {
            catapult.Launch();
        }

        //Test motor
        motorTester.SetSpeed(secControl.getTrigger() ? secControl.getY() : 0);
    }
}
