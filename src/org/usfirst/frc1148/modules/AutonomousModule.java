package org.usfirst.frc1148.modules;

import edu.wpi.first.wpilibj.Timer;
import org.usfirst.frc1148.Robot;
import org.usfirst.frc1148.data.MoveData;
import org.usfirst.frc1148.interfaces.RobotModule;


public class AutonomousModule implements RobotModule {
    RobotDriver driver;
    int state = 0;
    Timer timer;
    boolean autonomousEnabled = true;
    AutoDriveModule autoDrive;
    Robot robot;

    public AutonomousModule(Robot robot) {
        this.robot = robot;
    }

    public void initModule() {
        timer = new Timer();
        autoDrive = (AutoDriveModule)robot.GetModuleByName("autoDrive");
        this.driver = (RobotDriver)robot.GetModuleByName("robotDriver");
    }

    public void activateModule() {
        state = 0;
        System.out.println("Autonomous activated!");
    }

    public void deactivateModule() {
        System.out.println("Autonomous deactivated!");
        autoDrive.Disable();
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
                autoDrive.OrientTo(0);
                state++;
                break;
            case 1:
                moveData.speed = 0.5;
                moveData.angle = 0; 
                if (timer.get() > 3) {
                    state++;
                }
                break;
            case 2:
                autoDrive.Disable();
                break;
            default: //If it finishes or there is no next state (failsafe)
                moveData.speed = 0;
                moveData.angle = 0;
                break;
            }
        }
    }
}
