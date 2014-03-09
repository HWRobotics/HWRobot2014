package org.usfirst.frc1148.modules;

import edu.wpi.first.wpilibj.Timer;
import org.usfirst.frc1148.Robot;
import org.usfirst.frc1148.data.MoveData;
import org.usfirst.frc1148.interfaces.RobotModule;


public class AutonomousModule implements RobotModule {

    RobotDriver driver;
    int state = 0;
    Timer timer;
    Timer secondTimer;
    int wiggle = 0;
    boolean autonomousEnabled = true;
    AutoDriveModule autoDrive;
    Robot robot;
    float rotSpeed;
    double driveY;
    double driveX;
    MoveData data;
    MoveData oldData;


    public AutonomousModule(Robot robot) {
        this.robot = robot;
        data = new MoveData();
        
    }

    public void initModule() {
        timer = new Timer();
        secondTimer = new Timer();
        autoDrive = (AutoDriveModule)robot.GetModuleByName("autoDrive");
        this.driver = (RobotDriver)robot.GetModuleByName("robotDriver");
    }

    public void activateModule() {
        rotSpeed = 1;
        driveY = 1;
        driveX = 0;
        state = 0;
        System.out.println("Autonomous activated!");

    }

    public void deactivateModule() {
        rotSpeed =0;
        driveY=0;
        driveX = 0;
        System.out.println("Autonomous deactivated!");
        
    }

    void SetAllowed(boolean aBoolean) {
        autonomousEnabled = aBoolean;
        if (true)
        {
            //oldData=driver.getMoveData();
            //driver.setMoveData(data);
        }
        else
        {
            //driver.setMoveData(oldData);
        }
    }

    //For autonomous we just do timed states
    //Each state increments to the next state (timed)
    //This way we can be sure of the timing of autonomous
    public void updateTick(int mode) {
        if (mode == 1 && autonomousEnabled) {
            MoveData moveData = driver.getMoveData();
            driver.NotRelative();
            switch (state) {
            case 0: //startup 0 seconds
                timer.reset();
                timer.start();
                state++;
                break;
            case 1:
                moveData.speed = 0.5;
                moveData.angle = 0;
                //driver.Relative();               
                if (timer.get() > 3) {
                    state++;
                }
                break;
            default: //If it finishes or there is no next state (failsafe)
                
                //autoDrive.Disable();
                moveData.speed = 0;
                moveData.angle = 0;
                //driver.Relative();
                break;
            }
        }
    }

    double GetState() {
        return state;
    }
}
