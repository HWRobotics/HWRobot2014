package org.usfirst.frc1148.modules;

import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.RobotDrive.MotorType;
import edu.wpi.first.wpilibj.Talon;
import org.usfirst.frc1148.Robot;
import org.usfirst.frc1148.data.MoveData;
import org.usfirst.frc1148.interfaces.RobotModule;
import edu.wpi.first.wpilibj.Timer;

public class RobotDriver implements RobotModule {

    Robot robot;
    Talon frontLeft;
    Talon backLeft;
    Talon frontRight;
    Talon backRight;
    public Gyro robotGyro;
    boolean relativeDrive = true;
    int angleOffset = 0;
    MoveData moveData;
    RobotDrive driver;
    boolean enableMotors = true;
    Timer timer;

    public RobotDriver(Robot robot) {
        this.robot = robot;
        moveData = new MoveData();
    }
    /*
     * Set the move data for the controller
     */

    public void setMoveData(MoveData data) {
        moveData = data;
    }

    public void initModule() {
        System.out.println("Initialzing robot driver module!");
        frontLeft = new Talon(6); //connected to 6
        frontRight = new Talon(7); //connected to 7
        robotGyro = new Gyro(1);
        backRight = new Talon(3);
        backLeft = new Talon(1);
        driver = new RobotDrive(frontLeft, backLeft, frontRight, backRight);
        driver.setInvertedMotor(MotorType.kFrontRight, true);
        driver.setInvertedMotor(MotorType.kRearRight, true);
        System.out.println("Robot driver module initialized.");
    }

    public void activateModule() {
        System.out.println("Robot driver module activated!");
        moveData.angle = 0;
        moveData.speed = 0;
        //robotGyro.reset();
    }

    public void resetGyro() {
        angleOffset = 0;
        robotGyro.reset();
    }

    public void deactivateModule() {
        System.out.println("Robot driver module deactivated!");
        driver.stopMotor();
    }

    public RobotDriver ToggleRelative() {
        relativeDrive = !relativeDrive;
        return this;
    }

    public RobotDriver Relative() {
        relativeDrive = true;
        return this;
    }

    public RobotDriver NotRelative() {
        relativeDrive = false;
        return this;
    }

    public void processMovementVector() {
        if(enableMotors) {
            double speed = moveData.speed;
            double moveAngle = moveData.angle;

            if (relativeDrive) {
                moveAngle -= robotGyro.getAngle() + angleOffset;
            }

            //Replace with % operator
            while (moveAngle < 0 || moveAngle > 360) {
                if (moveAngle < 0) {
                    moveAngle += 360;
                } else if (moveAngle > 360) {
                    moveAngle -= 360;
                }
            }

            double rotSpeed = moveData.rotationSpeed;
            /*
            moveAngle = moveAngle / 180 * Math.PI; //Convert to Radians for method use
            
            
            
           double frontLefts = speed*Math.sin(moveAngle+(Math.PI/4))+rotSpeed;
           double frontRights = speed*Math.sin(moveAngle+(Math.PI/4))-rotSpeed;
           double backLefts = frontRights;
           double backRights = frontLefts;
           
           double frontLefts = speed *4/3* Math.sin(moveAngle + (Math.PI / 4)) + rotSpeed;
           double frontRights = (speed*4/3 * Math.cos(moveAngle + (Math.PI / 4)) - rotSpeed);
              double backLefts = speed *4/3* Math.cos(moveAngle + (Math.PI / 4)) + rotSpeed;
           double backRights = (speed *4/3* Math.sin(moveAngle + (Math.PI / 4)) - rotSpeed);
        
           double[] values = new double[]{frontLefts, frontRights, backLefts, backRights};
           double max = Math.abs(values[0]);
           for (int i = 1; i < 4; i++) {
               if (Math.abs(values[i]) > max) {
                   max = Math.abs(values[i]);
               }
           }
           if (max > 1) {
               frontLefts /= max;
               frontRights /= max;
               backLefts /= max;
               backRights /= max;
           }else if(max < speed){
            
            
           }
           //*/

           /* V2
		
            

           //apply motor movement
           if (enableMotors) {
               frontLeft.set(frontLefts);
               frontRight.set(frontRights);
               backLeft.set(backLefts);
               backRight.set(backRights);
           } else {
               System.out.println("FL: "+frontLefts+" FR: "+frontRights+" BL: "+backLefts+" BR: "+backRights);
               System.out.println("IS: "+moveData.speed+" ANG: "+moveData.angle + " ROT: "+moveData.rotationSpeed);
           } */
           driver.mecanumDrive_Polar(speed, moveAngle, rotSpeed);       
        } else {
            driver.stopMotor();
        }
    }

    public void updateTick(int mode) {
        processMovementVector();

    }

    public MoveData getMoveData() {
        return moveData;
    }

    public double getGyroAngle() {
        return robotGyro.getAngle();
    }

    public double getGyroAngleInRange() {
        double angle = getGyroAngle();
        while (angle < 0 || angle > 360) {
            if (angle < 0) {
                angle += 360;
            } else if (angle > 360) {
                angle -= 360;
            }
        }
        return angle;
    }

    MoveData getDriveData() {
        return moveData;
    }

    void SetMotorsEnabled(boolean doEnable) {
        enableMotors = doEnable;
    }
}
